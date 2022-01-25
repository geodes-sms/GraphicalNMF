package geodes.sms.gnmf.application.utils.treeviews;

import javafx.scene.control.TreeCell;

public interface CellCreator<T>
{
    TreeCell<T> createCell();
}
