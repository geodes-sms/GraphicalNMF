package controllers;

import utils.observers.Observable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends Observable
{
    private final DatabaseController controller;

    protected CommandManager(DatabaseController controller)
    {
        super();
        this.controller = controller;
    }

    /**
     * Execute a command. This method delegate to {@link #silentExecution(Command command)}
     * </p>
     * Returns the result of the execution.
     * <ul>
     *     <li>{@link ExecutionResult.Result#Success} if the execution was successful.</li>
     *     <li>{@link ExecutionResult.Result#Failure} if the execution failed.</li>
     * </ul>
     * </p>
     *
     * In case of a failure, the observer will not be notified.
     *
     * @param command The command to execute
     * @return The result of the execution
     * @see #silentExecution(Command command)
     */
    public ExecutionResult execute(Command command)
    {
        ExecutionResult result = silentExecution(command);

        if(result.getResult() == ExecutionResult.Result.Success)
        {
            notifyObserver();
        }

        return result;
    }

    /**
     * Execute a batch of command all at once and return all the results. </br>
     * Observer are notified once all command are executed, no matter what the results are.
     * </p>
     * This method is very safe as it can leave the database in an unpredictable state.
     *
     * @param commands The commands to execute
     * @return The results of the execution
     * @see #silentExecution(Command command)
     */
    public List<ExecutionResult> executeBatch(List<Command> commands)
    {
        List<ExecutionResult> results = new ArrayList<>(commands.size());

        for(Command command : commands)
        {
            ExecutionResult result;

            try
            {
                result = silentExecution(command);
            }
            catch(Exception e)
            {
                result = new ExecutionResult(ExecutionResult.Result.Failure, e.getMessage(), e);
            }

            results.add(result);
        }

        notifyObserver();
        return results;
    }

    /**
     * Execute a command. </br>
     * Returns the result of the execution.
     * <ul>
     *      <li>{@link ExecutionResult.Result#Success} if the execution was successful.</li>
     *      <li>{@link ExecutionResult.Result#Failure} if the execution failed.</li>
     * </ul>
     * </p>
     *
     * This will not notify observers.
     *
     * @param command The command to execute
     * @return The result of the execution
     */
    private ExecutionResult silentExecution(Command command)
    {
        if(!controller.isConnected()) throw new ConnectionException("No connection to the database");

        ExecutionResult result;

        try
        {
            command.execute(controller);
            result = new ExecutionResult(ExecutionResult.Result.Success, "Success");
        }
        catch(Exception e)
        {
            result = new ExecutionResult(ExecutionResult.Result.Failure, e);
        }

        return result;
    }
}
