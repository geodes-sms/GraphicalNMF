package utils.editablelists;

import utils.controls.DoubleField;
import utils.controls.LongField;
import view.FXMLException;

public class EditableDoubleListWindow extends EditableListWindow<Double>
{
    public EditableDoubleListWindow() throws FXMLException
    {
        super(new LongField());
    }
    public EditableDoubleListWindow(String name) throws FXMLException
    {
        super(new LongField(), name);
    }
    public EditableDoubleListWindow(DoubleField control) throws FXMLException
    {
        super(control);
    }
    public EditableDoubleListWindow(DoubleField control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(DoubleField field)
    {
        super.setControl(field);
    }

    @Override
    public Double getInputValue()
    {
        return getControl().getValue();
    }

    @Override
    protected DoubleField getControl()
    {
        if(!(super.getControl() instanceof DoubleField))
        {
            throw new RuntimeException("Unexpected control");
        }

        return (DoubleField) super.getControl();
    }
}
