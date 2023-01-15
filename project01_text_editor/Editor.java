
import java.util.Scanner;
import java.io.*;

/**
 * The following class is the top level class for Project 01. Processing
 * begins will a call of main passing the desired type, either "gap" or
 * "linked" to choose the GapBuffer data structure as opposed to the linked list
 * data structure. Following this, the user is prompted to open a file using the o command.
 * When the user does this the file of their choosing will be stored to the data structure.
 * Now the user has access to a number of commands for cursor manipulation and text editing.
 * In addition to the base functionality I implemented a backspace 'b' command to delete characters
 * in the current line. The program will then compute the number of backspaces to complete or simply 
 * move one if none were specified. 
 * 
 * The cursor maintains its position between lines, but
 * it will reset to the first position if a new line 'ab' or 'aa' is used as expected. 
 * The functionality to do this operation was implemented in BufferStructure.
 *
 * @author Alexander Labell
 * @version 1.0
 */

public class Editor
{  
    private static Scanner scnr;
    private static Document doc;
    private static File file;
    private static String filename;

    public Editor() {}

    /**
     * The main method is the entry point for processing. The user passes the desired data structure into main and main will prompt to open a file
     * When the user complies a file of their choosing will be loaded into Buffer Structure. The Scanner class was used to continue prompting the
     * user for input until the 'q' command is asserted to quit. In this loop, there are several methods that have similar functionality but different operations.
     * All the methods take input and interpret the input to execute the various commands. The majority of them check the first character of the string
     * and then use substring to get the rest of the input. For example, in the case of moving the cursor right, the program checks if the user has
     * typed 'l' and then looks further to see if there are any integers in the input, if so this indicates the number of positions to move right and the
     * program parses this string to an int and exectutes the lower level method. 
     * 
     * The clear function deviates slightly. It creates a new empty file and then
     * loads this empty file into Buffer Structure and then proceeds to store the result in the current file. This effectively clears all data from
     * the current file and allows the user to continue editing from sratch. 
     *
     * @param  str  a string that represents the type of data structure
     * @return void
     */
    public static void main(String str) throws Exception {
        System.out.println("Welcome to SimpleTextEditor");
        System.out.print("(open a file)");
        System.out.println();
        String t = str;
        boolean flag = true;
        while (flag) {
            if (t.trim().equals("linked")) {
                t = "linked";
                flag = false;
            } else if (t.trim().equals("gap")) {
                t = "gap";
                flag = false;
            } else {
                return; //"gap" nor "linked" were entered, exit
            }
        }
        scnr = new Scanner(System.in);
        String user_input = scnr.nextLine();
        while (user_input.equals("")) {
                user_input = scnr.nextLine();
        }
        while (user_input.charAt(0) != 'q') { //loop until the user quits listening for user input all the while
            if (user_input != "") {
                //methods required for commands and clear data functionality
                open_file(t, user_input);
                file_io(user_input, filename);
                edit_current(user_input);
                cursor_movement(user_input);
                edit_lines(user_input);
                clear_data(t, user_input);
            }
            display_line(); //used to view the data and as an added feature the physical cursor pos to enhance viewability
            user_input = scnr.nextLine();
            //if the user has typed enter without entering a command the user will be reprompted until they type valid text or quit
            while (user_input.equals("")) {
                user_input = scnr.nextLine();
            }
        }
        System.out.println("Bye!");
        if (scnr != null) {
            scnr.close();
        }
        return;
    }

    /**
     * The display_line method will print the current line in the structure. To be clear, within the 2-D structure. display_line will print
     * the line at the cursor_pos for the downwards dimension of the array. I also added a feature to print a physical cursor position for
     * viewability reasons using substring -> current cursor_pos.
     *
     * @return void
     */
    public static void display_line() throws Exception {
        if (filename == null || doc.cursor_pos_in_line() == -1) {
            return;
        }
        doc.store_file(filename);
        FileInputStream fis = new FileInputStream(filename); 
        Scanner file_scnr = new Scanner(fis); //for reading the file
        int i = 0;
        String line = "";
        line = file_scnr.nextLine();
        while (file_scnr.hasNextLine()) { //read the file
            if (i == doc.line_count()) { //get to the current line of the current cursor_pos by looping until the cursor_pos line_count returns the cursor position
                break;
            } else {
                line = file_scnr.nextLine();
                i++; 
            }
        }
        String temp_s = ""; //hold the string of current line
        int cursor_pos = doc.cursor_pos_in_line();
        try {
            temp_s = line.substring(0, cursor_pos) + "^" + line.substring(cursor_pos, line.length()); //manipulate and display the current line
        } catch (StringIndexOutOfBoundsException e) {
            return;
        }
        String display_line = temp_s;
        System.out.println(display_line);
    }

