import java.io.*;
import java.util.Scanner;

/**
 * The DocumentIO class will be used to handle some of the file input/output functionality such as opening, closing, reading and writing
 * to divide some of the functionality between Document and DocumentIO and create a better sense of modularity within the program as a whole.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class DocumentIO implements DocumentIOInterface
{
    private String filename;
    private Scanner scnr_read; //scanner used for reading file
    private FileInputStream fis;
    
    public DocumentIO(String f)
    {
        filename = f;
        scnr_read = new Scanner(filename);
        fis = null;
    }
    
    /**
     * The open_file method will prime a file for reading.
     *
     * @param  file_name
     * @return void
     */
    @Override
    public void open_file(String file_name) throws IOException {
        fis = new FileInputStream(file_name);
        scnr_read = new Scanner(fis);
    }
    
    /**
     * The close_file method will close the scanner and FileInputStream.
     *
     * @return void
     */
    @Override
    public void close_file() throws IOException {
        fis.close();
        scnr_read.close(); //close the Scanner object as well
    }
    
    /**
     * The read_line method will read a single line using the scaner object and return the line.
     *
     * @return void
     */
    @Override
    public String read_line() throws IOException {
        String line = scnr_read.nextLine(); //read a line
        return line;
    }
    
     /**
     * The has_more_lines method will see if the file has more lines to be read.
     *
     * @return boolean
     */
    @Override
    public boolean has_more_lines() throws IOException {
        if (scnr_read.hasNextLine()) { //if the scanner has more lines
            return true;
        }
        return false;
    }
}
