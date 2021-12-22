package utils.properties;

import java.util.Collection;

public class ValueChoiceProperty<T> extends ChoiceProperty<T> implements IValueChoiceProperty<T>
{
    private T value;

    public ValueChoiceProperty(String name)
    {
        super(name);
    }

    public ValueChoiceProperty(String name, Collection<T> choices)
    {
        super(name, choices);
    }

    public ValueChoiceProperty(String name, Collection<T> choices, T value)
    {
        super(name, choices);
        setValue(value);
    }

    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public boolean setValue(T value)
    {
        if(value != null && !isValidValue(value)) return false;

        this.value = value;
        return true;
    }

    @Override
    public void setChoices(Collection<T> choices)
    {
        super.setChoices(choices);

        if(!isValidValue(getValue()))
        {
            setValue(null);
        }
    }
}
