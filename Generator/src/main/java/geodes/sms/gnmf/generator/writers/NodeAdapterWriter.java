package geodes.sms.gnmf.generator.writers;

import org.stringtemplate.v4.ST;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeAdapterWriter extends AdapterWriter
{
    private static class Component
    {
        public String name;
        public String type;
        public String superType = null;
        public boolean isMany = false;
        public boolean isEnum = false;

        public Component(String name, String type)
        {
            this.name = name;
            this.type = type;
        }
        public Component(String name, String type, String superType)
        {
            this.name = name;
            this.type = type;
            this.superType = superType;
        }
        public Component(String name, String type, boolean isMany)
        {
            this.name = name;
            this.type = type;
            this.isMany = isMany;
        }
        public Component(String name, String type, String superType, boolean isMany)
        {
            this.name = name;
            this.type = type;
            this.superType = superType;
            this.isMany = isMany;
        }
        public Component(String name, String type, boolean isMany, boolean isEnum)
        {
            this.name = name;
            this.type = type;
            this.isEnum = isEnum;
        }
        public Component(String name, String type, String superType, boolean isMany, boolean isEnum)
        {
            this.name = name;
            this.type = type;
            this.superType = superType;
            this.isEnum = isEnum;
            this.isMany = isMany;
        }
    }

    private static final String FILE = "node_adapter.stg";
    private static final String TEMPLATE = "node_adapter";
    private static final String CLASS_NAME = "node_adapter_class";

    private static final String ATTR_LABEL = "label";
    private static final String ATTR_ATTRIBUTES = "attributes";
    private static final String ATTR_CHILDREN = "children";
    private static final String ATTR_LIST_CHILDREN = "list_children";
    private static final String ATTR_REFERENCES = "references";
    private static final String ATTR_LIST_REFERENCES = "list_references";
    private static final String ATTR_ID = "id";

    private String label;
    private final HashMap<String, Component> attributes = new HashMap<>();
    private final HashMap<String, Component> children = new HashMap<>();
    private final HashMap<String, Component> references = new HashMap<>();
    private Component id;

    public NodeAdapterWriter() throws URISyntaxException
    {
        template = createTemplate(TEMPLATE, FILE);
    }
    public NodeAdapterWriter(String label) throws URISyntaxException
    {
        this.label = label;
        template = createTemplate(TEMPLATE, FILE);
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void addAttribute(String name, String type)
    {
        Component component = new Component(name, type);
        attributes.put(name, component);
    }

    public void addEnum(String name, String type)
    {
        Component component = new Component(name, type, false, true);
        attributes.put(name, component);
    }

    public void removeAttribute(String name)
    {
        attributes.remove(name);

        if(name.equals(id.name))
        {
            id = null;
        }
    }

    public List<String> getAttributes()
    {
        return new ArrayList<>(attributes.keySet());
    }

    public void addChildren(String name, String type, String superType, boolean isMany)
    {
        Component component = new Component(name, type, superType, isMany);
        children.put(name, component);
    }

    public void removeChildren(String name)
    {
        children.remove(name);
    }

    public List<String> getChildren()
    {
        return new ArrayList<>(children.keySet());
    }

    public void addReference(String name, String type, boolean isMany)
    {
        Component component = new Component(name, type, isMany);
        references.put(name, component);
    }

    public void removeReference(String name)
    {
        references.remove(name);
    }

    public List<String> getReferences()
    {
        return new ArrayList<>(references.keySet());
    }

    public void setId(String name)
    {
        if(attributes.containsKey(name))
        {
            id = attributes.get(name);
        }
    }

    public String getId()
    {
        return id.name;
    }

    @Override
    public String getClassName() throws URISyntaxException
    {
        ST temp = createTemplate(CLASS_NAME, FILE);
        temp.add("name", label);

        return temp.render();
    }

    @Override
    protected void setupTemplate()
    {
        super.setupTemplate();

        //Add the label
        template.add(ATTR_LABEL, label);

        //Add the attributes
        Component defaultId = null;
        for(Component comp : attributes.values())
        {
            template.add(ATTR_ATTRIBUTES, comp);

            //Use a string attribut as a default id
            if(id == null && comp.type.equals("java.lang.String"))
            {
                id = comp;
            }
        }

        //Add the children
        for(Component comp : children.values())
        {
            if(comp.isMany)
            {
                template.add(ATTR_LIST_CHILDREN, comp);
            }
            else
            {
                template.add(ATTR_CHILDREN, comp);
            }
        }

        //Add the references
        for(Component comp : references.values())
        {
            if(comp.isMany)
            {
                template.add(ATTR_LIST_REFERENCES, comp);
            }
            else
            {
                template.add(ATTR_REFERENCES, comp);
            }
        }

        //Add the id
        template.add(ATTR_ID, id);
    }
}
