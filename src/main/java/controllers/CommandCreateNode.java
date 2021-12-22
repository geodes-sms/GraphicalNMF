package controllers;

import adapters.AdapterException;
import adapters.INodeAdapter;

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
