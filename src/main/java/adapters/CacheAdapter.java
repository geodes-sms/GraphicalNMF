package adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;

import java.util.ArrayList;
import java.util.HashMap;

class CacheAdapter
{
    private static class CacheEntry
    {
        private String label;
        private AbstractNodeAdapter adapter;
        private int refCount;

        private CacheEntry(String label, AbstractNodeAdapter adapter, int refCount)
        {
            this.label = label;
            this.adapter = adapter;
            this.refCount = refCount;
        }
    }

    /**
     * We override the cache of NMF to fix issues with loading & unloading nodes.
     */
    private HashMap<Long, CacheEntry> cache = new HashMap<>();
    private ArrayList<AbstractNodeAdapter> creationBuffer = new ArrayList<>();


    /**
     * Store the adapter of a newly created node
     * until the node is saved.
     *
     * @param node The adapter to store
     */
    public void storeCreatedAdapter(AbstractNodeAdapter node)
    {
        creationBuffer.add(node);
    }

    /**
     * Remove an adapter from the cache or the creation buffer.
     *
     * @param node The adapter to remove
     */
    public void remove(AbstractNodeAdapter node)
    {
        cache.remove(node.getId());
        creationBuffer.remove(node);
    }


    /**
     * Add an {@link AbstractNodeAdapter} to the cache. If there is
     * already an adapter of the same id, do not the new adapter. </br>
     * The new adapter starts with a refcount of 1.
     *
     * @param adapter The {@link AbstractNodeAdapter} to add
     * @return True if the adapter was added to the cache, false otherwise
     * @throws CacheException If there is an adapter with the same id in the cache
     */
    public boolean addToCache(AbstractNodeAdapter adapter) throws CacheException
    {
        if(cache.containsKey(adapter.getId())) throw new CacheException("Duplicate id in the cache");

        CacheEntry entry = new CacheEntry(adapter.getLabel(), adapter, 1);
        cache.put(adapter.getId(), entry);

        return true;
    }

    /**
     * Remove the adapter associated with the given id from
     * the cache.
     *
     * @param id The id to remove
     */
    public void removeFromCache(long id)
    {
        cache.remove(id);
    }

    /**
     * Remove the given adapter from the cache.
     *
     * @param adapter The adapter to remove
     */
    public void removeFromCache(AbstractNodeAdapter adapter)
    {
        removeFromCache(adapter.getId());
    }


    /**
     * Move created adapters to the cache. </br>
     * Must be called after the saving is done.
     *
     * @throws CacheException If an adapter is already if the cache
     */
    public void saveCache() throws CacheException
    {
        for(AbstractNodeAdapter adapter : creationBuffer)
        {
            if(!addToCache(adapter))
            {
                throw new CacheException("Duplicate id in the cache");
            }
        }
        creationBuffer.clear();
    }

    /**
     * Remove all nodes from the cache.
     */
    public void clearCache()
    {
        cache.clear();
        creationBuffer.clear();
    }


    /**
     * Load a {@link INodeAdapter} from the cache. If the node exist
     * in the cache, it is returned and its refCount is incremented.
     *
     * @param id The id of the adapter to load
     * @return An {@link AbstractNodeAdapter}
     */
    public AbstractNodeAdapter loadFromCache(long id)
    {
        AbstractNodeAdapter adapter = null;

        CacheEntry entry = cache.get(id);
        if(entry != null && entry.adapter != null)
        {
            adapter = entry.adapter;        //Get the adapter
            entry.refCount++;               //Increment ref count
        }
        //Remove empty entries
        else if(entry != null)
        {
            cache.remove(id);
        }

        return adapter;
    }

    /**
     * Load a {@link INodeAdapter} from the cache. If the adapter exist,
     * then its reference to an {@link INodeEntity} is replaced by the
     * given node and its refCount is incremented.
     *
     * @param node The {@link INodeEntity} of the adapter to load
     * @return An {@link AbstractNodeAdapter} with the given {@link INodeEntity}
     * @throws CacheException If the label of the adapter in the cache doesn't match that of the given node
     */
    public AbstractNodeAdapter loadFromCache(INodeEntity node) throws CacheException
    {
        AbstractNodeAdapter adapter = null;

        CacheEntry entry = cache.get(node.get_id());
        if(entry != null && entry.adapter != null)
        {
            try
            {
                entry.adapter.setNode(node);    //Replace the INodeEntity of the adapter

                adapter = entry.adapter;        //Get the adapter
                entry.refCount++;               //Increment ref count
            }
            catch(AdapterException e)
            {
                throw new CacheException("Adapter already exist, but for a different label", e);
            }
        }
        //Remove empty entry
        else if(entry != null)
        {
            cache.remove(node.get_id());
        }

        return adapter;
    }


    /**
     * Decrement the reference count for the adapter associated to
     * the given id. </br>
     * If the new count is 0, then remove the adapter from
     * the cache.
     *
     * @param id The id of the adapter
     * @return True if the adapter is still in the cache, false otherwise
     */
    public boolean unloadFromCache(long id)
    {
        boolean inCache = true;

        if(cache.containsKey(id))
        {
            CacheEntry entry = cache.get(id);

            entry.refCount--;

            if (entry.refCount <= 0 || entry.adapter == null)       //empty entry = unloaded node
            {
                cache.remove(id);
                inCache = false;
            }
        }

        return inCache;
    }

    /**
     * Decrement the reference count of the given adapter. </br>
     * If the new count is 0, then remove the adapter
     * from the cache.
     *
     * @param adapter The adapter to check
     * @return True if the adapter is still in the cache, false otherwise
     * @see #unloadFromCache(long)
     */
    public boolean unloadFromCache(AbstractNodeAdapter adapter)
    {
        return unloadFromCache(adapter.getId());
    }


    /**
     * Check if there is an {@link AbstractNodeAdapter} for
     * the given id in the cache.
     *
     * @param id The id to look for
     * @return True if there is an adapter, false otherwise
     */
    protected boolean isInCache(long id)
    {
        CacheEntry entry = cache.get(id);

        if(entry != null && entry.adapter != null)
        {
            return true;
        }
        //Remove empty entry
        else if(entry != null)
        {
            cache.remove(id);
        }

        return false;
    }
}
