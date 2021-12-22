package controllers;

import adapters.AdapterException;

public interface Command
{
    void execute(DatabaseController controller) throws CommandException, AdapterException;
}
