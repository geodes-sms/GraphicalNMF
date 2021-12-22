package adapters;

import java.util.List;

public interface INodeAdapter
{
    <T> void setAttribute(String attrName, T attrValue) throws AdapterException;
    <T> T getAttribute(String attrName) throws AdapterException;

    INodeAdapter addChild(String refName, String type) throws AdapterException;

    void setReference(String refName, INodeAdapter node) throws AdapterException;
    void unsetReference(String refName, INodeAdapter node) throws AdapterException;
    INodeAdapter getReference(String refName) throws AdapterException;
    List<INodeAdapter> getReferenceList(String refName) throws AdapterException;

    long getId();
    String getLabel();
    String getDisplayName();
}
