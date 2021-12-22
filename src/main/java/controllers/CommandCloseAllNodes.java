package controllers;

public class CommandCloseAllNodes implements Command
{
    @Override
    public void execute(DatabaseController controller)
    {
        controller.proxyManager.closeAllProxy();
    }
}
