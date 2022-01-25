package geodes.sms.gnmf.application.utils.editablelists;

import geodes.sms.gnmf.application.utils.FXMLException;
import geodes.sms.gnmf.application.utils.controls.LongField;

public class EditableLongListWindow extends EditableListWindow<Long>
{
    public EditableLongListWindow() throws FXMLException
    {
        super(new LongField());
    }
    public EditableLongListWindow(String name) throws FXMLException
    {
        super(new LongField(), name);
    }
    public EditableLongListWindow(LongField control) throws FXMLException
    {
        super(control);
    }
    public EditableLongListWindow(LongField control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(LongField field)
    {
        super.setControl(field);
    }

    @Override
    public Long getInputValue()
    {
        return getControl().getValue();
    }

    @Override
    protected LongField getControl()
    {
        if(!(super.getControl() instanceof LongField))
        {
            throw new RuntimeException("Unexpected control");
        }

        return (LongField) super.getControl();
    }
}
