package utils.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;


public class IntegerField extends TextField
{
    private TextFormatter<Integer> formatter;

    public IntegerField()
    {
        super();
        init();
    }

    public IntegerField(int initValue)
    {
        super();
        init();

        this.setValue(initValue);
    }

    private void init()
    {
        //Setting up the text formatter
        formatter = new TextFormatter<>(new IntegerStringConverter(), 0);
        this.setTextFormatter(formatter);
    }

    public void setValue(int value)
    {
        this.formatter.setValue(value);
    }

    public int getValue()
    {
        return formatter.getValue();
    }
}
