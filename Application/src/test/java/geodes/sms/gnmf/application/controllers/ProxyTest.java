package geodes.sms.gnmf.application.controllers;

import geodes.sms.gnmf.generator.adapters.INodeAdapter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.util.Iterator;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProxyTest
{
    private ProxyManager manager;
    private CompositeNodeProxy subChildA;
    private CompositeNodeProxy childA;
    private CompositeNodeProxy childB;
    private CompositeNodeProxy childC;
    private CompositeNodeProxy childD;
    private CompositeNodeProxy parentA;
    private CompositeNodeProxy parentB;

    @BeforeAll
    public void init()
    {
        manager = new ProxyManager(null);       //can give null because the controller isn't used
    }

    @AfterEach
    public void reset()
    {
        manager.closeAllProxy();
    }


    @BeforeEach
    public void populateManager()
    {
        subChildA = manager.openProxy(createAdapter("SubChildA", "SubChild", 0));

        childA = manager.openProxy(createAdapter("ChildA", "Child", 1));
        childA.addChild(subChildA);
        childB = manager.openProxy(createAdapter("ChildB", "Child", 2));
        childC = manager.openProxy(createAdapter("ChildC", "Child", 3));
        childD = manager.openProxy(createAdapter("ChildD", "Child", 4));

        parentA = manager.openProxy(createAdapter("ParentA", "Parent", 5));
        parentA.addChild(childA);
        parentA.addChild(childB);
        parentA.addChild(childC);
        parentB = manager.openProxy(createAdapter("ParentB", "Parent", 6));
        parentB.addChild(childD);

    }

    @Test
    @DisplayName("Test Creating Proxy")
    public void testCreation()
    {
        String name = "Display Name";
        String label = "Node";
        long id = 12;

        INodeAdapter adapter = createAdapter(name, label, id);

        NodeProxy proxy = ProxyManager.createProxy(adapter);
        Assertions.assertEquals(proxy.getName(), name, "Creation failed - Wrong name");
        Assertions.assertEquals(proxy.getLabel(), label, "Creation failed - Wrong label");
        Assertions.assertEquals(proxy.getId(), id, "Creation failed - Wrong id");

        proxy = manager.openProxy(adapter);
        Assertions.assertEquals(proxy.getName(), name, "Creation failed - Wrong name");
        Assertions.assertEquals(proxy.getLabel(), label, "Creation failed - Wrong label");
        Assertions.assertEquals(proxy.getId(), id, "Creation failed - Wrong id");
    }

    @Test
    @DisplayName("Test Opening Proxy")
    public void testOpening()
    {
        String name = "Display Name";
        String label = "Node";
        long id = 12;

        INodeAdapter adapter = createAdapter(name, label, id);
        manager.openProxy(adapter);

        NodeProxy proxy = manager.getCompositeProxy(id);
        Assertions.assertNotNull(proxy, "Opening failed");
        Assertions.assertEquals(proxy.getName(), name, "Opening failed - Wrong name");
        Assertions.assertEquals(proxy.getLabel(), label, "Opening failed - Wrong label");
        Assertions.assertEquals(proxy.getId(), id, "Opening failed - Wrong id");
    }

    @Test
    @DisplayName("Parent-Child Relationship Test")
    public void testParentChildRelation()
    {
        CompositeNodeProxy containerA = new CompositeNodeProxy("PA", "Container", 0);
        CompositeNodeProxy containerB = new CompositeNodeProxy("PB", "Container", 1);

        CompositeNodeProxy containedA = new CompositeNodeProxy("CA", "Child", 2);
        CompositeNodeProxy containedB = new CompositeNodeProxy("CB", "Child", 3);

        //Test adding child
        containerA.addChild(containedA);

        Iterator<CompositeNodeProxy> parentAChildren = containerA.childrenIterator();
        Assertions.assertTrue(parentAChildren.hasNext(), "Add Child failed - No child added");
        Assertions.assertSame(parentAChildren.next(), containedA, "Add Child failed - Wrong child added");
        Assertions.assertFalse(parentAChildren.hasNext(), "Add Child failed - More than 1 child added");
        Assertions.assertSame(containedA.getParent(), containerA, "Add Child failed - Parent unchanged");

        //Test removing child
        containerA.removeChild(containedA);

        parentAChildren = containerA.childrenIterator();
        Assertions.assertFalse(parentAChildren.hasNext(), "Remove Child failed - Child not removed");
        Assertions.assertNull(containedA.getParent(), "Remove Child failed - Parent not removed");

        //Test setting parent
        containedB.setParent(containerB);

        Iterator<CompositeNodeProxy> parentBChildren = containerB.childrenIterator();
        Assertions.assertSame(containedB.getParent(), containerB, "Set Parent failed - Parent unchanged");
        Assertions.assertTrue(parentBChildren.hasNext(), "Set Parent failed - No added to children");
        Assertions.assertSame(parentBChildren.next(), containedB, "Set Parent failed - Wrong child added");
        Assertions.assertFalse(parentBChildren.hasNext(), "Set Parent failed - More than 1 child added");

        containerB.removeChild(containedB);

        //Test changing parent
        containerA.addChild(containedA);
        containerB.addChild(containedA);

        parentBChildren = containerB.childrenIterator();
        Assertions.assertSame(containedA.getParent(), containerB, "Change Parent failed - Parent unchanged");
        Assertions.assertTrue(parentBChildren.hasNext(), "Change Parent failed - No added to children");
        Assertions.assertSame(parentBChildren.next(), containedA, "Change Parent failed - Wrong child added");
        Assertions.assertFalse(parentBChildren.hasNext(), "Change Parent failed - More than 1 child added");

        //Test unsetting parent
        containedA.setParent(null);

        parentBChildren = containerB.childrenIterator();
        Assertions.assertNull(containedA.getParent(), "Remove Parent failed - Parent unchanged");
        Assertions.assertFalse(parentBChildren.hasNext(), "Remove Parent failed - Child not removed");
    }

    @Test
    @DisplayName("Visit order test")
    public void testVisitOrder()
    {
        boolean[] visitTable = new boolean[7];

        VisitorCompositeNodeProxy visitor = new VisitorCompositeNodeProxy()
        {
            @Override
            public VisitResult visit(CompositeNodeProxy node)
            {
                Assertions.assertFalse(visitTable[(int) node.getId()], "Visit failed - Node visited twice");
                visitTable[(int) node.getId()] = true;

                if(node.getParent() != null)
                {
                    Assertions.assertTrue(visitTable[(int) node.getParent().getId()], "Visit failed - Child visited before parent");
                }

                return VisitResult.Continue;
            }
        };
        manager.visitCompositeProxy(visitor);

        for(int i = 0; i < visitTable.length; i++)
        {
            Assertions.assertTrue(visitTable[i], "Visit failed - Node " + i + " not visited");
        }
    }

    @Test
    @DisplayName("Visit with SkipChildren test")
    public void testSkipChildren()
    {
        boolean[] visitTable = new boolean[7];

        VisitorCompositeNodeProxy visitor = new VisitorCompositeNodeProxy()
        {
            @Override
            public VisitResult visit(CompositeNodeProxy node)
            {
                visitTable[(int) node.getId()] = true;

                if(node == parentA)
                {
                    return VisitResult.SkipChildren;
                }
                else
                {
                    return VisitResult.Continue;
                }
            }
        };
        manager.visitCompositeProxy(visitor);

        Assertions.assertFalse(visitTable[(int) childA.getId()], "Visit failed - Child was visited");
        visitTable[(int) childA.getId()] = true;
        Assertions.assertFalse(visitTable[(int) childB.getId()], "Visit failed - Child was visited");
        visitTable[(int) childB.getId()] = true;
        Assertions.assertFalse(visitTable[(int) childC.getId()], "Visit failed - Child was visited");
        visitTable[(int) childC.getId()] = true;
        Assertions.assertFalse(visitTable[(int) subChildA.getId()], "Visit failed - Subchild was visited");
        visitTable[(int) subChildA.getId()] = true;

        for(int i = 0; i < visitTable.length; i++)
        {
            Assertions.assertTrue(visitTable[i], "Visit failed - Node " + i + " not visited");
        }
    }

    @Test
    @DisplayName("Visit with Terminate test")
    public void testVisitTerminate()
    {
        boolean[] visitTable = new boolean[7];

        VisitorCompositeNodeProxy visitor = new VisitorCompositeNodeProxy()
        {
            @Override
            public VisitResult visit(CompositeNodeProxy node)
            {
                visitTable[(int) node.getId()] = true;

                if(node == childA)
                {
                    return VisitResult.Terminate;
                }
                else
                {
                    return VisitResult.Continue;
                }
            }
        };
        manager.visitCompositeProxy(visitor);

        Assertions.assertFalse(visitTable[(int) subChildA.getId()], "Visit failed - Following node was visited");
        visitTable[(int) subChildA.getId()] = true;
    }

    @Test
    @DisplayName("Visit with SkipSiblings test")
    public void testVisitSkipSiblings()
    {
        boolean[] visitTable = new boolean[7];

        VisitorCompositeNodeProxy visitor = new VisitorCompositeNodeProxy()
        {
            @Override
            public VisitResult visit(CompositeNodeProxy node)
            {
                if(node.getParent() == parentA)
                {
                    visitTable[(int) childA.getId()] = true;
                    return VisitResult.SkipSiblings;
                }
                else
                {
                    visitTable[(int) node.getId()] = true;
                    return VisitResult.Continue;
                }
            }
        };
        manager.visitCompositeProxy(visitor);

        Assertions.assertFalse(visitTable[(int) childB.getId()], "Visit failed - Siblings was visited");
        visitTable[(int) childB.getId()] = true;
        Assertions.assertFalse(visitTable[(int) childC.getId()], "Visit failed - Siblings was visited");
        visitTable[(int) childC.getId()] = true;
        Assertions.assertFalse(visitTable[(int) subChildA.getId()], "Visit failed - Siblings was visited");
        visitTable[(int) subChildA.getId()] = true;

        for(int i = 0; i < visitTable.length; i++)
        {
            Assertions.assertTrue(visitTable[i], "Visit failed - Node " + i + " not visited");
        }
    }

    private INodeAdapter createAdapter(String name, String label, long id)
    {
        return new INodeAdapter()
        {
            @Override
            public <T> void setAttribute(String attrName, T attrValue)
            {
                //ignored
            }

            @Override
            public <T> T getAttribute(String attrName)
            {
                return null;
            }

            @Override
            public INodeAdapter addChild(String refName, String type)
            {
                return null;
            }

            @Override
            public void setReference(String refName, INodeAdapter node)
            {
                //ignored
            }

            @Override
            public void unsetReference(String refName, INodeAdapter node)
            {
                //ignored
            }

            @Override
            public INodeAdapter getReference(String refName)
            {
                return null;
            }

            @Override
            public List<INodeAdapter> getReferenceList(String refName)
            {
                return null;
            }

            @Override
            public String getDisplayName()
            {
                return name;
            }

            @Override
            public @NotNull String getLabel()
            {
                return label;
            }

            @Override
            public long getId()
            {
                return id;
            }
        };
    }
}
