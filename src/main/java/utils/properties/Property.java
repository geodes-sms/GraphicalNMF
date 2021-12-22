package utils.properties;

public abstract class Property implements IProperty
{
    private final String name;

    public Property(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
