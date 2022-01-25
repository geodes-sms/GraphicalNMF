package geodes.sms.gnmf.application.utils.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

public class DoubleField extends TextField
{
    private TextFormatter<Double> formatter;

    public DoubleField()
    {
        super();
        init();
    }

    public DoubleField(double initValue)
    {
        super();
        init();

        this.setValue(initValue);
    }

    private void init()
    {
        //Setting up the text formatter
        formatter = new TextFormatter<>(new DoubleStringConverter(), 0.0);
        this.setTextFormatter(formatter);
    }

    public void setValue(double value)
    {
        this.formatter.setValue(value);
    }

    public double getValue()
    {
        return formatter.getValue();
    }
}