    /**
     * The open_file method will check if the user input begins with 'o'. If so the user has indicated that they would like to open a file, they must
     * follow this command with a valid filename. If the filename entered is not valid, the user is alerted of this and the method exits. If the filename
     * is vlaid a new document object is created that takes the filename and the type. Finally,the files contents are loaded into the
     * buffer structure.
     *
     * @param  t, user_input  t is the type of data structure, it can be either GapBuffer or LinkedListBuffer.
     * @return void
     */
    public static void open_file(String t, String user_input) throws IOException {
        if (user_input.charAt(0) == 'o') {
            if (user_input.substring(1) != "") {
                //I added trim so that if the user types ofilename.txt or o filename.txt there are no problems
                filename = user_input.substring(1).trim();
                if (filename.substring(filename.length() - 4).equals(".txt")) { //check if invalid file
                    doc = new Document(t,filename);
                    System.out.println("===============================");
                    doc.load_file(filename);
                } else {
                    System.out.println("Invalid file name, not a .txt file");
                    filename = null;
                }
            }
        }
    }

    /**
     * The file_io method is called in the loop within main. It is used to perform file IO based on the user's input.
     * If the user enters 'c' the data is stored into the file. Therefore c acts as a 'save' command to the physical file itself.
     * It uses the store_file method implemented in Document to write the data to the file. The 's' command is used to create a new file
     * and save to this file.
     * @param String input, String file_name
     * @return void
     */
    public static void file_io(String input, String file_name) throws IOException {
        if (doc == null) {
            return;
        }
        switch (input.charAt(0)) {
            case 'c':
                doc.store_file(file_name); //save
                break;
            case 's':
                //save the current loaded data to a new file
                if (input.length() > 1) {
                    String new_filename = input.substring(1).trim(); //get filename
                    if (new_filename.substring(new_filename.length() - 4).equals(".txt")) { //lbyl
                        File new_file = new File(new_filename); //create new file
                        filename = new_filename;
                        file = new_file;
                        doc.store_file(new_filename); //populate new file
                    } else {
                        System.out.println("Invalid file name, not a .txt file");
                        filename = null;
                    }
                }
                break;
            default:
                return;
        }
    }

