package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/** TODO Improve
 * INodeEntity cannot be unloaded from here when a INodeAdapter can't be created.
 * If this happens, then an unreferenced loaded node exist in the application and it
 * can lead to more problems.
 */

public abstract class AbstractNodeAdapter implements INodeAdapter
{
    protected AbstractAdapterFactory factory;
    protected CacheAdapter cache;

    @Override
    public final AbstractNodeAdapter addChild(String refName, String type) throws AdapterException
    {
        AbstractNodeAdapter adapter = null;
        INodeEntity node = addChildDelegate(refName, type);

        if(node != null)
        {
            try
            {
                adapter = factory.createAdapter(node);          //Create the adapter

                cache.storeCreatedAdapter(adapter);             //Store the adapter in the creation buffer
                adapter.cache = this.cache;
            }
            catch(AdapterException e)
            {
                //Remove if adapter can't be created
                unsetReferenceDelegate(refName, node);

                throw e;
            }
        }

        return adapter;
    }

    @Override
    public final void setReference(String refName, INodeAdapter ref) throws AdapterException
    {
        if(ref instanceof AbstractNodeAdapter)
        {
            setReferenceDelegate(refName, ((AbstractNodeAdapter) ref).getNode());
        }
        else
        {
            throw new AdapterException("Reference is not an AbstractNodeAdapter");
        }
    }

    @Override
    public final void unsetReference(String refName, INodeAdapter ref) throws AdapterException
    {
        if(ref instanceof AbstractNodeAdapter)
        {
            unsetReferenceDelegate(refName, ((AbstractNodeAdapter) ref).getNode());
        }
        else
        {
            throw new AdapterException("Reference is not an AbstractNodeAdapter");
        }
    }

    @Override
    public final AbstractNodeAdapter getReference(String refName) throws AdapterException
    {
        AbstractNodeAdapter adapter = null;
        INodeEntity node = getReferenceDelegate(refName);

        if(node != null)
        {
            //Load the adapter from the cache
            if(cache.isInCache(node.get_id()))
            {
                adapter = cache.loadFromCache(node);
                //fixme should we check for CacheException -> label issue
            }
            //Create the adapter
            else
            {
                adapter = factory.createAdapter(node);      //Create the adapter

                cache.addToCache(adapter);                  //Add the adapter to the cache
                adapter.cache = this.cache;
            }

            System.out.println("Loading " + node.getLabel() + ": " + node.get_id());
        }

        return adapter;
    }

    @Override
    public final List<INodeAdapter> getReferenceList(String refName) throws AdapterException
    {
        ArrayList<INodeAdapter> adapters = new ArrayList<>();
        List<INodeEntity> nodes;

        try
        {
            nodes = getReferenceListDelegate(refName);
        }
        catch(NoSuchAttributeException e)
        {
            //If not a list reference, check for single value reference
            try
            {
                nodes = new ArrayList<>();
                INodeEntity node = getReferenceDelegate(refName);
                nodes.add(node);
            }
            catch(NoSuchAttributeException other)
            {
                throw e;
            }
        }

        if(nodes != null)
        {
            try
            {
                for (INodeEntity node : nodes)
                {
                    AbstractNodeAdapter adapter = null;

                    //Load the adapter from the cache
                    if(cache.isInCache(node.get_id()))
                    {
                        adapter = cache.loadFromCache(node);
                        //fixme should we check for CacheException -> label issue
                    }
                    //Create the adapter
                    else
                    {
                        adapter = factory.createAdapter(node);          //Create the adapter

                        cache.addToCache(adapter);                      //Add the adapter to the cache
                        adapter.cache = this.cache;
                    }

                    System.out.println("Loading " + node.getLabel() + ": " + node.get_id());
                    adapters.add(adapter);
                }
            }
            catch(AdapterException e)
            {
                //Remove geodes.sms.gnmf.generator.adapters added to the cache before the error
                for(INodeAdapter adapter : adapters)
                {
                    cache.unloadFromCache((AbstractNodeAdapter) adapter);
                }

                throw e;
            }
        }

        return adapters;
    }

    @Override
    public @NotNull String getLabel()
    {
        return getNode().getLabel();
    }

    @Override
    public long getId()
    {
        return getNode().get_id();
    }

    protected abstract INodeEntity addChildDelegate(String refName, String type) throws AdapterException;

    protected abstract void setReferenceDelegate(String refName, INodeEntity ref) throws AdapterException;
    protected abstract void unsetReferenceDelegate(String refName, INodeEntity ref) throws AdapterException;
    protected abstract INodeEntity getReferenceDelegate(String refName) throws AdapterException;
    protected abstract List<INodeEntity> getReferenceListDelegate(String refName) throws AdapterException;

    protected abstract INodeEntity getNode();
    protected abstract void setNode(INodeEntity node) throws AdapterException;

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof AbstractNodeAdapter)) return super.equals(obj);

        return ((AbstractNodeAdapter) obj).getId() == this.getId();
    }
}
