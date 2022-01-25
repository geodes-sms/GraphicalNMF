package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.application.controllers.VisitorCompositeNodeProxy.VisitResult;
import geodes.sms.gnmf.generator.adapters.INodeAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProxyManager
{
    private final DatabaseController controller;        //xxx not needed
    private final ArrayList<CompositeNodeProxy> proxies = new ArrayList<>();

    ProxyManager(DatabaseController controller)
    {
        this.controller = controller;
    }

    /**
     * Create a {@link CompositeNodeProxy} and add it to the
     * list of opened proxies.
     *
     * @param adapter The adapter from which to create the proxy
     * @return A {@link CompositeNodeProxy}
     */
    CompositeNodeProxy openProxy(INodeAdapter adapter)
    {
        CompositeNodeProxy proxy = new CompositeNodeProxy(adapter.getDisplayName(),
                adapter.getLabel(), adapter.getId());
        proxies.add(proxy);

        return proxy;
    }

    /**
     * Create a {@link CompositeNodeProxy} and add it to the
     * list of opened proxies. </br>
     * Also add the new proxy to the children list of
     * another proxy.
     * </p>
     * This is a utility method and could be removed.
     *
     * @param adapter The adapter from which to create the proxy
     * @param parent The parent of the new proxy
     * @return A {@link CompositeNodeProxy} who is a child of the given parent proxy
     */
    CompositeNodeProxy openProxyAsChild(INodeAdapter adapter, CompositeNodeProxy parent)
    {
        CompositeNodeProxy child = openProxy(adapter);
        parent.addChild(child);

        return child;
    }

    /**
     * Close a proxy, removing it from the list of opened proxies. </br>
     * If the proxy had a parent, also remove it from its parent.
     * If the proxy had children, the children are closed too.
     * </p>
     * Does nothing if the given proxy is not contained in
     * the list of open proxies (should always be the case).
     *
     * @param proxy The proxy to close, can be null
     */
    void closeProxy(CompositeNodeProxy proxy)
    {
        if(!proxies.contains(proxy)) return;

        //Get all the children (deep search) of the node
        VisitorNodeExtractor extractor = new VisitorNodeExtractor();
        proxy.visitProxy(extractor);
        List<CompositeNodeProxy> toClose = extractor.getNodes();

        //Close all the children
        for(CompositeNodeProxy childToClose : toClose)
        {
            childToClose.setParent(null);           //remove them from their parent for safety
            proxies.remove(childToClose);
        }
    }

    /**
     * Close all proxies, removing them from the list.
     */
    void closeAllProxy()
    {
        proxies.clear();        //Close all proxies by clearing the list
    }

    //region Publics

    /**
     * Return the proxy associated with the given id, or null if
     * there is none.
     *
     * @param id The id of the proxy to return
     * @return A {@link CompositeNodeProxy} with the given id
     */
    public CompositeNodeProxy getCompositeProxy(long id)
    {
        for(CompositeNodeProxy proxy : proxies)
        {
            if(proxy.getId() == id)
            {
                return proxy;
            }
        }

        return null;
    }

    /**
     * Visit all the opened proxies in the order of their
     * parent-children relationship.
     *
     * @param visitor The visitor
     */
    public void visitCompositeProxy(VisitorCompositeNodeProxy visitor)
    {
        Iterator<CompositeNodeProxy> iterator = proxies.listIterator();

        VisitResult result = VisitResult.Continue;
        while(iterator.hasNext() && result != VisitResult.Terminate)
        {
            CompositeNodeProxy proxy = iterator.next();

            //Ignore proxy with parent (they will be visited from their parent)
            if(proxy.getParent() == null)
            {
                result = proxy.doRecursiveVisit(visitor);
            }
        }
    }

    /**
     * THIS IS FOR TESTING WILL BE REMOVED IN THE FINAL VERSION.
     *
     * Returns a list of all the proxies in this manager. Also unset
     * all parent-child relationship. </br>
     *
     * This is used to catch closed node that might not have been
     * removed properly.
     *
     * @return A list of proxy
     */
    public List<CompositeNodeProxy> showAll()
    {
        ArrayList<CompositeNodeProxy> nodes = new ArrayList<>();

        for(CompositeNodeProxy proxy : proxies)
        {
            nodes.add(proxy);
            proxy.setParent(null);
        }

        return nodes;
    }

    //endregion

    //region Statics

    /**
     * Create a {@link NodeProxy} from an adapter. </br>
     * This serves as a factory for node proxy.
     *
     * @param adapter An adapter from which to create the proxy
     * @return A {@link NodeProxy}
     */
    protected static NodeProxy createProxy(INodeAdapter adapter)
    {
        return new NodeProxy(adapter.getDisplayName(), adapter.getLabel(), adapter.getId());
    }

    //endregion
}
