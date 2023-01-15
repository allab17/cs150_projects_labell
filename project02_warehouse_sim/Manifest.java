import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Each truck has a manifest that represents the manifest items it must complete, (the shipments it must complete). Each manifest
 * consists of a priority queue of manifest items that will assert priority for each manifest depending on the truck's current location
 * and the source warehouse for the pickup in question as well as the age of the shipment. Shipments with ids that are smaller are thus older
 * and therefore should have higher priority. The truck object will utilize this highest priority manifest item to decide which order
 * to perform its pickups.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Manifest
{
    private Random rand = new Random();
    private ArrayList<ManifestItem> a;
    private PriorityQueue<ManifestItem> pq; //priority queue of manifest items, acts to reorder shipment pickups depending on id and distance from the truck
    private int truck_warehouse_dst;
    private Clock c;
    private Truck t;
    private int manifest_size;
    
    public Manifest(Clock c, Truck t, int manifest_size) {
        this.c = c;
        this.t = t;
        this.manifest_size = manifest_size;
        init(c.get_warehouses());
    }

    /**
     * We need a complete warehouse list in order to create a manifest, therefore in the Clock object we first call init warehouses to
     * create the warehouses. This method will create an array list of manifest items, each manifest item will be given a source and a 
     * destination chosen randomly within the array of warehouses, ensuring that no source and destination warehouse are the same. 
     * This array list of manifest items will then be added to the priority queue that must be initalized before any processing can begin.
     *
     * @param       Array of warehouses
     */
    private void init(Warehouse[] w) {
        a = new ArrayList<ManifestItem>();
        //create a random number of manifest items within a capacity set at a higher level
        //there will be at least 1 manifest items for each truck
        for (int i=0; i<=manifest_size; i++) {
            int s = 0;
            int d = 1;
            if (w.length < 0) {
                //error, just set the source and destination to s[0] and s[1] for this error state
            } else {
                //set a random source within the number of warehouses
                s = rand.nextInt(w.length);
                //set a random destination within the number of warehouses
                d = rand.nextInt(w.length);
            }
            if (w[s].equals(w[d])) { //loop longer because the source and destination warehouses are the same
                manifest_size++;
            } else {
                //the source and destination warehouses are not the same add the new manifest item to this initialization array
                a.add(new ManifestItem(w[s], w[d], t));
                //now we have the array of manifest items and this array can be added to the priority queue of manifest items at a later step
            }
        }
    }
    
    /**
     * Create a new priority queue and populate the priority queue with the array list. 
     */
    public void init_priority_queue() {
        pq = new PriorityQueue<ManifestItem>();
        for (int i=0; i<a.size(); i++) {
            pq.offer(a.get(i));
        }
    }
    
    /**
     * Get the array list of manifest items.
     * 
     * @return   ArrayList manifest items
     */
    public ArrayList<ManifestItem> get_manifest_list() {
        return a;
    }
    
    /**
     * Get the highest priority manifest item.
     * 
     * @return   ManifestItem
     */
    public ManifestItem get_highest_priority_manifest() {
        return pq.poll();
    }
    
    /**
     * Get the priority queue of manifest items.
     * 
     * @return   PriorityQueue
     */
    public PriorityQueue<ManifestItem> get_manifest_queue() {
        return pq;
    }
    
}
