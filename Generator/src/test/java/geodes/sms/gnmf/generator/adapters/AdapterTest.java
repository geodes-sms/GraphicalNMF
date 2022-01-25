package geodes.sms.gnmf.generator.adapters;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdapterTest
{
    private MindMapsModelAdapter modelAdapter;
    private MindMapsAdapterFactory factory;
    private CacheAdapter cache;

    @BeforeAll
    public void setup()
    {
        factory = new MindMapsAdapterFactory(null);        //EPackage not needed for testing
        modelAdapter = (MindMapsModelAdapter) factory.connect("bolt://localhost:7687", "neo4j", "zyr");

        cache = modelAdapter.cache;     //get the cache
    }

    @AfterAll
    public void close()
    {
        modelAdapter.closeDatabase();
    }

    @AfterEach
    public void reset()
    {
        modelAdapter.clearCache();
    }

    @Test
    @DisplayName("Loading by ID")
    public void testIDLoading() throws AdapterException
    {
        MindMapNodeAdapter mindMap = (MindMapNodeAdapter) modelAdapter.loadNode("MindMap", 26);
        System.out.println(mindMap.getDisplayName() + ": " + mindMap.getNode());

        MindMapNodeAdapter mindMapB = (MindMapNodeAdapter) modelAdapter.loadNode("MindMap", 26);
        System.out.println(mindMap.getDisplayName() + ": " + mindMap.getNode());
        System.out.println(mindMapB.getDisplayName() + ": " + mindMapB.getNode());
    }

    @Test
    @DisplayName("Loading by label")
    public void testLabelLoading() throws AdapterException
    {
        SubTopicNodeAdapter adapterA;
        SubTopicNodeAdapter adapterB;

        adapterA = (SubTopicNodeAdapter) modelAdapter.loadAllNodes("SubTopic").get(0);
        adapterA.setAttribute("Name", "NewName");
        System.out.println(adapterA.getDisplayName() + ": " + adapterA.getNode());  //NewName



        adapterB = (SubTopicNodeAdapter) modelAdapter.loadAllNodes("SubTopic").get(0);
        System.out.println(adapterA.getDisplayName() + ": " + adapterA.getNode());  //précédemment
        System.out.println(adapterB.getDisplayName() + ": " + adapterB.getNode());
    }

    @Test
    @DisplayName("Loading by References")
    public void testRefLoading() throws AdapterException
    {
        MindMapNodeAdapter mindMap = (MindMapNodeAdapter) modelAdapter.loadNode("MindMap", 26);
        System.out.println(mindMap.getDisplayName() + ": " + mindMap.getNode());

        MarkerNodeAdapter markerA = (MarkerNodeAdapter) mindMap.getReferenceList("markers").get(0);
        System.out.println(markerA.getDisplayName() + ": " + markerA.getNode());

        MarkerNodeAdapter markerB = (MarkerNodeAdapter) mindMap.getReferenceList("markers").get(0);
        System.out.println(markerB.getDisplayName() + ": " + markerB.getNode());
    }

}
