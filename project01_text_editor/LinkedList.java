
/**
 * The LinkedList class has the basic functionality of a linked list; add, remove, size, get.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class LinkedList
{
    Node head;
    
    public void add(char e) { //add a new element at the end of the linked list
        Node node = new Node(e); //create a new node, pass the data to the new node
        
        //check if the linked list is empty, in that case the added node should the head of the linked list
        if (head == null) {
            head = node; //set the node to the head of the list
        } else { //if the head is not equal to null, meaning there is data within the linked list, we must traverse the linked list and then set the last Node's next to the created Node
            Node n = head; //start from the head
            while (n.getNext() != null) {
                n = n.getNext(); //traverse the list until we get to the final node that has a null reference to next
            }
            n.setNext(node); //set the last nodes next to the created node
        }
    }
    
    public void add(int index, char e) {
       //loop to the index before the one to add to, set the n.next to new node
       Node new_node = new Node(e); //create a new node
       int pos = 0;
       Node n = head;
       if (index == 0) {
           new_node.setNext(head);
           head = new_node;
       } else {
           while (pos != index - 1) {
                if (n == null) return;
                n = n.getNext();
                pos++;
           }
           Node node_point = n.getNext(); //node refers to the node that the new one should point to
           n.setNext(new_node); //point the current nodes next to new node
           new_node.setNext(node_point); //set the next of node to the node after
       }
    }
    
    public int remove(int index) {
        int pos = 0;
        Node n = head; //loop to the index before the one to remove
        //the head must be removed
        if (index == 0) {
            head = head.getNext(); //the current head can be removed by making the new head the Node that the head was pointing to
        } else {
            while (pos != index - 1) {
                if (n == null) return -1; //return if the node is null and end processing
                n = n.getNext(); //step
                pos++;
            }
            //node before the one to remove
            Node prior_node_remove = n; //set the current node to prior_node_remove to store the node
            n = n.getNext();
            n = n.getNext(); //we have now moved to the node past the one to delete
            prior_node_remove.setNext(n); //set the node before the one to delete to the current one effectively breaking the pointer to the node to remove
        }
        return index;
    }
    
    public int size() {
       Node n = head; //create a node and set it equal to the head of the list
       int cnt = 0;
       while (n != null) {  //traverse through the linked list incrementing cnt, effectively returns the number of Nodes in the linked list
           n = n.getNext();
           cnt++;
       }
       return cnt;
    }
    
    public Node get(int index) {
        int pos = 0;
        Node n = head;
        if (index == 0) {
            return head;
        } else {
            while (pos != index) { //traverse until the pos is equal to the index passed
                if (n == null) return null;
                n = n.getNext(); //traverse until the index desired is reached
                pos++;
            }
            return n;
        }
    }
    
    public void clear() {
        head = null; //set the head of the list to null
    }
    
    public boolean isEmpty() {
       if (head == null) { //if the head is null, the linked list must be completely empty
           return true; 
       }
       return false;
    }
    
    public boolean contains(char d) {
        Node n = head;
        while (n != null) {
            if (n.getData() == d) {
                return true;
            }
            n = n.getNext();
        }
        return false;
    }
    
    public String toString() {
        //this method will display both the data and the index of the data in question
        Node n = head;
        String str = "";
        int cnt = 0;
        while (n != null) {
            str = str + n.getData(); //this will return the data of each node
            n = n.getNext();
            cnt++;
        }
        return str;
    }
    
    
}
