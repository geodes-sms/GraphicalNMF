package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModelAdapter implements IModelAdapter
{
    protected AbstractAdapterFactory factory;
    protected CacheAdapter cache;

    protected AbstractModelAdapter()
    {
        cache = new CacheAdapter();
    }

    @Override
    public final void saveToDatabase()
    {
        saveToDatabaseDelegate();
        cache.saveCache();
    }

    @Override
    public final void closeDatabase()
    {
        closeDatabaseDelegate();
        cache.clearCache();
        cache = null;
    }

    @Override
    public final void clearCache()
    {
        clearCacheDelegate();
        cache.clearCache();
    }

    @Override
    public final void clearDatabase()
    {
        clearDatabaseDelegate();
        cache.clearCache();
    }

    @Override
    public final AbstractNodeAdapter createNode(String label) throws AdapterException
    {
        AbstractNodeAdapter adapter = null;
        INodeEntity node = createNodeDelegate(label);

        if(node != null)
        {
            try
            {
                adapter = factory.createAdapter(node);

                cache.storeCreatedAdapter(adapter);         //Store the adapter in the creation buffer
                adapter.cache = this.cache;
            }
            catch (AdapterException e)
            {
                removeNodeDelegate(node);                   //Remove if the adapter can't be created
                throw e;
            }
        }

        return adapter;
    }

    @Override
    public final void removeNode(INodeAdapter node) throws AdapterException
    {
        if(node instanceof AbstractNodeAdapter)
        {
            AbstractNodeAdapter adapter = (AbstractNodeAdapter) node;

            //Remove the adapter from the cache
            cache.remove(adapter);
            adapter.cache = null;

            //Unload the node
            INodeEntity entity = adapter.getNode();
            removeNodeDelegate(entity);

            //FIXME Remove children from the cache
        }
        else
        {
            throw new AdapterException("Node must be an AbstractNodeAdapter");
        }
    }

    @Override
    public final AbstractNodeAdapter loadNode(String label, long id) throws AdapterException
    {
        AbstractNodeAdapter adapter = null;

        //Check in the cache
        if(cache.isInCache(id))
        {
            adapter = cache.loadFromCache(id);
        }
        //Load the node & create its adapter
        else
        {
            INodeEntity node = loadNodeDelegate(label, id);

            if(node != null)
            {
                System.out.println("Loading " + label + ": " + id);

                //Create the adapter
                try
                {
                    adapter = factory.createAdapter(node);

                    cache.addToCache(adapter);      //Add the adapter to the cache
                    adapter.cache = this.cache;
                }
                catch (AdapterException e)
                {
                    //Unload if the adapter can't be created
                    unloadNodeDelegate(node);
                    throw e;
                }
            }
        }

        return adapter;
    }

    @Override
    public final void unloadNode(INodeAdapter node) throws AdapterException
    {
        if(node instanceof AbstractNodeAdapter)
        {
            //Check the cache
            AbstractNodeAdapter adapter = (AbstractNodeAdapter) node;
            if(!cache.unloadFromCache(adapter))
            {
                //Unload the INodeEntity
                INodeEntity entity = adapter.getNode();
                unloadNodeDelegate(entity);

                adapter.cache = null;              //Set the cache reference to null just in case
            }
        }
        else
        {
            throw new AdapterException("Node must be an AbstractNodeAdapter");
        }
    }

    @Override
    public final List<INodeAdapter> loadAllNodes(String label) throws AdapterException
    {
        List<INodeEntity> nodes = loadAllNodesDelegate(label);
        ArrayList<INodeAdapter> adapters = new ArrayList<>(nodes.size());

        for(INodeEntity node : nodes)
        {
            AbstractNodeAdapter adapter;

            //Check if an adapter was already in the cache
            if(cache.isInCache(node.get_id()))
            {
                /*
                * adapter.node -> doesn't work
                * node -> overrides modification
                */

                adapter = cache.loadFromCache(node);
                //fixme should we check for CacheException -> label issue
            }
            //Create a new adapter
            else
            {
                try
                {
                    adapter = factory.createAdapter(node);

                    cache.addToCache(adapter);                    //Add the adapter to the cache
                    adapter.cache = this.cache;
                }
                catch(AdapterException e)
                {
                    //Unload if the adapter can't be created
                    unloadNodeDelegate(node);
                    throw e;
                }
            }

            adapters.add(adapter);
            System.out.println("Loading " + label + ": " + node.get_id());
        }

        return adapters;
    }

    protected abstract void saveToDatabaseDelegate();
    protected abstract void closeDatabaseDelegate();
    protected abstract void clearCacheDelegate();
    protected abstract void clearDatabaseDelegate();
    protected abstract INodeEntity createNodeDelegate(String label) throws AdapterException;
    protected abstract void removeNodeDelegate(INodeEntity node) throws NoSuchClassException;
    protected abstract INodeEntity loadNodeDelegate(String label, long id) throws AdapterException;
    protected abstract void unloadNodeDelegate(INodeEntity node) throws AdapterException;
    protected abstract List<INodeEntity> loadAllNodesDelegate(String label) throws AdapterException;


}
