package utils.properties;

import java.util.Collection;
import java.util.List;

public interface IChoiceProperty<T> extends IProperty
{
    List<T> getChoices();
    void setChoices(Collection<T> choices);
    boolean isValidValue(T value);
}
