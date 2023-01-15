
import java.lang.Exception;

/**
 * Gap Buffer. This class loads a single line of a string. Formats the string based on the cursor position, the text to the left is pushed all the way to 
 * the left and the text to the right is pushed all the way to the right. The middle is empty space. During the design phase I decided to create counters
 * for left and right based on shifting and manipulation of both the cursor_pos and newly added characters/strings. I created a private method called format
 * that formats the array based on the cursor_pos and the counters. See below for functionality. In brief, there are two loops that control where characters
 * are placed to format them as the various other methods are performing manipulations of them. The GapBuffer class passed general input tests however
 * more thorough testing will be carried out in a later version to ensure bugs are dealt with.
 *
 * @Alexander Labell
 * @version 1.0
 */
public class GapBuffer implements GapBufferInterface
{
    private char[] charArray;
    private String str;
    private int num_chars;
    private int cursor_pos;
    private int empty_space; //field to store the number of empty spaces in the charArray, always equal to the length of the character array - num_chars
    private int characters_left; //create counters for left and right based on shifting and adding
    private int characters_right;
    
    /**
     * Constructor for objects of class GapBuffer
     */
    public GapBuffer()
    {
        str = "";
        charArray = new char[90]; //initialize the length of the line in question, this should be roughly the number of characters that can be typed on a standard computer window ~80-90 characters
        num_chars = str.length();
        empty_space = charArray.length - num_chars; //num_chars is changing as characters are added
        characters_left = cursor_pos;
        characters_right = num_chars - characters_left;
        cursor_pos = 0; //init the cursor position to zero, a user would expect this
    }

    /**
     * The load_string method will load a string into the class, and updates fields as a result of the input string.
     *
     * @param  str_value  a string parameter
     * @return void
     */
    // Loads a string into the class, removing a previously stored
    // string.
    @Override
    public void load_string(String str_value) {
       //get the string into the class
       str = str_value; //set our local string to the string that is loaded into the method
       num_chars = str.length(); //update num_chars with the new string
       empty_space = charArray.length - num_chars; //update the empty space in the charArray
       characters_right = num_chars - characters_left;
    }
    
    // Returns the stored string without the empty space.
    @Override
    public String toString() {
        return str;
    }
    
    // Returns length of the string without the empty space.
    @Override
    public int length() {
        return str.length();
    }
    
    // Returns the current cursor postion at the start of the empty space.
    @Override
    public int cursor_position() {
        return cursor_pos;
    }
    
    @Override
    public void set_cursor_position(int cp) {
        cursor_pos = cp;
    }
    
    @Override
    public void format() { //format, private method, this method will format the charArray so that the correct characters appear at the beginning and end of the charArray
        for (int i=0; i<characters_left; i++) { //loop through character array until characters_left
            charArray[i] = str.charAt(i); //set character array contents equal to string on each iteration
        }
        //loop through character array, decrementing from charArray.length-1 until characters_right
        for (int i=charArray.length-1; i >= charArray.length-characters_right; i--) {
            try {
                charArray[i] = str.charAt(i-empty_space);
            } catch (StringIndexOutOfBoundsException e) {
                return;
            }
        }
    }
    
