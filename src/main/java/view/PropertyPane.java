package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utils.properties.IProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyPane extends ScrollPane
{
    private final ArrayList<IProperty> shownProperties = new ArrayList<>();
    private final HashMap<String, HBox> controls = new HashMap<>();

    private VBox listBox;

    public PropertyPane()
    {
        super();
        init();
    }

    protected void init()
    {
        listBox = new VBox();
        listBox.setAlignment(Pos.TOP_CENTER);
        listBox.setPadding(new Insets(10));
        listBox.setSpacing(10);

        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setContent(listBox);
    }

    public String addControl(IProperty property, EventHandler<ActionEvent> onAction)
    {
        Control control = ControlFactory.createControl(property, onAction);

        shownProperties.add(property);
        addControl(property.getName(), control);

        return property.getName();
    }

    public void addControl(String label, Control control)
    {
        //Check if the label already exist
        if(!controls.containsKey(label))
        {
            //Create a label
            Label lbl = new Label(label);
            lbl.setLabelFor(control);

            //Create the box with the label and the control
            HBox box = createControlBox();
            box.getChildren().add(lbl);
            box.getChildren().add(control);

            //Add the box to the view
            listBox.getChildren().add(box);

            //Add the control to the list
            controls.put(label, box);
        }
    }

    public boolean removeControl(IProperty property)
    {
        if(shownProperties.contains(property))
        {
            shownProperties.remove(property);

            return removeControl(property.getName());
        }

        return false;
    }

    public boolean removeControl(String label)
    {
        HBox box = controls.remove(label);

        if(box != null)
        {
            listBox.getChildren().remove(box);
            return true;
        }

        return false;
    }

    public void clearControls()
    {
        shownProperties.clear();
        controls.clear();

        listBox.getChildren().clear();
    }

    protected HBox createControlBox()
    {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefHeight(Region.USE_COMPUTED_SIZE);
        box.setSpacing(10);

        return box;
    }
}
