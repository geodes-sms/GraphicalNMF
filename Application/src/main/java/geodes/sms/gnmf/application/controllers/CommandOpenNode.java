package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

public class CommandOpenNode implements Command
{
    private long id;
    private String label;

    public CommandOpenNode(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws CommandException, AdapterException
    {
        if(controller.proxyManager.getCompositeProxy(id) != null) throw new CommandException("Node already opened");

        INodeAdapter adapter = controller.modelAdapter.loadNode(label, id);     //Load the node
        controller.proxyManager.openProxy(adapter);                             //Create a proxy for the node
        controller.modelAdapter.unloadNode(adapter);                            //Unload the node
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
