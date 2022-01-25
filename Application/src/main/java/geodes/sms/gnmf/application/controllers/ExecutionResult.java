package geodes.sms.gnmf.application.controllers;

public class ExecutionResult
{
    public enum Result { Success, Failure }

    private Result result;
    private String message;
    private Exception cause;

    public ExecutionResult(Result result, String message)
    {
        this.result = result;
        this.message = message;
    }

    public ExecutionResult(Result result, Exception cause)
    {
        this.result = result;
        this.cause = cause;
        this.message = cause.getMessage();
    }

    public ExecutionResult(Result result, String message, Exception cause)
    {
        this.result = result;
        this.message = message;
        this.cause = cause;
    }

    public Result getResult()
    {
        return result;
    }

    public String getMessage()
    {
        return message;
    }

    public Exception getCause()
    {
        return cause;
    }
}
