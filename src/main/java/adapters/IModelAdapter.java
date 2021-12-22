package adapters;

import java.util.List;

public interface IModelAdapter
{
    INodeAdapter createNode(String label) throws AdapterException;
    void removeNode(INodeAdapter node) throws AdapterException;

    INodeAdapter loadNode(String label, long id) throws AdapterException;
    List<INodeAdapter> loadAllNodes(String label) throws AdapterException;
    void unloadNode(INodeAdapter node) throws AdapterException;

    void saveToDatabase();
    void closeDatabase();
    void clearCache();
    void clearDatabase();
}
