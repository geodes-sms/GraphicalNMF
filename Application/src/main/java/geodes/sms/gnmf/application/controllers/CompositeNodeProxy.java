package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.application.controllers.VisitorCompositeNodeProxy.VisitResult;

import java.util.ArrayList;
import java.util.Iterator;

public class CompositeNodeProxy extends NodeProxy
{
    private CompositeNodeProxy parent;
    private final ArrayList<CompositeNodeProxy> children = new ArrayList<>();

    protected CompositeNodeProxy(String name, String label, long id)
    {
        super(name, label, id);
    }

    /**
     * Set the parent of this proxy. </br>
     * This proxy will be added to the new parent's children list,
     * if the new parent isn't null. If this proxy had a parent,
     * it will be removed fromthat parent children list.
     *
     * @param parent The new parent, can be null
     */
    protected void setParent(CompositeNodeProxy parent)
    {
        if(this.parent != parent)
        {
            CompositeNodeProxy oldParent = this.parent;

            //Remove itself from the old parent
            this.parent = null;
            if(oldParent != null)
            {
                oldParent.removeChild(this);
            }

            //Add itself to the new parent
            this.parent = parent;
            if(parent != null)
            {
                parent.addChild(this);
            }
        }
    }

    /**
     * Add a new proxy to the children of this proxy. </br>
     * If the child's parent isn't this proxy, then set
     * this proxy as the parent of the child.
     *
     * @param child The new proxy, cannot be null
     */
    protected void addChild(CompositeNodeProxy child)
    {
        if(child == null) throw new IllegalArgumentException();

        if(!children.contains(child))
        {
            children.add(child);        //Add the new child to the list

            //Set this node as the parent of the new child, if needed
            if(child.getParent() != this)
            {
                child.setParent(this);
            }
        }
    }

    /**
     * Remove a proxy from the children of this proxy. </br>
     * Also unset the parent of the removed proxy.
     *
     * @param child The proxy to remove, can be null
     */
    protected void removeChild(CompositeNodeProxy child)
    {
        if(children.contains(child))
        {
            children.remove(child);             //Remove the child from the list

            //Unset the parent of the child, if needed
            if(child.getParent() != null)
            {
                child.setParent(null);
            }
        }
    }

    public CompositeNodeProxy getParent()
    {
        return parent;
    }

    public Iterator<CompositeNodeProxy> childrenIterator()
    {
        return children.listIterator();
    }

    /**
     * Visit this proxy with the given visitor. </br>
     * This will not do the visit and instead delegate
     * to {@link #doRecursiveVisit(VisitorCompositeNodeProxy)}.
     *
     * @param visitor A visitor
     */
    public void visitProxy(VisitorCompositeNodeProxy visitor)
    {
        doRecursiveVisit(visitor);
    }

    /**
     * Do the visit of this node with a visitor, and return the result.
     * This result this returns will depend on the result of the visitor.
     * <ul>
     *     <li>Terminate - return Terminate</li>
     *     <li>SkipSiblings - return SkipSiblings</li>
     *     <li>SkipChildren - return Continue</li>
     *     <li>Continue - visit the children and return Continue or Terminate</li>
     * </ul>
     *
     * @param visitor The visitor
     * @return The result of this visit
     */
    protected VisitorCompositeNodeProxy.VisitResult doRecursiveVisit(VisitorCompositeNodeProxy visitor)
    {
        VisitResult result = visitor.visit(this);

        switch(result)
        {
            //Stop the visit and return the result
            case SkipSiblings:
            case Terminate:
                break;

            //Stop the visit and return continue
            case SkipChildren:
                result = VisitResult.Continue;
                break;

            //Visit the children
            case Continue:
                Iterator<CompositeNodeProxy> iterator = childrenIterator();

                //Stop the visit at Terminate or SkipSiblings
                while(iterator.hasNext() &&
                        result != VisitResult.Terminate &&
                        result != VisitResult.SkipSiblings)
                {
                    CompositeNodeProxy child = iterator.next();
                    result = child.doRecursiveVisit(visitor);
                }

                //If last result was SkipSiblings, treat it as Continue
                if(result != VisitResult.Terminate)
                {
                    result = VisitResult.Continue;
                }
        }

        return result;
    }
}
