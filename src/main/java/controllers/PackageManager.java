package controllers;

import adapters.INodeAdapter;
import controllers.properties.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import utils.properties.IChoiceProperty;
import utils.properties.IProperty;
import utils.properties.IValueProperty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PackageManager
{
    private IPropertyFactory propertyFactory;

    private final DatabaseController controller;
    private final EPackage ePackage;

    private HashMap<Long, PropertyWrapper> wrappers = new HashMap<>();

    PackageManager(DatabaseController controller, EPackage ePackage)
    {
        this.controller = controller;
        this.ePackage = ePackage;
        this.propertyFactory = new BasicPropertyFactory();
    }

    /**
     * Return the EClass whose name is <code>label</code>.
     * @param label The name of the EClass to return.
     * @return An EClass, or null
     */
    protected EClass getEClass(String label)
    {
        EClass eClass = null;

        for(EClassifier eClassifier : ePackage.getEClassifiers())
        {
            if(eClassifier instanceof EClass
                    && eClassifier.getName().equals(label))
            {
                eClass = (EClass) eClassifier;
            }
        }

        return eClass;
    }

    /**
     * Create a {@link PropertyWrapper} from the given {@link INodeAdapter} and saves it.
     * The node associated with the wrapper is considered "loaded". When unload, the
     * wrapper should be remove using {@link #closePropertyWrapper(PropertyWrapper)}.
     * </p>
     * The other boolean parameters controled which attributes and/or associations are loaded
     * in the wrapper.
     *
     * @param adapter The {@link INodeAdapter} for the wrapper
     * @param loadAttributes Should the wrapper contain attributes
     * @param loadChildren Should the wrapper contain children associations
     * @param loadReferences Should the wrapper contain reference associations
     * @return The {@link PropertyWrapper} created
     * @see PropertyWrapper
     */
    PropertyWrapper openPropertyWrapper(INodeAdapter adapter, boolean loadAttributes,
                                        boolean loadChildren, boolean loadReferences)
    {
        if(wrappers.size() >= 1) throw new RuntimeException("Can't load more than 1 node at a time");

        EClass eClass = getEClass(adapter.getLabel());
        if(eClass == null) throw new RuntimeException("Can't find EClass for " + adapter.getLabel());

        PropertyWrapper wrapper = new PropertyWrapper(adapter);         //Create the wrapper

        if(loadAttributes) addAttributesToWrapper(wrapper, eClass, propertyFactory);     //Add attributes to it
        if(loadChildren) addChildrenToWrapper(wrapper, eClass, propertyFactory);         //Add children association to it
        if(loadReferences) addReferencesToWrapper(wrapper, eClass, propertyFactory);     //Add reference association to it
        refresh(wrapper);                                                                //Set the values of the properties

        wrappers.put(adapter.getId(), wrapper);                        //Save it

        return wrapper;

    }

    /**
     * Create a wrapper with all attributes and associations loaded.
     *
     * @param adapter The {@link INodeAdapter}
     * @return The {@link PropertyWrapper} created
     * @see #openPropertyWrapper(INodeAdapter, boolean, boolean, boolean)
     */
    PropertyWrapper openPropertyWrapper(INodeAdapter adapter)
    {
        return openPropertyWrapper(adapter, true, true, true);
    }

    /**
     * Remove a {@link PropertyWrapper} from this manager. The adapter associated
     * with the wrapper should also be unload after this method is called.
     *
     * @param wrapper The {@link PropertyWrapper} to close
     */
    void closePropertyWrapper(PropertyWrapper wrapper)
    {
        closePropertyWrapper(wrapper.getAdapter().getId(), wrapper);
    }

    /**
     * Remove a {@link PropertyWrapper} with the given id from this manager. </br>
     * This method should be used if the adapter associated to the wrapper is
     * unload before trying to remove it.
     *
     * @param id The id of the wrapper, used as the key
     * @param wrapper The wrapper
     * @see #closePropertyWrapper(PropertyWrapper)
     */
    void closePropertyWrapper(long id, PropertyWrapper wrapper)
    {
        wrappers.remove(id, wrapper);
    }


    /** FIXME: This could be on {@link PropertyWrapper} instead.
     * Update the values of the {@link IProperty} on the given {@link PropertyWrapper}
     * by reading them from its {@link INodeAdapter}. </br>
     * Any property that cannot be updated (due to some kind of error) will be
     * removed from the wrapper.
     * </p>
     *
     * @param wrapper The wrapper to update
     */
    public void refresh(PropertyWrapper wrapper)
    {
        ArrayList<IProperty> toRemove = new ArrayList<>();

        Iterator<IProperty> iterator = wrapper.propertyIterator();
        while(iterator.hasNext())
        {
            IProperty property = iterator.next();

            try
            {
                if (property instanceof AssociationListProperty)
                {
                    //Clear the values of the property
                    AssociationListProperty refListProp = (AssociationListProperty) property;
                    refListProp.clear();

                    //Reset the choices
                    List<NodeProxy> choices = controller.getListOfNodes(refListProp.getLabel());
                    refListProp.setChoices(choices);

                    //Reset the values
                    try
                    {
                        List<INodeAdapter> values = wrapper.getAdapter().getReferenceList(refListProp.getName());
                        for (INodeAdapter value : values)
                        {
                            NodeProxy proxy = ProxyManager.createProxy(value);
                            refListProp.addValue(proxy);

                            controller.modelAdapter.unloadNode(value);
                        }
                    }
                    catch(NullPointerException ignored)
                    {
                        //ignore it
                    }
                }
                else if(property instanceof AssociationProperty)
                {
                    //Remove the value of the property
                    AssociationProperty refProp = (AssociationProperty) property;
                    refProp.setValue(null);

                    //Reset the choices
                    List<NodeProxy> choices = controller.getListOfNodes(refProp.getLabel());
                    refProp.setChoices(choices);

                    //Reset the value
                    try
                    {
                        INodeAdapter value = wrapper.getAdapter().getReference(refProp.getName());
                        refProp.setValue(ProxyManager.createProxy(value));
                        controller.modelAdapter.unloadNode(value);
                    }
                    catch (NullPointerException ignored)
                    {
                        //ignore it
                    }
                }
                else if(property instanceof IValueProperty<?>)
                {
                    IValueProperty<?> valueProp = (IValueProperty<?>) property;

                    try
                    {
                        valueProp.setValue(wrapper.getAdapter().getAttribute(valueProp.getName()));
                    }
                    catch(NullPointerException ignored)
                    {
                        //ignore it
                    }
                }
                else
                {
                    throw new Exception("Unexpected property " + property.getName());
                }
            }
            catch(Exception e)
            {
                System.err.println("Removing property " + property.getName());
                e.printStackTrace();

                toRemove.add(property);
            }
        }

        for(IProperty prop : toRemove)
        {
            wrapper.removeProperty(prop);
        }
    }

    /**
     * Return a list of the name of all the concrete classes of this package.
     *
     * @return A list of name
     */
    public List<String> getClasses()
    {
        ArrayList<String> classNames = new ArrayList<>();

        for(EClassifier classifier : ePackage.getEClassifiers())
        {
            if(classifier instanceof EClass
                    && !((EClass) classifier).isAbstract() && !((EClass) classifier).isInterface())
            {
                classNames.add(classifier.getName());
            }
        }

        return classNames;
    }

    /**
     * Returns all the subclasse of the given class. </br>
     * If the class denoted by label is a concrete class,
     * it will also be returned.
     *
     * @param label The label of the class
     * @return A list of the name of all the subclasses of the class denoted by label.
     */
    public List<String> getSubClasses(String label)
    {
        ArrayList<String> className = new ArrayList<>();

        EClass superTypeClass = getEClass(label);

        for(EClassifier classifier : ePackage.getEClassifiers())
        {
            if(classifier instanceof EClass
                    && !((EClass) classifier).isAbstract() && !((EClass) classifier).isInterface())
            {
                if(superTypeClass.isSuperTypeOf((EClass) classifier))
                {
                    className.add(classifier.getName());
                }
            }
        }

        return className;
    }

    /**
     * Returns all the possible subclasses for a children reference.
     *
     * @param label The label of the node with the reference
     * @param child The name of the reference
     * @return A list of the name of all possible subclasses for that reference, empty if none
     */
    public List<String> getChildArg(String label, String child)
    {
        EClass eClass = getEClass(label);
        EList<EReference> refs = eClass.getEAllContainments();

        for(EReference ref : refs)
        {
            if(ref.getName().equals(child))
            {
                return getSubClasses(ref.getEType().getName());
            }
        }

        return new ArrayList<>();
    }

    /**
     * Returns a list of the name of all the containment association of
     * a given class.
     *
     * @param parentLabel The label of the parent class
     * @return A list of name
     */
    public List<String> getChildrenAssociation(String parentLabel)
    {
        ArrayList<String> classNames = new ArrayList<>();

        EClass parentClass = getEClass(parentLabel);

        for(EReference reference : parentClass.getEAllReferences())
        {
            if(reference.isContainment())
            {
                classNames.add(reference.getName());
            }
        }

        return classNames;
    }

    public PropertyWrapper getPropertyWrapper(long id)
    {
        return wrappers.get(id);
    }

    public Iterator<PropertyWrapper> propertyWrapperIterator()
    {
        return wrappers.values().iterator();
    }

    public IPropertyFactory getPropertyFactory()
    {
        return propertyFactory;
    }

    public void setPropertyFactory(IPropertyFactory propertyFactory)
    {
        if(propertyFactory == null) throw new IllegalArgumentException();

        this.propertyFactory = propertyFactory;
    }

    //region Properties

    private static void addAttributesToWrapper(PropertyWrapper wrapper, EClass eClass, IPropertyFactory factory)
    {
        EList<EAttribute> attributes = eClass.getEAllAttributes();

        for(EAttribute attr : attributes)
        {
            try
            {
                IProperty property = createProperty(attr, factory);
                wrapper.addProperty(property);
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void addChildrenToWrapper(PropertyWrapper wrapper, EClass eClass, IPropertyFactory factory)
    {
        EList<EReference> children = eClass.getEAllContainments();

        for(EReference child : children)
        {
            try
            {
                IChoiceProperty<NodeProxy> property = createProperty(child, factory);
                wrapper.addProperty(property);
            }
            catch(RuntimeException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void addReferencesToWrapper(PropertyWrapper wrapper, EClass eClass, IPropertyFactory factory)
    {
        EList<EReference> refs = eClass.getEAllReferences();

        for(EReference ref : refs)
        {
            if(ref.isContainment() || ref.isContainer()) continue;

            try
            {
                IChoiceProperty<NodeProxy> property = createProperty(ref, factory);
                wrapper.addProperty(property);
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static IProperty createProperty(EAttribute attribute, IPropertyFactory factory)
    {
        IProperty property;
        EDataType type = attribute.getEAttributeType();

        if(type instanceof EEnum)
        {
            //Get all the possible value for the enum
            EList<EEnumLiteral> literals = ((EEnum) type).getELiterals();
            ArrayList<String> choices = new ArrayList<>(literals.size());
            for(EEnumLiteral literal : literals)
            {
                choices.add(literal.getLiteral());
            }

            //Create the property
            if(attribute.isMany())
            {
                property = factory.createChoiceProperty(attribute.getName(), choices, choices);         //Use a default value
            }
            else
            {
                property = factory.createChoiceProperty(attribute.getName(), choices, choices.get(0));  //Use a default value
            }
        }
        else
        {
            Object templateValue = null;

            //Get a template value
            switch (type.getName())
            {
                case "EBoolean":
                case "EBooleanObject":
                    templateValue = Boolean.FALSE;
                    break;

                case "EChar":
                case "ECharacterObject":
                    templateValue = ' ';
                    break;

                case "EDouble":
                case "EDoubleObject":
                    templateValue = 0.0;
                    break;

                case "EFloat":
                case "EFloatObject":
                    templateValue = 0f;
                    break;

                case "EInt":
                case "EIntegerObject":
                    templateValue = 0;
                    break;

                case "ELong":
                case "ELongObject":
                    templateValue = 0L;
                    break;

                case "EShort":
                case "EShortObject":
                    templateValue = (short) 0;
                    break;

                case "EString":
                    templateValue = " ";
                    break;

                case "EBigDecimal":
                    templateValue = new BigDecimal(0);
                    break;

                case "EBigInteger":
                    templateValue = new BigInteger("0");
                    break;

                default:
                    throw new RuntimeException("Unsupported type " + type.getName());
            }

            //Create the property
            if(attribute.isMany())
            {
                property = factory.createListProperty(attribute.getName(), templateValue);
            }
            else
            {
                property = factory.createValueProperty(attribute.getName(), templateValue);
            }
        }

        return property;
    }

    private static IChoiceProperty<NodeProxy> createProperty(EReference reference, IPropertyFactory factory)
    {
        EClass type = reference.getEReferenceType();

        IAssociationProperty property;
        if(reference.isMany())
        {
            property = factory.createAssociationListProperty(reference.getName(), type.getName());
        }
        else
        {
            property = factory.createAssociationProperty(reference.getName(), type.getName());
        }

        return property;
    }

    //endregion

}
