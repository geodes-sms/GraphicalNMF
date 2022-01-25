package geodes.sms.gnmf.application.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ListChoiceProperty<T> extends ChoiceProperty<T> implements IListChoiceProperty<T>
{
    private final ArrayList<T> values = new ArrayList<>();

    public ListChoiceProperty(String name)
    {
        super(name);
    }
    public ListChoiceProperty(String name, Collection<T> choices)
    {
        super(name, choices);
    }

    @Override
    public List<T> getValue()
    {
        return new ArrayList<>(values);
    }

    @Override
    public boolean addValue(T value)
    {
        if(isValidValue(value))
        {
            return this.values.add(value);
        }

        return false;
    }

    @Override
    public boolean addAll(Collection<T> values)
    {
        if(getChoices().containsAll(values))
        {
            return this.values.addAll(values);
        }

        return false;
    }

    @Override
    public boolean removeValue(T value)
    {
        return this.values.remove(value);
    }

    @Override
    public boolean removeAll(Collection<T> values)
    {
        return this.values.removeAll(values);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter)
    {
        return this.values.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<T> values)
    {
        return this.values.retainAll(values);
    }

    @Override
    public boolean contains(T value)
    {
        return this.values.contains(value);
    }

    @Override
    public boolean containsAll(Collection<T> values)
    {
        return this.values.containsAll(values);
    }

    @Override
    public void clear()
    {
        this.values.clear();
    }

    @Override
    public boolean isEmpty()
    {
        return this.values.isEmpty();
    }

    @Override
    public int size()
    {
        return this.values.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.values.listIterator();
    }
}
