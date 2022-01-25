package geodes.sms.gnmf.application.view;

import geodes.sms.gnmf.application.controllers.*;
import geodes.sms.gnmf.application.utils.FXMLException;
import geodes.sms.gnmf.application.utils.treeviews.CellCreator;
import geodes.sms.gnmf.application.utils.treeviews.CellFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DatabaseView extends Stage implements View
{
    private final DatabaseController controller;

    private Parent root;

    @FXML private MenuBar menuBar;
    @FXML private Menu editMenu;
    @FXML private TreeView<NodeProxy> nodeTree;
    @FXML private FlowPane graphView;

    public DatabaseView(DatabaseController controller) throws FXMLException
    {
        this.controller = controller;

        init();
    }

    /**
     * For initialization
     */
    protected void init() throws FXMLException
    {
        File file = new File(GraphicalNMFApplication.RESSOURCE_PATH+"Main Window.fxml");    //fixme change to ressource

        try
        {
            URL xml = file.toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(xml);
            loader.setController(this);

            root = loader.load();
            this.setScene(new Scene(root));
        }
        catch(Exception e)
        {
            throw new FXMLException("Can't open model view", e);
        }

        this.setTitle("Graphical Neo Modeling Framework");
        this.initModality(Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(event -> {
            quit();
            event.consume();
        });

        configureNodeTree();
        configureMenu();

        controller.commandManager.addObserver(this);
    }

    //region FXML

    /**
     * Prompt the user to select a model and load that model in the database.
     */
    @FXML
    public void loadModel()
    {
        //Prompt the user to select a file
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a model file");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Model Files", "*.xmi"));

        File file = chooser.showOpenDialog(this);
        if(file != null)
        {
            try
            {
                String path = file.getAbsolutePath();
                controller.loadModel(path);
            }
            catch (LoadingException e)
            {
                e.printStackTrace();
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getClass().getSimpleName());
            }
        }
    }

    /**
     * Request the view to close. This will prompt the user for confirmation,
     * and will not close if the user reject the confirmation.
     */
    @FXML
    public void quit()
    {
        System.out.println("Request to quit ModelView");

        if(GraphicalNMFApplication.askForConfirmation("Are you sure you want to quit?"))
        {
            close();
        }
    }

    /**
     * Prompt the user to open a connection with the database. If a connection
     * is already opened, close it using {@link #closeDatabase()}.
     */
    @FXML
    public void askForConnectionToDatabase()
    {
        closeDatabase();        //Close current database

        //Create the dialog -> fixme needs better implementation
        ConnectionDialog dialog = null;
        File file = new File(GraphicalNMFApplication.RESSOURCE_PATH+"ConnectDialog.fxml");      //fixme
        try
        {
            URL xml = file.toURI().toURL();
            FXMLLoader loader = new FXMLLoader(xml);
            loader.setLocation(xml);

            Parent parent = loader.load();
            dialog = new ConnectionDialog((DialogPane) parent);
        }
        catch(MalformedURLException e)      //Shouldn't happen
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, "Can't open connection dialog");
            e.printStackTrace();
        }

        if(dialog != null)
        {
            boolean isConnected = false;

            while (!isConnected)
            {
                Optional<ConnectionInfo> result = dialog.showAndWait();
                if (result.isPresent())
                {
                    this.hide();

                    try
                    {
                        //Open new controller
                        controller.connectToDatabase(result.get());
                        isConnected = true;
                    }
                    catch (Exception e)
                    {
                        GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getClass().getSimpleName());
                        e.printStackTrace();
                    }

                    this.show();
                }
                else
                {
                    isConnected = true;
                }
            }

            onOpenDatabase();       //Callback for setting up things when the connection is establish
        }
    }

    /**
     * Save the current changes to the database.
     */
    @FXML
    public void saveChangesToDatabase()
    {
        if(controller.isConnected())
        {
            controller.saveToDabase();
        }
    }

    /**
     * Close the database
     */
    @FXML
    public void closeDatabase()
    {
        if(controller.isConnected())
        {
            controller.closeDatabase();
            onCloseDatabase();
        }
    }

    /**
     * Prompt the user to create a node. </p>
     *
     * The prompt shown to the user allow for the selection
     * of the type of node to create.
     */
    @FXML
    public void promptCreateNode()
    {
        //TODO implement ModelView.promptCreateNode
    }

    /**
     * Prompt the user to open nodes. </p>
     *
     * The prompt shown to the user allow for the selection
     * of which nodes to open.
     */
    @FXML
    public void promptOpenNode()
    {
        if(!controller.isConnected())
        {
            GraphicalNMFApplication.showAlert(Alert.AlertType.WARNING, "Connect to the database");
            return;
        }

        try
        {
            NodeSelector nodeSelector = new NodeSelector(controller);
            nodeSelector.showAndWait();

            List<NodeProxy> toOpen = nodeSelector.getSelectedNodes();
            List<Command> commands = new ArrayList<>(toOpen.size());

            //Open the node
            for(NodeProxy proxy : toOpen)
            {
                Command command = new CommandOpenNode(proxy.getId(), proxy.getLabel());
                commands.add(command);
            }
            List<ExecutionResult> results = controller.commandManager.executeBatch(commands);

            //Handle the results
            for(ExecutionResult result : results)
            {
                GraphicalNMFApplication.handleExecutionResult(result);
            }
        }
        catch(FXMLException e)
        {
            GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    //endregion

    /**
     * Close the stage. </p>
     *
     * In general, you should use {@link #quit()} instead as it
     * prompts the user for confirmation.
     */
    @Override
    public void close()
    {
        System.out.println("Closing ModelView");

        closeDatabase();
        controller.commandManager.removeObserver(this);

        //Close the stage
        super.close();
    }

    @Override
    public void update()
    {
        updateNodeTree();
    }

    //region General

    protected void onOpenDatabase()
    {
        System.out.println("Connection is established");

        updateNodeTree();
    }

    protected void onCloseDatabase()
    {
        System.out.println("Connection is closed");

        clearNodeTree();
    }

    protected void createNode(String label)
    {
        if(!controller.isConnected())
        {
            GraphicalNMFApplication.showAlert(Alert.AlertType.WARNING, "Connect to the database");
            return;
        }

        Command cmd = new CommandCreateNode(label);
        ExecutionResult result = controller.commandManager.execute(cmd);
        GraphicalNMFApplication.handleExecutionResult(result);
    }

    protected void createChild(String refName, NodeProxy proxy)
    {
        List<String> subclasses = controller.packageManager.getChildArg(proxy.getLabel(), refName);

        String type = null;
        if(subclasses.size() != 1)
        {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(subclasses.get(0), subclasses);
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            Optional<String> result = dialog.showAndWait();

            if(result.isEmpty())
            {
                return;
            }

            type = result.get();
        }

        Command cmd = new CommandCreateNodeAsChild(type, refName, proxy.getId(), proxy.getLabel());
        ExecutionResult result = controller.commandManager.execute(cmd);
        GraphicalNMFApplication.handleExecutionResult(result);
    }

    protected void deleteNode(NodeProxy proxy)
    {
        if(!controller.isConnected())
        {
            GraphicalNMFApplication.showAlert(Alert.AlertType.WARNING, "Connect to the database");
            return;
        }

        if(GraphicalNMFApplication.askForConfirmation("Are you sure you want to delete " +
                proxy.getName() + "?"))
        {
            Command cmd = new CommandRemoveNode(proxy.getId(), proxy.getLabel());
            ExecutionResult result = controller.commandManager.execute(cmd);
            GraphicalNMFApplication.handleExecutionResult(result);
        }
    }

    protected void editNode(NodeProxy proxy)
    {
        //Load the node
        Command cmd = new CommandLoadNode(proxy.getId(), proxy.getLabel());
        ExecutionResult result = controller.commandManager.execute(cmd);
        GraphicalNMFApplication.handleExecutionResult(result);

        //If the loading was succesful, open a NodeView for the node
        if(result.getResult() == ExecutionResult.Result.Success)
        {
            try
            {
                //Get the loaded node
                PropertyWrapper wrapper = controller.packageManager.getPropertyWrapper(proxy.getId());
                NodeView view = new NodeView(controller, wrapper);

                //Open the NodeView and block -> only one opened node at a time
                view.showAndWait();

                //Unload the node
                Command newCmd = new CommandUnloadNode(proxy.getId(), proxy.getLabel());
                result = controller.commandManager.execute(newCmd);
                GraphicalNMFApplication.handleExecutionResult(result);
            }
            catch (Exception e)
            {
                GraphicalNMFApplication.showAlert(Alert.AlertType.ERROR, e.getMessage(), e.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

    //endregion

    //region NodeTree

    private void configureNodeTree()
    {
        CellCreator<NodeProxy> creator = () -> {
            TreeCell<NodeProxy> cell = new TextFieldTreeCell<>();

            cell.setOnContextMenuRequested(event -> {
                TreeCell<NodeProxy> sourceCell = (TreeCell<NodeProxy>) event.getSource();
                NodeProxy proxy = sourceCell.getTreeItem().getValue();
                event.consume();

                ContextMenu menu = createNodeContextMenu(proxy);
                cell.setContextMenu(menu);
            });
            cell.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() >= 2)
                {
                    TreeCell<NodeProxy> sourceCell = (TreeCell<NodeProxy>) mouseEvent.getSource();
                    NodeProxy proxy = sourceCell.getTreeItem().getValue();
                    mouseEvent.consume();

                    editNode(proxy);
                }
            });

            return cell;
        };

        CellFactory<NodeProxy> factory = new CellFactory<>();
        factory.setCellCreator(creator);

        nodeTree.setCellFactory(factory);
        nodeTree.setShowRoot(false);
    }

    private void updateNodeTree()
    {
        HashSet<NodeProxy> expanded = saveExpandedItem(nodeTree);
        nodeTree.setRoot(null);

        if(controller.isConnected())
        {
            TreeItem<NodeProxy> rootItem = new TreeItem<>();
            VisitorCompositeNodeProxy visitor = new VisitorCompositeNodeProxy()
            {
                TreeItem<NodeProxy> context = rootItem;

                @Override
                public VisitResult visit(CompositeNodeProxy node)
                {
                    //Create a TreeItem for the proxy
                    TreeItem<NodeProxy> item = new TreeItem<>(node);
                    context.getChildren().add(item);

                    //Expands the node if it was expanded
                    if (expanded.remove(item.getValue()))
                    {
                        item.setExpanded(true);
                    }

                    //Save the current context and change it to this node
                    TreeItem<NodeProxy> oldContext = context;
                    context = item;

                    //Visit the children of this node
                    Iterator<CompositeNodeProxy> iterator = node.childrenIterator();
                    while (iterator.hasNext())
                    {
                        CompositeNodeProxy child = iterator.next();
                        child.visitProxy(this);
                    }

                    //Return to the original context
                    context = oldContext;

                    return VisitResult.SkipChildren;
                }
            };
            controller.proxyManager.visitCompositeProxy(visitor);

            nodeTree.setRoot(rootItem);
        }

        expanded.clear();
    }

    private <T> HashSet<T> saveExpandedItem(TreeView<T> tree)
    {
        HashSet<T> expanded = new HashSet<>();

        Stack<TreeItem<T>> itemStack = new Stack<>();
        itemStack.push(tree.getRoot());

        while(!itemStack.isEmpty())
        {
            TreeItem<T> item = itemStack.pop();
            if(item == null) continue;

            if(item.isExpanded())
            {
                expanded.add(item.getValue());
                item.getChildren().forEach(itemStack::push);
            }
        }

        return expanded;
    }

    /** FIXME Needs better implementation
     * Clear the tree and all the proxy in it.
     */
    private void clearNodeTree()
    {
        Stack<TreeItem<NodeProxy>> stack = new Stack<>();
        stack.push(nodeTree.getRoot());

        nodeTree.setRoot(null);

        while(!stack.isEmpty())
        {
            TreeItem<NodeProxy> item = stack.pop();
            item.getChildren().forEach(stack::push);
            item.getChildren().clear();

            item.valueProperty().unbind();
            item.setValue(null);
        }
    }

    //endregion

    //region Menu

    private void configureMenu()
    {
        //***** OPEN NODES *****//
        MenuItem itemOpen = new MenuItem("Open Nodes");
        itemOpen.setOnAction(event -> promptOpenNode());
        editMenu.getItems().add(itemOpen);

        //***** CLOSE ALL *****//
        MenuItem itemClose = new MenuItem("Close All");
        itemClose.setOnAction(event -> {
            Command cmd = new CommandCloseAllNodes();
            ExecutionResult result = controller.commandManager.execute(cmd);
            GraphicalNMFApplication.handleExecutionResult(result);
        });
        editMenu.getItems().add(itemClose);

        editMenu.getItems().add(new SeparatorMenuItem());

        //***** CREATE NODES *****//
        List<String> labels = controller.packageManager.getClasses();
        for(String label : labels)
        {
            MenuItem item = new MenuItem("Create " + label);
            item.setOnAction(event -> createNode(label));
            editMenu.getItems().add(item);
        }

        editMenu.getItems().add(new SeparatorMenuItem());

        //***** DEBUGGING *****//
        MenuItem showAllItem = new MenuItem("Show All");
        showAllItem.setOnAction(event -> {
            controller.proxyManager.showAll();
            update();
        });
        editMenu.getItems().add(showAllItem);
    }

    private ContextMenu createNodeContextMenu(NodeProxy proxy)
    {
        ContextMenu menu = new ContextMenu();

        //***** EDIT NODE *****//
        MenuItem editItem = new MenuItem("Edit " + proxy.getName());
        editItem.setOnAction(event -> editNode(proxy));
        menu.getItems().add(editItem);

        menu.getItems().add(new SeparatorMenuItem());

        //***** OPEN CHILDREN *****//
        MenuItem openChildren = new MenuItem("Open Children");
        openChildren.setOnAction(event -> {
            Command cmd = new CommandOpenChildren(proxy.getId(), proxy.getLabel());
            ExecutionResult result = controller.commandManager.execute(cmd);
            GraphicalNMFApplication.handleExecutionResult(result);
        });
        menu.getItems().add(openChildren);

        //***** CLOSE CHILDREN *****//
        MenuItem closeChildren = new MenuItem("Close Children");
        closeChildren.setOnAction(event -> {
            Command cmd = new CommandCloseChildren(proxy.getId(), proxy.getLabel());
            ExecutionResult result = controller.commandManager.execute(cmd);
            GraphicalNMFApplication.handleExecutionResult(result);
        });
        menu.getItems().add(closeChildren);

        menu.getItems().add(new SeparatorMenuItem());

        //***** CREATE CHILD *****//
        List<String> children = controller.packageManager.getChildrenAssociation(proxy.getLabel());
        for(String child : children)
        {
            MenuItem item = new MenuItem("Add " + child);
            item.setOnAction(event -> createChild(child, proxy));
            menu.getItems().add(item);
        }

        menu.getItems().add(new SeparatorMenuItem());

        //***** CLOSE THIS NODE *****//
        MenuItem closeItem = new MenuItem("Close this " + proxy.getLabel());
        closeItem.setOnAction(event -> {
            Command cmd = new CommandCloseNode(proxy.getId(), proxy.getLabel());
            ExecutionResult result = controller.commandManager.execute(cmd);
            GraphicalNMFApplication.handleExecutionResult(result);
        });
        menu.getItems().add(closeItem);

        //***** DELETE THIS NODE *****//
        MenuItem menuDelete = new MenuItem("Delete this " + proxy.getLabel());
        menuDelete.setOnAction(event -> deleteNode(proxy));
        menu.getItems().add(menuDelete);

        return menu;
    }

    //endregion
}
