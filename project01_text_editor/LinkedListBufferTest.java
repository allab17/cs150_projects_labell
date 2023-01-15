

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class LinkedListBufferTest.
 *
 * @author  Alexander Labell
 * @version 1.0
 */
public class LinkedListBufferTest
{
    /**
     * Default constructor for test class LinkedListBufferTest
     */
    public LinkedListBufferTest()
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
    public void load_string_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0
        assertEquals(llb.getString().charAt(0), '^', "fail");
        assertEquals(llb.getString().charAt(1), 't', "fail");
    }
    
    @Test
    public void cursor_left_right_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0
        assertTrue(llb.cursor_position() == 0);
        assertFalse(llb.cursor_left()); //this should accomplist nothing because the cursor pos is at 0
        llb.cursor_right(); //[1]
        assertEquals(llb.cursor_position(), 1, "fail");
        llb.cursor_right(); //[2]
        llb.cursor_left(); //the cursor now should be at index 1
        assertTrue(llb.cursor_position() == 1);
        assertEquals(llb.getString().charAt(1), '^', "fail");
        llb.cursor_right(5); //[6]
        assertTrue(llb.cursor_position() == 6);
        assertEquals(llb.getString().charAt(6), '^', "fail");
        llb.cursor_left(2); //[4]
        llb.cursor_right(); //[5]
        llb.cursor_left(4); //[1]
        assertTrue(llb.cursor_position() == 1);
        assertEquals(llb.getString().charAt(1), '^', "fail");
        assertFalse(llb.cursor_right(15));
    }
    
    @Test
    public void cursor_start_end_line_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0
        llb.cursor_right(4); //[4]
        assertEquals(llb.getString().charAt(4), '^');
        assertEquals(llb.getString().charAt(0), 't');
        llb.cursor_move_start_line(); //move the cursor to the first line
        assertEquals(llb.getString().charAt(0), '^');
        assertEquals(llb.getString().charAt(1), 't');
        assertEquals(llb.getString().charAt(11), 'g');
        llb.cursor_move_end_line(); //move the cursor to the last line, line 10, recall that the string is length 11 but the cursor_pos increases the length by 1 therefore the LinkedList is 12 nodes, therefore the last node is really 11
        assertEquals(llb.getString().charAt((llb.getString()).length()-1), '^', "fail"); 
        assertEquals(llb.getString().charAt((llb.getString()).length()-2), 'g', "fail");
    }
    
    @Test
    public void remove_char_toleft_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0   //^this string
        llb.cursor_right(4); //this^ string
        llb.remove_char_toleft(); //thi^ string
        assertEquals(llb.getString().charAt(3), '^');
        assertEquals(llb.getString().charAt(2), 'i');
        assertEquals(llb.getString().charAt(4), ' ');
        llb.cursor_move_start_line(); //^thi string
        assertFalse(llb.remove_char_toleft()); 
        llb.cursor_right(); //t^hi string
        assertEquals(llb.toString(), "thi string", "fail"); //test the string was manipulated properly
        llb.remove_char_toleft(); //^hi string
        assertEquals(llb.getString().charAt(0), '^', "fail");
        assertEquals(llb.getString().charAt(1), 'h', "fail");
        assertEquals(llb.getString().charAt(2), 'i', "fail");
        assertEquals(llb.toString(), "hi string", "fail"); //test the string was manipulated properly
    }
    
    @Test
    public void remove_char_count_toleft_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0   //^this string
        llb.cursor_right(4); //this^ string
        assertTrue(llb.cursor_position() == 4);
        llb.remove_char_toleft(2); //th^ string
        assertTrue(llb.cursor_position() == 2);
        assertEquals(llb.getString().charAt(1), 'h', "fail");
        assertEquals(llb.getString().charAt(0), 't', "fail");
        assertEquals(llb.getString().charAt(2), '^', "fail");
        assertEquals(llb.toString(), "th string", "fail"); //test the string was manipulated properly
    }
    
    @Test
    public void insert_text_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0   //^this string
        llb.insert_text("txt"); //txt^this string
        assertEquals(llb.getString().charAt(0), 't', "fail");
        assertEquals(llb.getString().charAt(1), 'x', "fail");
        assertEquals(llb.getString().charAt(2), 't', "fail");
        assertEquals(llb.getString().charAt(3), '^', "fail");
        assertEquals(llb.getString().charAt(4), 't', "fail");
        assertTrue(llb.cursor_position() == 3);
        assertEquals(llb.toString(), "txtthis string", "fail"); //test the string was manipulated properly
        llb.insert_text("very very long string again long");
        assertEquals(llb.toString(), "txtvery very long string again longthis string", "fail");
        assertTrue(llb.cursor_position() == 35);
    }
    
    @Test
    public void insert_char_tb() {
        LinkedListBuffer llb = new LinkedListBuffer();
        llb.load_string("this string");
        //the cursor_pos is initially 0   //^this string
        llb.cursor_right(4); //this^ string
        llb.insert_text('c'); //thisc^ string
        assertEquals(llb.getString().charAt(3), 's', "fail");
        assertEquals(llb.getString().charAt(4), 'c', "fail");
        assertEquals(llb.getString().charAt(5), '^', "fail");
        llb.insert_text(' ');
        llb.insert_text('d');
        llb.insert_text('o');
        llb.insert_text('u');
        llb.insert_text('b');
        llb.insert_text('l');
        llb.insert_text('e');
        llb.insert_text(' ');
        llb.insert_text('d');
        llb.insert_text('a');
        llb.insert_text('b');
        llb.insert_text('b');
        llb.insert_text('l');
        llb.insert_text('e'); //thisc double dabble^ string
        assertEquals(llb.getString().charAt(5), ' ', "fail");
        assertEquals(llb.getString().charAt(6), 'd', "fail");
        assertEquals(llb.getString().charAt(9), 'b', "fail");
        assertEquals(llb.getString().charAt(13), 'd', "fail");
        assertEquals(llb.getString().charAt(15), 'b', "fail");
        assertEquals(llb.getString().charAt(19), '^', "fail");
        assertTrue(llb.cursor_position() == 19);
        assertEquals(llb.toString(), "thisc double dabble string");
    }
}
