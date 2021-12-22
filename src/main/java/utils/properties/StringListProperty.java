package utils.properties;

import java.util.Collection;

public class StringListProperty extends ListProperty<String>
{
    public StringListProperty(String name)
    {
        super(name);
    }

    public StringListProperty(String name, Collection<String> value)
    {
        super(name, value);
    }
}
