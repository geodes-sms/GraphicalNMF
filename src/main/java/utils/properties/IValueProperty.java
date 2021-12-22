package utils.properties;

public interface IValueProperty<T> extends IProperty
{
    T getValue();
    boolean setValue(T value);
}
