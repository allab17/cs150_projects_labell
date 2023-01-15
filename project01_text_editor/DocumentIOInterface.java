
/**
 * Write a description of interface DocumentIOInterface here.
 *
 * @author Alexander Labell
 * @version 1.0
 */

import java.io.*;

public interface DocumentIOInterface
{
    /**
     * An example of a method header - replace this comment with your own
     *
     * @param  y a sample parameter for a method
     * @return   the result produced by sampleMethod
     */
    
    public void open_file(String file_name) throws IOException;
    public void close_file() throws IOException;
    public String read_line() throws IOException;
    public boolean has_more_lines() throws IOException;
}
