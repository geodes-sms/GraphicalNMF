package utils.properties;

import java.util.Collection;

public class IntegerListProperty extends ListProperty<Integer>
{
    public IntegerListProperty(String name)
    {
        super(name);
    }

    public IntegerListProperty(String name, Collection<Integer> value)
    {
        super(name, value);
    }
}
