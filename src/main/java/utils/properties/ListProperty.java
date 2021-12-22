package utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ListProperty<T> extends ValueProperty<List<T>> implements ICollectionProperty<T>
{
    public ListProperty(String name)
    {
        super(name, new ArrayList<>());
    }
    public ListProperty(String name, Collection<T> value)
    {
        super(name, new ArrayList<>(value));
    }

    @Override
    public List<T> getValue()
    {
        return new ArrayList<>(super.getValue());
    }

    @Override
    public boolean setValue(List<T> value)
    {
        ArrayList<T> newValue = new ArrayList<>(value);
        return super.setValue(newValue);
    }

    @Override
    public boolean addValue(T value)
    {
        return super.getValue().add(value);
    }

    @Override
    public boolean removeValue(T value)
    {
        return super.getValue().remove(value);
    }

    @Override
    public boolean addAll(Collection<T> values)
    {
        return super.getValue().addAll(values);
    }

    @Override
    public boolean removeAll(Collection<T> values)
    {
        return super.getValue().removeAll(values);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter)
    {
        return super.getValue().removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<T> values)
    {
        return super.getValue().retainAll(values);
    }

    @Override
    public boolean contains(T value)
    {
        return getValue().contains(value);
    }

    @Override
    public boolean containsAll(Collection<T> values)
    {
        return getValue().containsAll(values);
    }

    @Override
    public void clear()
    {
        super.getValue().clear();
    }

    @Override
    public boolean isEmpty()
    {
        return getValue().isEmpty();
    }

    @Override
    public int size()
    {
        return getValue().size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return super.getValue().listIterator();
    }
}
