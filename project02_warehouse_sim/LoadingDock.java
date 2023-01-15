import java.util.Queue;
import java.util.LinkedList;

/**
 * Write a description of class LoadingDock here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class LoadingDock
{
    //empty field is true when a Truck is not loading/unloading at the LoadingDock
    private boolean is_empty;
    
    private Queue<Truck> tq = new LinkedList<Truck>();
    
    public void LoadingDock() {
        is_empty = true; //all loading docks are initially empty
    }
    
    public void line_up(Truck t) {
        tq.offer(t);
    }
    
    public Truck remove_truck() {
        return tq.poll();
    }
    
    public int get_line_length() {
        return tq.size();
    }
    
    public Queue<Truck> get_queue() {
        return tq;
    }
    
    public boolean empty_line() {
        return tq.isEmpty();
    }
    
}
