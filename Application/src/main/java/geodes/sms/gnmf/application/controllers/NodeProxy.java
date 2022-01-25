package geodes.sms.gnmf.application.controllers;

public class NodeProxy
{
    private String name;
    private String label;
    private long id;

    protected NodeProxy(String name, String label, long id)
    {
        this.name = name;
        this.label = label;
        this.id = id;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getLabel()
    {
        return label;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return label + ": " + name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof NodeProxy)) return super.equals(obj);

        return ((NodeProxy) obj).getId() == this.getId();
    }
}
