package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.CentralTopic;
import geodes.sms.nmf.editor.mindMaps.MainTopic;
import geodes.sms.nmf.editor.mindMaps.Marker;

import java.util.ArrayList;
import java.util.List;

public class CentralTopicNodeAdapter extends AbstractNodeAdapter
{
    private CentralTopic entity;

    public CentralTopicNodeAdapter(CentralTopic entity)
    {
        this.entity = entity;
    }

    @Override
    public <T> void setAttribute(String attrName, T attrValue) throws NoSuchAttributeException
    {
        switch(attrName.toLowerCase())
        {
            case "keywords":
                entity.setKeywords((List<java.lang.String>) attrValue);
                break;

            case "name":
                entity.setName((java.lang.String) attrValue);
                break;

            case "priority":
                entity.setPriority((long) attrValue);
                break;


            default:
                throw new NoSuchAttributeException(attrName, "CentralTopic");
        }
    }

    @Override
    public <T> T getAttribute(String attrName) throws NoSuchAttributeException
    {
        T attr = null;

        switch(attrName.toLowerCase())
        {
            case "keywords":
                attr = (T) entity.getKeywords();
                break;

            case "name":
                attr = (T) entity.getName();
                break;

            case "priority":
                attr = (T) entity.getPriority();
                break;


            default:
                throw new NoSuchAttributeException(attrName, "CentralTopic");
        }

        return attr;
    }

    @Override
    protected INodeEntity addChildDelegate(String refName, String type) throws NoSuchAttributeException
    {
        INodeEntity child;

        switch(refName.toLowerCase())
        {
            case "maintopic":
                child = entity.addMainTopic();
                break;


            default:
                throw new NoSuchAttributeException(refName, "CentralTopic");
        }

        return child;
    }

    @Override
    protected void setReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "marker":
                entity.setMarker((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "CentralTopic");
        }
    }

    @Override
    protected void unsetReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "maintopic":
                entity.unsetMainTopic((MainTopic) ref);
                break;

            case "marker":
                entity.unsetMarker((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "CentralTopic");
        }
    }

    @Override
    protected INodeEntity getReferenceDelegate(String refName) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "CentralTopic");
    }

    //FIXME Do something about limits
    @Override
    protected List<INodeEntity> getReferenceListDelegate(String refName) throws NoSuchAttributeException
    {
        ArrayList<INodeEntity> refs;

        switch (refName.toLowerCase())
        {
            case "maintopic":
                refs = new ArrayList<>(entity.getMainTopic(1000));
                break;

            case "marker":
                refs = new ArrayList<>(entity.getMarker(1000));
                break;

            default:
                throw new NoSuchAttributeException(refName, "CentralTopic");
        }

        return refs;
    }

    @Override
    protected INodeEntity getNode()
    {
        return entity;
    }

    @Override
    protected void setNode(INodeEntity node) throws AdapterException
    {
        if(!(node instanceof CentralTopic)) {
            throw new AdapterException("CentralTopicNodeAdapter can't have " + node.getLabel() + " as a node");
        }

        this.entity = (CentralTopic) node;
    }

    @Override
    public String getDisplayName()
    {
        return String.valueOf(entity.getName());
    }
}
