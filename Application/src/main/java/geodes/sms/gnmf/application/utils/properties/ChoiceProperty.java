package geodes.sms.gnmf.application.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class ChoiceProperty<T> extends Property implements IChoiceProperty<T>
{
    private ArrayList<T> choices;

    public ChoiceProperty(String name)
    {
        super(name);
        choices = new ArrayList<>();
    }
    public ChoiceProperty(String name, Collection<T> choices)
    {
        super(name);
        this.choices = new ArrayList<>(choices);
    }

    @Override
    public void setChoices(Collection<T> choices)
    {
        this.choices = new ArrayList<>(choices);
    }

    @Override
    public List<T> getChoices()
    {
        return new ArrayList<>(choices);
    }

    @Override
    public boolean isValidValue(T value)
    {
        return choices.contains(value);
    }
}
