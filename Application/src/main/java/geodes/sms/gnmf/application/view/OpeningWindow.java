package geodes.sms.gnmf.application.view;

import geodes.sms.gnmf.application.utils.FXMLException;
import geodes.sms.gnmf.application.utils.FileSelectionDialog;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;
import java.util.Optional;

/** FIXME
 * Factory loading should be done in the compiler
 */

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
            File file = new File(GraphicalNMFApplication.RESSOURCE_PATH+"Opening Window.fxml");  //fixme
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

            this.hide();
            application.generateAndOpen(metamodel, genDir, compileDir);
            this.close();
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
}
