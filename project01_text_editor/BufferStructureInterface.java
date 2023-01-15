
/**
 * Write a description of interface BufferStructureInterface here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public interface BufferStructureInterface
{
    /**
     * An example of a method header - replace this comment with your own
     *
     * @param  y a sample parameter for a method
     * @return   the result produced by sampleMethod
     */
    
    // Creates a new buffer and places relative to current buffers
    public void load_line_at_start (String str_value);
    public void load_line_at_end (String str_value);
    // Creates a new buffer and places at the identified position,
    // shifting the buffers currently at that position and below down.
    public void load_line_at_position (String str_value, int position);
    public boolean insert_empty_line_above();
    public boolean insert_empty_line_below();
    // Returns identified information.
    public int line_count();
    public int curr_line_length();
    // Returns the line where the cursor is positioned and where
    // in the current line it is positioned.
    public int cursor_line_position();
    public int cursor_position_in_line();
    // The cursor movement methods.
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
    // Will remove current line and characters in current line.
    public boolean remove_line();
    public boolean remove_char_toleft();
    public boolean remove_char_toleft(int char_count);
    // Will add a character or string to current line.
    public boolean insert_text(String str_value);
    public boolean insert_text(char char_value);
}
