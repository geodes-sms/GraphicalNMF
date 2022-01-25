package geodes.sms.gnmf.application.controllers.properties;


import geodes.sms.gnmf.application.utils.properties.*;

import java.util.Collection;
import java.util.List;

public interface IPropertyFactory
{
    /**
     * Create an {@link IValueProperty} of type T.
     *
     * @param name The name of the property
     * @param templateValue A template value to identify the type of the property
     * @param <T> The type of the property
     * @return An {@link IValueProperty}
     */
    <T> IValueProperty<T> createValueProperty(String name, T templateValue);

    /**
     * Create an {@link ICollectionProperty} of type T.
     *
     * @param name The name of the property
     * @param templateValue A value to identify the type of the property
     * @param <T> The type of the property
     * @return An {@link IValueProperty}
     */
    <T> ICollectionProperty<T> createCollectionProperty(String name, T templateValue);

    /**
     * Create an {@link ListProperty} of type T.
     *
     * @param name The name of the property
     * @param templateValue A value to identify the type of the property
     * @param <T> The type of the property
     * @return An {@link ListProperty}
     */
    <T> ListProperty<T> createListProperty(String name, T templateValue);

    /**
     * Create an {@link IValueChoiceProperty} of type T with
     * the given choices.
     *
     * @param name The name of the property
     * @param choices The choices of the property
     * @param templateValue A value to identify the type of the property
     * @param <T> The type of the property
     * @return An {@link IValueChoiceProperty}
     */
    <T> IValueChoiceProperty<T> createChoiceProperty(String name, Collection<T> choices, T templateValue);

    /**
     * Create an {@link IListChoiceProperty} of type T with
     * the given choices.
     *
     * @param name The name of the property
     * @param choices The choices of the property
     * @param templateValue A value to identify the type of the property
     * @param <T> The type of the property
     * @return An {@link IListChoiceProperty}
     */
    <T> IListChoiceProperty<T> createChoiceProperty(String name, Collection<T> choices, List<T> templateValue);

    /**
     * Create an {@link AssociationProperty}.
     *
     * @param name The name of the property
     * @param label The label of the property
     * @return An {@link AssociationProperty}
     */
    default AssociationProperty createAssociationProperty(String name, String label)
    {
        return new AssociationProperty(name, label);
    }

    /**
     * Create an {@link AssociationListProperty}.
     *
     * @param name The name of the property
     * @param label The label of the property
     * @return An {@link AssociationListProperty}
     */
    default AssociationListProperty createAssociationListProperty(String name, String label)
    {
        return new AssociationListProperty(name, label);
    }
}
