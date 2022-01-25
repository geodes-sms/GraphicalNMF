package geodes.sms.gnmf.application.controllers.properties;

import geodes.sms.gnmf.application.controllers.NodeProxy;
import geodes.sms.gnmf.application.utils.properties.IChoiceProperty;

import java.util.Collection;
import java.util.Iterator;

public interface IAssociationProperty extends IChoiceProperty<NodeProxy>
{
    String getLabel();

    default boolean checkLabel(NodeProxy proxy)
    {
        if(proxy == null) return true;
        return proxy.getLabel().equals(getLabel());
    }

    default boolean checkLabel(Collection<NodeProxy> proxies)
    {
        boolean valid = true;
        Iterator<NodeProxy> iterator = proxies.iterator();

        while(valid && iterator.hasNext())
        {
            NodeProxy proxy = iterator.next();
            valid = proxy.getLabel().equals(getLabel());
        }

        return valid;
    }
}
