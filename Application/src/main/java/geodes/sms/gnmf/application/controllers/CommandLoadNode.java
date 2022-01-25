package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

public class CommandLoadNode implements Command
{
    private long id;
    private String label;

    public CommandLoadNode(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException, CommandException
    {
        PropertyWrapper wrapper = controller.packageManager.getPropertyWrapper(id);
        if(wrapper != null) throw new CommandException("Node already loaded");

        //Load the node
        INodeAdapter adapter = controller.modelAdapter.loadNode(label, id);

        //Open a property wrapper for the node
        try
        {
            controller.packageManager.openPropertyWrapper(adapter, true, false, true);      //do not load children
        }
        catch(Exception e)
        {
            controller.modelAdapter.unloadNode(adapter);
            throw new CommandException(e);
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
