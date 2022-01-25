package geodes.sms.gnmf.application.controllers.properties;

import geodes.sms.gnmf.application.controllers.NodeProxy;
import geodes.sms.gnmf.application.utils.properties.ListChoiceProperty;

import java.util.Collection;

public class AssociationListProperty extends ListChoiceProperty<NodeProxy> implements IAssociationProperty
{
    private String label;

    public AssociationListProperty(String name, String label)
    {
        super(name);
        this.label = label;
    }

    public AssociationListProperty(String name, String label, Collection<NodeProxy> choices)
    {
        super(name, choices);
        this.label = label;

        if(!checkLabel(choices)) throw new IllegalArgumentException("Proxies of wrong label");
    }

    @Override
    public boolean addValue(NodeProxy value)
    {
        if(!checkLabel(value)) throw new IllegalArgumentException("Proxy of wrong label");

        return super.addValue(value);
    }

    @Override
    public boolean addAll(Collection<NodeProxy> values)
    {
        if(!checkLabel(values)) throw new IllegalArgumentException("Proxies of wrong label");

        return super.addAll(values);
    }

    @Override
    public boolean retainAll(Collection<NodeProxy> values)
    {
        if(!checkLabel(values)) throw new IllegalArgumentException("Proxies of wrong label");

        return super.retainAll(values);
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
