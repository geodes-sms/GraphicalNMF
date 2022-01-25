package geodes.sms.gnmf.application.utils.properties;

public interface IValueProperty<T> extends IProperty
{
    T getValue();
    boolean setValue(T value);
}
