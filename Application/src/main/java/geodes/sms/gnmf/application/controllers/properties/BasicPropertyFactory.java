package geodes.sms.gnmf.application.controllers.properties;

import geodes.sms.gnmf.application.utils.properties.*;

import java.util.Collection;
import java.util.List;

public class BasicPropertyFactory implements IPropertyFactory
{
    @Override
    public <T> IValueProperty<T> createValueProperty(String name, T templateValue)
    {
        if(templateValue instanceof String)
        {
            return (IValueProperty<T>) createValueProperty(name, (String) templateValue);
        }
        if(templateValue instanceof Integer)
        {
            return (IValueProperty<T>) createValueProperty(name, (Integer) templateValue);
        }
        if(templateValue instanceof Long)
        {
            return (IValueProperty<T>) createValueProperty(name, (Long) templateValue);
        }
        if(templateValue instanceof Float)
        {
            return (IValueProperty<T>) createValueProperty(name, (Float) templateValue);
        }
        if(templateValue instanceof Double)
        {
            return (IValueProperty<T>) createValueProperty(name, (Double) templateValue);
        }
        if(templateValue instanceof Character)
        {
            return (IValueProperty<T>) createValueProperty(name, (Character) templateValue);
        }
        if(templateValue instanceof Boolean)
        {
            return (IValueProperty<T>) createValueProperty(name, (Boolean) templateValue);
        }

        return createValuePropertyDelegate(name, templateValue);
    }

    public StringProperty createValueProperty(String name, String templateValue)
    {
        return new StringProperty(name, templateValue);
    }

    public IntegerProperty createValueProperty(String name, Integer templateValue)
    {
        return new IntegerProperty(name, templateValue);
    }

    public LongProperty createValueProperty(String name, Long templateValue)
    {
        return new LongProperty(name, templateValue);
    }

    public FloatProperty createValueProperty(String name, Float templateValue)
    {
        return new FloatProperty(name, templateValue);
    }

    public DoubleProperty createValueProperty(String name, Double templateValue)
    {
        return new DoubleProperty(name, templateValue);
    }

    public CharacterProperty createValueProperty(String name, Character templateValue)
    {
        return new CharacterProperty(name, templateValue);
    }

    public BooleanProperty createValueProperty(String name, Boolean templateValue)
    {
        return new BooleanProperty(name, templateValue);
    }

    @Override
    public <T> ICollectionProperty<T> createCollectionProperty(String name, T templateValue)
    {
        return createListProperty(name, templateValue);
    }

    @Override
    public <T> ListProperty<T> createListProperty(String name, T templateValue)
    {
        if(templateValue instanceof String)
        {
            return (ListProperty<T>) createListProperty(name, (String) templateValue);
        }
        if(templateValue instanceof Integer)
        {
            return (ListProperty<T>) createListProperty(name, (Integer) templateValue);
        }
        if(templateValue instanceof Long)
        {
            return (ListProperty<T>) createListProperty(name, (Long) templateValue);
        }
        if(templateValue instanceof Float)
        {
            return (ListProperty<T>) createListProperty(name, (Float) templateValue);
        }
        if(templateValue instanceof Double)
        {
            return (ListProperty<T>) createListProperty(name, (Double) templateValue);
        }
        if(templateValue instanceof Character)
        {
            return (ListProperty<T>) createListProperty(name, (Character) templateValue);
        }
        if(templateValue instanceof Boolean)
        {
            return (ListProperty<T>) createListProperty(name, (Boolean) templateValue);
        }

        return createListPropertyDelegate(name, templateValue);
    }

    public StringListProperty createListProperty(String name, String templateValue)
    {
        return new StringListProperty(name);
    }

    public IntegerListProperty createListProperty(String name, Integer templateValue)
    {
        return new IntegerListProperty(name);
    }

    public LongListProperty createListProperty(String name, Long templateValue)
    {
        return new LongListProperty(name);
    }

    public FloatListProperty createListProperty(String name, Float templateValue)
    {
        return new FloatListProperty(name);
    }

    public DoubleListProperty createListProperty(String name, Double templateValue)
    {
        return new DoubleListProperty(name);
    }

    public CharacterListProperty createListProperty(String name, Character templateValue)
    {
        return new CharacterListProperty(name);
    }

    @Override
    public <T> IValueChoiceProperty<T> createChoiceProperty(String name, Collection<T> choices, T templateValue)
    {
        ValueChoiceProperty<T> property = new ValueChoiceProperty<>(name, choices);
        property.setValue(templateValue);

        return property;
    }

    @Override
    public <T> IListChoiceProperty<T> createChoiceProperty(String name, Collection<T> choices, List<T> templateValue)
    {
        ListChoiceProperty<T> property = new ListChoiceProperty<>(name, choices);
        property.addAll(templateValue);

        return property;
    }

    protected <T> IValueProperty<T> createValuePropertyDelegate(String name, T templateValue)
    {
        throw new RuntimeException("Unsupported type <" + templateValue.getClass().getSimpleName() + ">");
    }

    protected <T> ListProperty<T> createListPropertyDelegate(String name, T templateValue)
    {
        throw new RuntimeException("Unsupported type <" + templateValue.getClass().getSimpleName() + ">");
    }
}
