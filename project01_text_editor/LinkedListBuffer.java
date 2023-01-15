
/**
 * Similar functionality to the GapBuffer class, but it replaces the array with a linked list
 *
 * @author Alexander Labell
 * @version 1.1
 */
public class LinkedListBuffer implements GapBufferInterface
{
    private String str;
    private int cursor_pos;
    private LinkedList linked_list;

    public LinkedListBuffer()
    {
        str = "";
        cursor_pos = 0; //init the cursor position to zero, a user would expect this
        linked_list = null;
    }

    /**
     * The load_string method will load a string into the class, removing a previously stored
     * updating the str field and call init_populate
     *
     * @param  str_value  a string
     * @return  void
     */
    // .
    @Override
    public void load_string(String str_value) {
        //get the string into the class
        str = str_value; //set our local string to the string that is loaded into the method
        init_populate();
    }

    /**
     * Returns the stored string without the empty space.
     *
     * @param
     * @return      The string in field str
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * The getString method will return a string representation of the linked list.
     *
     * @return String
     */
    public String getString() {
        return linked_list.toString();
    }

    //do nothing
    @Override
    public void format() {}

    // Returns length of the string without the empty space.
    /**
     * The length method will return the length of the string
     *
     * @return int
     */
    @Override
    public int length() {
        return str.length();
    }

    // Returns the current cursor postion at the start of the empty space.
    /**
     * The cursor_position method will return the current cursor postion
     *
     * @return int
     */
    @Override
    public int cursor_position() {
        return cursor_pos;
    }

    @Override
    public void set_cursor_position(int cp) {
        cursor_pos = cp;
    }

    /**
     * The init_populate method will populate the data structure initially
     *
     * @return void
     */
    private void init_populate() {
        linked_list = new LinkedList();
        for (int i=0; i<str.length(); i++) { //loop through the string adding each character to the LinkedList
            linked_list.add(str.charAt(i));
        }
        linked_list.add(cursor_pos, '^'); //the node to hold the cursor is intialized
    }

    //The cursor movement methods.
    /**
     * The cursor_left method will move the cursor one character to the left
     *
     * @return boolean
     */
    @Override
    public boolean cursor_left() {
        if (cursor_pos > 0) { //when we try to move the cursor the left there is a possibility that it is already at 0 and therefore if we attempt to decrement the cursor will be in limbo
            linked_list.remove(cursor_pos); //remove the cursor Node at its current position
            cursor_pos--; //the cursor_pos is now one to the left
            linked_list.add(cursor_pos, '^'); //re-add the cursor position node
            return true;
        }
        return false;
    }

    /**
     * The cursor_left method will move the cursor char_count characters to the left
     *
     * @param char_count
     * @return boolean
     */
    @Override
    public boolean cursor_left(int char_count) {
        if (cursor_pos >= char_count) {
            linked_list.remove(cursor_pos); //remove the cursor Node at the current cursor position
            cursor_pos -= char_count;
            linked_list.add(cursor_pos, '^'); //add node at the new cursor_pos, effectively moving it
            return true; //we sucessfully moved cursor_left
        }
        return false;
    }

    /**
     * The cursor_right method will move the cursor a single character to the right
     *
     * @return boolean
     */
    @Override
    public boolean cursor_right() {
        if (cursor_pos < linked_list.size()-1) { //check if we will fail upon incrementing cursor_pos
            linked_list.remove(cursor_pos);
            cursor_pos++; //move cursor one to the right
            linked_list.add(cursor_pos, '^');
            return true; //we sucessfully moved cursor_right
        }
        //we need to make the linked list bigger
        return false;
    }

    /**
     * The cursor_right method will move the cursor char_count to the right
     *
     * @param char_count
     * @return boolean
     */
    @Override
    public boolean cursor_right(int char_count) {
        if (cursor_pos < linked_list.size() - char_count) {
            linked_list.remove(cursor_pos);
            cursor_pos += char_count;
            linked_list.add(cursor_pos, '^');
            return true; //we sucessfully moved cursor_right
        }
        //we need to make the linked list bigger
        return false;
    }

    /**
     * The cursor_move_start_line method will move the cursor to the first position in the linked list
     *
     * @return boolean
     */
    @Override
    public boolean cursor_move_start_line() {
        try {
            linked_list.remove(cursor_pos);
            cursor_pos = 0; //move the cursor to the first position
            linked_list.add(cursor_pos, '^');
        } catch (Exception e) {
            System.err.println("Error, the cursor failed to move to position 0");
            return false;
        }
        return true;
    }

