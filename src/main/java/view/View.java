package view;

import utils.observers.Observer;

public interface View extends Observer
{
    /**
     * Request the view to close.
     */
    void quit();

    /**
     * Close the view. </p>
     *
     * In general, you should use {@link #quit()}
     * instead.
     */
    void close();
}
