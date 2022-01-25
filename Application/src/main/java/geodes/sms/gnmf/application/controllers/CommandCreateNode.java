package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.AdapterException;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

public class CommandCreateNode implements CommandWithSaving
{
    private String label;

    public CommandCreateNode(String label)
    {
        this.label = label;
    }

    @Override
    public void execute(DatabaseController controller) throws AdapterException, CommandException
    {
        INodeAdapter adapter = controller.modelAdapter.createNode(label);       //Create a node

        controller.saveToDabase();                                              //save changes to database

        controller.proxyManager.openProxy(adapter);                             //Open the newly created node

        controller.modelAdapter.unloadNode(adapter);
    }

    public String getLabel()
    {
        return label;
    }
}
