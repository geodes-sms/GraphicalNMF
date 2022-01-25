package geodes.sms.gnmf.compiler;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CodeCompiler
{
    private String inputDir;

    private final HashSet<String> toCompileJava = new HashSet<>();
    private final HashSet<String> toCompileKotlin = new HashSet<>();

    public CodeCompiler(String inputDir) throws CompilerException
    {
        this.inputDir = inputDir;
        readClasses(inputDir);
    }

    private void readClasses(String inputDir) throws CompilerException
    {
        try
        {
            File apiFolder = new File(inputDir);
            Files.walkFileTree(apiFolder.toPath(), new FileVisitor<>()
            {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
                {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    if (file.toString().endsWith(".java"))
                    {
                        toCompileJava.add(file.toAbsolutePath().toString());
                    }
                    else if (file.toString().endsWith(".kt"))
                    {
                        toCompileKotlin.add(file.toAbsolutePath().toString());
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
                {
                    throw exc;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
                {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch(IOException e)
        {
            throw new CompilerException("Can't read file to compile", e);
        }
    }

    public void compile(String outputDir) throws CompilerException
    {
        Path argfileKt;
        Path argfileJv;
        try
        {
            argfileKt = writeArgumentFile("argfileKt.txt", toCompileKotlin);
            argfileJv = writeArgumentFile("argfileJv.txt", toCompileJava);
        }
        catch(IOException e)
        {
            throw new CompilerException("Can't create argument files", e);
        }

        String classpath = System.getProperty("java.class.path");       //Find the classpath

        //Create the command
        String commandKt = "kotlinc.bat @" + argfileKt + " -classpath \"" + classpath
                + "\" -d \"" + outputDir + "\"";
        String commandJv = "javac @" + argfileJv + " -classpath \"" + outputDir + ";" + classpath
                + "\" -d \"" + outputDir + "\"";

        //Compile Kotlin classes
        try
        {
            int result = runProcess(commandKt);

            if(result != 0)
            {
                throw new Exception("Exit value " + result);
            }
        }
        catch(Exception e)
        {
            throw new CompilerException("Can't compile kotlin classes", e);
        }
//        finally
//        {
//            //Delete the argument file -> optional
//            try
//            {
//                Files.deleteIfExists(argfileKt);
//            }
//            catch(IOException e)
//            {
//                System.err.println("Can't delete argument file");
//                e.printStackTrace();
//            }
//        }

        //Compile Java classes
        try
        {
            int result = runProcess(commandJv);

            if(result != 0)
            {
                throw new Exception("Exit value " + result);
            }
        }
        catch(Exception e)
        {
            throw new CompilerException("Can't compile java classes", e);
        }
//        finally
//        {
//            //Delete the argument file -> optional
//            try
//            {
//                Files.deleteIfExists(argfileJv);
//            }
//            catch(IOException e)
//            {
//                System.err.println("Can't delete argument file");
//                e.printStackTrace();
//            }
//        }
    }

    public String getInputDir()
    {
        return this.inputDir;
    }

    public void setInputDir(String inputDir) throws CompilerException
    {
        this.inputDir = inputDir;

        toCompileJava.clear();
        toCompileKotlin.clear();
        readClasses(inputDir);
    }

    public void addClassesFromDir(String dir) throws CompilerException
    {
        readClasses(dir);
    }

    private static int runProcess(String command) throws Exception
    {
        Process pro = Runtime.getRuntime().exec(command);

        String commandName = command.split(" ")[0];

        printLines(commandName + " stdout:", pro.getInputStream());
        printLines(commandName + " stderr:", pro.getErrorStream());
        pro.waitFor();

        System.out.println(commandName + " exitValue(): " + pro.exitValue());
        return pro.exitValue();
    }

    private static void printLines(String cmd, InputStream ins) throws Exception
    {
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));

        while ((line = in.readLine()) != null)
        {
            System.out.println(cmd + " " + line);
        }
    }

    private static Path writeArgumentFile(String name, Collection<String> classes) throws IOException
    {
        Path file = Paths.get(name);
        FileWriter writer = new FileWriter(file.toFile());

        try
        {
            for (String c : classes)
            {
                writer.write(c);
                writer.write("\n");
            }

            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            writer.flush();
            writer.close();

            Files.deleteIfExists(file);
            throw e;
        }

        return file;
    }

    public static List<String> classExtractor(String folderPath) throws IOException
    {
        ArrayList<String> classes = new ArrayList<>();

        File apiFolder = new File(folderPath);
        Files.walkFileTree(apiFolder.toPath(), new FileVisitor<>()
        {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
            {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                classes.add(file.toAbsolutePath().toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
            {
                throw exc;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
                return FileVisitResult.CONTINUE;
            }
        });

        return classes;
    }
}
