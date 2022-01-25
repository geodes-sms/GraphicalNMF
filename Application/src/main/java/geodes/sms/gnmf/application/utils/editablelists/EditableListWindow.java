package geodes.sms.gnmf.application.utils.editablelists;

import geodes.sms.gnmf.application.utils.FXMLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public abstract class EditableListWindow<T> extends Stage
{
    @FXML private BorderPane root;
    @FXML private Label lblName;
    @FXML private ListView<T> listView;
    @FXML private Pane paneInput;

    private Control input;
    //private Callback<Control, T> valueExtractor;

    /**
     * Used only by extending classes to allow the customization of
     * the control.
     */
    protected EditableListWindow() throws FXMLException
    {
        init();
    }

    public EditableListWindow(Control control) throws FXMLException
    {
        init();
        setControl(control);
    }

    public EditableListWindow(Control control, String name) throws FXMLException
    {
        init();
        setName(name);
        setControl(control);
    }

    private void init() throws FXMLException
    {
        //Create the window using fxml
        File file = new File("./src/main/resources/Editable List Window.fxml");       //fixme

        try
        {
            URL xml = file.toURI().toURL();
            FXMLLoader loader = new FXMLLoader(xml);
            loader.setLocation(xml);
            loader.setController(this);

            loader.load();
        }
        catch(Exception e)
        {
            throw new FXMLException("Can't open EditableListWindow", e);
        }

        //Setting up window properties
        this.setScene(new Scene(root));
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);

        //Create a default ContextMenu


        //Create a default ListCell factory
        listView.setCellFactory(param -> {
            TextFieldListCell<T> cell = new TextFieldListCell<>();

            ContextMenu menu = new ContextMenu();
            MenuItem item = new MenuItem("Remove");
            item.setOnAction(event -> {
                T value = cell.getItem();
                removeValue(value);
            });
            menu.getItems().add(item);

            cell.setContextMenu(menu);
            return cell;
        });
    }

    private void addValue(T value)
    {
        this.listView.getItems().add(value);
    }

    private void removeValue(T value)
    {

        this.listView.getItems().remove(value);
    }

    @FXML
    private void addElement()
    {
        T value = getInputValue();
        addValue(value);
    }

    @FXML
    private void removeElement()
    {
        //Values to delete
        ObservableList<T> selectedValues = listView.getSelectionModel().getSelectedItems();

        //Move values to another list
        ArrayList<T> toRemove = new ArrayList<>(selectedValues);

        //Clear the selection
        listView.getSelectionModel().clearSelection();

        //Remove the values
        for(T value : toRemove)
        {
            removeValue(value);
        }
    }

    //region General

    public String getName()
    {
        return lblName.getText();
    }

    public void setName(String name)
    {
        lblName.setText(name);
        this.setTitle(name);
    }

    //endregion

    //region List

    public ObservableList<T> getElements()
    {
        return this.listView.getItems();
    }

    public void setElements(ObservableList<T> elements)
    {
        this.listView.setItems(elements);
    }

    public Callback<ListView<T>, ListCell<T>> getCellFactory()
    {
        return listView.getCellFactory();
    }

    public void setCellFactory(Callback<ListView<T>, ListCell<T>> factory)
    {
        this.listView.setCellFactory(factory);
    }

    //endregion

    //region Control

    protected Control getControl()
    {
        return input;
    }

    protected void setControl(@NotNull Control control)
    {
        paneInput.getChildren().remove(this.input);
        this.input = control;
        paneInput.getChildren().add(this.input);
    }

    public abstract T getInputValue();

    //endregion







}
