package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.Marker;
import geodes.sms.nmf.editor.mindMaps.SubTopic;

import java.util.ArrayList;
import java.util.List;

public class SubTopicNodeAdapter extends AbstractNodeAdapter
{
    private SubTopic entity;

    public SubTopicNodeAdapter(SubTopic entity)
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

            case "isfinished":
                entity.setIsFinished((boolean) attrValue);
                break;


            default:
                throw new NoSuchAttributeException(attrName, "SubTopic");
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

            case "isfinished":
                attr = (T) entity.getIsFinished();
                break;


            default:
                throw new NoSuchAttributeException(attrName, "SubTopic");
        }

        return attr;
    }

    @Override
    protected INodeEntity addChildDelegate(String refName, String type) throws NoSuchAttributeException
    {
        INodeEntity child;

        switch(refName.toLowerCase())
        {
            case "subsubtopics":
                child = entity.addSubsubTopics();
                break;


            default:
                throw new NoSuchAttributeException(refName, "SubTopic");
        }

        return child;
    }

    @Override
    protected void setReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "crossref":
                entity.setCrossRef((SubTopic) ref);
                break;

            case "marker":
                entity.setMarker((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "SubTopic");
        }
    }

    @Override
    protected void unsetReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "subsubtopics":
                entity.unsetSubsubTopics((SubTopic) ref);
                break;

            case "crossref":
                entity.unsetCrossRef((SubTopic) ref);
                break;

            case "marker":
                entity.unsetMarker((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "SubTopic");
        }
    }

    @Override
    protected INodeEntity getReferenceDelegate(String refName) throws NoSuchAttributeException
    {
        INodeEntity ref;

        switch (refName.toLowerCase())
        {
            case "crossref":
                ref = entity.getCrossRef();
                break;

            default:
                throw new NoSuchAttributeException(refName, "SubTopic");
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
            case "subsubtopics":
                refs = new ArrayList<>(entity.getSubsubTopics(1000));
                break;

            case "marker":
                refs = new ArrayList<>(entity.getMarker(1000));
                break;

            default:
                throw new NoSuchAttributeException(refName, "SubTopic");
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
        if(!(node instanceof SubTopic)) {
            throw new AdapterException("SubTopicNodeAdapter can't have " + node.getLabel() + " as a node");
        }

        this.entity = (SubTopic) node;
    }

    @Override
    public String getDisplayName()
    {
        return String.valueOf(entity.getName());
    }
}
