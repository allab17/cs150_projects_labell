

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

/**
 * The test class EditorTest. All methods were tested with a file with the following format.
 *   line 1
 *   line 2
 *   line 3
 *   line 4
 *   line 5
 *   line 6
 *   line 7
 *   
 *   Some tests modify this file when inserting, deleting, loading, etc therefore it is necessary to rewrite the file after each test in the format
 *   above in order for all tests to pass.
 *
 * @author  Alexander Labell
 * @version 1.0
 */
public class EditorTest
{
    /**
     * Default constructor for test class EditorTest
     */
    public EditorTest()
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
    public void open_file_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("gap", "o filename.txt");
        //store the contents of a file containing 7 lines
        String str = e.doc_toString();
        assertEquals(str, "line 1line 2line 3line 4line 5line 6line 7");
    }
    
    @Test
    public void edit_current_tb() throws IOException{
        Editor e = new Editor();
        e.open_file("gap", "o filename.txt");
        //store the contents of a file containing 7 lines
        String str = e.doc_toString();
        assertEquals(str, "line 1line 2line 3line 4line 5line 6line 7");
        e.cursor_movement("d"); //move to pos 2
        e.edit_current("e text to add");
        assertEquals(e.line_toString(1), "text to addline 2", "fail");
        e.cursor_movement("l 11");
        e.cursor_movement("d"); //move to pos3
        e.cursor_movement("r 3"); //move the line cursor_pos to the right
        e.edit_current("e txt");
        assertEquals(e.line_toString(2), "lintxte 3");
        e.cursor_movement("l 6");
        e.cursor_movement("d 3");
        e.cursor_movement("r 1");
        e.edit_current("e moretext.");
        assertEquals(e.line_toString(5), "lmoretext.ine 6");
        e.file_io("c", "filename.txt");
    }
    
    @Test
    public void linked_edit_current_tb() throws IOException{
        Editor e = new Editor();
        e.open_file("linked", "o filename.txt");
        //store the contents of a file containing 7 lines
        String str = e.doc_toString();
        assertEquals(str, "line 1line 2line 3line 4line 5line 6line 7");
        e.cursor_movement("d"); //move to pos 1
        e.edit_current("e text to add");
        assertEquals(e.line_toString(1), "text to addline 2", "fail");
        e.cursor_movement("l 11");
        e.cursor_movement("d 2"); //move to pos 3
        e.cursor_movement("r 3"); //move the line cursor_pos to the right
        e.edit_current("e txt");
        assertEquals(e.line_toString(3), "lintxte 4");
        e.cursor_movement("l 6");
        e.cursor_movement("d 3"); //move to pos 6
        e.cursor_movement("r 1");
        e.edit_current("e moretext.");
        assertEquals(e.line_toString(6), "lmoretext.ine 7");
    }
    
    @Test
    public void new_file_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("gap", "o filename.txt");
        e.file_io("s newfilename.txt", "filename.txt");
        e.get_filename();
        assertEquals(e.get_filename(), "newfilename.txt");
        //the new file now contains the contents for filename.txt
        e.file_io("c", e.get_filename());
        String str = e.doc_toString();
        assertEquals(str, "line 1line 2line 3line 4line 5line 6line 7");
    }
    
    @Test
    public void linked_new_file_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("linked", "o filename.txt");
        e.file_io("s newfilename.txt", "filename.txt");
        e.get_filename();
        assertEquals(e.get_filename(), "newfilename.txt");
        //the new file now contains the contents for filename.txt
        e.file_io("c", e.get_filename());
        String str = e.doc_toString();
        assertEquals(str, "line 1line 2line 3line 4line 5line 6line 7");
    }
    
    @Test
    public void edit_lines_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("linked", "o filename.txt");
        e.cursor_movement("d 6");
        e.edit_lines("ab");
        assertEquals(e.line_toString(7), "", "fail");
        String str_doc = e.doc_toString();
        e.cursor_movement("d 6");
        e.edit_lines("ab");
        assertEquals(e.line_toString(13), "", "fail");
        e.edit_lines("aa");
        assertEquals(e.line_toString(11), "", "fail");
        e.cursor_movement("u 6");
        e.edit_lines("aa");
        assertEquals(e.line_toString(5), "", "fail");
        e.cursor_movement("u 6"); //move to pos 0
        //delete Line 1
        e.edit_lines("dl");
        assertEquals(e.line_toString(0), "line 2", "fail");
        //delete line 2 and line 3
        e.edit_lines("dl 2");
        assertEquals(e.line_toString(0), "line 5", "fail");
    }
    
    @Test 
    public void clear_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("linked", "o filename.txt");
        e.clear_data("linked", "cl");
        String str = e.doc_toString();
        assertEquals(str, "", "fail");
    }
    
    @Test
    public void save_to_new_file() throws IOException {
        Editor e = new Editor();
        e.open_file("gap", "o filename.txt");
        e.file_io("s new_file.txt","filename.txt");
        String fn = e.get_filename();
        assertEquals(fn, "new_file.txt", "fail");
        String str = e.doc_toString();
        assertEquals(str, "line 1line 2line 3line 4line 5line 6line 7", "fail");
    } 
    
     @Test
    public void delete_current_line_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("linked", "o filename.txt");
        //the cursor position should be 0
        e.edit_lines("dl");
        assertEquals(e.line_toString(0), "line 2", "fail");
        String str = e.doc_toString();
        assertEquals(str, "line 2line 3line 4line 5line 6line 7");
    }
    
     @Test
    public void delete_lines_tb() throws IOException {
        Editor e = new Editor();
        e.open_file("linked", "o filename.txt");
        e.cursor_movement("d");
        //cursor is at row 1
        e.edit_lines("dl 2");
        assertEquals(e.line_toString(0), "line 1", "fail");
        assertEquals(e.line_toString(1), "line 5", "fail");
        String str = e.doc_toString();
        assertEquals(str, "line 1line 5line 6line 7");
    }
}
