package view;

import adapters.IAdapterFactory;
import compiler.CodeCompiler;
import generator.CodeGenerator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.eclipse.emf.ecore.EPackage;
import utils.FileSelectionDialog;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Optional;

public class OpeningWindow extends Stage implements View
{
    private GraphicalNMFApplication application;

    private Parent root;

    public OpeningWindow(GraphicalNMFApplication application) throws FXMLException
    {
        this.application = application;
        init();
    }

    protected void init() throws FXMLException
    {
        try
        {
            File file = new File("..\\Prototypes\\Opening Window.fxml");  //fixme
            root = GraphicalNMFApplication.loadFXML(file, this);
        }
        catch(Exception e)
        {
            throw new FXMLException("Can't open Opening Window", e);
        }

        this.setScene(new Scene(root));
        this.setOnCloseRequest(event -> {
            event.consume();
            quit();
        });
    }

    @FXML
    public void openFromMetamodel()
    {
        //TODO Make a better file chooser

        FileSelectionDialog dialog = new FileSelectionDialog(this);
        dialog.addFileSelector("Metamodel");
        dialog.addFileSelector("Generation Folder", true);
        dialog.addFileSelector("Compilation Folder", true);

        Optional<Map<String, String>> result = dialog.showAndWait();
        if(result.isPresent())
        {
            Map<String, String> paths = result.get();
            String metamodel = paths.get("Metamodel");
            String genDir = paths.get("Generation Folder");
            String compileDir = paths.get("Compilation Folder");

            generateAndOpen(metamodel, genDir, compileDir);
        }
    }

    @FXML
    public void openFromCode()
    {
        GraphicalNMFApplication.showAlert(Alert.AlertType.WARNING, "Not implemented yet");
    }

    @FXML
    @Override
    public void quit()
    {
        close();
    }

    @Override
    public void close()
    {
        super.close();
    }

    @Override
    public void update()
    {
        //nothing to do
    }

    /**
     * Temporary implementation of the generate and compile code.
     *
     * @param metamodel The path of the metamodel file
     * @param generationDir The path to the generation directory
     * @param compileDir The path to the compilation directory
     */
    private void generateAndOpen(String metamodel, String generationDir, String compileDir)
    {
        this.hide();

        try
        {
            //Generate
            CodeGenerator generator = new CodeGenerator(metamodel);
            String factoryName = generator.generate(generationDir);
            System.out.println("Finished generating " + generator.getEPackage().getName());

            EPackage pkg = generator.getEPackage();

            //Compile
            CodeCompiler compiler = new CodeCompiler(generationDir);
            compiler.compile(compileDir);
            System.out.println("Finished compiling class");

            //Load
            URL url = new File(compileDir).toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader loader = new URLClassLoader(urls, CodeGenerator.class.getClassLoader());
            Class<?> cl = loader.loadClass(factoryName);

            Constructor<?>[] constructors = cl.getConstructors();
            IAdapterFactory factory = (IAdapterFactory) constructors[0].newInstance(pkg);

            //Open
            application.open(factory);
            this.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getClass().getSimpleName());

            this.close();
        }
    }
}
