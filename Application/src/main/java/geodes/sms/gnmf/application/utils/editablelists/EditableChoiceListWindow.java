package geodes.sms.gnmf.application.utils.editablelists;

import geodes.sms.gnmf.application.utils.FXMLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.Collection;

public class EditableChoiceListWindow<T> extends EditableListWindow<T>
{
    public EditableChoiceListWindow() throws FXMLException
    {
        super(new ComboBox<>());
    }
    public EditableChoiceListWindow(ObservableList<T> choices) throws FXMLException
    {
        super(new ComboBox<>(choices));
    }
    public EditableChoiceListWindow(Collection<T> choices) throws FXMLException
    {
        super(new ComboBox<>(FXCollections.observableArrayList(choices)));
    }
    public EditableChoiceListWindow(ObservableList<T> choices, String name) throws FXMLException
    {
        super(new ComboBox<>(choices), name);
    }
    public EditableChoiceListWindow(Collection<T> choices, String name) throws FXMLException
    {
        super(new ComboBox<>(FXCollections.observableArrayList(choices)), name);
    }
    public EditableChoiceListWindow(ComboBox<T> control) throws FXMLException
    {
        super(control);
    }

    public EditableChoiceListWindow(ComboBox<T> control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(ComboBox<T> control)
    {
        super.setControl(control);
    }

    public ObservableList<T> getChoices()
    {
        return getControl().getItems();
    }

    public void setChoices(ObservableList<T> choices)
    {
        getControl().setItems(choices);
        //todo reset selection
    }

    public void setChoices(Collection<T> choices)
    {
        getControl().setItems(FXCollections.observableArrayList(choices));
        //todo reset selection
    }

    @Override
    protected ComboBox<T> getControl()
    {
        if(!(super.getControl() instanceof ComboBox))
        {
            throw new RuntimeException("Unexpected control");
        }

        ComboBox<T> control;
        try
        {
            control = (ComboBox<T>) super.getControl();
        }
        catch(ClassCastException e)
        {
            throw new RuntimeException("Unexpected control");
        }

        return control;
    }

    @Override
    public T getInputValue()
    {
        return getControl().getValue();
    }
}
