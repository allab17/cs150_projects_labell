

import java.io.*;
import java.util.Scanner;

/**
 * All interactions with data structures go through this class. This class creates a new BufferStructure object and gives this object
 * the type to used either GapBuffer or LinkedListBuffer. The class loads and stores to a file, therefore interactions between the data structure
 * ie. BufferStructure and the file must go through this class. Additionally, note that some functionality is split between Document and DocumentIO but essentially
 * DocumentIO is used to break up some of the functionality required for the structure file interactions.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Document implements DocumentInterface
{
    private String file_name;
    private BufferStructure b_struct;
    private DocumentIO doc_io;
    
    public Document(String s, String filename)
    {
        file_name = filename;
        b_struct = new BufferStructure(s);
        doc_io = new DocumentIO(file_name);
    }
    
    // Load and store from/to an identified file.
    /**
     * The load_file method will load the contents of a file into the BufferStructure. It accomplishes by looping through the file and
     * loading each consecutive line at the end of the BufferStructure.
     *
     * @param filename
     * @return boolean
     */
    @Override
    public boolean load_file (String filename) throws IOException {
        file_name = filename;
        try {
            doc_io.open_file(filename); //creates a new FileInputStream and primes a scnr to read
            while (doc_io.has_more_lines()) {
                String line = doc_io.read_line(); //get line
                b_struct.load_line_at_end(line); //load line into buffer structure
            } //each line in file is loaded into BufferStructure starting with index 0
            return true;
        } catch  (IOException e) {
            return false;
        } finally {
            doc_io.close_file(); //we should check if the stream is not null
        }
    }
    
    /**
     * The store_file method will store the contents of the BufferStructure to the file. It accomplishes this by looping through the BufferStructure
     * and using a print stream and the println() method to write the toString() of the current line in question.
     *
     * @param filename
     * @return boolean
     */
    @Override
    public boolean store_file(String filename) throws IOException {
        file_name = filename; //update the current file_name, now the current file will simply be represented by the latest call to this method, we should provide functionality for the user to specify a file but I assume that is done later
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream(filename);
            ps = new PrintStream(fos);
            int i=0;
            while (i < b_struct.get_buffer_structure_size()) {
                ps.println(b_struct.gap_buffer_toString(i));
                i++;
            } //the file is populated
        } finally { //I see that this method throws IOException but I want to use finally to ensure that FileOutputStream and PrintStream are closed
            if (fos != null) {
                fos.close(); //close the file but check for null beforehand
            }
            if (ps != null) {
                ps.close(); //close printstream
            }
        }
        return true;
    }
    
    // Store to the current file.
    /**
     * The store_file method will store the contents of the BufferStructure to the file. It accomplishes this by looping through the BufferStructure
     * and using a print stream and the println() method to write the toString() of the current line in question.
     *
     * @return boolean
     */
    @Override
    public boolean store_file() throws IOException {
        //functionality very similar to store_file
        if (file_name == null) { //check if there is a file_name to process because this method was not provided one, it specifies "current"
            return false;
        }
        FileOutputStream fos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream(file_name);
            ps = new PrintStream(fos);
            int i=0;
            while (i < b_struct.get_buffer_structure_size()) {
                ps.println(b_struct.gap_buffer_toString(i));
                i++;
            } //the file is populated
        } finally {
            if (fos != null) {
                fos.close(); //close the file but check for null beforehand
            }
            if (ps != null) {
                ps.close(); //close printstream
            }
        }
        return true;
    }
    
    /**
     * The current_file_name method will get the file name of the working file.
     *
     * @return String
     */
    @Override
    public String current_file_name() {
        return file_name;
    }
    
    // Return information currently stored in the data structure
    // as a string.
    /**
     * The toStringDocument method will get the contents of the document object as a string.
     *
     * @return String
     */
    @Override
    public String toStringDocument() {
        //loop through 2-D array outputting each line
        String str = "";
        int i=0;
        //loop through BufferStructure concatenating each line with a string, this string will be returned after the loop completes
        while (i < b_struct.get_buffer_structure_size()) {
            str = str + b_struct.gap_buffer_toString(i); //uses string builder
            i++;
        }
        return str;
    }
    
    /**
     * The toStringLine method will get the contents of a line at an index specified by line_index
     *
     * @param line_index
     * @return String
     */
    @Override
    public String toStringLine(int line_index) {
        return b_struct.gap_buffer_toString(line_index); //return the string representation of the line in question
    }
    
    // Length of a certain line.
    /**
     * The line_length method will get the length of a line at line_index
     *
     * @param line_index
     * @return int
     */
    @Override
    public int line_length(int line_index) {
        int inc = 0;
        while (b_struct.line_count() != line_index) { //while the current cursor pos does not equal the line_index continue looping, if it does, output the length of the line in question
            b_struct.cursor_down(); //move the cursor down, the cursor_pos_first_dim will increment until it is equal to the line index specified
            inc++;
        }
        int length = b_struct.curr_line_length();
        b_struct.cursor_up(inc);
        return length;
    }
    
    // Length of line pointed to by cursor.
    /**
     * The line_length method will get the length of a line at the current cursor position
     *
     * @return int
     */
    @Override
    public int line_length() {
        return b_struct.curr_line_length();
    }
    
    // Current line count.
    /**
     * The line_count method will get the cursor position for the data structure
     *
     * @return int
     */
    @Override
    public int line_count() {
        return b_struct.line_count();
    }
    //get the cursor position within the line in question
    /**
     * The cursor_pos_in_line method will get the cursor position in the line in question
     *
     * @return int
     */
    public int cursor_pos_in_line() {
        return b_struct.cursor_position_in_line();
    }
    
    // See other interfaces for the following functionality.
    /**
     * The cursor_left method will move the cursor left one character
     *
     * @return boolean
     */
    @Override
    public boolean cursor_left() {
        return b_struct.cursor_left();
    }
    
    /**
     * The cursor_left method will move the cursor left char_count characters
     *
     * @param char_count
     * @return boolean
     */
    @Override
    public boolean cursor_left(int char_count) {
        return b_struct.cursor_left(char_count);
    }
    
    /**
     * The cursor_right method will move the cursor right one character
     *
     * @return boolean
     */
    @Override
    public boolean cursor_right() {
        return b_struct.cursor_right();
    }
    
    /**
     * The cursor_right method will move the cursor right char_count characters
     *
     * @param char_count
     * @return boolean
     */
    @Override
    public boolean cursor_right(int char_count) {
        return b_struct.cursor_right(char_count);
    }
    
    /**
     * The cursor_up method will move the cursor up one line
     * @return boolean
     */
    @Override
    public boolean cursor_up() {
        return b_struct.cursor_up();
    }
    
    /**
     * The cursor_up method will move the cursor up line_count lines
     *
     * @param line_count
     * @return boolean
     */
    @Override
    public boolean cursor_up(int line_count) {
        return b_struct.cursor_up(line_count);
    }
    
    /**
     * The cursor_down method will move the cursor down one line
     *
     * @return boolean
     */
    @Override
    public boolean cursor_down() {
        return b_struct.cursor_down();
    }
    
    /**
     * The cursor_down method will move the cursor down line_count lines
     *
     * @param line_count
     * @return boolean
     */
    @Override
    public boolean cursor_down(int line_count) {
        return b_struct.cursor_down(line_count);
    }
    
    /**
     * The cursor_move_first_line method will move the cursor to the first line
     *
     * @return boolean
     */
    @Override
    public boolean cursor_move_first_line() {
        return b_struct.cursor_move_first_line();
    }
    
     /**
     * The cursor_move_last_line method will move the cursor to the last line
     *
     * @return boolean
     */
    @Override
    public boolean cursor_move_last_line() {
        return b_struct.cursor_move_last_line();
    }
    
     /**
     * The cursor_move_start_line method will move the cursor to the start
     *
     * @return boolean
     */
    @Override
    public boolean cursor_move_start_line() {
        return b_struct.cursor_move_start_line();
    }
    
     /**
     * The cursor_move_end_line method will move the cursor to the end
     *
     * @return boolean
     */
    @Override
    public boolean cursor_move_end_line() {
        return b_struct.cursor_move_end_line();
    }
    
     /**
     * The insert_line_above method will insert an empty line above the current line.
     *
     * @return boolean
     */
    @Override
    public boolean insert_line_above() {
        return b_struct.insert_empty_line_above();
    }
    
     /**
     * The insert_line_below method will insert an empty line below the current line.
     *
     * @return boolean
     */
    @Override
    public boolean insert_line_below() {
        return b_struct.insert_empty_line_below();
    }
    
     /**
     * The remove_line method will remove the current line.
     *
     * @return boolean
     */
    @Override
    public boolean remove_line() {
        return b_struct.remove_line();
    }
    
     /**
     * The remove_char_toleft method will remove a single character to the left of the cursor position.
     *
     * @return boolean
     */
    @Override
    public boolean remove_char_toleft() {
        return b_struct.remove_char_toleft();
    }
    
     /**
     * The remove_char_toleft method will remove char_count characters to the left of the cursor position.
     *
     * @param char_count
     * @return boolean
     */
    @Override
    public boolean remove_char_toleft(int char_count) {
        return b_struct.remove_char_toleft(char_count);
    }
    
     /**
     * The insert_text method will insert a string at the current cursor position.
     *
     * @param str_value
     * @return boolean
     */
    @Override
    public boolean insert_text(String str_value) {
        return b_struct.insert_text(str_value);
    }
    
     /**
     * The insert_text method will insert a character at the current cursor position.
     *
     * @param char_value
     * @return boolean
     */
    @Override
    public boolean insert_text(char char_value) {
        return b_struct.insert_text(char_value);
    }
}
