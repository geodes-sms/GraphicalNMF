package utils.treeviews;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.*;
import javafx.util.Callback;

public class DraggableCellProperty<T> implements CellFactoryProperty<T>
{
    public static class Pair<T> {
        private final TreeItem<T> draggedItem;
        private final TreeItem<T> dropTarget;

        public Pair(TreeItem<T> draggedItem, TreeItem<T> dropTarget)
        {
            this.draggedItem = draggedItem;
            this.dropTarget = dropTarget;
        }

        public TreeItem<T> getDraggedItem()
        {
            return draggedItem;
        }

        public TreeItem<T> getDropTarget()
        {
            return dropTarget;
        }
    }

    public enum DropType {
        AsChild, AsSibling, None
    }

    private static final DataFormat FORMAT = new DataFormat("modnode");
    private static final String DROP_IN_STYLE = "-fx-background-color: #755AFF;";
    private static final String DROP_UNDER_STYLE = "-fx-border-color: #0000FF; -fx-border-width: 0 0 2 0; -fx-padding: 0 1 1 1";

    private TreeItem<T> draggedItem;

    private Callback<TreeItem<T>, Boolean> dragValidator;                   //Validate if an item can be dragged.
    private Callback<Pair<T>, Boolean> dropValidator;                       //Validate if the dragged item can be dropped in another item. <DraggredItem, DropInItem, return>
    private Callback<Pair<T>, Boolean> onDrop;                              //Action to do on drop. <DraggedItem, dropInItem, return>. Ignore return

    @Override
    public void configureCell(TreeCell<T> cell)
    {
        cell.setOnDragDetected(mouseEvent -> dragDetected(mouseEvent, cell));
        cell.setOnDragEntered(dragEvent -> dragEntered(dragEvent, cell));
        cell.setOnDragOver(dragEvent -> dragOver(dragEvent, cell));
        cell.setOnDragExited(dragEvent -> dragExited(dragEvent, cell));
        cell.setOnDragDropped(dragEvent -> drop(dragEvent, cell));
    }

    //region Drag & Drop

    protected void dragDetected(MouseEvent event, TreeCell<T> cell)
    {
        //Check if the cell is the root (root cannot be dragged)
        if (cell.getTreeItem().getParent() != null)
        {
            System.out.println("Drag Detected: " + cell.getTreeItem().getValue());

            //Validate if the cell can be drag -> true by default
            if(dragValidator == null || dragValidator.call(cell.getTreeItem()))
            {
                draggedItem = cell.getTreeItem();                           //Save the item being dragged

                //Put this item on the dragboard
                Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(FORMAT, draggedItem.getValue().toString());
                db.setContent(content);
                db.setDragView(cell.snapshot(null, null));
            }
        }

        event.consume();
    }

    protected void dragEntered(DragEvent event, TreeCell<T> cell)
    {
        //Check if the drag board has the right content
        if(event.getDragboard().hasContent(FORMAT))
        {
            System.out.println("Drag Entered: " + cell.getTreeItem().getValue());

            TreeItem<T> target = cell.getTreeItem();         //Get the item under the drag

            //Validate that the item under the drag is not the item being dragged
            if (target != null && draggedItem != null && !target.equals(draggedItem))
            {
                Pair<T> pair = new Pair<>(draggedItem, target);

                if(dropValidator == null || dropValidator.call(pair))
                {
                    showCellHasDropTarget(cell);
                }
            }
        }
    }

    protected void dragOver(DragEvent event, TreeCell<T> cell)
    {
        //Check if the drag board has the right content
        if(event.getDragboard().hasContent(FORMAT))
        {
            System.out.println("Drag Over: " + cell.getTreeItem().getValue());

            //*** Scrolling ****///

            //*** Drag & Drop Validation ***//
            TreeItem<T> target = cell.getTreeItem();         //Get the item under the drag

            //Validate that the item under the drag is not the item being dragged
            if (target != null && draggedItem != null && !target.equals(draggedItem))
            {
                Pair<T> pair = new Pair<>(draggedItem, target);

                if(dropValidator == null || dropValidator.call(pair))
                {
                    event.acceptTransferModes(TransferMode.MOVE);       //Use to show the node as a valid drop target
                }
            }
        }
    }

    protected void dragExited(DragEvent event, TreeCell<T> cell)
    {
        if(event.getDragboard().hasContent(FORMAT))
        {
            if(cell.getTreeItem() != null)
            {
                System.out.println("Drop Exit: " + cell.getTreeItem().getValue());
            }

            clearCellHasDropTarget(cell);       //Clear the cell
        }
    }

    protected void drop(DragEvent event, TreeCell<T> cell)
    {
        //Check if the drag board has the right content
        if(event.getDragboard().hasContent(FORMAT))
        {
            System.out.println("Drag Drop: " + cell.getTreeItem().getValue());

            TreeItem<T> target = cell.getTreeItem();         //Get the item under the drag

            //Validate that the item under the drag is not the item being dragged
            if (target != null && draggedItem != null && !target.equals(draggedItem))
            {
                Pair<T> pair = new Pair<>(draggedItem, target);

                //Validate if the dragged item can be dropped -> true by default
                if (dropValidator == null || dropValidator.call(pair))
                {
                    event.acceptTransferModes(TransferMode.MOVE);       //Accept the drop

                    //Do the drop
                    if(onDrop != null)
                    {
                        onDrop.call(pair);
                    }
                }
            }
        }


        draggedItem = null;                     //Clear the dragged item
        clearCellHasDropTarget(cell);           //Clear the cell

        event.setDropCompleted(true);           //Finish the drag&drop
    }

    protected void showCellHasDropTarget(TreeCell<T> cell)
    {
        cell.setStyle(DROP_IN_STYLE);
    }

    protected void clearCellHasDropTarget(TreeCell<T> cell)
    {
        if(cell.getStyle().equals(DROP_IN_STYLE))
        {
            cell.setStyle(null);
        }
    }

    //endregion

    public Callback<TreeItem<T>, Boolean> getDragValidator()
    {
        return dragValidator;
    }

    public void setDragValidator(final Callback<TreeItem<T>, Boolean> dragValidator)
    {
        this.dragValidator = dragValidator;
    }

    public Callback<Pair<T>, Boolean> getDropValidator()
    {
        return dropValidator;
    }

    public void setDropValidator(final Callback<Pair<T>, Boolean> dropValidator)
    {
        this.dropValidator = dropValidator;
    }

    public Callback<Pair<T>, Boolean> getOnDrop()
    {
        return onDrop;
    }

    public void setOnDrop(final Callback<Pair<T>, Boolean> onDrop)
    {
        this.onDrop = onDrop;
    }
}
