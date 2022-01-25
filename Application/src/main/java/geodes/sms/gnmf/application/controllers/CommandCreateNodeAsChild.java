package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

public class CommandCreateNodeAsChild extends CommandCreateNode
{
    private String refName;
    private long parentId;
    private String parentLabel;

    public CommandCreateNodeAsChild(String label, String refName, long parentId, String parentLabel)
    {
        super(label);
        this.refName = refName;
        this.parentId = parentId;
        this.parentLabel = parentLabel;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException, CommandException
    {
        //Get the parent proxy
        CompositeNodeProxy parentProxy = controller.proxyManager.getCompositeProxy(parentId);
        if(parentProxy == null) throw new CommandException("Parent not opened");

        //Load the parent
        INodeAdapter parentAdapter = controller.modelAdapter.loadNode(parentLabel, parentId);

        //Create & Open the child
        INodeAdapter childAdapter = parentAdapter.addChild(refName, getLabel());
        controller.saveToDabase();                                                  //save changes to database
        CompositeNodeProxy childProxy = controller.proxyManager.openProxy(childAdapter);

        //Place the child in the parent
        parentProxy.addChild(childProxy);

        //Unload nodes
        controller.modelAdapter.unloadNode(parentAdapter);
        controller.modelAdapter.unloadNode(childAdapter);
    }

    public String getRefName()
    {
        return refName;
    }

    public long getParentId()
    {
        return parentId;
    }

    public String getParentLabel()
    {
        return parentLabel;
    }
}
