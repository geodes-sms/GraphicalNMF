package geodes.sms.gnmf.application.utils.editablelists;

import geodes.sms.gnmf.application.utils.FXMLException;
import geodes.sms.gnmf.application.utils.controls.IntegerField;

public class EditableIntegerListWindow extends EditableListWindow<Integer>
{
    public EditableIntegerListWindow() throws FXMLException
    {
        super(new IntegerField());
    }
    public EditableIntegerListWindow(String name) throws FXMLException
    {
        super(new IntegerField(), name);
    }
    public EditableIntegerListWindow(IntegerField control) throws FXMLException
    {
        super(control);
    }
    public EditableIntegerListWindow(IntegerField control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(IntegerField field)
    {
        super.setControl(field);
    }

    @Override
    public Integer getInputValue()
    {
        return getControl().getValue();
    }

    @Override
    protected IntegerField getControl()
    {
        if(!(super.getControl() instanceof IntegerField))
        {
            throw new RuntimeException("Unexpected control");
        }

        return (IntegerField) super.getControl();
    }
}
