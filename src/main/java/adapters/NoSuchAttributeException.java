package adapters;

public class NoSuchAttributeException extends AdapterException
{
    private String attributName;
    private String label;

    public NoSuchAttributeException(String attributName, String label)
    {
        super("No attribut \"" + attributName + "\" on " + label);
        this.attributName = attributName;
        this.label = label;
    }

    public NoSuchAttributeException(String attributName, String label, String message)
    {
        super(message);
        this.attributName = attributName;
        this.label = label;
    }

    public NoSuchAttributeException(String attributName, String label, String message, Throwable cause)
    {
        super(message, cause);
        this.attributName = attributName;
        this.label = label;
    }

    public NoSuchAttributeException(String attributName, String label, Throwable cause)
    {
        super("No attribut \"" + attributName + "\" on " + label, cause);
        this.attributName = attributName;
        this.label = label;
    }

    public NoSuchAttributeException(String attributName, String label, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super("No attribut \"" + attributName + "\" on " + label, cause, enableSuppression, writableStackTrace);
        this.attributName = attributName;
        this.label = label;
    }

    public NoSuchAttributeException(String attributName, String label, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.attributName = attributName;
        this.label = label;
    }

    public String getAttributName()
    {
        return attributName;
    }

    public String getLabel()
    {
        return label;
    }
}
