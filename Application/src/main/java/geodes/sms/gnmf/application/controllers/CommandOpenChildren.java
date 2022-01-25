package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;

import java.util.List;

public class CommandOpenChildren implements Command
{
    private long id;
    private String label;

    public CommandOpenChildren(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException, CommandException
    {
        List<String> childClassNames = controller.packageManager.getChildrenAssociation(label);

        for(String name : childClassNames)
        {
            new CommandOpenAssociation(id, label, name, true).execute(controller);
        }
    }

    public long getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }
}
