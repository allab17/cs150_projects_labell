

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Scanner;

import java.io.*;

/**
 * The test class DocumentTest.
 *
 * @author  Alexander Labell
 * @version 1.0
 */
public class DocumentTest
{
    /**
     * Default constructor for test class DocumentTest
     */
    public DocumentTest()
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
    @DisplayName("testing loading file given a filename")
    public void load_file_tb() throws IOException {
        Document doc = new Document("gap", "filename.txt"); //create a new Document object
        File file = new File("filename.txt");
        doc.load_file("filename.txt");
        doc.insert_line_below();
        doc.store_file("filename.txt");
    }
    
    @Test
    public void toStringDocument_tb() throws IOException {
        Document doc = new Document("gap", "filename.txt");
        File file = new File("filename.txt");
        doc.load_file("filename.txt");
        String str = doc.toStringLine(2);
        assertEquals("line 3", str, "fail");
        String doc_str = doc.toStringDocument();
        assertEquals("line 1line 2line 3line 4line 5line 6line 7", doc_str, "fail");
        String str_1 = doc.toStringLine(3);
        assertEquals("line 4", str_1, "fail");
    } 
    
    @Test
    public void toStringDocument_linked_tb() throws IOException {
        Document doc = new Document("linked", "filename.txt");
        File file = new File("filename.txt");
        doc.load_file("filename.txt");
        String str = doc.toStringLine(2);
        assertEquals("line 3", str, "fail");
        String doc_str = doc.toStringDocument();
        assertEquals("line 1line 2line 3line 4line 5line 6line 7", doc_str, "fail");
        String str_1 = doc.toStringLine(3);
        assertEquals("line 4", str_1, "fail");
    } 

    @Test
    public void line_length_linked_tb() throws IOException {
        Document doc = new Document("linked", "filename.txt");
        File file = new File("filename.txt");
        doc.load_file("filename.txt");
        int l = doc.line_length(2);
        assertTrue(l == 6);
        doc.cursor_down();
        doc.cursor_down();
        doc.insert_text("add more text to line 5");
        assertTrue(doc.line_length() == 29);
        doc.cursor_move_start_line(); //move the cursor to the first line
        //assertEquals(doc.line_count(), 0, "fail");
    }
    
    @Test
    public void line_length_tb() throws IOException {
        Document doc = new Document("gap", "filename.txt");
        File file = new File("filename.txt");
        doc.load_file("filename.txt");
        assertEquals(doc.line_count(), 0, "fail");
        assertEquals(doc.toStringLine(2), "line 3", "fail");
        assertTrue(doc.line_length(2) == 6);
        doc.cursor_down(2);
        assertEquals(doc.line_count(), 2, "fail");
        assertEquals(doc.toStringLine(4), "line 5", "fail");
        assertTrue(doc.line_length(4) == 6);
        doc.cursor_down(2);
        assertEquals(doc.line_count(), 4, "fail");
        doc.insert_text("more_text");
        assertEquals(doc.toStringLine(4), "more_textline 5", "fail");
        assertEquals(doc.line_length(4), 15, "fail");
        doc.store_file("filename.txt"); //to view results in file
    }
    
    @Test
    public void linked_line_length_tb() throws IOException {
        Document doc = new Document("linked", "filename.txt");
        File file = new File("filename.txt");
        doc.load_file("filename.txt");
        assertEquals(doc.line_count(), 0, "fail");
        assertEquals(doc.toStringLine(2), "line 3", "fail");
        assertTrue(doc.line_length(2) == 6);
        doc.cursor_down(2);
        assertEquals(doc.line_count(), 2, "fail");
        assertEquals(doc.toStringLine(4), "line 5", "fail");
        assertTrue(doc.line_length(4) == 6);
        doc.cursor_down(2);
        assertEquals(doc.line_count(), 4, "fail");
        doc.insert_text("more_text");
        assertEquals(doc.toStringLine(4), "more_textline 5", "fail");
        assertEquals(doc.line_length(4), 15, "fail");
        doc.store_file("filename.txt"); //to view results in file
    }
}
