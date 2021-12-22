package generator;

import geodes.sms.nmf.codegen.core.Context;
import geodes.sms.nmf.codegen.template.nmf.NMFGenerator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CodeGenerator
{
    private final String metamodel;
    private final EPackage pkg;

    public CodeGenerator(String metamodel) throws GeneratorException
    {
        this.metamodel = metamodel;
        this.pkg = readMetamodel(metamodel);
    }

    public CodeGenerator(EPackage pkg)
    {
        this.metamodel = pkg.eResource().getURI().toFileString();
        this.pkg = pkg;
    }

    public String generate(String output) throws GeneratorException
    {
        generateAPI(pkg, output);
        return generateAdapters(pkg, output);
    }

    public String getMetamodel()
    {
        return metamodel;
    }

    public EPackage getEPackage()
    {
        return pkg;
    }

    /**
     * Generate the API for a metamodel. The API is written
     * in the given output directory.
     *
     * @param pkg The EPackage of the metamodel
     * @param outputDir The output directory of the API
     */
    private static void generateAPI(EPackage pkg, String outputDir)
    {
        String packagePath = EcoreWriterFactory.PKG_ROOT;                            //fixme centralize package name
        new NMFGenerator(new Context(pkg, packagePath, outputDir)).generate();
    }

    /**
     * Generate the adapters for the EPackage and write it in the output directory. </br>
     * Also return the name of the factory class.
     * </p>
     * If the generation fails, all correctly generated classes will be deleted.
     *
     * @return The name of the factory class
     * @throws GeneratorException If an adapter can't be written
     */
    private static String generateAdapters(EPackage pkg, String outputDir) throws GeneratorException
    {
        String factoryName = null;
        ArrayList<String> generatedClasses = new ArrayList<>();

        try
        {
            //Generate the model adapter
            String className = EcoreWriterFactory.createModelAdapterWriter(pkg).write(outputDir);
            generatedClasses.add(className);

            //Generate the node adapters
            for (EClassifier classifier : pkg.getEClassifiers())
            {
                if (classifier instanceof EClass
                        && !((EClass) classifier).isInterface()
                        && !((EClass) classifier).isAbstract())
                {
                    className = EcoreWriterFactory.createNodeAdapterWriter((EClass) classifier).write(outputDir);
                    generatedClasses.add(className);
                }
            }

            //Generate the factory
            AdapterFactoryWriter factoryWriter = EcoreWriterFactory.createAdapterFactoryWriter(pkg);
            factoryWriter.write(outputDir);

            factoryName = factoryWriter.getPackage() + "." + factoryWriter.getClassName();
        }
        catch(GeneratorException e)
        {
            for(String generatedClass : generatedClasses)
            {
                try
                {
                    Path file = Paths.get(generatedClass);
                    Files.delete(file);
                }
                catch(IOException ioe)      //shouldn't happen
                {
                    ioe.printStackTrace();
                }
            }

            throw e;
        }

        return factoryName;
    }

    /**
     * Read a metamodel .ecore file to produce an EPackage.
     *
     * @param metamodel The path to the .ecore file
     * @return An EPackage
     * @throws GeneratorException If the metamodel can't be found
     */
    public static EPackage readMetamodel(String metamodel) throws GeneratorException
    {
        //Load the metamodel
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(URI.createFileURI(metamodel));
        try
        {

            resource.load(null);
        }
        catch(IOException e)
        {
            throw new GeneratorException("Cannot load " + metamodel);
        }

        //Return the package
        EList<EObject> contents = resource.getContents();
        EObject root = contents.get(0);

        if(root instanceof EPackage)
        {
            return (EPackage) root;
        }
        else
        {
            throw new GeneratorException("Metamodel doesn't exist");
        }
    }
}
