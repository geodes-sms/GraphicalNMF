package controllers;

public abstract class VisitorCompositeNodeProxy
{
    public enum VisitResult {
        Continue, SkipChildren, SkipSiblings, Terminate
    }

    public abstract VisitResult visit(CompositeNodeProxy node);
}
