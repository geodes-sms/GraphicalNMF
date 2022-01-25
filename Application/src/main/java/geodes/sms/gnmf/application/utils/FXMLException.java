package geodes.sms.gnmf.application.utils;

public class FXMLException extends Exception
{
    public FXMLException()
    {
    }

    public FXMLException(String message)
    {
        super(message);
    }

    public FXMLException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FXMLException(Throwable cause)
    {
        super(cause);
    }

    public FXMLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
