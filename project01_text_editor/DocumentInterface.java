
import java.io.*;
/**
 * Write a description of interface DocumentInterface here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public interface DocumentInterface
{
    /**
     * An example of a method header - replace this comment with your own
     *
     * @param  y a sample parameter for a method
     * @return   the result produced by sampleMethod
     */

    // Load and store from/to an identified file.
    public boolean load_file (String filename) throws IOException;
    
    
    
    public boolean store_file(String filename) throws IOException;
    // Store to the current file.
    public boolean store_file() throws IOException;
    public String current_file_name();
    // Return information currently stored in the data structure
    // as a string.
    public String toStringDocument();
    public String toStringLine(int line_index);
    // Length of a certain line.
    public int line_length(int line_index);
    // Length of line pointed to by cursor.
    public int line_length();
    // Current line count.
    public int line_count();
    // See other interfaces for the following functionality.
    public boolean cursor_left();
    public boolean cursor_left(int char_count);
    public boolean cursor_right();
    public boolean cursor_right(int char_count);
    public boolean cursor_up();
    public boolean cursor_up(int line_count);
    public boolean cursor_down();
    public boolean cursor_down(int line_count);
    public boolean cursor_move_first_line();
    public boolean cursor_move_last_line();
    public boolean cursor_move_start_line();
    public boolean cursor_move_end_line();
    public boolean insert_line_above();
    public boolean insert_line_below();
    public boolean remove_line();
    public boolean remove_char_toleft();
    public boolean remove_char_toleft(int char_count);
    public boolean insert_text(String str_value);
    public boolean insert_text(char char_value);
}
