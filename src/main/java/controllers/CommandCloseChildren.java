package controllers;

import adapters.AdapterException;

import java.util.List;

public class CommandCloseChildren implements Command
{
    private long id;
    private String label;

    public CommandCloseChildren(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException
    {
        List<String> childClassNames = controller.packageManager.getChildrenAssociation(label);

        for(String name : childClassNames)
        {
            new CommandCloseAssociation(id, label, name).execute(controller);
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
