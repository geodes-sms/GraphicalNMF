package generator;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AdapterWriter implements ISTWriter
{
    public static final String RESSOURCE_PATH = "./src/main/resources/";      //fixme

    protected static final String ATTR_IMPORTS = "imports";
    protected static final String ATTR_PACKAGE = "package";

    protected ST template;

    private String packageName;
    private HashSet<String> imports = new HashSet<>();

    protected AdapterWriter() { }

    @Override
    public String write(String outputDir) throws GeneratorException
    {
        String filePath;

        setupTemplate();

        String[] parents = getPackage().split("\\.");
        String fileName = parents[parents.length-1] + "\\" + getClassName() + ".java";

        //Write the template to the file
        try
        {
            File file = openFile(outputDir, fileName);
            FileWriter writer = new FileWriter(file);

            try
            {
                writer.write(template.render());
                filePath = file.getAbsolutePath();
            }
            finally
            {
                writer.flush();
                writer.close();
            }
        }
        catch (IOException e)
        {
            throw new GeneratorException("Can't write " + fileName, e);
        }

        return filePath;
    }

    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    public String getPackage()
    {
        return packageName;
    }

    public void addImport(String imp)
    {
        imports.add(imp);
    }

    public void removeImport(String imp)
    {
        imports.remove(imp);
    }

    public List<String> getImports()
    {
        return new ArrayList<>(imports);
    }

    public abstract String getClassName();

    /**
     * Setup all the attributes of the template before writing. </br>
     * Extending classes should override this method.
     */
    protected void setupTemplate()
    {
        template.add(ATTR_PACKAGE, this.packageName);
        for(String imp : imports)
        {
            template.add(ATTR_IMPORTS, imp);
        }
    }

    /**
     * Create a template with the given name, located in the given file.
     *
     * @param name The name of the template
     * @param file The file in which the template is located
     * @return A String Template
     */
    protected static ST createTemplate(String name, String file)
    {
        STGroup factoryGroup = new STGroupFile(RESSOURCE_PATH + file);
        factoryGroup.registerRenderer(String.class, new ModelAttributeRenderer());

        return factoryGroup.getInstanceOf(name);
    }

    /**
     * Open and return a file, creating it if it doesn't exist.
     *
     * @param folder The path of the folder in which to search (or create) the file
     * @param fileName The name of the file
     * @return A file
     * @throws IOException If the path is invalid or the file can't be created
     */
    protected static File openFile(String folder, String fileName) throws IOException
    {
        Path path = Paths.get(folder, fileName);
        if(!Files.exists(path))
        {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }

        return path.toFile();
    }
}
