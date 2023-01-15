
/**
 * A shipment is the physical object that must be moved by the trucks. The shipments are created at the warehouses corresponding
 * to the manifest. Each shipment has a size field 1-3, an id field and a source and destination for the shipment. The size field is
 * randomly generated and is important for the loading of the trucks, as a truck cannot fit a combined size of shipments that exceeds 
 * the load type of the truck. Each shipment has a unique id created as each shipment itself is created, this id will be used by the
 * priority queue of manifest items to assert priority as to the order of pick-ups for the manifest items.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Shipment
{
    private int size;
    private int id; //unique id code created for each shipment of a particular truck
    private Warehouse source;
    private Warehouse destination;
    
    public Shipment(int b_id, int s) {
        id = b_id + 1;
        size = s;
    }
    
    /**
     * Get id
     *
     * @return       id
     */
    public int get_id() {
        return id;
    }
    
    /**
     * Set the source warehouse for the shipment in question.
     *
     * @param       source warehouse
     */
    public void set_source(Warehouse s) {
        source = s;
    }
    
    /**
     * Set the destination warehouse for the shipment in question.
     *
     * @param       destination warehouse
     */
    public void set_destination(Warehouse d) {
        destination = d;
    }
    
    /**
     * Gets the size of the shipment.
     *
     * @return       size of the shipment
     */
    public int get_size() {
        return size;
    }
    
    /**
     * Gets the source warehouse for the shipment, assigned randomly upon creation of the manifest item.
     *
     * @return       source warehouse
     */
    public Warehouse get_source() {
        return source;
    }
    
    /**
     * Gets the destination warehouse for the shipment, assigned randomly upon creation of the manifest item.
     *
     * @return       destination warehouse
     */
    public Warehouse get_destination() {
        return destination;
    }
}
