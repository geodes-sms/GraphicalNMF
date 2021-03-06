package utils.treeviews;

import javafx.scene.control.TreeCell;

public interface CellFactoryProperty<T>
{
    void configureCell(final TreeCell<T> cell);
}
