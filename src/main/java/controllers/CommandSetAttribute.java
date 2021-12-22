package controllers;

import adapters.AdapterException;
import adapters.INodeAdapter;
import controllers.properties.AssociationListProperty;
import controllers.properties.AssociationProperty;
import utils.properties.IProperty;
import utils.properties.IValueProperty;

import java.util.ArrayList;
import java.util.List;

public class CommandSetAttribute implements CommandWithSaving
{
    private IProperty property;
    private PropertyWrapper wrapper;

    public CommandSetAttribute(IProperty property, PropertyWrapper wrapper)
    {
        this.property = property;
        this.wrapper = wrapper;
    }

    @Override
    public void execute(DatabaseController controller) throws CommandException, AdapterException
    {
        if(property instanceof AssociationProperty)
        {
            setReference((AssociationProperty) property, wrapper.getAdapter(), controller);
        }
        else if(property instanceof AssociationListProperty)
        {
            setReferenceList((AssociationListProperty) property, wrapper.getAdapter(), controller);
        }
        else if(property instanceof IValueProperty<?>)
        {
            setAttribute((IValueProperty<?>) property, wrapper.getAdapter(), controller);
        }
        else
        {
            throw new CommandException("Can't handle " + property.getClass().getSimpleName());
        }

        controller.saveToDabase();
    }

    protected <T> void setAttribute(IValueProperty<T> property, INodeAdapter adapter,
                                    DatabaseController controller) throws AdapterException
    {
        adapter.setAttribute(property.getName(), property.getValue());

        //Refresh the display name of the proxy, just in case
        NodeProxy proxy = controller.proxyManager.getCompositeProxy(adapter.getId());
        if(proxy != null) proxy.setName(adapter.getDisplayName());
    }

    protected void setReference(AssociationProperty property, INodeAdapter adapter,
                                DatabaseController controller) throws AdapterException
    {
        //Unset current reference
        INodeAdapter currentRef = adapter.getReference(property.getName());
        if(currentRef != null)
        {
            adapter.unsetReference(property.getName(), currentRef);
            controller.modelAdapter.unloadNode(currentRef);
        }

        //Set new reference
        NodeProxy proxy = property.getValue();
        if(proxy != null)
        {
            INodeAdapter ref = controller.modelAdapter.loadNode(proxy.getLabel(), proxy.getId());
            adapter.setReference(property.getName(), ref);
            controller.modelAdapter.unloadNode(ref);
        }
    }

    protected void setReferenceList(AssociationListProperty property, INodeAdapter adapter,
                                    DatabaseController controller) throws AdapterException
    {
        //Covert the proxy list to a adapter list
        ArrayList<INodeAdapter> newRefs = new ArrayList<>();
        for(NodeProxy proxy : property.getValue())
        {
            INodeAdapter ref = controller.modelAdapter.loadNode(proxy.getLabel(), proxy.getId());
            newRefs.add(ref);
        }

        //Remove references
        List<INodeAdapter> currentRefs = adapter.getReferenceList(property.getName());
        for(INodeAdapter ref : currentRefs)
        {
            //If not in the new references then remove it from the adapter
            if(!newRefs.contains(ref))       //maybe always false -> to check
            {
                adapter.unsetReference(property.getName(), ref);
            }
            //If yes, remove it from the new references (no need to be added)
            else
            {
                newRefs.remove(ref);
            }

            controller.modelAdapter.unloadNode(ref);        //might cause problem
        }

        //Add references
        for(INodeAdapter ref : newRefs)
        {
            adapter.setReference(property.getName(), ref);
            controller.modelAdapter.unloadNode(ref);
        }
    }

    public IProperty getProperty()
    {
        return property;
    }

    public PropertyWrapper getWrapper()
    {
        return wrapper;
    }
}
