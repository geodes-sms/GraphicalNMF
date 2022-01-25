package geodes.sms.gnmf.application.utils.editablelists;

import geodes.sms.gnmf.application.utils.FXMLException;
import geodes.sms.gnmf.application.utils.controls.CharacterField;
import geodes.sms.gnmf.application.utils.controls.LongField;

public class EditableCharacterListWindow extends EditableListWindow<Character>
{
    public EditableCharacterListWindow() throws FXMLException
    {
        super(new LongField());
    }
    public EditableCharacterListWindow(String name) throws FXMLException
    {
        super(new LongField(), name);
    }
    public EditableCharacterListWindow(CharacterField control) throws FXMLException
    {
        super(control);
    }
    public EditableCharacterListWindow(CharacterField control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(CharacterField field)
    {
        super.setControl(field);
    }

    @Override
    public Character getInputValue()
    {
        return getControl().getValue();
    }

    @Override
    protected CharacterField getControl()
    {
        if(!(super.getControl() instanceof CharacterField))
        {
            throw new RuntimeException("Unexpected control");
        }

        return (CharacterField) super.getControl();
    }
}
