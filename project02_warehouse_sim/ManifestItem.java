import java.util.Comparator;

/**
 * Each manifest item represents a shipment that must be completed by each truck. The manifest items are used within a priority queue
 * of manifest items at the manifest level. Each manifest item has a source and destination warehouse, randomly initialized using the 
 * array of warehouses. Each manifest item uses a compareTo object to compare using two pieces of data. The shipment id that corresponds to
 * the manifest item and the current distance from the Truck's current location to the source warehouse that will recieve the pickup.
 *
 * @author Alexander Labell
 * @version (a version number or a date)
 */
public class ManifestItem implements Comparable<ManifestItem>
{
    private Warehouse source;
    private Warehouse destination;
    private double truck_source_dst;
    private Shipment shipment;
    private Truck t;
    
    public ManifestItem(Warehouse s, Warehouse d, Truck t) {
        source = s;
        destination = d;
        this.t = t;
        compute_distance();
    }
    
    /**
     * Get source warehouse.
     *
     * @return       Warehouse
     */
    public Warehouse get_source() {
        return source;
    }
    
    /**
     * Get destination warehouse.
     *
     * @return       Warehouse
     */
    public Warehouse get_destination() {
        return destination;
    }
    
    /**
     * Set shipment for the manifest item.
     *
     * @param       shipment to set
     */
    public void set_shipment(Shipment s) {
        shipment = s;
    }
    
    /**
     * Get the shipment id of the shipment.
     *
     * @return        int, the shipment id
     */
    public int get_shipment_id() {
        return shipment.get_id();
    }
    
    /**
     * Get the shipment.
     *
     * @return        Shipment
     */
    public Shipment get_shipment() {
        return shipment;
    }
    
    /**
     * Compute the distance from the Truck to the pickup warehouse
     *
     */
    private void compute_distance() {
        double t_xpos = t.get_truck_xpos(); //get trucks position
        double t_ypos = t.get_truck_ypos();
        double w_xpos = source.get_xpos(); //get source warehouse position
        double w_ypos = source.get_ypos();
        double d = Math.sqrt(Math.pow(t_xpos - w_xpos, 2) + Math.pow(t_ypos - w_ypos, 2)); //compute distance using distance formula
        truck_source_dst = d;
    }
    
    /**
     * Accessor method for the distance between the truck and source warehouse.
     * 
     * @return   distance computation
     */
    private double get_dst() {
        return truck_source_dst;
    }
    
    /**
     * CompareTo method to assert priority for the priority queue of manifest items. Each priority queue will assert priority based
     * on the truck_source_dst field and the shipment id. The compareTo method uses the comparator object and accessor methods to compare
     * based on the values of these fields.
     * 
     * @return   distance computation
     */
    @Override
    public int compareTo(ManifestItem o) {
        return Comparator.comparing(ManifestItem::get_dst)
              .thenComparing(ManifestItem::get_shipment_id)
              .compare(this, o);
    }
}
