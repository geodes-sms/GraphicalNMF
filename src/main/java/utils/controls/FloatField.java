package utils.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;

public class FloatField extends TextField
{
    private TextFormatter<Float> formatter;

    public FloatField()
    {
        super();
        init();
    }

    public FloatField(float initValue)
    {
        super();
        init();

        this.setValue(initValue);
    }

    private void init()
    {
        //Setting up the text formatter
        formatter = new TextFormatter<>(new FloatStringConverter(), 0f);
        this.setTextFormatter(formatter);
    }

    public void setValue(float value)
    {
        this.formatter.setValue(value);
    }

    public float getValue()
    {
        return formatter.getValue();
    }
}
