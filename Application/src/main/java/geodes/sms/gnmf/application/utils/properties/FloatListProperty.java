package geodes.sms.gnmf.application.utils.properties;

import java.util.Collection;

public class FloatListProperty extends ListProperty<Float>
{
    public FloatListProperty(String name)
    {
        super(name);
    }

    public FloatListProperty(String name, Collection<Float> value)
    {
        super(name, value);
    }
}
