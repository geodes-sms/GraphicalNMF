package geodes.sms.gnmf.generator.adapters;


import geodes.sms.neo4j.io.entity.INodeEntity;
import geodes.sms.nmf.editor.mindMaps.*;
import geodes.sms.nmf.editor.mindMaps.neo4jImpl.ModelManagerImpl;
import org.eclipse.emf.ecore.EPackage;

public class MindMapsAdapterFactory extends AbstractAdapterFactory
{
    public MindMapsAdapterFactory(EPackage ePackage)
    {
        super(ePackage);
    }


    @Override
    protected MindMapsModelAdapter connectDelegate(String uri, String user, String password)
    {
        ModelManagerImpl modelManager = new ModelManagerImpl(uri, user, password);
        return new MindMapsModelAdapter(modelManager);
    }

    @Override
    protected AbstractNodeAdapter createAdapterDelegate(INodeEntity node) throws NoSuchClassException
    {
        AbstractNodeAdapter adapter;

        switch(node.getLabel())
        {
            case "CentralTopic":
                adapter = new CentralTopicNodeAdapter((CentralTopic) node);
                break;

            case "MindMap":
                adapter = new MindMapNodeAdapter((MindMap) node);
                break;

            case "SubTopic":
                adapter = new SubTopicNodeAdapter((SubTopic) node);
                break;

            case "MainTopic":
                adapter = new MainTopicNodeAdapter((MainTopic) node);
                break;

            case "Marker":
                adapter = new MarkerNodeAdapter((Marker) node);
                break;

            default:
                throw new NoSuchClassException(node.getLabel());
        }

        return adapter;
    }
}
