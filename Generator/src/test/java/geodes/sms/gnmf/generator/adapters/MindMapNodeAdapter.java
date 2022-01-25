package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.CentralTopic;
import geodes.sms.nmf.editor.mindMaps.Marker;
import geodes.sms.nmf.editor.mindMaps.MindMap;
import geodes.sms.nmf.editor.mindMaps.MindMapState;

import java.util.ArrayList;
import java.util.List;

public class MindMapNodeAdapter extends AbstractNodeAdapter
{
    private MindMap entity;

    public MindMapNodeAdapter(MindMap entity)
    {
        this.entity = entity;
    }

    @Override
    public <T> void setAttribute(String attrName, T attrValue) throws NoSuchAttributeException
    {
        switch(attrName.toLowerCase())
        {
            case "title":
                entity.setTitle((java.lang.String) attrValue);
                break;

            case "mindmapstate":
                entity.setMindMapState(MindMapState.valueOf((String) attrValue));
                break;


            default:
                throw new NoSuchAttributeException(attrName, "MindMap");
        }
    }

    @Override
    public <T> T getAttribute(String attrName) throws NoSuchAttributeException
    {
        T attr = null;

        switch(attrName.toLowerCase())
        {
            case "title":
                attr = (T) entity.getTitle();
                break;

            case "mindmapstate":
                MindMapState val = entity.getMindMapState();
                if(val != null) attr = (T) val.toString();
                break;


            default:
                throw new NoSuchAttributeException(attrName, "MindMap");
        }

        return attr;
    }

    @Override
    protected INodeEntity addChildDelegate(String refName, String type) throws NoSuchAttributeException
    {
        INodeEntity child;

        switch(refName.toLowerCase())
        {
            case "centraltopic":
                child = entity.addCentralTopic();
                break;

            case "markers":
                child = entity.addMarkers();
                break;


            default:
                throw new NoSuchAttributeException(refName, "MindMap");
        }

        return child;
    }

    @Override
    protected void setReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "MindMap");
    }

    @Override
    protected void unsetReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        switch (refName.toLowerCase())
        {
            case "centraltopic":
                entity.unsetCentralTopic((CentralTopic) ref);
                break;

            case "markers":
                entity.unsetMarkers((Marker) ref);
                break;

            default:
                throw new NoSuchAttributeException(refName, "MindMap");
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
                throw new NoSuchAttributeException(refName, "MindMap");
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
            case "markers":
                refs = new ArrayList<>(entity.getMarkers(1000));
                break;

            default:
                throw new NoSuchAttributeException(refName, "MindMap");
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
        if(!(node instanceof MindMap)) {
            throw new AdapterException("MindMapNodeAdapter can't have " + node.getLabel() + " as a node");
        }

        this.entity = (MindMap) node;
    }

    @Override
    public String getDisplayName()
    {
        return String.valueOf(entity.getTitle());
    }
}
