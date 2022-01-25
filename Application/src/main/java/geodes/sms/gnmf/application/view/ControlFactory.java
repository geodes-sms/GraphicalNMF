package geodes.sms.gnmf.application.view;

import geodes.sms.gnmf.application.utils.FXMLException;
import geodes.sms.gnmf.application.utils.controls.*;
import geodes.sms.gnmf.application.utils.editablelists.*;
import geodes.sms.gnmf.application.utils.properties.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

/**
 * It works, but it isn't ideal
 */

public final class ControlFactory
{
    public static Control createControl(IProperty property, EventHandler<ActionEvent> onAction)
    {
        if(property instanceof ICollectionProperty<?>)
        {
            return createControl((ICollectionProperty<?>) property, onAction);
        }
        if(property instanceof IValueProperty)
        {
            return createControl((IValueProperty<?>) property, onAction);
        }

        throw new RuntimeException("Can't create a control for " + property);
    }

    public static <T> Control createControl(IValueProperty<T> property,
                                            EventHandler<ActionEvent> onAction)
    {
        if(property instanceof StringProperty)
        {
            return createStringControl((StringProperty) property, onAction);
        }
        if(property instanceof CharacterProperty)
        {
            return createCharacterControl((CharacterProperty) property, onAction);
        }
        if(property instanceof IntegerProperty)
        {
            return createIntegerControl((IntegerProperty) property, onAction);
        }
        if(property instanceof FloatProperty)
        {
            return createFloatControl((FloatProperty) property, onAction);
        }
        if(property instanceof LongProperty)
        {
            return createLongControl((LongProperty) property, onAction);
        }
        if(property instanceof DoubleProperty)
        {
            return createDoubleControl((DoubleProperty) property, onAction);
        }
        if(property instanceof BooleanProperty)
        {
            return createBooleanControl((BooleanProperty) property, onAction);
        }
        if(property instanceof ValueChoiceProperty)
        {
            return createChoiceControl((ValueChoiceProperty<?>) property, onAction);
        }

        throw new RuntimeException("Can't create a control for " + property);
    }

    public static <T> Button createControl(ICollectionProperty<T> property,
                                           EventHandler<ActionEvent> onAction)
    {
        if(property instanceof ListChoiceProperty)
        {
            return createChoiceListControl((ListChoiceProperty<T>) property, onAction);
        }
        if(property instanceof StringListProperty)
        {
            return createStringListControl((StringListProperty) property, onAction);
        }
        if(property instanceof IntegerListProperty)
        {
            return createIntegerListControl((IntegerListProperty) property, onAction);
        }
        if(property instanceof LongListProperty)
        {
            return createLongListControl((LongListProperty) property, onAction);
        }
        if(property instanceof FloatListProperty)
        {
            return createFloatListControl((FloatListProperty) property, onAction);
        }
        if(property instanceof DoubleListProperty)
        {
            return createDoubleListControl((DoubleListProperty) property, onAction);
        }
        if(property instanceof CharacterListProperty)
        {
            return createCharacterListControl((CharacterListProperty) property, onAction);
        }

        throw new RuntimeException("Can't create a control for " + property);
    }

    public static TextField createStringControl(ValueProperty<String> property,
                                                EventHandler<ActionEvent> onAction)
    {
        TextField field = new TextField();
        if(property.getValue() != null)
        {
            field.setText(property.getValue());
        }

        field.setOnAction(event -> {
            property.setValue(field.getText());
            if(onAction != null) onAction.handle(event);
        });

        return field;
    }

    public static CharacterField createCharacterControl(ValueProperty<Character> property,
                                                        EventHandler<ActionEvent> onAction)
    {
        CharacterField field = new CharacterField();
        if(property.getValue() != null)
        {
            field.setValue(property.getValue());
        }

        field.setOnAction(event -> {
            property.setValue(field.getValue());
            if(onAction != null) onAction.handle(event);
        });

        return field;
    }

    public static IntegerField createIntegerControl(ValueProperty<Integer> property,
                                                    EventHandler<ActionEvent> onAction)
    {
        IntegerField field = new IntegerField();
        if(property.getValue() != null)
        {
            field.setValue(property.getValue());
        }

        field.setOnAction(event -> {
            property.setValue(field.getValue());
            if(onAction != null) onAction.handle(event);
        });

        return field;
    }

    public static FloatField createFloatControl(ValueProperty<Float> property,
                                                EventHandler<ActionEvent> onAction)
    {
        FloatField field = new FloatField();
        if(property.getValue() != null)
        {
            field.setValue(property.getValue());
        }

        field.setOnAction(event -> {
            property.setValue(field.getValue());
            if(onAction != null) onAction.handle(event);
        });

        return field;
    }

