package generator;

import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AdapterFactoryWriter extends AdapterWriter
{
    private static final String FILE = "adapter_factory.stg";
    private static final String TEMPLATE = "adapter_factory";
    private static final String CLASS_NAME = "adapter_factory_class";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_CLASSES = "classes";

    private String name;
    private HashSet<String> classes = new HashSet<>();

    public AdapterFactoryWriter()
    {
        template = createTemplate(FILE, TEMPLATE);
    }
    public AdapterFactoryWriter(String name)
    {
        this.name = name;
        template = createTemplate(TEMPLATE, FILE);
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void addClass(String className)
    {
        classes.add(className);
    }

    public void removeClass(String className)
    {
        classes.remove(className);
    }

    public List<String> getClasses()
    {
        return new ArrayList<>(classes);
    }

    @Override
    public String getClassName()
    {
        ST temp = createTemplate(CLASS_NAME, FILE);
        temp.add("name", name);

        return temp.render();
    }

    @Override
    protected void setupTemplate()
    {
        super.setupTemplate();

        //Add the name
        template.add(ATTR_NAME, name);

        //Add the classes
        for(String className : classes)
        {
            template.add(ATTR_CLASSES, className);
        }
    }
}
