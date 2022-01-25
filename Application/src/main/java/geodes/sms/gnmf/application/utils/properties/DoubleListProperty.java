package geodes.sms.gnmf.application.utils.properties;

import java.util.Collection;

public class DoubleListProperty extends ListProperty<Double>
{
    public DoubleListProperty(String name)
    {
        super(name);
    }

    public DoubleListProperty(String name, Collection<Double> value)
    {
        super(name, value);
    }
}
