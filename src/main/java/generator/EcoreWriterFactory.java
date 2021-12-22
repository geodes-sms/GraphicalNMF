package generator;

import org.eclipse.emf.ecore.*;

/**
 * This class is the connection between the {@link AdapterWriter} and
 * the {@link CodeGenerator} (or any classes wishing to create nmf adapter template).
 * </br>
 * This class could be improve by generalizing the way it reads the {@link EPackage}.
 */

public final class EcoreWriterFactory
{
    public static final String PKG_ROOT = "graphicalnmf";
    public static final String MODEL_MANAGER_REL_PKG = ".neo4jImpl.ModelManagerImpl";

    public static AdapterFactoryWriter createAdapterFactoryWriter(EPackage pkg)
    {
        String name = capitalize(pkg.getName());
        AdapterFactoryWriter writer = new AdapterFactoryWriter(name);

        //Import the modelManagerImpl
        String modelManagerImplImport = getPackagePath(pkg);
        modelManagerImplImport += MODEL_MANAGER_REL_PKG;
        writer.addImport(modelManagerImplImport);

        //Import API
//        String apiImport = getPackagePath(pkg);
//        apiImport += ".*";
//        writer.addImport(apiImport);

        //Set the package
        writer.setPackage(getPackagePath(pkg));

        //Set the classes
        for(EClassifier classifier : pkg.getEClassifiers())
        {
            if(classifier instanceof EClass
                    && !((EClass) classifier).isInterface()
                    && !((EClass) classifier).isAbstract())
            {
                writer.addClass(classifier.getName());
            }
        }

        return writer;
    }

    public static ModelAdapterWriter createModelAdapterWriter(EPackage pkg)
    {
        String name = capitalize(pkg.getName());
        ModelAdapterWriter writer = new ModelAdapterWriter(name);

        //Import modelManagerImpl
        String modelManagerImplImport = getPackagePath(pkg);
        modelManagerImplImport += MODEL_MANAGER_REL_PKG;
        writer.addImport(modelManagerImplImport);

        //Import API
//        String apiImport = getPackagePath(pkg);
//        apiImport += ".*";
//        writer.addImport(apiImport);

        //Set the package
        writer.setPackage(getPackagePath(pkg));

        //Set the classes
        for(EClassifier classifier : pkg.getEClassifiers())
        {
            if(classifier instanceof EClass
                    && !((EClass) classifier).isInterface()
                    && !((EClass) classifier).isAbstract())
            {
                writer.addClass(classifier.getName());
            }
        }

        return writer;
    }

    public static NodeAdapterWriter createNodeAdapterWriter(EClass eClass)
    {
        NodeAdapterWriter writer = new NodeAdapterWriter(eClass.getName());

        //Import API
//        String apiImport = getPackagePath(eClass.getEPackage());
//        apiImport += ".*";
//        writer.addImport(apiImport);

        //Set the package
        writer.setPackage(getPackagePath(eClass.getEPackage()));

        //Set the attributs
        for(EAttribute attr : eClass.getEAllAttributes())
        {
            String type = getAttributeType(attr);
            if(attr.isMany())
            {
                type = "List<" + type + ">";
            }

            if(attr.getEAttributeType() instanceof EEnum)
            {
                writer.addEnum(attr.getName(), type);
            }
            else
            {
                writer.addAttribute(attr.getName(), type);
            }
        }

        //Set the references and children
        for(EReference ref : eClass.getEAllReferences())
        {
            //Child
            if(ref.isContainment())
            {
                if(hasSubType((EClass) ref.getEType()))     //Will use an type argument to create children
                {
                    String enumTypeName = getEnumTypeName((EClass) ref.getEType());

                    writer.addChildren(ref.getName(), ref.getEType().getName(), enumTypeName, ref.isMany());
                }
                else                                        //Won't use a type argument
                {
                    writer.addChildren(ref.getName(), ref.getEType().getName(), null, ref.isMany());
                }
            }
            //Reference
            else
            {
                writer.addReference(ref.getName(), ref.getEType().getName(), ref.isMany());
            }
        }

        //Set the id
        if(eClass.getEIDAttribute() != null)
        {
            writer.setId(eClass.getEIDAttribute().getName());
        }

        return writer;
    }


    public static String getAttributeType(EAttribute attr)
    {
        String type;

        if(attr.getEAttributeType() instanceof EEnum)
        {
            type = attr.getEAttributeType().getName();
        }
        else
        {
            type = attr.getEAttributeType().getInstanceTypeName();      //fixme needs better implementation
        }

        return type;
    }

    public static String getPackagePath(EPackage pkg)
    {
        String name = decapitalize(pkg.getName());
        return PKG_ROOT + "." + name;
    }

    public static boolean hasSubType(EClass eClass)
    {
        for(EClassifier classifier : eClass.getEPackage().getEClassifiers())
        {
            if(classifier instanceof EClass && classifier != eClass
                    && !((EClass) classifier).isAbstract() && eClass.isSuperTypeOf((EClass) classifier))
            {
                return true;
            }
        }

        return false;
    }

    public static String getEnumTypeName(EClass eClass)
    {
        return eClass.getName() + "Type";
    }

    public static String decapitalize(String str)
    {
        return str.toLowerCase().charAt(0) + str.substring(1);
    }

    public static String capitalize(String str)
    {
        return str.toUpperCase().charAt(0) + str.substring(1);
    }
}
