package geodes.sms.gnmf.application.utils.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.CharacterStringConverter;

public class CharacterField extends TextField
{
    private TextFormatter<Character> formatter;

    public CharacterField()
    {
        super();
        init();
    }

    public CharacterField(char initValue)
    {
        super();
        init();

        this.setValue(initValue);
    }

    private void init()
    {
        //Setting up the text formatter
        formatter = new TextFormatter<>(new CharacterStringConverter());
        this.setTextFormatter(formatter);
    }

    public void setValue(char value)
    {
        this.formatter.setValue(value);
    }

    public char getValue()
    {
        return formatter.getValue();
    }
}
