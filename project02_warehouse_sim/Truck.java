
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import java.util.Queue;
import java.util.Random;
import java.util.LinkedList;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The Truck class represents Truck objects within the simulation. Each truck will move from warehouse to warehouse, picking up items
 * depending on a manifest randomly created for each truck. Each manifest has a priority queue of manifest items that match shipments 
 * created in each warehouse. The trucks will then perform pickups at the next highest priority manifest item. Each truck will move toward
 * the source warehouse for that manifest item, once arrived, the truck will join the shortest queue at one of the LoadingDock(s).
 * The truck will wait, with each processed truck being removed until it is next. For picking up, this truck will then find the shipment
 * and add it to a Stack of shipments that represent the inside of the Truck. If the next shipment to pickup
 * is too big for the current truck size: a temporary arraylist is created to hold the manifest items while we look to the next
 * prority manifest item and see if it will fit. If it does not fit we will keep trying until we have exhausted the manifest. At that point 
 * we deliver despite the truck not being completely full. Once the truck is full we set is_delivering to true, and add the unused manifest items
 * in the temp array back into the priority queue. Then that shipment will be delivered to the destination that 
 * was randomly chosen for that shipment. Once arrived at the destination the shipment is removed from the stack and added to the array of shipments
 * at each Warehouse. Once all the stack of shipments within the Truck is exhausted, is_delivering will be set to false and we will get the next
 * priority manifest item and continue with moving toward it... picking up... delivering... etc until all manifest items within the manifest have 
 * been picked up and delivered. At this point we are done and we exit simulation.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Truck implements Schedule, Render
{
    private double xpos, ypos;
    private int type, speed; //1, 2, 3, 4, or 5 loads of cargo
    private Random rand = new Random();
    private Manifest travel_manifest; 
    private Stack<Shipment> shipments = new Stack<Shipment>(); //stack acts as the interior of the truck
    private boolean is_delivering = false;
    //to view data flow//////////////////////////
    // 1: true   2: false
    int moving_source = 1;
    int moving_destination = 0; 
    int picking_up = 0;
    int unloading = 0;
    /////////////////////////////////////////////
    private boolean pickup_status = false;
    private boolean manifest_complete = false;
    private ManifestItem curr_manifest_item;
    private int shipment_size_cnt = 0;
    private DynamicMapCntrl dmc;
    private ArrayList<ManifestItem> tempa;
    private int r, gr, b; //the trucks are blue when delivering and green when finished
    private StringBuilder sb_t;
    private Clock c;
    //when we create the travel manifest for the current Truck object
    //we must give the manifest the Array of Warehouses, this is because
    //each manifest item needs a source as well as a destination
    public Truck(int x, int y, Clock c, DynamicMapCntrl dmc, StringBuilder sb_t, int manifest_size) {
        this.c = c;
        this.dmc = dmc;
        this.sb_t = sb_t;
        tempa = new ArrayList<ManifestItem>(); //create temp array here, each pickup this temp array will persist to cleanup any manifest items that are too large to fit into the Truck
        r = 255;
        gr = 15;
        b = 15;
        xpos = x;
        ypos = y;
        type = rand.nextInt(6-1) + 1; //type of truck determined
        set_speed(); //speed of each Truck calculated depending on the load spec
        travel_manifest = new Manifest(c, this, manifest_size); //creates the manifest for each truck passing a reference to clock and the Truck itself
    }
    
    public void set_state(int ms, int md, int p, int u ) {
        moving_source = ms;
        moving_destination = md; 
        picking_up = p;
        unloading = u;
    }

    /**
     * The action method is called each hour. It will call the move method which moves toward the source warehouse 
     * corresponding to the current manifest item if we are delivering. Otherwise it will move toward the destination warehouse.
     * The method uses dmc.refresh() to refresh the screen to see the Truck's position being updated and Thread.sleep(5) is used to 
     * wait for ~10 ms to view the Trucks moving on the screen.
     *
     */
    @Override
    public void action() {
        move();   
        dmc.refresh();
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
        }
        write_truck_data(sb_t); 
    }

    private void write_truck_data(StringBuilder sb_t) {
        sb_t.append(c.get_hour());
        sb_t.append(',');
        sb_t.append(xpos);
        sb_t.append(',');
        sb_t.append(ypos);
        sb_t.append(',');
        sb_t.append(type);
        sb_t.append(',');
        sb_t.append(speed);
        sb_t.append(',');
        sb_t.append(moving_source);
        sb_t.append(',');
        sb_t.append(moving_destination);
        sb_t.append(',');
        sb_t.append(picking_up);
        sb_t.append(',');
        sb_t.append(unloading);
        sb_t.append(',');
        sb_t.append(pickup_status);
        sb_t.append(',');
        sb_t.append(manifest_complete);
        sb_t.append(',');
        sb_t.append('\n');
    }

    /**
     * Get stack of shipments within the truck.
     *
     * @return  Stack
     */
    public Stack<Shipment> get_shipments() {
        return shipments;
    }

    /**
     * Sets the current manifest item being picked-up depending on the highest priority manifest item within the manifest.
     *
     */
    public boolean set_curr_manifest_item() {
        curr_manifest_item = travel_manifest.get_highest_priority_manifest();
        if (curr_manifest_item != null) return true;
        return false;
    }

    /**
     * Return the type of Truck, whether it is 1-load, 2-load ... 5-load 
     *
     * @return    the type of truck 1-5
     */
    public int get_type() {
        return type;
    }

    /**
     * It will get the current manifest item being processed within the simulation
     * This field is utilized exclusively for pick-ups
     *
     * @return      current manifest item
     */
    public ManifestItem get_curr_manifest_item() {
        return curr_manifest_item;
    }

    /**
     * It will set the speed of the Truck depending on the type of Truck. 5-load trucks move 1mph, 4-load 2mph and so on.
     *
     */
    private void set_speed() {
        switch (type) {
            case 5 : speed = 1; break;
            case 4 : speed = 2; break;
            case 3 : speed = 3; break;
            case 2 : speed = 4; break;
            case 1 : speed = 5; break;
            default : speed = 0;
        }
    }

    public void set_r(int c) {
        r = c;
    }

    public void set_gr(int c) {
        gr = c;
    }

    public void set_b(int c) {
        b = c;
    }

    /**
     * It will get the manifest.
     *
     * @return      manifest
     */
    public Manifest get_manifest() {
        return travel_manifest;
    }

    /**
     * It will get the status of the Truck, whether it is delivering or picking up.
     *
     * @return      true or false depending on is_delvering
     */
    public boolean get_status() {
        return is_delivering;
    }

    /**
     * It will set set the status of the truck.
     */
    public void set_status(boolean s) {
        is_delivering = s;
    }

    /**
     * it will get the manifest status of the Truck, whether it has completed all pickups and deliveries.
     *
     * @return   true or false whether the manifest is complete
     */
    public boolean get_manifest_status() {
        return manifest_complete;
    }

    /**
     * Get the truck's xpos
     * 
     * @return      the trucks xpos
     *
     */
    public double get_truck_xpos() {
        return xpos;
    }

    /**
     * Get the truck's ypos
     * 
     * @return      the trucks ypos
     */
    public double get_truck_ypos() {
        return ypos;
    }

    /**
     * The move method is called each hour by the action method. If the truck is not delivering 
     * it will move toward the source Warehouse corresponding to the current
     * manifest item otherwise it will move toward the destination warehouse corresponding to each shipment for the stack of shipments
     * within each truck.
     *
     */
    public void move() {
        if (manifest_complete) return;
        if (is_delivering) { 
            move_destination(); //we are delivering, move towared the destination warehouse
        } else {
            move_source(); //we are picking up, move toward the source warehouse of the current manifest item
        }
    }

    /**
     * Computes whether the truck has arrived at its source or destination warehouse depending on a range.
     * The range depends on the 1/2 the width and height of the warehouse. If the x difference and y difference betwen the warehouse
     * and truck are within this range, the truck has arrived.
     *
     */
    private boolean arrived(double x_difference, double y_difference, Warehouse s) {
        if (((x_difference < ((double)0.5*s.get_width())) && (x_difference > ((double)-0.5*s.get_width()))) && ((y_difference < ((double)0.5*s.get_height())) && (y_difference > ((double)-0.5*s.get_height())))) {
            return true;
        }
        return false;
    }

    /**
     * We check if the stack of shipments is empty, if so we have finished delivering and we set is_delivering to false and
     * return to pickup next priority manifest. In the case that the stack of shipments is empty and the truck is done picking up we
     * set manifest_complete to true and the truck is done with the simulation.
     * Otherwise, we get the top of the shipment stack within the truck. This is the destination warehouse. We check if we have
     * arrived at the destination. The arrived method is used to judge this. Simply put, we determine if the x_difference and y_difference 
     * are within a range that is acceptable for an arrival. In the case that we have arrived we now add this truck to one of potentially 
     * 1-3 loading docks at the warehouse. We add this truck to the loading dock with the shortest line (see Warehouse call line_up method for functionality). 
     * In the case that we have not arrived, we move toward the destination warehouse by finding the x difference and y difference 
     * from the destination to the truck's current position, we scale the x difference and y difference for the movement of the 
     * truck in the x and y for each iteration of move() to move the truck closer and closer to its destination.
     *
     */
    public void move_destination() {
        //to get the destination warehouse, we simply get the top
        //of our shipment stack and deliver the shipment at the top 
        //of the stack, then remove that shipment from the stack once it is delivered
        //when our truck is empty (shipment queue isEmpty) we set is_delivering to false
        //and then we will go for a pickup next iteration of action
        if (shipments.isEmpty() && pickup_status) {
            //if we have finished pickup on all items on manifest and delivered the last manifest item: take
            manifest_complete = true;
            set_state(0,0,0,0);
            r = 84; //change the color of the Truck to black in the case that the Truck finishes picking up
            gr = 156;
            b = 48;
        } else if (shipments.isEmpty()) {
            is_delivering = false;
            set_state(1,0,0,0);
            r = 255;
            gr = 15;
            b = 15;
            set_curr_manifest_item();
            return;
            //we are done delivering, now we will be picking up so we get the next priority manifest item from the list and set it as curr_manifest_item
        }
        Shipment shipment = null;
        if (!shipments.isEmpty()) {
            shipment = shipments.peek(); //peek the top shipment from the stack of shipments
        }
        if (shipment == null) return;
        double d_xpos = shipment.get_destination().get_xpos(); //get the current position of the destination warehouse of the top shipment
        double d_ypos = shipment.get_destination().get_ypos(); 
        double x_difference = d_xpos - xpos;
        double y_difference = d_ypos - ypos;
        if (arrived(x_difference, y_difference, shipment.get_destination())) { //the truck has reached the warehouse
            set_state(0,0,0,1);
            shipment.get_destination().line_up(this); //add to the queue waiting at the warehouse
        } else {
            xpos += (x_difference * speed) / 40;
            ypos += (y_difference * speed) / 40;
        }

    }

    /**
     * We check if the priority queue of manifest items isEmpty(). In the case that it is the situation is one of two.
     * Either we have picked up all items, or we have exhausted all possiblities of shipments fitting into the truck depending 
     * on the size and load-type of the truck in question. We know the situation is the latter if the priority queue is empty
     * AND the temporary array that holds shipments that could not fit into the truck is not empty. If both of these are true than
     * we are not done because there are still manifest items to be completed, so we add the manifest items from the array back into
     * the priority queue and continue processing.
     * In the latter case, if the temp array is empty and the priority queue is empty, we are done, picking up. We set is_delivering to true
     * to finish dropping off any last shipments.
     * 
     * Otherwise, we get the current manifest item from the highest priority manifest item within the priority queue.
     * This is the source warehouse. We get the xpos and ypos of the source warehouse. Next, the x difference
     * and y difference between the source warehouse and truck is computed. We check if we have
     * arrived at the destination. In the case that we have arrived we now add this truck to one of potentially 
     * 1-3 loading docks at the warehouse. We add this truck to the loading dock with the shortest line (see Warehouse call 
     * line_up method for functionality). In the case that we have not arrived, we move toward the source warehouse by 
     * finding the x difference and y difference from the source to the truck's current position, we scale the x difference 
     * and y difference for the movement of the truck in the x and y for each iteration of move() to move the truck closer and 
     * closer to its source.
     *
     */
    public void move_source() {
        //check if truck is picking up or dropping off
        //get the current manifest item
        //if the truck is full we can no longer pickup any more shipments and we set is_delivering to true and return from the method
        if (travel_manifest.get_manifest_queue().isEmpty() && !tempa.isEmpty()) { //if the priority queue is empty, we will get a null item, the priority queue should only be empty in the case that all pickups have been completed for the Truck
            //temporary queue of failed shipment adds, if it contains items they must be added back and the pickups performed still
            //these manifest items still need to be processed, we are not done picking up
            for (int i=0; i<tempa.size(); i++) {
                travel_manifest.get_manifest_queue().offer(tempa.remove(i));
            }
            tempa.clear();
            set_curr_manifest_item();
        } else if (curr_manifest_item == null || (travel_manifest.get_manifest_queue().isEmpty() && tempa.isEmpty())) {
            is_delivering = true; //ensure last item is delivered
            set_state(0,1,0,0);
            r = 0;
            gr = 0;
            b = 255;
            pickup_status = true; //indicates that we have finished picking up
            return;
        }

        //otherwise if the truck is not full get the highest priority meanifest item and remove it from the priority queue and set it as the current shipment being processed
        double s_xpos = curr_manifest_item.get_source().get_xpos(); //get warehouse source position from manifest item
        double s_ypos = curr_manifest_item.get_source().get_ypos();
        double x_difference = s_xpos - xpos; //compute movement required in x to get to the source from truck location
        double y_difference = s_ypos - ypos; //compute movement required in y to get to the source from truck location
        //now we increment/decrement the trucks position until we
        //reach the source warehouse, once the source warehouse is reached we add it to one of the loading dock queues at the warehouse

        if (arrived(x_difference, y_difference, curr_manifest_item.get_source())) { //the truck has reached the source warehouse 
            set_state(0,0,1,0);
            curr_manifest_item.get_source().line_up(this); //add to one of the loading dock queues waiting at the warehouse
        } else {
            xpos += (x_difference * speed) / 40; //the speed will scale with different types of trucks
            ypos += (y_difference * speed) / 40;
        }
    }

    //each truck that arrives at the warehouse will have a field
    //that indicates if it is picking up or dropping off called is_delivering
    //if the truck is picking up it will be added to the queue of other trucks
    // and then wait its turn, once its turn comes it will then wait for
    // an empty loading dock, once a loading dock is available, we will
    // loop through all shipments until we match the shipment to the manifest
    //shipment in question. now we add that shipment to the truck
    // now we check if the shipment on the manifest can fit into the truck
    //

    /**
     * Each pickup will be stored into a stack within the truck the truck keeps a count of its total load size
     * and increments as we add shipments to it. If the count is greater than the type of truck, the shipment could not fit.
     * Therefore we add the manifest item corresponding to the shipment to a temporary array list. Now we will get the next highest priority
     * manifest item, this process will continue until we find one that can fit, for every shipment that doesn't fit we add
     * its manifest item to the temp array, if in the entire manifest none fit, than we simply deliver what we can.
     * In the case that we eventually find a fit, then we must add the manifest items in the temporary array
     * back to the main priority queue. In the case that we exhaust all possiblities and the temp array contains manifest items
     * we accept that the truck will deliver with a non-full load.
     * 
     * @param s   the shipment picked up at the warehouse in accordance with the manifest
     *
     */
    public void pickup(Shipment s) {
        if (s == null) {
            //if there is no shipment there is an error in the pickup, we will now deliver
            //this should never happen, but we have fallback
            set_curr_manifest_item();
            is_delivering = true;
            //set state to moving toward destination because we are now delivering
            set_state(0,1,0,0);
            r = 0;
            gr = 0;
            b = 255;
        }
        shipment_size_cnt += s.get_size();
        if(shipment_size_cnt > type) {
            //subtract the shipment size that could not fit
            shipment_size_cnt -= s.get_size();
            //if we fail to build a shipment that can fit after going through the entire manifest we know that the priority
            //queue will be empty and the temp array will not be empty, thus we will 
            //deliver what we have and add the manifest items back to the priority queue
            tempa.add(curr_manifest_item);
            if (travel_manifest.get_manifest_queue().isEmpty() && !tempa.isEmpty()) { //we have gone through the entire manifest and our temporary array contains our failures
                //we have exhausted all possiblities on the manifest, there is nothing
                //further that will fit into the truck
                //put the failed attempts back into the priority queue and deliver what is in the truck
                for (int i=0; i<tempa.size(); i++) {
                    travel_manifest.get_manifest_queue().offer(tempa.remove(i));
                }
                shipment_size_cnt = 0;
                is_delivering = true;
                //set state to moving toward destination because we are now delivering
                set_state(0,1,0,0);
                r = 0;
                gr = 0;
                b = 255;
                tempa.clear(); //clear the temp array because we are done with this pickup
                //set next priority manifest item for next pickup
                set_curr_manifest_item();
                return; //exit method
            }
            set_curr_manifest_item(); //get the next priority manifest item
        } else if (type == shipment_size_cnt) {
            shipments.add(s); //the type of truck can fit the shipment so we add it
            //if the shipment size count is equal to the load type the truck is full and the truck will now be delivering
            //we have filled the current truck, now we must add back the shipments that would not fit into the truck
            if (!tempa.isEmpty()) { //if the temporary array contains manifest items they must be added back to the pq
                for (int i=0; i<tempa.size(); i++) {
                    travel_manifest.get_manifest_queue().offer(tempa.remove(i));
                }
                tempa.clear();
                //clear the temp array because we have already added back the items
                //now on the next pickup the system will have a new temp array to use to store manifests temporarily
            }
            //set the shipment_size_cnt back to zero because we have finished picking up and must start the next pickup back at zero 
            shipment_size_cnt = 0;
            is_delivering = true;
            //set state to moving destination because we are now delivering
            set_state(0,1,0,0);
            r = 0;
            gr = 0;
            b = 255;
            return;
            //we have finished picking up because the Truck is full
        } else { //we did not fill the truck completely, we add the shipment to the truck
            shipments.add(s); 
            set_curr_manifest_item(); //get the next priority pickup
            set_state(1,0,0,0);
        } //in the case that the size of the shipment added did not exceed the type of truck we simply add it to the Stack within the Truck
    }

    /**
     * We unload the truck by removing the top of the shipments stack.
     * 
     * @return      the shipment to be added to the warehouse stores
     *
     */
    public Shipment unload() {
        //deliveries in last-in first-out fashion
        //we want to pop the top of the stack of shipments within the 
        //Truck because we are unloading
        if (shipments.isEmpty()) {
            return null;
        }
        Shipment s = shipments.pop();
        return s; //this will be added to the warehouse stock array
    }

    public void draw(Graphics g) {
        Graphics2D graphicsObj = (Graphics2D) g;
        Rectangle rect = new Rectangle((int)xpos, (int)ypos, type*2, type*2);
        Color binColor1 = new Color(r, gr, b);
        graphicsObj.setColor(binColor1);
        graphicsObj.fill(rect);
    }

    @Override
    public int log_status() {
        return 0;
    }
}
