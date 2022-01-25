package geodes.sms.gnmf.application.utils.observers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Observable
{
    private final ArrayList<Observer> observers;

    public Observable()
    {
        this.observers = new ArrayList<>();
    }

    /**
     * Add an observer to this observable.
     * @param observer The observer to add
     */
    public void addObserver(@NotNull Observer observer)
    {
        observers.add(observer);
    }

    /**
     * Remove an observer from this observable
     * @param observer The observer to remove
     */
    public void removeObserver(Observer observer)
    {
        observers.remove(observer);
    }

    /**
     * Notify observers of this object.
     */
    public void notifyObserver()
    {
        for(Observer observer : observers)
        {
            observer.update();
        }
    }
}
