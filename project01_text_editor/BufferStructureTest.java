

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * The test class BufferStructureTest.
 *
 * @author  Alexander Labell
 * @version 1.0
 */
public class BufferStructureTest
{
    /**
     * Default constructor for test class BufferStructureTest
     */
    public BufferStructureTest()
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
    @DisplayName("Load a line and test characteristics of the loaded line")
    public void load_line_at_start_tb() {
        BufferStructure bs = new BufferStructure("gap");
        //load consecutive lines into the buffer structure, "new string at pos 1 should be at the first index"
        bs.load_line_at_start("a new line loaded at the start"); 
        bs.load_line_at_start("this string");
        bs.load_line_at_start("new string");
        bs.load_line_at_start("pos 1");
        bs.load_line_at_start("new string at pos 1");
        String str = bs.gap_buffer_toString(0); 
        String str_1 = bs.gap_buffer_toString(1);
        String str_2 = bs.gap_buffer_toString(2);
        String str_3 = bs.gap_buffer_toString(3);
        String str_4 = bs.gap_buffer_toString(4);
        assertEquals("new string at pos 1", str, "fail");
        assertEquals("pos 1", str_1, "fail");
        assertEquals("new string", str_2, "fail");
        assertEquals("this string", str_3, "fail");
        assertEquals("a new line loaded at the start", str_4, "fail");
    }
    
    @Test
    @DisplayName("Load a line and test characteristics of the loaded line")
    public void linked_load_line_at_start_tb() {
        BufferStructure bs = new BufferStructure("linked");
        //load consecutive lines into the buffer structure, "new string at pos 1 should be at the first index"
        bs.load_line_at_start("a new line loaded at the start"); 
        bs.load_line_at_start("this string");
        bs.load_line_at_start("new string");
        bs.load_line_at_start("pos 1");
        bs.load_line_at_start("new string at pos 1");
        String str = bs.gap_buffer_toString(0); 
        String str_1 = bs.gap_buffer_toString(1);
        String str_2 = bs.gap_buffer_toString(2);
        String str_3 = bs.gap_buffer_toString(3);
        String str_4 = bs.gap_buffer_toString(4);
        assertEquals("new string at pos 1", str, "fail");
        assertEquals("pos 1", str_1, "fail");
        assertEquals("new string", str_2, "fail");
        assertEquals("this string", str_3, "fail");
        assertEquals("a new line loaded at the start", str_4, "fail");
    }
    
