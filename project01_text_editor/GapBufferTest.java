

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * The test class GapBufferTest.
 *
 * @author  Alexander Labell
 * @version 1.0
 */
public class GapBufferTest
{
    /**
     * Default constructor for test class GapBufferTest
     */
    public GapBufferTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
    
    @Test
    @DisplayName("test putting a string into GapBuffer and getting it out properly")
    public void string_in_string_out_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("I am a loaded string"); //put a string into file
        String t_s = gb.toString();
        assertTrue(t_s.equals("I am a loaded string"));
        int l = gb.length();
        assertTrue(l == 20);
    }
    
    @Test
    @DisplayName("test getting the cursor pos")
    public void cursor_pos_init_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        int cp = gb.cursor_position();
        assertTrue(cp == 0);
        gb.cursor_right();
        cp = gb.cursor_position();
        assertTrue(cp == 1);
        gb.cursor_right(2);
        cp = gb.cursor_position();
        assertTrue(cp == 3);
    }
    
    @Test
    @DisplayName("test formatting string in array")
    public void format_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right(4); //move cursor to position 4
        assertTrue(gb.cursor_position() == 4);
        gb.cursor_right(1); //cursor is now at position 5
        assertTrue(gb.cursor_position() == 5);
        gb.format();
    }
    
    @Test
    @DisplayName("test moving cursor right and the string should reformat")
    public void cursor_right_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right();
        gb.cursor_right(2);
        assertTrue(gb.cursor_position() == 3);
    }
    
    @Test
    @DisplayName("test moving cursor left out of bounds")
    public void cursor_left_out_of_bounds_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right(5); //move cursor to position 5
        gb.cursor_right(); //move cursor one more, should be at position 6
        //test moving larger amounts
        gb.cursor_right(3);
        assertTrue(gb.cursor_position() == 9);
        boolean t_f = gb.cursor_left(20);
        assertFalse(t_f);
        gb.cursor_left(9);
        assertEquals(gb.cursor_position(), 0, "fail");
        assertFalse(gb.cursor_left());
    }
    
    @Test
    @DisplayName("test moving cursor right out of bounds")
    public void cursor_right_out_of_bounds_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        boolean result = gb.cursor_right(24);
        assertFalse(result);
    }
    
    @Test
    @DisplayName("remove the character to the left of cursor")
    public void remove_char_toleft_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right(5); //move cursor to position 5
        gb.cursor_right(); //move cursor one more, should be at position 6
        //assertTrue(gb.charArray[5] == 's');
        gb.remove_char_toleft();
        //assertTrue(gb.str.equals("this tring")); //sucessfully altered string
        //assertTrue(gb.cursor_position() == 5);
    }
    
    @Test
    @DisplayName("inserting text")
    public void insert_text_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right(5); //move cursor to position 5
        gb.cursor_right(3); //move cursor one more, should be at position 8
        assertTrue(gb.cursor_position() == 8); //this string
        gb.insert_text("hel");
        String str = gb.toString();
        assertTrue(str.equals("this strheling"));
    }
    
    @Test
    @DisplayName("insert too much text, should stop adding text once array limit is reached")
    public void insert_text_beyond_array_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right(4); //move cursor to position 4
        boolean f = gb.insert_text("I am a very long string, but I will still fit");
        assertTrue(f);
        boolean result = gb.insert_text("too_long_llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
        assertFalse(result); //passed
    }
    
    @Test
    @DisplayName("observe empty space when adding strings")
    public void empty_space_tb() {
        GapBuffer gb = new GapBuffer(); //creates a character array and initializes a gap_buffer
        gb.load_string("this string"); //put a string into file
        //cursor position should be zero
        gb.cursor_right(4); //move cursor to position 4
        gb.insert_text("he");
        String str = gb.toString();
        assertTrue(str.equals("thishe string"));
    }
}
