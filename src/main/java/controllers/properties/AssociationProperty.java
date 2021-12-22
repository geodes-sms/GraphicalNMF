package controllers.properties;

import controllers.NodeProxy;
import utils.properties.ValueChoiceProperty;

import java.util.Collection;

public class AssociationProperty extends ValueChoiceProperty<NodeProxy> implements IAssociationProperty
{
    private String label;

    public AssociationProperty(String name, String label)
    {
        super(name);
        this.label = label;
    }

    public AssociationProperty(String name, String label, Collection<NodeProxy> choices)
    {
        super(name, choices);
        this.label = label;

        if(!checkLabel(choices)) {
            throw new IllegalArgumentException("Proxies of wrong label");
        }
    }

    public AssociationProperty(String name, String label, Collection<NodeProxy> choices, NodeProxy value)
    {
        super(name, choices, value);
        this.label = label;

        if(!checkLabel(choices) && !checkLabel(value)) {
            throw new IllegalArgumentException("Proxies of wrong label");
        }
    }

    @Override
    public boolean setValue(NodeProxy value)
    {
        if(!checkLabel(value)) throw new IllegalArgumentException("Proxy of wrong label");

        return super.setValue(value);
    }

    @Override
    public void setChoices(Collection<NodeProxy> choices)
    {
        if(!checkLabel(choices)) throw new IllegalArgumentException("Proxies of wrong label");

        super.setChoices(choices);
    }

    @Override
    public boolean isValidValue(NodeProxy value)
    {
        return super.isValidValue(value) && checkLabel(value);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
