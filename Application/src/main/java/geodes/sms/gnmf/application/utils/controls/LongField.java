package geodes.sms.gnmf.application.utils.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.LongStringConverter;

public class LongField extends TextField
{
    //private long minValue = Long.MIN_VALUE;
    //private long maxValue = Long.MAX_VALUE;

    private TextFormatter<Long> formatter;

    public LongField()
    {
        super();
        init();
    }

    public LongField(long initValue)
    {
        super();
        init();

        this.setValue(initValue);
    }

    private void init()
    {
        //Setting up the text formatter
        formatter = new TextFormatter<>(new LongStringConverter(), 0L);
        this.setTextFormatter(formatter);
    }

    public void setValue(long value)
    {
        this.formatter.setValue(value);
    }

    public long getValue()
    {
        return formatter.getValue();
    }

//    public long getMinValue()
//    {
//        return minValue;
//    }
//
//    public void setMinValue(long minValue)
//    {
//        this.minValue = minValue;
//
//        if(getValue() < minValue)
//        {
//            setValue(minValue);
//        }
//    }
//
//    public long getMaxValue()
//    {
//        return maxValue;
//    }
//
//    public void setMaxValue(long maxValue)
//    {
//        this.maxValue = maxValue;
//
//        if(getValue() > maxValue)
//        {
//            setValue(maxValue);
//        }
//    }
}
