package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;

public class CommandCloseNode implements Command
{
    private long id;
    private String label;

    public CommandCloseNode(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws CommandException, AdapterException
    {
        CompositeNodeProxy proxy = controller.proxyManager.getCompositeProxy(id);
        if(proxy == null) throw new CommandException("Node already closed");

        //Unload the node if it was loaded
        PropertyWrapper wrapper = controller.packageManager.getPropertyWrapper(proxy.getId());
        if(wrapper != null)
        {
            long id = proxy.getId();
            controller.modelAdapter.unloadNode(wrapper.getAdapter());
            controller.packageManager.closePropertyWrapper(id, wrapper);
        }

        controller.proxyManager.closeProxy(proxy);
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
