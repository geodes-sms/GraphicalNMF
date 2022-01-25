package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.Marker;

import java.util.List;

public class MarkerNodeAdapter extends AbstractNodeAdapter
{
    private Marker entity;

    public MarkerNodeAdapter(Marker entity)
    {
        this.entity = entity;
    }

    @Override
    public <T> void setAttribute(String attrName, T attrValue) throws NoSuchAttributeException
    {
        switch(attrName.toLowerCase())
        {
            case "name":
                entity.setName((java.lang.String) attrValue);
                break;


            default:
                throw new NoSuchAttributeException(attrName, "Marker");
        }
    }

    @Override
    public <T> T getAttribute(String attrName) throws NoSuchAttributeException
    {
        T attr = null;

        switch(attrName.toLowerCase())
        {
            case "name":
                attr = (T) entity.getName();
                break;


            default:
                throw new NoSuchAttributeException(attrName, "Marker");
        }

        return attr;
    }

    @Override
    protected INodeEntity addChildDelegate(String refName, String type) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "Marker");
    }

    @Override
    protected void setReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "Marker");
    }

    @Override
    protected void unsetReferenceDelegate(String refName, INodeEntity ref) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "Marker");
    }

    @Override
    protected INodeEntity getReferenceDelegate(String refName) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "Marker");
    }

    //FIXME Do something about limits
    @Override
    protected List<INodeEntity> getReferenceListDelegate(String refName) throws NoSuchAttributeException
    {
        throw new NoSuchAttributeException(refName, "Marker");
    }

    @Override
    protected INodeEntity getNode()
    {
        return entity;
    }

    @Override
    protected void setNode(INodeEntity node) throws AdapterException
    {
        if(!(node instanceof Marker)) {
            throw new AdapterException("MarkerNodeAdapter can't have " + node.getLabel() + " as a node");
        }

        this.entity = (Marker) node;
    }

    @Override
    public String getDisplayName()
    {
        return String.valueOf(entity.getName());
    }
}
