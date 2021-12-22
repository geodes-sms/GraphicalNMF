package utils.properties;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public interface ICollectionProperty<T> extends IProperty
{
    boolean addValue(T value);
    boolean addAll(Collection<T> values);

    boolean removeValue(T value);
    boolean removeAll(Collection<T> values);
    boolean removeIf(Predicate<? super T> filter);

    boolean retainAll(Collection<T> values);

    boolean contains(T value);
    boolean containsAll(Collection<T> values);

    void clear();
    boolean isEmpty();
    int size();

    List<T> getValue();
    Iterator<T> iterator();
}
