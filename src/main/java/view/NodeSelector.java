package view;

import controllers.DatabaseController;
import controllers.NodeProxy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NodeSelector extends Stage
{
    private final DatabaseController controller;

    @FXML private ListView<NodeProxy> elementsView;
    @FXML private ComboBox<String> classSelector;

    private ObservableList<NodeProxy> elementsShown;

    public NodeSelector(DatabaseController controller) throws FXMLException
    {
        this.controller = controller;

        init();
    }

    private void init() throws FXMLException
    {
        //Create the window using fxml
        File file = new File("..\\Prototypes\\Class Selector Window.fxml");       //fixme

        try
        {
            URL xml = file.toURI().toURL();
            FXMLLoader loader = new FXMLLoader(xml);
            loader.setLocation(xml);
            loader.setController(this);

            //Open the pane
            Parent root = loader.load();
            this.setScene(new Scene(root));
        }
        catch(Exception e)
        {
            throw new FXMLException("Can't open NodeSelector", e);
        }

        //Setting up window properties
        this.setTitle("Open Elements");
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);

        this.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            close();
        });

        //Setting up the class selector
        ObservableList<String> classes = FXCollections.observableArrayList(
                controller.packageManager.getClasses());
        classSelector.setItems(classes);

        //Setting up the elements view
        elementsShown = FXCollections.observableArrayList();
        elementsView.setItems(elementsShown);
        elementsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void selectClass()
    {
        String selectedClass = classSelector.getValue();

        List<NodeProxy> proxies = new ArrayList<>();
        if(controller.isConnected())
        {
            try
            {
                proxies = controller.getListOfNodes(selectedClass);     //Get the proxies
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        //Add the proxies to the view
        elementsShown.clear();                                          //Remove the currently shown proxies
        elementsShown.addAll(proxies);                                  //Add the proxies
    }

    @FXML
    private void cancelSelection()
    {
        elementsView.getSelectionModel().clearSelection();
        close();
    }

    @FXML
    @Override
    public void close()
    {
        super.close();
    }

    public List<NodeProxy> getSelectedNodes()
    {
        return elementsView.getSelectionModel().getSelectedItems();
    }
}
