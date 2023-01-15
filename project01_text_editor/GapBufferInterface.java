    
/**
 * Write a description of interface GapBufferInterface here.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public interface GapBufferInterface
{
    /**
     * An example of a method header - replace this comment with your own
     *
     * @param  y a sample parameter for a method
     * @return   the result produced by sampleMethod
     */
        
    // Loads a string into the class, removing a previously stored
    // string.
    public void load_string(String str_value);
    // Returns the stored string without the empty space.
    public String toString();
    // Returns length of the string without the empty space.
    public int length();
    // Returns the current cursor postion at the start of the empty space.
    public int cursor_position();
    // The cursor movement methods.
    public boolean cursor_left();
    public boolean cursor_left(int char_count);
    public boolean cursor_right();
    public boolean cursor_right(int char_count);
    public boolean cursor_move_start_line();
    public boolean cursor_move_end_line();
    // Will remove one or a variable number of chars from the string.
    public boolean remove_char_toleft();
    public boolean remove_char_toleft(int char_count);
    // Will add a character or string to the empty space.
    public boolean insert_text(String str_value);
    public boolean insert_text(char char_value);
    public void set_cursor_position(int cp);
    public void format();
        
}
