package adapters;

public class NoSuchClassException extends AdapterException
{
    private String label;

    public NoSuchClassException(String label)
    {
        super("Unknown class \"" + label + "\"");
        this.label = label;
    }

    public NoSuchClassException(String label, String message)
    {
        super(message);
        this.label = label;
    }

    public NoSuchClassException(String label, String message, Throwable cause)
    {
        super(message, cause);
        this.label = label;
    }

    public NoSuchClassException(String label, Throwable cause)
    {
        super("Unknown class \"" + label + "\"", cause);
        this.label = label;
    }

    public NoSuchClassException(String label, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super("Unknown class \"" + label + "\"", cause, enableSuppression, writableStackTrace);
        this.label = label;
    }

    public NoSuchClassException(String label, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
}
