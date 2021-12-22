package utils.properties;

import java.util.Collection;

public class CharacterListProperty extends ListProperty<Character>
{
    public CharacterListProperty(String name)
    {
        super(name);
    }

    public CharacterListProperty(String name, Collection<Character> value)
    {
        super(name, value);
    }
}
