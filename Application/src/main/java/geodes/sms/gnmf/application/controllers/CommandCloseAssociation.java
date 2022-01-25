package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

import java.util.List;

public class CommandCloseAssociation implements Command
{
    private long id;
    private String label;
    private String associationName;

    public CommandCloseAssociation(long id, String label, String associationName)
    {
        this.id = id;
        this.label = label;
        this.associationName = associationName;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException
    {
        INodeAdapter adapter = controller.modelAdapter.loadNode(label, id);

        List<INodeAdapter> refs = adapter.getReferenceList(associationName);
        for(INodeAdapter ref : refs)
        {
            CompositeNodeProxy proxy = controller.proxyManager.getCompositeProxy(ref.getId());
            if(proxy != null)
            {
                controller.proxyManager.closeProxy(proxy);
            }

            controller.modelAdapter.unloadNode(ref);
        }

        controller.modelAdapter.unloadNode(adapter);
    }

    public long getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }

    public String getAssociationName()
    {
        return associationName;
    }
}