    @Test
    @DisplayName("Load a line and test characteristics of the loaded line")
    public void load_line_at_end_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string");
        bs.load_line_at_start("another string");
        bs.load_line_at_start("I should be in the first pos");
        bs.load_line_at_end("this string at end");        
        assertTrue(bs.line_count() == 0); //ensure cursor is on the first line
        bs.cursor_down(3); //move cursor down three
        assertTrue(bs.cursor_line_position() == 3);
        assertTrue(bs.get_buffer_structure_size() == 4);
        bs.load_line_at_end("another string at the end");
        String str = bs.gap_buffer_toString(0); 
        String str_end = bs.gap_buffer_toString(4);
        String str_second_end = bs.gap_buffer_toString(3);
        String str_0 = bs.gap_buffer_toString(0);
        String str_1 = bs.gap_buffer_toString(1);
        String str_2 = bs.gap_buffer_toString(2);
        assertEquals("I should be in the first pos", str_0, "fail");
        assertEquals("another string", str_1, "fail");
        assertEquals("this string", str_2, "fail");
        assertEquals("this string at end", str_second_end, "fail");
        assertEquals("another string at the end", str_end, "fail");
    }
    
    @Test
    @DisplayName("Load a line and test characteristics of the loaded line")
    public void linked_load_line_at_end_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string");
        bs.load_line_at_start("another string");
        bs.load_line_at_start("I should be in the first pos");
        bs.load_line_at_end("this string at end");        
        assertTrue(bs.line_count() == 0); //ensure cursor is on the first line
        bs.cursor_down(3); //move cursor down three
        assertTrue(bs.cursor_line_position() == 3);
        assertTrue(bs.get_buffer_structure_size() == 4);
        bs.load_line_at_end("another string at the end");
        String str = bs.gap_buffer_toString(0); 
        String str_end = bs.gap_buffer_toString(4);
        String str_second_end = bs.gap_buffer_toString(3);
        String str_0 = bs.gap_buffer_toString(0);
        String str_1 = bs.gap_buffer_toString(1);
        String str_2 = bs.gap_buffer_toString(2);
        assertEquals("I should be in the first pos", str_0, "fail");
        assertEquals("another string", str_1, "fail");
        assertEquals("this string", str_2, "fail");
        assertEquals("this string at end", str_second_end, "fail");
        assertEquals("another string at the end", str_end, "fail");
    }
    
    @Test
    @DisplayName("Load a line into a specific position test bench")
    public void load_line_at_position_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); 
        bs.load_line_at_position("another loaded line", 15);
        bs.load_line_at_position("I am the loaded line at a specific position", 5);
        bs.load_line_at_position("a string that will be loaded out of bounds", 50);
        bs.load_line_at_position("another string loaded out of bounds", -1);
        bs.load_line_at_position("final string", 19);
        bs.load_line_at_position("I should fail", 20);
        bs.load_line_at_position("at pos 0", 0);
        bs.load_line_at_position("another string loaded", 3);
        String str_1 = bs.gap_buffer_toString(15);
        String str_2 = bs.gap_buffer_toString(5);
        assertEquals("final string", bs.gap_buffer_toString(19), "fail");
        assertEquals("at pos 0", bs.gap_buffer_toString(0), "fail");
        assertEquals("another string loaded", bs.gap_buffer_toString(3), "fail");
        assertEquals("another loaded line", str_1, "fail");
        assertEquals("I am the loaded line at a specific position", str_2);
    }
    
    @Test
    @DisplayName("Load a line into a specific position test bench")
    public void linked_load_line_at_position_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); 
        bs.load_line_at_position("another loaded line", 15);
        bs.load_line_at_position("I am the loaded line at a specific position", 5);
        bs.load_line_at_position("a string that will be loaded out of bounds", 50);
        bs.load_line_at_position("another string loaded out of bounds", -1);
        bs.load_line_at_position("final string", 19);
        bs.load_line_at_position("I should fail", 20);
        bs.load_line_at_position("at pos 0", 0);
        bs.load_line_at_position("another string loaded", 3);
        String str_1 = bs.gap_buffer_toString(15);
        String str_2 = bs.gap_buffer_toString(5);
        assertEquals("final string", bs.gap_buffer_toString(19), "fail");
        assertEquals("at pos 0", bs.gap_buffer_toString(0), "fail");
        assertEquals("another string loaded", bs.gap_buffer_toString(3), "fail");
        assertEquals("another loaded line", str_1, "fail");
        assertEquals("I am the loaded line at a specific position", str_2);
    }
    
    @Test
    @DisplayName("insert an empty line above the current cursor_pos")
    public void insert_empty_line_above_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.load_line_at_position("string at pos 1", 1);
        bs.load_line_at_position("string at pos 2", 2);
        bs.load_line_at_end("string at pos 3");
        bs.cursor_down(3);
        assertTrue(bs.line_count() == 3);
        bs.insert_empty_line_above(); //above the current line there should be a /n
        assertEquals("", bs.gap_buffer_toString(2), "fail"); //pos 2 should now be "\n" because we were at line 3 and loaded an empty line above
        bs.cursor_up(50); //move the cursor out of range, this should actually fail immediately because we are trying to move the cursor outside of the allocated range
        int l = bs.line_count();
        assertTrue(l == 3);
        bs.insert_empty_line_above();
        assertFalse(bs.cursor_down(577));
        bs.cursor_down(3);
        assertTrue(bs.line_count() == 6);
        bs.insert_empty_line_above();
        bs.cursor_down(3);
        assertTrue(bs.line_count() == 9);
        bs.insert_empty_line_above();
        assertEquals("", bs.gap_buffer_toString(5), "fail"); //pos 2 should now be "\n" because we were at line 6 and loaded an empty line above
        assertEquals("", bs.gap_buffer_toString(8), "fail"); //pos 2 should now be "\n" because we were at line 9 and loaded an empty line above
        bs.cursor_down(5);
        assertEquals(14, bs.line_count(), "fail");
        bs.insert_empty_line_above();
        assertEquals("", bs.gap_buffer_toString(13), "fail"); //pos 2 should now be "\n" because we were at line 14 and loaded an empty line above
        bs.cursor_down(5);
        assertEquals(19, bs.line_count(), "fail");
        bs.insert_empty_line_above();
        assertEquals("", bs.gap_buffer_toString(18), "fail"); //pos 2 should now be "\n" because we were at line 19 and loaded an empty line above
        assertFalse(bs.cursor_down());
    }
    
    @Test
    @DisplayName("insert an empty line above the current cursor_pos")
    public void linked_insert_empty_line_above_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.load_line_at_position("string at pos 1", 1);
        bs.load_line_at_position("string at pos 2", 2);
        bs.load_line_at_end("string at pos 3");
        bs.cursor_down(3);
        assertTrue(bs.line_count() == 3);
        bs.insert_empty_line_above(); //above the current line there should be a /n
        assertEquals("", bs.gap_buffer_toString(2), "fail"); //pos 2 should now be "\n" because we were at line 3 and loaded an empty line above
        bs.cursor_up(50); //move the cursor out of range, this should actually fail immediately because we are trying to move the cursor outside of the allocated range
        int l = bs.line_count();
        assertTrue(l == 3);
        bs.insert_empty_line_above();
        assertFalse(bs.cursor_down(577));
        bs.cursor_down(3);
        assertTrue(bs.line_count() == 6);
        bs.insert_empty_line_above();
        bs.cursor_down(3);
        assertTrue(bs.line_count() == 9);
        bs.insert_empty_line_above();
        assertEquals("", bs.gap_buffer_toString(5), "fail"); //pos 2 should now be "\n" because we were at line 6 and loaded an empty line above
        assertEquals("", bs.gap_buffer_toString(8), "fail"); //pos 2 should now be "\n" because we were at line 9 and loaded an empty line above
        bs.cursor_down(5);
        assertEquals(14, bs.line_count(), "fail");
        bs.insert_empty_line_above();
        assertEquals("", bs.gap_buffer_toString(13), "fail"); //pos 2 should now be "\n" because we were at line 14 and loaded an empty line above
        bs.cursor_down(5);
        assertEquals(19, bs.line_count(), "fail");
        bs.insert_empty_line_above();
        assertEquals("", bs.gap_buffer_toString(18), "fail"); //pos 2 should now be "\n" because we were at line 19 and loaded an empty line above
        assertFalse(bs.cursor_down());
    }
    
    @Test
    @DisplayName("insert an empty line below the current cursor_pos")
    public void insert_empty_line_below_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        assertFalse(bs.cursor_up());
        assertFalse(bs.insert_empty_line_below());
        bs.cursor_down();
        assertTrue(bs.line_count() == 1);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(2), "fail");
        bs.cursor_down(2);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(4), "fail");
        bs.cursor_down(16);
        assertTrue(bs.line_count() == 19);
        assertFalse(bs.cursor_down());
        assertFalse(bs.insert_empty_line_below());
        bs.cursor_up(1);
        assertTrue(bs.line_count() == 18);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(19), "fail");
        bs.cursor_up(5);
        assertTrue(bs.line_count() == 13);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(14), "fail");
    }
    
    @Test
    @DisplayName("insert an empty line below the current cursor_pos")
    public void linked_insert_empty_line_below_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        assertFalse(bs.cursor_up());
        assertFalse(bs.insert_empty_line_below());
        bs.cursor_down();
        assertTrue(bs.line_count() == 1);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(2), "fail");
        bs.cursor_down(2);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(4), "fail");
        bs.cursor_down(16);
        assertTrue(bs.line_count() == 19);
        assertFalse(bs.cursor_down());
        assertFalse(bs.insert_empty_line_below());
        bs.cursor_up(1);
        assertTrue(bs.line_count() == 18);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(19), "fail");
        bs.cursor_up(5);
        assertTrue(bs.line_count() == 13);
        bs.insert_empty_line_below();
        assertEquals("", bs.gap_buffer_toString(14), "fail");
    }
    
    
    @Test
    @DisplayName("test that getting the cursor row works properly")
    public void line_count_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down(3); //we have moved the cursor down three, we should be at cursor_pos_first_dim = 3
        assertTrue(bs.line_count() == 3);
        bs.cursor_up(); //the cursor is now at 2
        assertTrue(bs.line_count() == 2);
        bs.cursor_down(4);
        assertTrue(bs.line_count() == 6);
    }
    
    @Test
    @DisplayName("test that getting the cursor row works properly")
    public void linked_line_count_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down(3); //we have moved the cursor down three, we should be at cursor_pos_first_dim = 3
        assertTrue(bs.line_count() == 3);
        bs.cursor_up(); //the cursor is now at 2
        assertTrue(bs.line_count() == 2);
        bs.cursor_down(4);
        assertTrue(bs.line_count() == 6);
    }
    
    @Test
    @DisplayName("test getting the length of the current line")
    public void curr_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        assertTrue(bs.curr_line_length() == 11); //the cursor_pos_first_dim should be at zero and we have one object with a length of 11
        bs.load_line_at_position("test str", 1);
        bs.cursor_down(); //move the cursor down to where the new line was loaded
        assertTrue(bs.curr_line_length() == 8);
        bs.load_line_at_position("another test str", 2);
        bs.cursor_down(); 
        assertEquals(bs.curr_line_length(), 16, "fail");
        bs.cursor_down(2);
        bs.load_line_at_position("t", 4);
        assertEquals(bs.curr_line_length(), 1, "fail");
        bs.load_line_at_position("12", 19);
        bs.cursor_down(15);
        assertEquals(bs.curr_line_length(), 2, "fail");
        assertFalse(bs.cursor_down());
        assertTrue(bs.cursor_up(19));
        assertFalse(bs.cursor_up());
    }
    
    @Test
    @DisplayName("test getting the length of the current line")
    public void linked_curr_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        assertTrue(bs.curr_line_length() == 11); //the cursor_pos_first_dim should be at zero and we have one object with a length of 11
        bs.load_line_at_position("test str", 1);
        bs.cursor_down(); //move the cursor down to where the new line was loaded
        assertTrue(bs.curr_line_length() == 8);
        bs.load_line_at_position("another test str", 2);
        bs.cursor_down(); 
        assertEquals(bs.curr_line_length(), 16, "fail");
        bs.cursor_down(2);
        bs.load_line_at_position("t", 4);
        assertEquals(bs.curr_line_length(), 1, "fail");
        bs.load_line_at_position("12", 19);
        bs.cursor_down(15);
        assertEquals(bs.curr_line_length(), 2, "fail");
        assertFalse(bs.cursor_down());
        assertTrue(bs.cursor_up(19));
        assertFalse(bs.cursor_up());
    }
    
    @Test
    public void cursor_position_in_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string");
        bs.load_line_at_position("a different string", 1);
        bs.load_line_at_end("last string");
        assertEquals(bs.cursor_position_in_line(), 0, "fail");
        bs.cursor_down();
        assertFalse(bs.cursor_left());
        bs.cursor_right(2);
        assertEquals(bs.cursor_position_in_line(), 2, "fail"); 
        bs.cursor_right(5);
        assertEquals(bs.cursor_position_in_line(), 7, "fail");
        bs.insert_text("txt");
        assertEquals(bs.gap_buffer_toString(1), "a diffetxtrent string");
        assertEquals(bs.cursor_position_in_line(), 10, "fail");
        bs.cursor_right(9);
        assertEquals(bs.cursor_position_in_line(), 19, "fail");
        assertFalse(bs.cursor_right(100));
        bs.cursor_left(5);
        assertEquals(bs.cursor_position_in_line(), 14, "fail");
        bs.cursor_left();
        assertEquals(bs.cursor_position_in_line(), 13, "fail");
        bs.cursor_left(9);
        assertEquals(bs.cursor_position_in_line(), 4, "fail");
        bs.cursor_up();
        assertTrue(bs.line_count() == 0);
        bs.cursor_left();
        assertEquals(bs.cursor_position_in_line(), 3, "fail");
    }
    
    @Test
    public void linked_cursor_position_in_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string");
        bs.load_line_at_position("a different string", 1);
        bs.load_line_at_end("last string");
        assertEquals(bs.cursor_position_in_line(), 0, "fail");
        bs.cursor_down();
        assertFalse(bs.cursor_left());
        bs.cursor_right(2);
        assertEquals(bs.cursor_position_in_line(), 2, "fail"); 
        bs.cursor_right(5);
        assertEquals(bs.cursor_position_in_line(), 7, "fail");
        bs.insert_text("txt");
        assertEquals(bs.gap_buffer_toString(1), "a diffetxtrent string");
        assertEquals(bs.cursor_position_in_line(), 10, "fail");
        bs.cursor_right(9);
        assertEquals(bs.cursor_position_in_line(), 19, "fail");
        bs.cursor_left(5);
        assertEquals(bs.cursor_position_in_line(), 14, "fail");
        bs.cursor_left();
        assertEquals(bs.cursor_position_in_line(), 13, "fail");
        bs.cursor_left(9);
        assertEquals(bs.cursor_position_in_line(), 4, "fail");
        bs.cursor_up();
        assertTrue(bs.line_count() == 0);
        bs.cursor_left();
        assertEquals(bs.cursor_position_in_line(), 3, "fail");
    }
    
    @Test
    public void cursor_move_first_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down();
        bs.cursor_move_first_line();
        assertEquals(bs.cursor_line_position(), 0, "fail");
        bs.cursor_down(10);
        bs.cursor_move_first_line();
        assertEquals(bs.cursor_line_position(), 0, "fail");
        assertFalse(bs.cursor_up());
        bs.cursor_down(19);
        assertFalse(bs.cursor_down());
        bs.cursor_move_first_line();
        assertEquals(bs.cursor_line_position(), 0, "fail");
    }
    
    @Test
    public void linked_cursor_move_first_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down();
        bs.cursor_move_first_line();
        assertEquals(bs.cursor_line_position(), 0, "fail");
        bs.cursor_down(10);
        bs.cursor_move_first_line();
        assertEquals(bs.cursor_line_position(), 0, "fail");
        assertFalse(bs.cursor_up());
        bs.cursor_down(19);
        assertFalse(bs.cursor_down());
        bs.cursor_move_first_line();
        assertEquals(bs.cursor_line_position(), 0, "fail");
    }
    
    @Test
    public void cursor_move_last_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down();
        bs.cursor_move_last_line();
        assertEquals(bs.cursor_line_position(), 1, "fail");
        bs.cursor_down(10);
        bs.load_line_at_start("a second string");
        bs.cursor_move_last_line();
        assertEquals(bs.cursor_line_position(), 2, "fail");
        bs.cursor_down(16);
        bs.load_line_at_position("another string", 3);
        bs.cursor_move_last_line();
        assertEquals(bs.cursor_line_position(), 3, "fail");
    }
    
    @Test
    public void linked_cursor_move_last_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down();
        bs.cursor_move_last_line();
        assertEquals(bs.cursor_line_position(), 1, "fail");
        bs.cursor_down(10);
        bs.load_line_at_start("a second string");
        bs.cursor_move_last_line();
        assertEquals(bs.cursor_line_position(), 2, "fail");
        bs.cursor_down(16);
        bs.load_line_at_position("another string", 3);
        bs.cursor_move_last_line();
        assertEquals(bs.cursor_line_position(), 3, "fail");
    }
    
    @Test
    public void cursor_move_start_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_right(2);
        bs.cursor_right();
        assertEquals(bs.cursor_position_in_line(), 3, "fail");
        bs.cursor_move_start_line();
        assertEquals(bs.cursor_position_in_line(), 0, "fail");
        bs.cursor_right(6);
        bs.cursor_move_start_line();
        assertEquals(bs.cursor_position_in_line(), 0, "fail");
        assertFalse(bs.cursor_left());
    }
    
    @Test
    public void linked_cursor_move_start_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_right(2);
        bs.cursor_right();
        assertEquals(bs.cursor_position_in_line(), 3, "fail");
        bs.cursor_move_start_line();
        assertEquals(bs.cursor_position_in_line(), 0, "fail");
        bs.cursor_right(6);
        bs.cursor_move_start_line();
        assertEquals(bs.cursor_position_in_line(), 0, "fail");
        assertFalse(bs.cursor_left());
    }
    
    @Test
    public void cursor_move_end_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_right(2);
        bs.cursor_right();
        assertEquals(bs.cursor_position_in_line(), 3, "fail");
        bs.cursor_move_end_line();
        assertEquals(bs.cursor_position_in_line(), 89, "fail");
        assertFalse(bs.cursor_right());
    }
    
    @Test
    public void linked_cursor_move_end_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_right(2);
        bs.cursor_right();
        assertEquals(bs.cursor_position_in_line(), 3, "fail");
        bs.cursor_move_end_line();
        assertEquals(bs.cursor_position_in_line(), 11, "fail");
        bs.cursor_right(6);
        bs.cursor_move_end_line();
        assertEquals(bs.cursor_position_in_line(), 11, "fail");
        assertFalse(bs.cursor_right());
    }
    
    @Test
    public void remove_line_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.load_line_at_position("a line", 1);
        bs.load_line_at_end("a last line");
        assertTrue(bs.line_count() == 0);
        bs.remove_line();
        //we removed the GapBuffer at pos 0, it should be replaced by "a line"
        assertEquals(bs.gap_buffer_toString(0), "a line", "fail");
        assertEquals(bs.gap_buffer_toString(1), "a last line", "fail"); 
        bs.load_line_at_end("new line"); //pos 2
        bs.load_line_at_position("string", 3);
        bs.load_line_at_end("new last line");
        bs.cursor_down(2); //at pos 2
        bs.remove_line();
        assertEquals(bs.gap_buffer_toString(2), "string", "fail"); 
        assertEquals(bs.gap_buffer_toString(3), "new last line");
        bs.cursor_down(10); 
        assertFalse(bs.remove_line()); //there is nothing to remove, should return false
        bs.load_line_at_start("new start line");
        assertEquals(bs.gap_buffer_toString(3), "string", "fail"); 
        bs.cursor_up(5); //pos 7
        assertEquals(bs.line_count(), 7, "fail");
        bs.load_line_at_position("test string", 7);
        bs.remove_line();
        assertEquals(bs.gap_buffer_toString(4), "new last line", "fail");
        bs.load_line_at_position("test", 18);
        bs.load_line_at_position("another string", 17);
        bs.cursor_down(10); //pos 17
        assertEquals(bs.line_count(), 17, "fail");
        bs.remove_line();
        assertEquals(bs.gap_buffer_toString(17), "test", "fail");
    }

        
    @Test
    public void linked_remove_line_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.load_line_at_position("a line", 1);
        bs.load_line_at_end("a last line");
        assertTrue(bs.line_count() == 0);
        bs.remove_line();
        //we removed the GapBuffer at pos 0, it should be replaced by "a line"
        assertEquals(bs.gap_buffer_toString(0), "a line", "fail");
        assertEquals(bs.gap_buffer_toString(1), "a last line", "fail"); 
        bs.load_line_at_end("new line"); //pos 2
        bs.load_line_at_position("string", 3);
        bs.load_line_at_end("new last line");
        bs.cursor_down(2); //at pos 2
        bs.remove_line();
        assertEquals(bs.gap_buffer_toString(2), "string", "fail"); 
        assertEquals(bs.gap_buffer_toString(3), "new last line");
        bs.cursor_down(10); 
        assertFalse(bs.remove_line()); //there is nothing to remove, should return false
        bs.load_line_at_start("new start line");
        assertEquals(bs.gap_buffer_toString(3), "string", "fail"); 
        bs.cursor_up(5); //pos 7
        assertEquals(bs.line_count(), 7, "fail");
        bs.load_line_at_position("test string", 7);
        bs.remove_line();
        assertEquals(bs.gap_buffer_toString(4), "new last line", "fail");
        bs.load_line_at_position("test", 18);
        bs.load_line_at_position("another string", 17);
        bs.cursor_down(10); //pos 17
        assertEquals(bs.line_count(), 17, "fail");
        bs.remove_line();
        assertEquals(bs.gap_buffer_toString(17), "test", "fail");
    }
    
    @Test
    public void remove_char_toleft() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string");
        bs.cursor_right(5);
        assertEquals(bs.cursor_position_in_line(), 5, "fail");
        bs.remove_char_toleft();
        assertEquals(bs.cursor_position_in_line(), 4, "fail");
        //this should remove the space
        assertEquals(bs.gap_buffer_toString(0), "thisstring", "fail");
    }
    
    @Test
    public void linked_remove_char_toleft() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string");
        bs.cursor_right(5);
        assertEquals(bs.cursor_position_in_line(), 5, "fail");
        bs.remove_char_toleft();
        assertEquals(bs.cursor_position_in_line(), 4, "fail");
        //this should remove the space
        assertEquals(bs.gap_buffer_toString(0), "thisstring", "fail");
    }
    
    @Test
    @DisplayName("test inserting text")
    public void insert_text_tb() {
        BufferStructure bs = new BufferStructure("gap");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down();
        bs.cursor_down(); //move the cursor down 2
        assertFalse(bs.insert_text("text to add"));
        bs.load_line_at_position("loaded line", 2);
        bs.cursor_right(2);
        bs.insert_text("text to add");
        assertEquals(bs.gap_buffer_toString(2),"lotext to addaded line", "fail");
        bs.load_line_at_end("line"); //pos 3
        assertEquals(bs.gap_buffer_toString(3),"line", "fail");
        bs.cursor_down();
        assertTrue(bs.line_count() == 3);
        bs.insert_text("injection");
        assertEquals(bs.gap_buffer_toString(3),"injectionline", "fail");
        bs.remove_char_toleft(6);
        assertEquals(bs.gap_buffer_toString(3),"injline", "fail");
        bs.insert_text("new");
        assertEquals(bs.gap_buffer_toString(3),"injnewline", "fail");
        bs.cursor_down(); //move to line 4
        bs.load_line_at_position("another line", 4);
        bs.cursor_right(5);
        bs.insert_text("txt");
        assertEquals(bs.gap_buffer_toString(4),"anothtxter line", "fail");
        assertFalse(bs.insert_text("very long piece of text, this is a problem for the programkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"));
    }
    
    @Test
    @DisplayName("test inserting text")
    public void linked_insert_text_tb() {
        BufferStructure bs = new BufferStructure("linked");
        bs.load_line_at_start("this string"); //loads a line at the start
        bs.cursor_down();
        bs.cursor_down(); //move the cursor down 2
        assertFalse(bs.insert_text("text to add"));
        bs.load_line_at_position("loaded line", 2);
        bs.cursor_right(2);
        bs.insert_text("text to add");
        assertEquals(bs.gap_buffer_toString(2),"lotext to addaded line", "fail");
        bs.load_line_at_end("line"); //pos 3
        assertEquals(bs.gap_buffer_toString(3),"line", "fail");
        bs.cursor_down();
        assertTrue(bs.line_count() == 3);
        bs.insert_text("injection");
        assertEquals(bs.gap_buffer_toString(3),"injectionline", "fail");
        bs.remove_char_toleft(6);
        assertEquals(bs.gap_buffer_toString(3),"injline", "fail");
        bs.insert_text("new");
        assertEquals(bs.gap_buffer_toString(3),"injnewline", "fail");
        bs.cursor_down(); //move to line 4
        bs.load_line_at_position("another line", 4);
        bs.cursor_right(5);
        bs.insert_text("txt");
        assertEquals(bs.gap_buffer_toString(4),"anothtxter line", "fail");
    }
}
