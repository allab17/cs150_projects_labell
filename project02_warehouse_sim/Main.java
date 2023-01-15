import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * The main method is responsible for running the program and generating data to analyze the performance of the program.
 *
 *  Note: To run call simulate() method, this will generate four consecutive random simulations and write the data for those simulations
 *  to four csv files for data analysis.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class Main
{
    private static Random rand = new Random();
    /////////////////////////////////////////////
    private static int num_warehouses;
    private static int warehouse_width = 30;
    private static int warehouse_height = 20;
    private static int num_trucks;
    private static int manifest_size;
    //////////////////////////////////////////////
    private static int x_constraint_max = 500;
    private static int y_constraint_max = 600;
    
    //CSV fields
    private static StringBuilder sb_g;
    private static StringBuilder sb_t;
    private static StringBuilder sb_w;
    
    public static void simulate() {
        //four simulations
        for (int i=0; i<4; i++) {
            Main m = new Main();
            m.main(i);
        }
    }

    public static void main(int n) {
        sb_g = new StringBuilder(); //general data
        sb_t = new StringBuilder(); //truck data
        sb_w = new StringBuilder(); //warehouse data
        num_warehouses = rand.nextInt(40-2)+2; //2-40 range
        num_trucks = rand.nextInt(20-1)+1;  //1-20 range
        manifest_size = rand.nextInt(20 - 1) + 1; //1-20 range
        PrintWriter writer = null;
        try {
            File file = new File("sim_data " + n + ".csv");
            writer = new PrintWriter(file);
    
            sb_g.append("num warehouses");
            sb_g.append(',');
            sb_g.append("num trucks");
            sb_g.append(',');
            sb_g.append("manifest size"); //max number of manifest items that can be generated per manifest
            sb_g.append(',');
            sb_g.append('\n');
            
            sb_t.append("Hour");
            sb_t.append(',');
            sb_t.append("xPos");
            sb_t.append(',');
            sb_t.append("yPos");
            sb_t.append(',');
            sb_t.append("type");
            sb_t.append(',');
            sb_t.append("speed");
            sb_t.append(',');
            sb_t.append("moving_source");
            sb_t.append(',');
            sb_t.append("moving_destination");
            sb_t.append(',');
            sb_t.append("picking_up");
            sb_t.append(',');
            sb_t.append("unloading");
            sb_t.append(',');
            sb_t.append("pickup_status");
            sb_t.append(',');
            sb_t.append("manifest_complete");
            sb_t.append(',');
            sb_t.append('\n');
            
            sb_w.append("Hour");
            sb_w.append(',');
            sb_w.append("xPos");
            sb_w.append(',');
            sb_w.append("yPos");
            sb_w.append(',');
            sb_w.append("loading_dock_quantity");
            sb_w.append(',');
            sb_w.append("width");
            sb_w.append(',');
            sb_w.append("height");
            sb_w.append(',');
            sb_w.append('\n');
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        DynamicMapCntrl dmc = new DynamicMapCntrl("Dynamic Map", x_constraint_max, y_constraint_max);
        Clock c = new Clock(dmc, sb_g, sb_t, sb_w, num_warehouses, warehouse_width, warehouse_height, num_trucks, manifest_size, x_constraint_max, y_constraint_max);
        for (int i=0; i<c.get_trucks().length; i++) {
            dmc.add(c.get_trucks()[i]);
        }
        for (int i=0; i<c.get_warehouses().length; i++) {
            dmc.add(c.get_warehouses()[i]);
        }
        c.count_hour(); //calls the action method of each Truck until all Trucks have picked-up each item on their manifests and delivered all pickups
        writer.write(sb_g.toString());
        writer.write(sb_t.toString());
        writer.write(sb_w.toString());
    }
}
