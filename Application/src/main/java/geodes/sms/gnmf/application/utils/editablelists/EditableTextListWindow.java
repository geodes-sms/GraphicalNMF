package geodes.sms.gnmf.application.utils.editablelists;

import geodes.sms.gnmf.application.utils.FXMLException;
import javafx.scene.control.TextField;


public class EditableTextListWindow extends EditableListWindow<String>
{
    public EditableTextListWindow() throws FXMLException
    {
        super(new TextField());
    }
    public EditableTextListWindow(String name) throws FXMLException
    {
        super(new TextField(), name);
    }
    public EditableTextListWindow(TextField control) throws FXMLException
    {
        super(control);
    }
    public EditableTextListWindow(TextField control, String name) throws FXMLException
    {
        super(control, name);
    }

    public void setControl(TextField field)
    {
        super.setControl(field);
    }

    @Override
    public String getInputValue()
    {
        return getControl().getText();
    }

    @Override
    protected TextField getControl()
    {
        if(!(super.getControl() instanceof TextField))
        {
            throw new RuntimeException("Unexpected control");
        }

        return (TextField) super.getControl();
    }
}
