package geodes.sms.gnmf.generator.writers;

import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public class ModelAttributeRenderer implements AttributeRenderer<String>
{
    @Override
    public String toString(String value, String formatString, Locale locale)
    {
        if(formatString == null) return value;

        switch(formatString)
        {
            case "none":
                return value;

            case "camelcase":
            case "cc":
                return renderCamelCase(value);

            case "lowercase":
            case "lc":
                return value.toLowerCase();

            default:
                return null;
        }
    }

    private String renderCamelCase(String value)
    {
        return String.valueOf(value.charAt(0)).toUpperCase() + value.substring(1);
    }
}
