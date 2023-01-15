
/**
 * BufferStructure is an object that holds an array of GapBuffer objects. It specifies a cursor_pos in the first dimension so 
 * that lines can be accessed and thus individual characters within those lines as we move into the 2nd dimension of the 2-D structure.
 * The BufferStructure is passed the type to be used in it's constructor and sets the data structure accordingly. 'linked' specifies an array
 * of objects that use a linked list to manipulate the data while gap specifies an array of objects that use a standard array to manipulate
 * the data.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class BufferStructure implements BufferStructureInterface
{
    private GapBufferInterface[] gb_array; //array to store the GapBuffer objects
    private int gb_cnt; //this counter keeps track of the number of GapBuffer objects that we have created
    private int cursor_pos_first_dim; //stores the cursor position for the rows
    private String type;

    public BufferStructure(String s)
    {
        if (s.equals("linked")) {
            gb_array = new LinkedListBuffer[20];
            type = "linked";
        } else if (s.equals("gap")) {
            gb_array = new GapBuffer[20];
            type = "gap";
        }
        gb_cnt = 0;
        cursor_pos_first_dim = 0; //cursor_pos intialized to zero
    }

    //method to maintain the cursor pos across line calls, therfore any time
    //we manipulate the cursor pos of one GapBuffer all other GapBuffers must also
    //reflect the cursor pos of the change, thus when we move up a line
    //the cursor pos will be maintained which is what a user expects
    /**
     * The synchronize_cursor_pos method loop is used to maintain the line cursor position as the cursor is moved up and down. When a user
     * types in a text document they expect the cursor to maintain its position from one line to the next.
     *
     * @param  int cursor_pos   the cursor position of the line from the last cursor manipulation
     * @return void
     */
    public void synchronize_cursor_pos(int cursor_pos) {
        //loop through all lines and set the cursor pos of all lines to current
        //this must be called anytime the cursor is manipulated
        for(int i=0; i<gb_cnt; i++) {
            if (gb_array[i] != null) {
                gb_array[i].set_cursor_position(cursor_pos);
            }
        }
    }

    //everytime we add GapBuffers to the array we must have a cnt that increments
    //if the count is greater than the length of the gb_array we must increase the size of
    //the array by creating a new array and setting the length equal to the overflow
    // ie. cnt - gb_array.length
    /**
     * The inc_gb_count method will increment to keep a count of the number of GapBuffers in the array.
     *
     * @return void
     */
    private void inc_gb_count() {
        gb_cnt++;
    }

    /**
     * The get_buffer_structure_size method will get the number of GapBuffer objects
     *
     * @return int
     */
    public int get_buffer_structure_size() {
        return gb_cnt;
    }

    // Creates a new buffer and places relative to current buffers
    /**
     * The load_line_at_start method will create a buffer depending on the type field and then proceed to add the GapBuffer at the begining
     * of the array and shift all other GapBuffers to make room for the inserted GapBuffer
     *
     * @param String str_value
     * @return void
     */
    @Override
    public void load_line_at_start(String str_value) {
        GapBufferInterface b = null;
        if (type.equals("gap")) {
            b = new GapBuffer(); //create a new buffer
        } else if (type.equals("linked")) {
            b = new LinkedListBuffer();
        }
        if (b == null) {
            return;
        }
        b.load_string(str_value); //load the string in question into the GapBuffer
        inc_gb_count(); //increment the number of gap buffer objects

        //when we add a gap buffer at the beginnng, we must shift all the gap buffers down
        for (int i=gb_cnt; i>0; i--) {
            gb_array[i] = gb_array[i-1];
        } //elements are now shifted down
        gb_array[0] = b; //add the new gap buffer at the first index
    }

    /**
     * The load_line_at_end will load a new GapBuffer or LinkedListBuffer (depending on type field) into the array at the last available position
     *
     * @param String str_value  the string to be loaded
     * @return void
     */
    @Override
    public void load_line_at_end (String str_value) {
        GapBufferInterface b = null;
        if (type.equals("gap")) {
            b = new GapBuffer(); //create a new buffer
        } else if (type.equals("linked")) {
            b = new LinkedListBuffer();
        }
        if (b == null) {
            return;
        } 
        b.load_string(str_value); //load the string in question into the GapBuffer
        if (gb_array[gb_cnt] != null) {
            int i=gb_cnt; // start at the number of gab buffer objects and loop until null, effectively adding the new line at the last available slot in the structure
            while (gb_array[i] != null) {
                i++;
            }
            gb_array[i] = b;
        }
        gb_array[gb_cnt] = b;
        inc_gb_count();
    }

    // Creates a new buffer and places at the identified position,
    // shifting the buffers currently at that position and below down.
    /**
     * The load_line_at_position will load a new GapBuffer or LinkedListBuffer (depending on type field) into the array at a position specified by the position argument
     *
     * @param String str_value, int position    int position is the index to load the GapBuffer/LinkedListBuffer object into
     * @return void
     */
    @Override
    public void load_line_at_position (String str_value, int position) {
        if ((position < 0) || (position > gb_array.length - 1)) {
            return;
        }
        GapBufferInterface b = null;
        if (type.equals("gap")) {
            b = new GapBuffer(); //create a new buffer
        } else if (type.equals("linked")) {
            b = new LinkedListBuffer();
        }
        if (b == null) {
            return;
        }
        b.load_string(str_value); //load the string in question into the GapBuffer
        gb_array[position] = b; //set the object at the position specified to the newly created LinkedListBuffer/GapBuffer
        inc_gb_count();
    }

    /**
     * The insert_empty_line_above will create a new GapBuffer/LinkedListBuffer depending on the type field and set the object at the current
     * cursor position minus one to the newly created GapBuffer/LinkedListBuffer object.
     * Error checking must be performed to ensure the above line can be altered without throwing an exception.
     * 
     * @return void
     */
    @Override
    public boolean insert_empty_line_above() {
        GapBufferInterface b = null;
        if (type.equals("gap")) {
            b = new GapBuffer(); //create a new buffer
        } else if (type.equals("linked")) {
            b = new LinkedListBuffer();
        }
        if (b == null) {
            return false;
        }
        if (cursor_pos_first_dim > 0 && cursor_pos_first_dim <= gb_array.length - 1) { //check for validity
            b.load_string("");
            gb_array[cursor_pos_first_dim-1] = b; //add an empty line at the positon above the current cursor position
            inc_gb_count();
            return true;
        }
        return false;
    }

    /**
     * The insert_empty_line_below will create a new GapBuffer/LinkedListBuffer depending on the type field and set the object at the current
     * cursor position plus one to the newly created GapBuffer/LinkedListBuffer object.
     * Error checking must be performed to ensure the above line can be altered without throwing an exception.
     * 
     * @return void
     */
    @Override
    public boolean insert_empty_line_below() {
        GapBufferInterface b = null;
        if (type.equals("gap")) {
            b = new GapBuffer(); //create a new buffer
        } else if (type.equals("linked")) {
            b = new LinkedListBuffer();
        }
        if (b == null) {
            return false;
        }
        if (cursor_pos_first_dim > 0 && cursor_pos_first_dim < gb_array.length - 1) { //check for validity
            b.load_string("");
            gb_array[cursor_pos_first_dim+1] = b; //add an empty line at the positon below the current cursor position
            inc_gb_count();
            return true;
        }
        return false;
    }

    // Returns identified information.
    /**
     * line_count get the current row
     * 
     * @return void
     */
    @Override
    public int line_count() {
        return cursor_pos_first_dim; //get the current row, this acts as the index of the first dimension of the GapBuffer array
    }

    /**
     * curr_line_length get the length of a line
     * 
     * @return int returns the length of a line
     */
    @Override
    public int curr_line_length() {
        int current_row = line_count();
        return gb_array[current_row].length(); //calls .length() in the GB class that returns the length of a line
    }

    //I added this method I need to access the string of a gap buffer or else I cannot write it to a file or implement the higher level toString() methods
    /**
     * The method curr_line_length will get the length of a line
     * 
     * @return String the string in the GapBuffer object specified by index
     */
    public String gap_buffer_toString(int index) {
        if (gb_array[index] != null) {
            return gb_array[index].toString(); //get the string represented inside a specific GapBuffer object inside gb_array depending on the index
        }
        return "";
    }

    // Returns the line where the cursor is positioned and where
    // in the current line it is positioned.
    /**
     * The method cursor_line_position will get the cursor's position in the first dimensional sense, by row that is
     * 
     * @return int the cursor position
     */
    @Override
    public int cursor_line_position() {
        return line_count();
    }

    /**
     * The method cursor_position_in_line will get the cursor's position within a specific row
     * 
     * @return int the cursor position within a line
     */
    @Override
    public int cursor_position_in_line() {
        if (gb_array[cursor_pos_first_dim] != null) {
            return gb_array[cursor_pos_first_dim].cursor_position(); //calls .cursor_position() in GB class
        }
        return -1;
    }

    // The cursor movement methods.
    /**
     * The method cursor_left will move the cursor left one character
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_left() {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is indeed an item at the current cursor pos
            boolean r = gb_array[cursor_pos_first_dim].cursor_left(); //move the cursor left
            if (r) {
                int t_cp = cursor_position_in_line(); //get the new cursor position
                synchronize_cursor_pos(t_cp); //synchronize all other object's cursor positions to the new cursor position of the current line
                gb_array[cursor_pos_first_dim].format(); //reformat the array, keep in mind reformat does nothing if we are using a LinkedListBuffer for obvious reasons
            }
            return r;
        }
        return false;
    }

    /**
     * The method cursor_left will move the cursor left a number of characters specified by the argument.
     * 
     * @param char_count the number of characters to move the cursor left by
     * @return boolean
     */
    @Override
    public boolean cursor_left(int char_count) {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is indeed an item at the current cursor pos
            boolean r = gb_array[cursor_pos_first_dim].cursor_left(char_count); //move left
            if (r) {
                int t_cp = cursor_position_in_line(); //get position
                synchronize_cursor_pos(t_cp); //synchronize cursor position with other objects
                gb_array[cursor_pos_first_dim].format();  //format array to specifications, see GapBuffer for more information
            }
            return r;
        }
        return false;
    }

    /**
     * The method cursor_right will move the cursor rightby a single character.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_right() {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is indeed an item at the current cursor pos
            boolean r = gb_array[cursor_pos_first_dim].cursor_right();
            if (r) {
                int t_cp = cursor_position_in_line();
                synchronize_cursor_pos(t_cp);
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }

    /**
     * The method cursor_right will move the cursor right a number of characters specified by the argument.
     * 
     * @param char_count the number of characters to move the cursor right by
     * @return boolean
     */
    @Override
    public boolean cursor_right(int char_count) {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is indeed an item at the current cursor pos
            boolean r = gb_array[cursor_pos_first_dim].cursor_right(char_count);
            if (r) {
                int t_cp = cursor_position_in_line();
                synchronize_cursor_pos(t_cp);
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }

    /**
     * The method cursor_up will move the cursor up by a single row maintaining the cursor position inherent before the call.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_up() {
        if (cursor_pos_first_dim > 0) { //check to ensure there will not be an index exception
            cursor_pos_first_dim--;
            return true;
        }
        return false;
    }

    /**
     * The method cursor_up will move the cursor up by the number of characters specified by line_count maintaining the cursor position inherent before the call.
     * 
     * @param line_count the number of characters to move up
     * @return boolean
     */
    @Override
    public boolean cursor_up(int line_count) {
        if (cursor_pos_first_dim >= line_count) { //check to ensure there will not be an index exception
            cursor_pos_first_dim -= line_count; //move up by line_count
            return true;
        }
        return false;
    }

    /**
     * The method cursor_down will move the cursor down by a single character maintaining the cursor position inherent before the call.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_down() {
        if (cursor_pos_first_dim < gb_array.length-1) {
            cursor_pos_first_dim++;
            return true;
        }
        return false;
    }

    /**
     * The method cursor_down will move the cursor down by the number of characters specified by line_count maintaining the cursor position inherent before the call.
     * 
     * @param line_count the number of characters to move down
     * @return boolean
     */
    @Override
    public boolean cursor_down(int line_count) {
        if (cursor_pos_first_dim < gb_array.length-line_count) {
            cursor_pos_first_dim += line_count;
            return true;
        }
        return false;
    }

    /**
     * The method cursor_move_first_line will move the cursor to the first row of the data structure.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_move_first_line() {
        if (cursor_pos_first_dim > -1) {
            cursor_pos_first_dim = 0;
            return true;
        }
        return false;
    }

    /**
     * The method cursor_move_last_line will move the cursor to the last row of the data structure that stores a GapBuffer.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_move_last_line() {
        if (gb_cnt <= gb_array.length) {
            cursor_pos_first_dim = gb_cnt; //move the cursor to the last line that stores a GapBuffer
            return true;
        }
        return false;
    }

    /**
     * The method cursor_move_start_line will move the cursor to the first line within a specific row of the data structure.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_move_start_line() {
        if (gb_array[cursor_pos_first_dim] != null) {
            boolean r = gb_array[cursor_pos_first_dim].cursor_move_start_line();
            if (r) {
                int t_cp = cursor_position_in_line(); //get new cursor position
                synchronize_cursor_pos(t_cp);   //synchronize all objects to new cursor position
                gb_array[cursor_pos_first_dim].format(); //format the array, ignore in the case of using a LinkedListBuffer
            }
            return r;
        }
        return false;
    }

    /**
     * The method cursor_move_end_line will move the cursor to the last line within a specific row of the data structure.
     * 
     * @return boolean
     */
    @Override
    public boolean cursor_move_end_line() {
        if (gb_array[cursor_pos_first_dim] != null) {
            boolean r = gb_array[cursor_pos_first_dim].cursor_move_end_line();
            if (r) {
                int t_cp = cursor_position_in_line();
                synchronize_cursor_pos(t_cp);
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }

    // Will remove current line and characters in current line.
    /**
     * The method remove_line will remove the current line of the data structure
     * 
     * @return boolean
     */
    @Override
    public boolean remove_line() {
        //to remove a line within the gb_array we will decrement gb_cnt
        //and we will also shift all the elements down from the bottom to the 
        //removed line
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is an item to remove                           
            for (int i=cursor_pos_first_dim; i<gb_array.length-1; i++) {
                gb_array[i] = gb_array[i+1];
            } //elements are now shifted up

            gb_cnt--; //we sucessfully removed the item and decrement the count that holds the total number of GapBuffers
            return true;
        }
        return false;
    }
    
    /**
     * The method remove_char_toleft will rremove a single character to the left of the cursor position within a specific line of the data structure
     * 
     * @return boolean
     */
    @Override
    public boolean remove_char_toleft() {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is an item to edit
            boolean r = gb_array[cursor_pos_first_dim].remove_char_toleft();    //remove a character to the left of the cursor position
            if (r) {
                int t_cp = cursor_position_in_line(); //get new cursor position
                synchronize_cursor_pos(t_cp);   //synchronize with other objects
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }

    /**
     * The method remove_char_toleft will remove char_count number of characters to the left of the cursor position within a 
     * specific line of the data structure. 
     * 
     * @param char_count the number of characters to remove to the left
     * @return boolean
     */
    @Override
    public boolean remove_char_toleft(int char_count) {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is an item to edit
            boolean r = gb_array[cursor_pos_first_dim].remove_char_toleft(char_count);
            if (r) {
                int t_cp = cursor_position_in_line();
                synchronize_cursor_pos(t_cp);
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }

    // Will add a character or string to current line.
    /**
     * The method insert_text will insert a string at the cursor position of the current line of the BufferStructure.
     * 
     * @param str_value  the text to be inserted
     * @return boolean
     */
    @Override
    public boolean insert_text(String str_value) {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is an item to edit
            boolean r = gb_array[cursor_pos_first_dim].insert_text(str_value);
            if (r) {
                int t_cp = cursor_position_in_line();
                synchronize_cursor_pos(t_cp);
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }

    /**
     * The method insert_text will insert a character at the cursor position of the current line of the BufferStructure.
     * 
     * @param char_value  the character to be inserted
     * @return boolean
     */
    @Override
    public boolean insert_text(char char_value) {
        if (gb_array[cursor_pos_first_dim] != null) { //check if there is an item to edit
            boolean r = gb_array[cursor_pos_first_dim].insert_text(char_value);
            if (r) {
                int t_cp = cursor_position_in_line();
                synchronize_cursor_pos(t_cp);
                gb_array[cursor_pos_first_dim].format();
            }
            return r;
        }
        return false;
    }
}
