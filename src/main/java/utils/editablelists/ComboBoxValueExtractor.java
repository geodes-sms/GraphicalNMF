package utils.editablelists;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.util.Callback;

public class ComboBoxValueExtractor<T> implements Callback<Control, T>
{
    @Override
    public T call(Control control)
    {
        ComboBox<T> comboBox = (ComboBox<T>) control;
        return comboBox.getSelectionModel().getSelectedItem();
    }
}