    /**
     * The cursor_move_end_line method will move the cursor to the last position in the linked list
     *
     * @return boolean
     */
    @Override
    public boolean cursor_move_end_line() {
        try {
            linked_list.remove(cursor_pos); //we removed the cursor node therefore the linkedlist is now one less in size than reality
            cursor_pos = linked_list.size(); //set the cursor_pos to the last index in the linked list
            linked_list.add(cursor_pos, '^');
        } catch (StringIndexOutOfBoundsException e) { 
            e.getMessage();
            System.err.println("error: cursor_pos is outside of range");
            return false;
        }
        return true;
    }

    // Will remove one or a variable number of chars from the string.
    /**
     * The remove_char_toleft method will remove one character to the left of the cursor position.
     *
     * @return boolean
     */
    @Override
    public boolean remove_char_toleft() {
        if (linked_list.get(cursor_pos-1) != null) {
            linked_list.remove(cursor_pos-1);
            cursor_pos--; //move cursor pos left because the cursor node has moved left after the prior node was deleted
            String temp_s = "";
            try {
                temp_s = str.substring(0, cursor_pos) + str.substring(cursor_pos + 1); //this will concatenate a substring that excludes the element that was at the cursor_pos/location of deletion
            } catch (StringIndexOutOfBoundsException e) {
                System.err.println("Error! remove_char_toleft failed due to string indexing error"); //for debugging purposes
                return false;
            }
            str = temp_s;
            return true;
        }
        return false;
    }

    /**
     * The remove_char_toleft method will remove char_count characters to the left of the cursor position.
     *
     * @return boolean
     */
    @Override
    public boolean remove_char_toleft(int char_count) {
        if (linked_list.get(cursor_pos - char_count) != null) {
            //loop from cursor_pos to cursor_pos - char_count inclusive and remove nodes by calling remove
            for (int i=cursor_pos - 1; i >= cursor_pos - char_count; i--) {
                linked_list.remove(i);
            }
            cursor_pos -= char_count; //move cursor pos left the number of deletions
            String temp_s = "";
            try {
                temp_s = str.substring(0, cursor_pos) + str.substring(cursor_pos + char_count); //this will concatenate a substring that excludes the element that was at the cursor_pos/location of deletion
            } catch (StringIndexOutOfBoundsException e) {
                System.err.println("Error! remove_char_toleft failed due to string indexing error"); //for debugging purposes
                return false;
            }
            str = temp_s; //update the string field
            return true;
        }
        return false;
    }

    //Will add a character or string to the empty space.
    /**
     * The insert_text method will add a string at the current cursor position.
     *
     * @return boolean
     */
    @Override
    public boolean insert_text(String str_value) { //add string in the current cursor position, then move cursor right
        String temp_s = ""; //create a new string and intialize it to empty
        try {
            temp_s = str.substring(0, cursor_pos) + str_value + str.substring(cursor_pos); //split the string at the current cursor_pos and concatenate with the passed string
        } catch (StringIndexOutOfBoundsException e) { 
            System.err.println("Error! insert_text failed due to string indexing error"); //for debugging purposes
            return false;
        }
        int chars_added = str_value.length();
        str = temp_s; //set string reference str to the temporary created string, now the field will reflect the new string

        int inc=0;
        for (int i=cursor_pos; i<cursor_pos+str_value.length(); i++) {
            linked_list.add(i, str_value.charAt(inc));
            inc++;
        }
        cursor_pos += chars_added; //the cursor_pos should move to the right the number of characters added
        return true;
    }

    /**
     * The insert_text method will add a character to the current cursor position.
     *
     * @param char_value    the character to insert
     * @return boolean
     */
    @Override
    public boolean insert_text(char char_value) {
        String temp_s = ""; //create a new string and intialize it to empty
        try {
            temp_s = str.substring(0, cursor_pos) + char_value + str.substring(cursor_pos); //split the string at the current cursor_pos and concatenate with the passed character
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Error! insert_text failed due to string indexing error"); //for debugging purposes
            return false;
        }
        str = temp_s; //set string reference str to the temporary created string, now the field will reflect the new string
        linked_list.add(cursor_pos, char_value); //add the char_value at the old cursor_pos
        cursor_pos++;
        return true;
    }
}
