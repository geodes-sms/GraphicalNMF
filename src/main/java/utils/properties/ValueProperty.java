package utils.properties;

public class ValueProperty<T> extends Property implements IValueProperty<T>
{
    private T value;

    public ValueProperty(String name, T value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public boolean setValue(T value)
    {
        this.value = value;
        return true;
    }
}
