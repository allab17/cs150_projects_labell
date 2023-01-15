import java.util.Random;

/**
 * Clock will intialize warehouses, trucks, shipments, the priority queue of manifest items. It will count the hours, each hour
 * all the trucks' action methods are called to either move toward a source, destination, pickup or unload accordingly. The action method
 * of each warehouse will also be called to handle the situation at the loading docks to either accept shipments that are being unloaded
 * or pickup shipments for the current truck at the loading docks.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Clock
{
    private Random rand = new Random();
    private int hour;
    private int num_warehouses;
    private int warehouse_width;
    private int warehouse_height;
    private int num_trucks;
    private int manifest_size;
    private int x_constraint_min = 1;
    private int y_constraint_min = 1;
    private int x_constraint_max;
    private int y_constraint_max;
    private int shipment_base_adr;  //'base address' to generate shipment ids
    private Warehouse[] warehouses;
    private Truck[] trucks;
    private DynamicMapCntrl dmc;
    private StringBuilder sb_t;
    private StringBuilder sb_w;
    
    public Clock(DynamicMapCntrl dmc, StringBuilder sb_g, StringBuilder sb_t, StringBuilder sb_w, int num_warehouses, int warehouse_width, int warehouse_height, int num_trucks, int manifest_size, int x_constraint_max, int y_constraint_max) {
        this.num_warehouses = num_warehouses;
        this.warehouse_width = warehouse_width;
        this.warehouse_height = warehouse_height;
        this.num_trucks = num_trucks;
        this.manifest_size = manifest_size;
        this.x_constraint_max = x_constraint_max;
        this.y_constraint_max = y_constraint_max;
        this.dmc = dmc;
        this.sb_t = sb_t;
        this.sb_w = sb_w;
        hour = 0;
        shipment_base_adr = 0;
        warehouses = new Warehouse[num_warehouses];
        trucks = new Truck[num_trucks];
        init_warehouses(); //initalizes warehouses, each is given random x, y location and a reference to this clock object
        init_trucks(); //initalizes trucks, each is given random x, y location and a reference to the clock object
        init_shipments(trucks); //intializes shipments
        init_priority_queue();
        init_highest_priority_manifest();
        csv_general_data(sb_g);
    }
    
    /**
     *  Method for adding general data to the CSV file
     *
     * @param   StringBuilder object
     */
    private void csv_general_data(StringBuilder sb_g) {
        sb_g.append(String.valueOf(num_warehouses));
        sb_g.append(',');
        sb_g.append(String.valueOf(num_trucks));
        sb_g.append(',');
        sb_g.append(String.valueOf(manifest_size));
        sb_g.append(',');
        sb_g.append('\n');
    }
    
    /**
     * Checks if all trucks have picked up and delivered all items in their manifest.
     *
     * @return   true or false whether all trucks have completed their pickups and deliveries
     */
    private boolean check_done(Truck[] trucks) {
        boolean flag = true;
        for (int i=0; i<trucks.length; i++) {
            if (!trucks[i].get_manifest_status()) { //if any truck is not done we will not assert done
                flag = false;
            }
            //if we get here all trucks manifest status is true, therefore all trucks have picked up and delivered all items in their manifest
        }
        return flag;
    }
    
    public int get_hour() {
        return hour;
    }

    /**
     * Increments the hours, looping through all trucks and warehouses and calling the action method every hour.
     * If check_done evaluates to true we stop counting hours.
     *
     */
    public void count_hour() {
        boolean done = false;
        while (!done) {
            //if each truck's manifest is complete we are done
            if (check_done(trucks)) {
                done = true;
                System.out.println("Simulation complete");
                return;
            }
            //loop through each Truck calling the action method of each Truck each hour
            for (int i=0; i<trucks.length; i++) {
                trucks[i].action();
            }
            //loop through each Warehouse calling method to process trucks at the loading docks of each Warehouse
            for (int i=0; i<warehouses.length; i++) {
                warehouses[i].process_arrivals();
            }
            hour++; //increment hour
        }
    }

    /**
     * Get trucks
     * 
     * @return     array of trucks
     */
    public Truck[] get_trucks() {
        return trucks;
    }

    /**
     * Get warehouses
     * 
     * @return     array of warehouses
     */
    public Warehouse[] get_warehouses() {
        return warehouses;
    }

    /**
     * Initialize the trucks array, for each truck give it a random position within the world, a reference to the clock object and dynamic map
     * so that the truck can be refreshed after the move method is called.
     * 
     */
    private void init_trucks() {
        for (int i=0; i<num_trucks; i++) {
            int x = rand.nextInt(x_constraint_max - x_constraint_min) + x_constraint_min;
            int y = rand.nextInt(y_constraint_max - y_constraint_min) + y_constraint_min;
            trucks[i] = new Truck(x, y, this, dmc, sb_t, manifest_size);
        }
    }
    
    /**
     * Initialize the current manifest item for each truck, this will act as the first pickup for each truck.
     * 
     */
    private void init_highest_priority_manifest() {
        for (int i=0; i<num_trucks; i++) {
            trucks[i].set_curr_manifest_item();
        }
    }

    /**
     * Initialize the warehouses array, for each warehouse give it a random position within the world and give each warehouse
     * a reference to the clock object as well as the width and height of the warehouse.
     * 
     */
    private void init_warehouses() {
        for (int i=0; i<num_warehouses; i++) {
            int x = rand.nextInt(x_constraint_max - x_constraint_min) + x_constraint_min;
            int y = rand.nextInt(y_constraint_max - y_constraint_min) + y_constraint_min; 
            warehouses[i] = new Warehouse(x, y, this, warehouse_width, warehouse_height, sb_w);
        }
    }   

    /**
     * Initialize the shipments. Generate the shipments at each warehouse depending on the manifest within each truck.
     * 
     */
    private void init_shipments(Truck[] trucks) {
        for (int i=0; i<warehouses.length; i++) {
            warehouses[i].init_shipments(trucks);
        }
    }

    /**
     * Initialize the priority queue of manifest items by copying the array of manifest items within the manifest for each truck to a 
     * priority queue that is created for each truck.
     * 
     */
    private void init_priority_queue() {
        //loop through each truck and for each truck copy its array to 
        //the priority queue
        for (int i=0; i<num_trucks; i++) {
            trucks[i].get_manifest().init_priority_queue();
        }
    }

    /**
     * Increment the base address of shipment objects as they are created.
     * 
     */
    public void inc_shipment_base_adr() {
        shipment_base_adr++;
    }

    /**
     * Get the base address of shipment objects.
     * 
     * @return shipment base address
     */
    public int get_shipment_base_adr() {
        return shipment_base_adr;
    }

}
