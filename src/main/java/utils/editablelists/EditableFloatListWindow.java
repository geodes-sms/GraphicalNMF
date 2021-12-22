package utils.editablelists;

import utils.controls.FloatField;
import utils.controls.LongField;
import view.FXMLException;

public class EditableFloatListWindow extends EditableListWindow<Float>
{
    public EditableFloatListWindow() throws FXMLException
    {
        super(new LongField());
    }
    public EditableFloatListWindow(String name) throws FXMLException
    {
        super(new LongField(), name);
    }
    public EditableFloatListWindow(FloatField control) throws FXMLException
    {
        super(control);
    }
    public EditableFloatListWindow(FloatField control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(FloatField field)
    {
        super.setControl(field);
    }

    @Override
    public Float getInputValue()
    {
        return getControl().getValue();
    }

    @Override
    protected FloatField getControl()
    {
        if(!(super.getControl() instanceof FloatField))
        {
            throw new RuntimeException("Unexpected control");
        }

        return (FloatField) super.getControl();
    }
}
