package controllers;

import adapters.AdapterException;
import adapters.INodeAdapter;

public class CommandUnloadNode implements Command
{
    private long id;
    private String label;

    public CommandUnloadNode(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws CommandException, AdapterException
    {
        PropertyWrapper wrapper = controller.packageManager.getPropertyWrapper(id);
        if(wrapper == null) throw new CommandException("Node already unloaded");

        //Unload the adapter then close the wrapper (order is important)
        controller.modelAdapter.unloadNode(wrapper.getAdapter());
        controller.packageManager.closePropertyWrapper(id, wrapper);

        //Refresh the display name of the proxy, just in case
        NodeProxy proxy = controller.proxyManager.getCompositeProxy(id);
        if(proxy != null) {
            INodeAdapter adapter = controller.modelAdapter.loadNode(label, id);
            proxy.setName(adapter.getDisplayName());
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