    //The cursor movement methods.
    @Override
    public boolean cursor_left() {
        if (cursor_pos > 0) { //when we try to move the cursor the left there is a possibility that it is already at 0 and therefore if we attempt to decrement we will be outside the range of the array
            cursor_pos--;
            characters_left--;
            characters_right++;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean cursor_left(int char_count) {
        if (cursor_pos >= char_count) {
            cursor_pos -= char_count;
            characters_left -= char_count;
            characters_right += char_count;
            return true; //we sucessfully moved cursor_left
        }
        return false;
    }
    
    @Override
    public boolean cursor_right() {
        if ((cursor_pos < charArray.length-1) && ((characters_right - 1) > -1)) { //check if we will fail upon incrementing cursor_pos
            cursor_pos++; //move cursor one to the right
            characters_left++; //increment characters_left and decrement characters_right, the empty space remains the same
            characters_right--;
            return true; //we sucessfully moved cursor_right
        }
        return false;
    }
    
    @Override
    public boolean cursor_right(int char_count) {
        if ((cursor_pos < charArray.length - char_count) && ((characters_right - char_count) > -1)) {
            cursor_pos += char_count;
            characters_left += char_count;
            characters_right -= char_count;
            return true; //we sucessfully moved cursor_right
        }
        return false;
    }
    
    @Override
    public boolean cursor_move_start_line() {
        try {
            cursor_pos = 0; //move the cursor to the first position
        } catch (Exception e ) {
            System.err.println("Error, the cursor failed to move to position 0");
            return false;
        }
        return true;
    }
    
    
    @Override
    public boolean cursor_move_end_line() {
        try {
            cursor_pos = charArray.length - 1;
        } catch (StringIndexOutOfBoundsException e) { 
            return false;
        }
        return true;
    }
    
    // Will remove one or a variable number of chars from the string.
    @Override
    public boolean remove_char_toleft() {
        cursor_pos--; //move cursor pos left, the cursor position is on the index of deletion
        String temp_s = "";
        try {
            temp_s = str.substring(0, cursor_pos) + str.substring(cursor_pos + 1); //this will concatenate a substring that excludes the element that was at the cursor_pos/location of deletion
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
        str = temp_s;
        characters_left--; //a character was deleted and therefore the characters_left should decrement
        num_chars--; //the number of characters in the string itself has decreased by 1
        empty_space = charArray.length - num_chars; //num_chars is changing as characters are added
        return true;
    }
    
    @Override
    public boolean remove_char_toleft(int char_count) {
        cursor_pos -= char_count; //move cursor pos left the number of deletions
        String temp_s = "";
        try {
            temp_s = str.substring(0, cursor_pos) + str.substring(cursor_pos + char_count); //this will concatenate a substring that excludes the element that was at the cursor_pos/location of deletion
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
        str = temp_s; //update the string field
        characters_left -= char_count; //char_count number of characters was deleted and therefore the characters_left should decrement by char_count
        num_chars -= char_count; //the number of characters in the string itself has decreased by char_count
        empty_space = charArray.length - num_chars; //num_chars is changing as characters are added
        return true;
    }
    
    //Will add a character or string to the empty space.
    @Override
    public boolean insert_text(String str_value) { //add string in the current cursor position, then move cursor right
        String temp_s = ""; //create a new string and intialize it to empty
        try {
            temp_s = str.substring(0, cursor_pos) + str_value + str.substring(cursor_pos); //split the string at the current cursor_pos and concatenate with the passed string
        } catch (StringIndexOutOfBoundsException e) { 
            return false;
        }
        int chars_added = str_value.length();
        str = temp_s; //set string reference str to the temporary created string, now the field will reflect the new string
        empty_space -= chars_added;
        if (empty_space <= 1) { //we have overflowed, there must always be an empty space of 1 because that is where the cursor is stored
            //the array must be reformatted
            return false;
        }
        
        
        int inc=0;
        for (int i=cursor_pos; i<cursor_pos+str_value.length(); i++) {
            charArray[i] = str_value.charAt(inc);
            inc++;
        }
        
        cursor_pos += chars_added; //the cursor_pos should move to the right the number of characters added
        characters_left += chars_added; //the number of characters on left is incremented by the number of characters added
        num_chars += chars_added; //increment num_chars by the number of *Characters added
        empty_space = charArray.length - num_chars; //num_chars is changing as characters are added
        
        return true;
    }
    
    @Override
    public boolean insert_text(char char_value) {
        String temp_s = ""; //create a new string and intialize it to empty
        try {
            temp_s = str.substring(0, cursor_pos) + String.valueOf(char_value) + str.substring(cursor_pos); //split the string at the current cursor_pos and concatenate with the passed character
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
        str = temp_s; //set string reference str to the temporary created string, now the field will reflect the new string
        empty_space--;
        if (empty_space <= 1) { //we have overflowed, there must always be an empty space of 1 because that is where the cursor is stored
            //go to new line because the user has typed too much
            return false;
        }
        charArray[cursor_pos] = char_value; //add the char_value at the old cursor position
        cursor_pos++;
        characters_left++;
        num_chars++; //one character was added
        
        empty_space = charArray.length - num_chars; //num_chars is changing as characters are added
        return true;
    }
}
