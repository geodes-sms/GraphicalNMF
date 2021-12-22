package utils.treeviews;

import javafx.scene.control.TreeCell;
import javafx.scene.input.DragEvent;

public class ScrollingDraggableCellProperty<T> extends DraggableCellProperty<T>
{

    @Override
    protected void dragOver(DragEvent event, TreeCell<T> cell)
    {
        super.dragOver(event, cell);

        int index = cell.getTreeView().getRow(cell.getTreeItem());

        double minY = cell.getBoundsInParent().getMinY();
        if(minY <= 10.0f)
        {
            cell.getTreeView().scrollTo(index - 1);
        }

        double maxY = cell.getTreeView().getBoundsInParent().getMaxY() - cell.getBoundsInParent().getMaxY();
        if(maxY <= 15.0f)
        {
            cell.getTreeView().scrollTo(index + 1);
        }

        event.consume();
    }
}
