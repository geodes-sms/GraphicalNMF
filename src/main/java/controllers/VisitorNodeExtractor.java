package controllers;

import java.util.ArrayList;

public class VisitorNodeExtractor extends VisitorCompositeNodeProxy
{
    private String childLabel = null;

    private ArrayList<CompositeNodeProxy> nodes = new ArrayList<>();

    public VisitorNodeExtractor()
    {

    }
    public VisitorNodeExtractor(String childLabel)
    {
        this.childLabel = childLabel;
    }

    @Override
    public VisitResult visit(CompositeNodeProxy node)
    {
        if(childLabel == null || childLabel.equals(node.getLabel()))
        {
            nodes.add(node);
        }

        return VisitResult.Continue;
    }

    public String getChildLabel()
    {
        return childLabel;
    }

    public ArrayList<CompositeNodeProxy> getNodes()
    {
        return nodes;
    }
}
