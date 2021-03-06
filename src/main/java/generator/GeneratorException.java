package generator;

public class GeneratorException extends Exception
{
    public GeneratorException()
    {
    }

    public GeneratorException(String message)
    {
        super(message);
    }

    public GeneratorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public GeneratorException(Throwable cause)
    {
        super(cause);
    }

    public GeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
