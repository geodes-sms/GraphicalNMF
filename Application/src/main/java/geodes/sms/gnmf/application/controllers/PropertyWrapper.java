package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.application.utils.properties.IProperty;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

import java.util.HashMap;
import java.util.Iterator;

public class PropertyWrapper
{
    private final INodeAdapter adapter;

    private final HashMap<String, IProperty> properties = new HashMap<>();

    public PropertyWrapper(INodeAdapter adapter)
    {
        this.adapter = adapter;
    }

    void addProperty(IProperty property)
    {
        if(property == null)
        {
            throw new IllegalArgumentException("Does not support null property");
        }
        else if(properties.containsKey(property.getName()))
        {
            throw new IllegalArgumentException("Duplicate property name");
        }

        properties.put(property.getName(), property);
    }

    void removeProperty(IProperty property)
    {
        if(property == null) return;
        else if(!properties.containsKey(property.getName())) return;

        properties.remove(property.getName());
    }

    INodeAdapter getAdapter()
    {
        return adapter;
    }

    public Iterator<IProperty> propertyIterator()
    {
        return properties.values().iterator();
    }

    public IProperty getProperty(String name)
    {
        if(name == null)
        {
            throw new IllegalArgumentException();
        }
        else if(!properties.containsKey(name))
        {
            return null;
        }

        return properties.get(name);
    }

    public long getId()
    {
        return adapter.getId();
    }

    public String getLabel()
    {
        return adapter.getLabel();
    }

    public String getDisplayName()
    {
        return adapter.getDisplayName();
    }
}
