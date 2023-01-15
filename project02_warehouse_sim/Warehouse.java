import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

/**
 * Warehouse will model the warehouses within the simulation. Each warehouse has 1-3 loading docks. As the trucks arrive they will be added
 * to the queue at one of these loading docks. Here they will wait until they are next. Once chosen, they will load or unload their cargo
 * depending on whether they are delivering or not.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Warehouse implements Render
{
    private Random rand = new Random();
    private double xpos, ypos;
    private int loading_doc_quantity, width, height;
    private ArrayList<Shipment> shipments = new ArrayList<Shipment>();
    private ArrayList<LoadingDock> loading_docks = new ArrayList<LoadingDock>();
    private Clock clock;
    private StringBuilder sb_w;

    public Warehouse(int x, int y, Clock c, int w, int h, StringBuilder sb_w)
    {
        this.sb_w = sb_w;
        xpos = x;
        ypos = y;
        width = w;
        height = h;
        loading_doc_quantity = rand.nextInt(4-1) + 1;
        init_loading_docks(); //add each loading dock to array of loading docks
        clock = c;  //reference to top level Clock object
    }

    /**
     * Add a shipment to the array of shipments at the warehouse.
     *
     * @param       shipment to be added
     */
    public void add_shipment(Shipment s) {
        shipments.add(s);
    }

    /**
     * Get xpos of the warehouse.
     *
     * @return       xpos
     */
    public double get_xpos() {
        return xpos;
    }

    /**
     * Get ypos of the warehouse.
     *
     * @param       ypos
     */
    public double get_ypos() {
        return ypos;
    }

    /**
     * We have 1-3 loading docks. Each loading dock corresponds to a queue of Trucks, each truck will inspect the length of the queue at the 
     * loading dock and join the shortest queue, it would be logical for a truck driver picking-up/delivering goods to join the shortest line.
     *
     * @param       truck object to be lined up
     */
    public void line_up(Truck truck) {
        //as each Truck arrives at the source/destination warehouse to either pickup or unload shipments each truck will be added to the
        //shortest loading dock queue, 
        //get shortest loading dock queue
        LoadingDock dock_shortest_line = loading_docks.get(0);
        for (int i=0; i<loading_doc_quantity; i++) {
            //loop through each loading dock
            //determine the shortest queue
            //determine if we will get index out of bounds exception
            if ((loading_doc_quantity - i) <= 1) {
                break;
            }
            if (loading_docks.get(i+1).get_line_length() < dock_shortest_line.get_line_length()) {
                dock_shortest_line = loading_docks.get(i+1);
            }
            //if the loading docks all have the same line length, the first one will be chosen
        }
        dock_shortest_line.line_up(truck); //line up the truck at the loading dock with the shortest line
    }

    /**
     * This method is called each hour for each warehouse object, it will look at the situation at the loading docks 
     * it will use poll to remove the next Truck for each loading dock line at the warehouse and then proceed to populate the truck
     * with the shipment according to the manifest in the case of delivering or add a shipment to its own stores in the case the 
     * truck is unloading.
     *
     */
    public void process_arrivals() {
        for (int i=0; i<loading_doc_quantity; i++) {
            //loop through each loading dock at the Warehouse
            //we can only process loading docks that actually contain trucks in line
            if (!(loading_docks.get(i)).empty_line()) {
                Truck t = loading_docks.get(i).remove_truck(); //remove and get the next Truck in line for each loading dock
                if (t != null) { //check if there is a truck to process
                    if (!t.get_status()) { //if is_delivering is false
                        boolean flag = false; //in the case that we don't find the shipment, we deliver once again
                        for (int j=0; j<shipments.size(); j++) { //loop through shipments and pickup the shipment on the manifest
                            if ((t.get_curr_manifest_item().get_shipment()).equals(shipments.get(j))) { //if we have found the current shipment to be picked up
                                flag = true;
                                Shipment s = shipments.get(j); //remove the shipment from the warehouse
                                shipments.remove(j); //remove the shipment
                                t.pickup(s); //place it into the truck in question
                                if (t.get_status()) {
                                    return;
                                }
                            }
                        }
                        if (!flag) {
                            //error we could not find the shipment 
                            //this should never occur, but in case we deliver and continue
                            t.set_status(true);
                            t.set_state(0,0,1,0);
                            t.set_r(0);
                            t.set_gr(0);
                            t.set_b(255);
                            t.set_curr_manifest_item();
                            return;
                        }
                    } else { //we are delivering
                        Shipment s = t.unload();
                        if (s != null) shipments.add(s);
                        //we added the current shipment to the destination warehouse
                        //we are now picking up and must compute the next priority shipment
                    }
                }
            }
        }
        write_warehouse_data(sb_w);
    }

    private void write_warehouse_data(StringBuilder sb_w) {
        sb_w.append(clock.get_hour());
        sb_w.append(',');
        sb_w.append(xpos);
        sb_w.append(',');
        sb_w.append(ypos);
        sb_w.append(',');
        sb_w.append(loading_doc_quantity);
        sb_w.append(',');
        sb_w.append(width);
        sb_w.append(',');
        sb_w.append(height);
        sb_w.append(',');
        sb_w.append('\n');
    }

    /**
     * Initializes the loading dock.
     *
     */
    private void init_loading_docks() {
        for (int i=0; i<loading_doc_quantity; i++) {
            loading_docks.add(new LoadingDock()); //create each LoadingDock object depending on random calculation
        }
    }

    /**
     * Get warehouse width.
     * 
     * @return    width of warehouse
     *
     */
    public int get_width() {
        return width;
    }

    /**
     * Get warehouse height.
     * 
     * @return    height of warehouse
     *
     */
    public int get_height() {
        return height;
    }

    /**
     * Generates the shipments at the warehouse corresponding to a source warehouse on a manifest item.
     * Thus, loop through trucks, each truck contains a manifest that consists of each shipment, giving the shipment a source and 
     * a destination, therefore we create a shipment at the warehouse specified by the source for each manifest item.
     * We need a base shipment id to be incremented each time we create a new shipment.
     * Additionally, we call init_shipments once we know that all the trucks and manifests have been created therefore 
     * we do not call it in the constructor.
     * 
     * @param    array of truck objects
     *
     */
    public void init_shipments(Truck[] trucks) {
        for (int i=0; i<trucks.length; i++) {
            for (int j=0; j<trucks[i].get_manifest().get_manifest_list().size(); j++) {
                if (this.equals(trucks[i].get_manifest().get_manifest_list().get(j).get_source())) {
                    //we have matched the current warehouse to a source in the manifest
                    //this means we should create a shipment here because a manifest item has listed
                    //this warehouse as the source of a shipment
                    ManifestItem manifest_item = trucks[i].get_manifest().get_manifest_list().get(j);
                    int size = 1;
                    if (trucks[i].get_type() >= 3) {
                        size = rand.nextInt(4-1) + 1;
                    } else if (trucks[i].get_type() == 2) {
                        size = rand.nextInt(3-1) + 1;
                    }
                    Shipment shipment = new Shipment(clock.get_shipment_base_adr(), size);
                    shipment.set_source(manifest_item.get_source());
                    shipment.set_destination(manifest_item.get_destination());
                    //we created a new shipment so the shipment id is incremented
                    clock.inc_shipment_base_adr();
                    //add the shipment to array of shipment objects at the warehouse
                    shipments.add(shipment);
                    //the manifest item has a reference to the particular shipment it should complete
                    manifest_item.set_shipment(shipment);
                }
            }
        }    
    }

    public void draw(Graphics g) {
        Graphics2D graphicsObj = (Graphics2D) g;
        Rectangle rect = new Rectangle((int)xpos, (int)ypos, width, height);
        Color binColor1 = new Color(178, 190, 181);
        graphicsObj.setColor(binColor1);
        graphicsObj.fill(rect);
    }
}
