
/**
 * Write a description of class Node here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Node
{
    private char data;
    private Node next;
    
    public Node(char d) {
        data = d;
    }
    
    public Node getNext() {
        return next;
    }
    
    public void setNext(Node n) {
        next = n;
    }
    
    public char getData() {
        return data;
    }
    
    public void setData(char d) {
        data = d;
    }  
}
