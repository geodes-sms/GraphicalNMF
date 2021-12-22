package utils.treeviews;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CellFactory<T> implements Callback<TreeView<T>, TreeCell<T>>
{
    protected CellCreator<T> cellCreator;
    protected ArrayList<CellFactoryProperty<T>> cellProperties;

    public CellFactory()
    {
        cellProperties = new ArrayList<>();
    }

    @Override
    public TreeCell<T> call(TreeView<T> param)
    {
        //Create the cell
        TreeCell<T> cell;
        if(cellCreator == null)
        {
            cell = new TreeCell<>();
        }
        else
        {
            cell = cellCreator.createCell();
        }

        //Configure the cell
        for(CellFactoryProperty<T> cellProperty : cellProperties)
        {
            cellProperty.configureCell(cell);
        }

        return cell;
    }

    public CellCreator<T> getCellCreator()
    {
        return cellCreator;
    }

    public void setCellCreator(final CellCreator<T> cellCreator)
    {
        this.cellCreator = cellCreator;
    }

    public boolean addCellFactoryProperty(@NotNull final CellFactoryProperty<T> cellProperty)
    {
        return cellProperties.add(cellProperty);
    }

    public boolean removeCellFactoryProperty(@NotNull final CellFactoryProperty<T> cellProperty)
    {
        return cellProperties.remove(cellProperty);
    }
}
