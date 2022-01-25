package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import org.eclipse.emf.ecore.EPackage;

public abstract class AbstractAdapterFactory implements IAdapterFactory
{
    private final EPackage ePackage;

    protected AbstractAdapterFactory(EPackage ePackage)
    {
        this.ePackage = ePackage;
    }

    @Override
    public final AbstractModelAdapter connect(String uri, String user, String password)
    {
        AbstractModelAdapter adapter = connectDelegate(uri, user, password);
        adapter.factory = this;

        return adapter;
    }

    @Override
    public final AbstractNodeAdapter createAdapter(INodeEntity node) throws AdapterException
    {
        AbstractNodeAdapter adapter = createAdapterDelegate(node);
        adapter.factory = this;

        return adapter;
    }

    @Override
    public EPackage getEPackage()
    {
        return ePackage;
    }

    protected abstract AbstractModelAdapter connectDelegate(String uri, String user, String password);
    protected abstract AbstractNodeAdapter createAdapterDelegate(INodeEntity node) throws AdapterException;
}