    public static LongField createLongControl(ValueProperty<Long> property,
                                              EventHandler<ActionEvent> onAction)
    {
        LongField field = new LongField();
        if(property.getValue() != null)
        {
            field.setValue(property.getValue());
        }

        field.setOnAction(event -> {
            property.setValue(field.getValue());
            if(onAction != null) onAction.handle(event);
        });

        return field;
    }

    public static DoubleField createDoubleControl(ValueProperty<Double> property,
                                                  EventHandler<ActionEvent> onAction)
    {
        DoubleField field = new DoubleField(property.getValue());
        field.setOnAction(event -> {
            property.setValue(field.getValue());
            if(onAction != null) onAction.handle(event);
        });

        return field;
    }

    public static RadioButton createBooleanControl(ValueProperty<Boolean> property,
                                                   EventHandler<ActionEvent> onAction)
    {
        RadioButton btn = new RadioButton();
        if(property.getValue() != null)
        {
            btn.setSelected(property.getValue());
        }

        btn.setOnAction(event -> {
            property.setValue(btn.isSelected());
            if(onAction != null) onAction.handle(event);
        });

        return btn;
    }

    public static <T> ComboBox<T> createChoiceControl(ValueChoiceProperty<T> property,
                                                      EventHandler<ActionEvent> onAction)
    {
        ComboBox<T> box = new ComboBox<>();

        //Set the items
        ObservableList<T> items = FXCollections.observableArrayList(property.getChoices());
        items.add(null);
        box.setItems(items);

        //Set the value
        if(property.getValue() != null)
        {
            box.setValue(property.getValue());
        }

        box.setOnAction(event -> {
            property.setValue(box.getValue());
            if(onAction != null) onAction.handle(event);
        });

        return box;
    }

    public static Button createStringListControl(ListProperty<String> property,
                                                 EventHandler<ActionEvent> onAction)
    {
        //Add a button to open an EditableListWindow
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the items window
                EditableTextListWindow window = new EditableTextListWindow(property.getName());

                ObservableList<String> items = FXCollections.observableArrayList(property.getValue());
                window.setElements(items);

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of items
                property.setValue(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }

    public static Button createIntegerListControl(ListProperty<Integer> property,
                                                  EventHandler<ActionEvent> onAction)
    {
        //Add a button to open an EditableListWindow
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the items window
                EditableListWindow<Integer> window = new EditableIntegerListWindow(property.getName());

                ObservableList<Integer> items = FXCollections.observableArrayList(property.getValue());
                window.setElements(items);

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of items
                property.setValue(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }

    public static Button createLongListControl(ListProperty<Long> property,
                                               EventHandler<ActionEvent> onAction)
    {
        //Add a button to open an EditableListWindow
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the items window
                EditableListWindow<Long> window = new EditableLongListWindow(property.getName());

                ObservableList<Long> items = FXCollections.observableArrayList(property.getValue());
                window.setElements(items);

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of items
                property.setValue(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }

    public static Button createFloatListControl(ListProperty<Float> property,
                                                EventHandler<ActionEvent> onAction)
    {
        //Add a button to open an EditableListWindow
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the items window
                EditableListWindow<Float> window = new EditableFloatListWindow(property.getName());

                ObservableList<Float> items = FXCollections.observableArrayList(property.getValue());
                window.setElements(items);

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of items
                property.setValue(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }

    public static Button createDoubleListControl(ListProperty<Double> property,
                                                 EventHandler<ActionEvent> onAction)
    {
        //Add a button to open an EditableListWindow
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the items window
                EditableListWindow<Double> window = new EditableDoubleListWindow(property.getName());

                ObservableList<Double> items = FXCollections.observableArrayList(property.getValue());
                window.setElements(items);

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of items
                property.setValue(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }

    public static Button createCharacterListControl(ListProperty<Character> property,
                                                    EventHandler<ActionEvent> onAction)
    {
        //Add a button to open an EditableListWindow
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the items window
                EditableListWindow<Character> window = new EditableCharacterListWindow(property.getName());

                ObservableList<Character> items = FXCollections.observableArrayList(property.getValue());
                window.setElements(items);

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of items
                property.setValue(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }

    public static <T> Button createChoiceListControl(ListChoiceProperty<T> property,
                                                     EventHandler<ActionEvent> onAction)
    {
        Button btn = new Button("Edit " + property.getName());
        btn.setOnAction(event -> {
            try
            {
                //Create the editable list window
                EditableListWindow<T> window = new EditableChoiceListWindow<>(property.getChoices(), property.getName());
                window.setElements(FXCollections.observableArrayList(property.getValue()));

                //Show the window and wait for it to close
                window.showAndWait();

                //Update the list of proxies
                property.clear();
                property.addAll(window.getElements());
                if(onAction != null) onAction.handle(event);
            }
            catch(FXMLException e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open window for " + property.getName());
                e.printStackTrace();
            }
        });

        return btn;
    }
}
