package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

public class CommandRemoveNode implements CommandWithSaving
{
    private long id;
    private String label;

    public CommandRemoveNode(long id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException
    {
        INodeAdapter adapter = controller.modelAdapter.loadNode(label, id);

        //Remove the wrapper (if there is one)
        PropertyWrapper wrapper = controller.packageManager.getPropertyWrapper(adapter.getId());
        if(wrapper != null) controller.packageManager.closePropertyWrapper(wrapper);

        //Remove the proxy (if there is one)
        CompositeNodeProxy proxy = controller.proxyManager.getCompositeProxy(id);
        if(proxy != null) controller.proxyManager.closeProxy(proxy);

        //TODO Check for other proxies that aren't children

        controller.modelAdapter.removeNode(adapter);        //Remove the node
        controller.saveToDabase();                          //save changes to database
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
