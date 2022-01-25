package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

import java.util.List;

public class CommandOpenAssociation implements Command
{
    private long id;
    private String label;
    private String associationName;
    private boolean asChildren = false;

    public CommandOpenAssociation(long id, String label, String associationName)
    {
        this.id = id;
        this.label = label;
        this.associationName = associationName;
    }
    public CommandOpenAssociation(long id, String label, String associationName, boolean asChildren)
    {
        this.id = id;
        this.label = label;
        this.associationName = associationName;
        this.asChildren = asChildren;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException, CommandException
    {
        INodeAdapter adapter = controller.modelAdapter.loadNode(label, id);

        //Get the parent proxy, if the proxies are supposed to be opened as children
        CompositeNodeProxy parentProxy = null;
        if(asChildren)
        {
            parentProxy = controller.proxyManager.getCompositeProxy(id);
            if(parentProxy == null) throw new CommandException("Parent node not opened");
        }

        //Load the association
        List<INodeAdapter> refs = adapter.getReferenceList(associationName);
        for(INodeAdapter ref : refs)
        {
            CompositeNodeProxy child = controller.proxyManager.getCompositeProxy(ref.getId());

            //If not already opened, open it
            if(child == null)
            {
                child = controller.proxyManager.openProxy(ref);
            }

            //Add the new proxy to the parent, if necessary
            if(asChildren)
            {
                parentProxy.addChild(child);
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
