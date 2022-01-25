package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.*;
import geodes.sms.nmf.editor.mindMaps.neo4jImpl.ModelManagerImpl;

import java.util.ArrayList;
import java.util.List;

public class MindMapsModelAdapter extends AbstractModelAdapter
{
    private final ModelManagerImpl modelManager;

    public MindMapsModelAdapter(ModelManagerImpl modelManager)
    {
        super();
        this.modelManager = modelManager;
    }

    @Override
    protected INodeEntity createNodeDelegate(String label) throws NoSuchClassException
    {
        INodeEntity node;

        switch(label)
        {
            case "CentralTopic":
                node = modelManager.createCentralTopic();
                break;

            case "MindMap":
                node = modelManager.createMindMap();
                break;

            case "SubTopic":
                node = modelManager.createSubTopic();
                break;

            case "MainTopic":
                node = modelManager.createMainTopic();
                break;

            case "Marker":
                node = modelManager.createMarker();
                break;

            default:
                throw new NoSuchClassException(label);
        }

        return node;
    }

    @Override
    protected void removeNodeDelegate(INodeEntity node) throws NoSuchClassException
    {
        switch(node.getLabel())
        {
            case "CentralTopic":
                modelManager.remove((CentralTopic) node);
                break;

            case "MindMap":
                modelManager.remove((MindMap) node);
                break;

            case "SubTopic":
                modelManager.remove((SubTopic) node);
                break;

            case "MainTopic":
                modelManager.remove((MainTopic) node);
                break;

            case "Marker":
                modelManager.remove((Marker) node);
                break;

            default:
                throw new NoSuchClassException(node.getLabel());
        }
    }

    @Override
    protected INodeEntity loadNodeDelegate(String label, long id) throws NoSuchClassException
    {
        INodeEntity node;

        switch(label)
        {
            case "CentralTopic":
                node = modelManager.loadCentralTopicById(id);
                break;

            case "MindMap":
                node = modelManager.loadMindMapById(id);
                break;

            case "SubTopic":
                node = modelManager.loadSubTopicById(id);
                break;

            case "MainTopic":
                node = modelManager.loadMainTopicById(id);
                break;

            case "Marker":
                node = modelManager.loadMarkerById(id);
                break;

            default:
                throw new NoSuchClassException(label);
        }

        return node;
    }

    @Override
    protected void unloadNodeDelegate(INodeEntity node) throws NoSuchClassException
    {
        switch(node.getLabel())
        {
            case "CentralTopic":
                modelManager.unload((CentralTopic) node);
                break;

            case "MindMap":
                modelManager.unload((MindMap) node);
                break;

            case "SubTopic":
                modelManager.unload((SubTopic) node);
                break;

            case "MainTopic":
                modelManager.unload((MainTopic) node);
                break;

            case "Marker":
                modelManager.unload((Marker) node);
                break;

            default:
                throw new NoSuchClassException(node.getLabel());
        }
    }

    //FIXME Do something about limits
    @Override
    protected List<INodeEntity> loadAllNodesDelegate(String label) throws NoSuchClassException
    {
        ArrayList<INodeEntity> nodes;

        switch(label)
        {
            case "CentralTopic":
                nodes = new ArrayList<>(modelManager.loadCentralTopicList(1000));
                break;

            case "MindMap":
                nodes = new ArrayList<>(modelManager.loadMindMapList(1000));
                break;

            case "SubTopic":
                nodes = new ArrayList<>(modelManager.loadSubTopicList(1000));
                break;

            case "MainTopic":
                nodes = new ArrayList<>(modelManager.loadMainTopicList(1000));
                break;

            case "Marker":
                nodes = new ArrayList<>(modelManager.loadMarkerList(1000));
                break;

            default:
                throw new NoSuchClassException(label);
        }

        return nodes;
    }

    @Override
    protected void saveToDatabaseDelegate()
    {
        modelManager.saveChanges();
    }

    @Override
    protected void closeDatabaseDelegate()
    {
        modelManager.close();
    }

    @Override
    protected void clearCacheDelegate()
    {
        modelManager.clearCache();
    }

    @Override
    protected void clearDatabaseDelegate()
    {
        modelManager.clearDB();
    }
}
