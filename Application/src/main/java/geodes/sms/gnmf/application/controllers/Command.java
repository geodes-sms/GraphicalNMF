package geodes.sms.gnmf.application.controllers;


import geodes.sms.gnmf.generator.adapters.AdapterException;

public interface Command
{
    void execute(DatabaseController controller) throws CommandException, AdapterException;
}