    /**
     * The edit_current method will contain the commands to edit and manipulate text within a line itself.
     * To add new text to a line the user asserts 'e' followed by the text they desire. The backspace functionality using 'b' was also implemented.
     * If the user types 'b' the code will check if the user wants to move a number of positions by using length and substring to parse text to int.
     * If the user specifies a number of characters to delete the appropriate Document method will be called, otherwise a single character is removed.
     * 
     *
     * @param  input    user input as a string
     * @return void
     */
    public static void edit_current(String input) {
        //edit the current line of text using the current cursor position
        if (filename == null) {
            return;
        }
        switch (input.charAt(0)) {
            case 'e':                           //add text
                try {
                    String text_to_add = input.substring(2);
                    doc.insert_text(text_to_add);
                } catch (StringIndexOutOfBoundsException e) {
                    break;
                }
                break;
            case 'b':                           //backspace to delete characters
                if (input.length() > 2) {
                    String num_c_b = input.substring(1);
                    try {
                        int num_c_b_int = Integer.parseInt(num_c_b.trim()); //get number of characters to delete
                        doc.remove_char_toleft(num_c_b_int);
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else {
                    doc.remove_char_toleft();
                }
                break;
            default:
                return;
        }
    }

    /**
     * The cursor_movement method will take user input and handle the cases where the user wants to move the cursor position. If the user types
     * 'r' the cursor will move right. If the user types r and specifies further text that can be parsed to an int the cursor will be moved
     * that number of positions to the right. Left cursor, up cursor and down cursor use similar functionality. 
     *
     * @param  input
     * @return void
     */
    public static void cursor_movement(String input) {
        if (doc == null || filename == null) {
            return;
        }
        switch (input.charAt(0)) {
            case 'r':
                if (input.length() > 1) {
                    String num_c_r = input.substring(1);
                    try {
                        int num_c_r_int = Integer.parseInt(num_c_r.trim()); //parse the string to an int that represents the number of characters to move to the right
                        doc.cursor_right(num_c_r_int); 
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else  { 
                    doc.cursor_right(); //move right one character
                }
                break;
            case 'l':
                if (input.length() > 1) {
                    String num_c_l = input.substring(1); //parse the string representing the characters to move left to an int
                    try {
                        int num_c_l_int = Integer.parseInt(num_c_l.trim()); //trim the input and parse to an integer
                        doc.cursor_left(num_c_l_int);
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else {
                    doc.cursor_left(); //move the cursor position one if no amount is specified
                }
                break;
            case 'd':
                if (input.length() > 1) {
                    String num_d = input.substring(1);
                    try {
                        int num_d_int = Integer.parseInt(num_d.trim());
                        doc.cursor_down(num_d_int);
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else {
                    doc.cursor_down();
                }
                break;
            case 'u':
                if (input.length() > 1) {
                    String num_u = input.substring(1);
                    try {
                        int num_u_int = Integer.parseInt(num_u.trim());
                        doc.cursor_up(num_u_int);
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else { 
                    doc.cursor_up();
                }
                break;
            default:
                return;
        }
    }

    /**
     * The edit_lines method will insert new lines above and below the cursor position in the first dimension of the data structure.
     * When the user asserts a new line above or below the cursor position will intially start at position zero. If the user types 'dl' the current
     * line will be deleted. If the user specifies a number after 'dl' that number will be parsed to an integer and the current line + n lines below
     * will be deleted.
     *
     * @param   input
     * @return void
     */
    public static void edit_lines(String input) {
        if (doc == null || input.length() < 2 || filename == null) {
            return;
        }
        switch (input.substring(0,2)) {
            case "ab":
                doc.insert_line_below();
                break;
            case "aa":
                doc.insert_line_above();
                break;
            case "dl":
                if (input.length() > 2) {           
                    String num_d = input.substring(2);
                    if (!num_d.trim().equals("")) {
                        int num_d_int = 0;
                        try {
                            num_d_int = Integer.parseInt(num_d.trim());
                        } catch (NumberFormatException e) {
                            return;
                        }
                        for (int i=0; i<num_d_int+1; i++) {
                            doc.remove_line();   //delete the number of lines specified by the user below the current line
                        }
                    }
                } else {
                    doc.remove_line(); //delete current line
                }
                break;
            default:
                return;
        }
    }

    /**
     * The clear_data method will create a new empty file and load this empty file into the data structure using open_file. Then the data is stored
     * to the original file and the original file is reopened
     *
     * @param   String t, String input  t is the type of data structure and input is user input
     * @return void
     */
    public static void clear_data(String t, String input) throws IOException {
        if (input.length() < 2) {
            return; 
        }
        if (input.substring(0,2).equals("cl")) {
            //create a new empty file, load the empty file into the data structure
            //effectively clearing the data structure
            File new_file;
            //print stream for writing to file
            FileOutputStream fos = null;
            PrintStream ps = null;
            new_file = new File("clear_data_file.txt");
            String temp_filename = filename;
            try {
                fos = new FileOutputStream(new_file);
                ps = new PrintStream(fos);
                ps.print(""); //populate with empty string
                open_file(t, "o clear_data_file.txt");
            } catch (NullPointerException e) {
                filename = temp_filename;
                file_io("c", filename);
                try {
                    open_file(t, "o " + get_filename());
                } catch (NullPointerException npe) {
                    return;
                }
            }
        }
    }

    //For debugging purposes
    public String doc_toString() {
        return doc.toStringDocument();
    }

    public String line_toString(int index) {
        return doc.toStringLine(index);
    }

    //get the filename used by the BufferStructure
    public static String get_filename() {
        return filename;
    }

}
