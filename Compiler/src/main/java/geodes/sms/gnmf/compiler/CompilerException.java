package geodes.sms.gnmf.compiler;

public class CompilerException extends Exception
{
    public CompilerException()
    {
    }

    public CompilerException(String message)
    {
        super(message);
    }

    public CompilerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CompilerException(Throwable cause)
    {
        super(cause);
    }

    public CompilerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
