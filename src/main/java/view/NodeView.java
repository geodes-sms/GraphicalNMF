package view;

import controllers.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.properties.IProperty;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

public class NodeView extends Stage implements View
{
    protected final DatabaseController controller;
    protected final PropertyWrapper wrapper;

    private Pane root;

    @FXML private Label nodeLabel;
    @FXML private AnchorPane mainPane;
    private PropertyPane propertyPane;

    public NodeView(DatabaseController controller, PropertyWrapper wrapper) throws FXMLException
    {
        this.controller = controller;
        this.wrapper = wrapper;

        init();
        showProperties();
        setHeader(wrapper.getLabel() + ": " + wrapper.getDisplayName());
    }

    private void init() throws FXMLException
    {
        File file = new File("..\\Prototypes\\Node View.fxml");     //fixme

        try
        {
            URL xml = file.toURI().toURL();
            FXMLLoader loader = new FXMLLoader(xml);
            loader.setLocation(xml);
            loader.setController(this);

            root = loader.load();
            this.setScene(new Scene(root));
        }
        catch(Exception e)
        {
            throw new FXMLException("Can't open NodeView", e);
        }

        //Setup stage properties
        this.setTitle("Node View");
        this.initModality(Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(event -> {
            quit();
            event.consume();
        });

        //Init the property pane
        propertyPane = new PropertyPane();
        AnchorPane.setBottomAnchor(propertyPane, 0.0);
        AnchorPane.setTopAnchor(propertyPane, 0.0);
        AnchorPane.setLeftAnchor(propertyPane, 0.0);
        AnchorPane.setRightAnchor(propertyPane, 0.0);
        mainPane.getChildren().add(propertyPane);

        //Register as observer to the CommandManager
        controller.commandManager.addObserver(this);
    }

    /**
     * Save changes to the database. Unsaved changes will be
     * discarded when this screen is closed.
     */
    @FXML
    public void save()
    {
        controller.saveToDabase();
    }

    /**
     * Request the view to close. This will prompt the user for confirmation,
     * and will not close if the user reject the confirmation.
     */
    @FXML
    public void quit()
    {
        boolean result = GraphicalNMFApplication.askForConfirmation("Are you sure?");
        if(result)
        {
            close();
        }
    }

    /**
     * Save then quit.
     */
    @FXML
    public void saveAndQuit()
    {
        save();
        quit();
    }

    /**
     * Close the stage. </p>
     *
     * In general, you should use {@link #quit()} instead as it
     * prompts the user for confirmation.
     */
    @Override
    public void close()
    {
        controller.commandManager.removeObserver(this);
        super.close();
    }

    @Override
    public void update()
    {
        //Update the header
        setHeader(wrapper.getLabel() + ": " + wrapper.getDisplayName());

        //Update the controls
        try
        {
            controller.packageManager.refresh(wrapper);
            showProperties();
        }
        catch(Exception e)
        {
            GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    protected void showProperties()
    {
        propertyPane.clearControls();

        //Recreate the controls for each property of the wrapper
        Iterator<IProperty> iterator = wrapper.propertyIterator();
        while(iterator.hasNext())
        {
            IProperty property = iterator.next();

            propertyPane.addControl(property, event -> {
                event.consume();
                executeModification(property);
            });
        }
    }

    protected void setHeader(String header)
    {
        nodeLabel.setText(header);
    }

    private void executeModification(IProperty property)
    {
        Command cmd = new CommandSetAttribute(property, wrapper);
        ExecutionResult result = controller.commandManager.execute(cmd);
        GraphicalNMFApplication.handleExecutionResult(result);

        update();       //Refresh the controls
    }
}
