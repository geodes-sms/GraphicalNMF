package geodes.sms.gnmf.application.view;


import geodes.sms.gnmf.application.utils.observers.Observer;

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
