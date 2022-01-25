package geodes.sms.gnmf.generator.writers;

import java.net.URISyntaxException;

public interface ISTWriter
{
    /**
     * Write this template in the given directory, and return
     * the absolute path of the file that was written.
     *
     * @param outputDir The directory in which to write
     * @return The absolute path of the written file
     * @throws GeneratorException If the file can't be written
     */
    String write(String outputDir) throws GeneratorException, URISyntaxException;
}
