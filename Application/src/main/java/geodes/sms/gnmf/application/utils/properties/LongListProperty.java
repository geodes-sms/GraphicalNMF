package geodes.sms.gnmf.application.utils.properties;

import java.util.Collection;

public class LongListProperty extends ListProperty<Long>
{
    public LongListProperty(String name)
    {
        super(name);
    }

    public LongListProperty(String name, Collection<Long> value)
    {
        super(name, value);
    }
}
