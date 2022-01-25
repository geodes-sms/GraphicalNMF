package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.CentralTopic;
import geodes.sms.nmf.editor.mindMaps.MainTopic;
import geodes.sms.nmf.editor.mindMaps.Marker;
import geodes.sms.nmf.editor.mindMaps.SubTopic;

import java.util.ArrayList;
import java.util.List;

public class MainTopicNodeAdapter extends AbstractNodeAdapter
{
    private MainTopic entity;

    public MainTopicNodeAdapter(MainTopic entity)
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
                throw new NoSuchAttributeException(attrName, "MainTopic");
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
                throw new NoSuchAttributeException(attrName, "MainTopic");
        }

        return attr;
    }

    @Override
    protected INodeEntity addChildDelegate(String refName, String type) throws NoSuchAttributeException
    {
        INodeEntity child;

        switch(refName.toLowerCase())
        {
            case "subtopics":
                child = entity.addSubTopics();
                break;


            default:
                throw new NoSuchAttributeException(refName, "MainTopic");
        }

        return child;
    }

    @Override
    protected void setReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "centraltopic":
                entity.setCentralTopic((CentralTopic) ref);
                break;

            case "marker":
                entity.setMarker((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "MainTopic");
        }
    }

    @Override
    protected void unsetReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "subtopics":
                entity.unsetSubTopics((SubTopic) ref);
                break;

            case "centraltopic":
                entity.unsetCentralTopic((CentralTopic) ref);
                break;

            case "marker":
                entity.unsetMarker((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "MainTopic");
        }
    }

    @Override
    protected INodeEntity getReferenceDelegate(String refName) throws NoSuchAttributeException
    {
        INodeEntity ref;

        switch (refName.toLowerCase())
        {
            case "centraltopic":
                ref = entity.getCentralTopic();
                break;

            default:
                throw new NoSuchAttributeException(refName, "MainTopic");
        }

        return ref;
    }

    //FIXME Do something about limits
    @Override
    protected List<INodeEntity> getReferenceListDelegate(String refName) throws NoSuchAttributeException
    {
        ArrayList<INodeEntity> refs;

        switch (refName.toLowerCase())
        {
            case "subtopics":
                refs = new ArrayList<>(entity.getSubTopics(1000));
                break;

            case "marker":
                refs = new ArrayList<>(entity.getMarker(1000));
                break;

            default:
                throw new NoSuchAttributeException(refName, "MainTopic");
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
        if(!(node instanceof MainTopic)) {
            throw new AdapterException("MainTopicNodeAdapter can't have " + node.getLabel() + " as a node");
        }

        this.entity = (MainTopic) node;
    }

    @Override
    public String getDisplayName()
    {
        return String.valueOf(entity.getName());
    }
}
