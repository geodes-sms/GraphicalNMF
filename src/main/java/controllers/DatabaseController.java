package controllers;

import adapters.AdapterException;
import adapters.IAdapterFactory;
import adapters.IModelAdapter;
import adapters.INodeAdapter;
import geodes.sms.nmf.loader.emf2neo4j.EmfModelLoader;
import geodes.sms.nmf.neo4j.io.GraphBatchWriter;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController
{
    private ConnectionInfo connectionInfo;

    final IAdapterFactory factory;
    IModelAdapter modelAdapter;

    public final ProxyManager proxyManager;
    public final CommandManager commandManager;
    public final PackageManager packageManager;

    private DatabaseController(IAdapterFactory factory)
    {
        this.factory = factory;

        proxyManager = new ProxyManager(this);
        commandManager = new CommandManager(this);
        packageManager = new PackageManager(this, factory.getEPackage());
    }

    /**
     * Load the given model in the database.
     * </p>
     * Even though this controller
     * needs to be connected to a database to load a model, the connection
     * will be closed during the loading and reopened after.
     * </p>
     * TODO Check the metamodel of the model
     *
     * @param model The path of the model to load.
     * @throws LoadingException If the loading fails
     */
    public void loadModel(String model) throws LoadingException
    {
        if(modelAdapter == null) throw new ConnectionException("No connection to the database");

        //We need to close the database first
        modelAdapter.closeDatabase();
        modelAdapter = null;

        //Load the model
        try
        {
            //Call the modelLoader
            GraphBatchWriter writer = new GraphBatchWriter(connectionInfo.uri,
                    connectionInfo.user, connectionInfo.password);
            EmfModelLoader.Companion.load(model, writer);
        }
        catch(Exception e)
        {
            //Reconnect to the database
            this.modelAdapter = factory.connect(connectionInfo.uri, connectionInfo.user, connectionInfo.password);

            throw new LoadingException("Can't load " + model + " in the database", e);
        }

        //Reconnect to the database
        this.modelAdapter = factory.connect(connectionInfo.uri, connectionInfo.user, connectionInfo.password);
    }

    /**
     * Connection to a database. </br>
     * The connection requires :
     * <ul>
     *     <li>The uri of the database</li>
     *     <li>A user</li>
     *     <li>A password</li>
     * </ul>
     * </p>
     * If this is already connected to a database, that connection
     * will be closed before opening the new one.
     *
     * @param info The information for the connection to the database.
     */
    public void connectToDatabase(ConnectionInfo info)
    {
        if(modelAdapter != null)
        {
            System.err.println("Trying to open a new connection without closing the previous one");
        }

        modelAdapter = factory.connect(info.uri, info.user, info.password);
        this.connectionInfo = info;
    }

    /**
     * Save changes to the database.
     *
     * @throws ConnectionException If this is not connected to a database
     */
    public void saveToDabase()
    {
        if(modelAdapter == null) throw new ConnectionException("No connection to the database");

        modelAdapter.saveToDatabase();

        //TODO Check if it would be possible to refresh proxies & adapters
    }

    /**
     * Close the connection to the database. </br>
     * The connection should always be closed before
     * closing the application.
     *
     * @throws ConnectionException If this is not connected to a database
     */
    public void closeDatabase()
    {
        if(modelAdapter == null) throw new ConnectionException("No connection to the database");

        proxyManager.closeAllProxy();


        modelAdapter.closeDatabase();
        modelAdapter = null;
        connectionInfo = null;
    }

    /**
     * @return True if this is connected to a database, false otherwise
     */
    public boolean isConnected()
    {
        return modelAdapter != null;
    }

    /**
     * Load and create proxies for all nodes of the given class in the database. </p>
     * The nodes are unloaded when this method finishes.
     *
     * @param label The label of the nodes to load
     * @return A list of node proxies
     */
    public List<NodeProxy> getListOfNodes(String label) throws Exception
    {
        if(modelAdapter == null) throw new ConnectionException("No connection to the database");

        List<NodeProxy> proxies;

        try
        {
            List<INodeAdapter> adapters = modelAdapter.loadAllNodes(label);
            proxies = new ArrayList<>(adapters.size());

            for (INodeAdapter adapter : adapters)
            {
                NodeProxy proxy = ProxyManager.createProxy(adapter);
                proxies.add(proxy);

                modelAdapter.unloadNode(adapter);
            }
        }
        catch(AdapterException e)
        {
            throw new Exception(e.getMessage(), e);
        }

        return proxies;
    }


    /**
     * Create and initialize a DatabaseController.
     *
     * @param factory The factory the controller should use.
     * @return A DatabaseController
     */
    public static DatabaseController initialize(IAdapterFactory factory)
    {
        return new DatabaseController(factory);
    }
}
