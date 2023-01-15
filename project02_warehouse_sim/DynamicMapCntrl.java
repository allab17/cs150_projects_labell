
import javax.swing.JFrame;

/**
 * DynamicMapCntrl is used to control the dynamic map used to display the trucks and warehouses.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class DynamicMapCntrl
{
    JFrame appFrame;
    DynamicMap map;
    
    public DynamicMapCntrl() {
        System.out.println("base constructor");
    }

    public void refresh() {
        map.repaint();
    }

    public void add(Render t) {
        map.add(t);
    }

    public DynamicMapCntrl(String str, int size_x, int size_y)
    {
        appFrame = new JFrame();
        map = new DynamicMap();
        appFrame.setSize(size_x+100, size_y+100);
        appFrame.setTitle("Histogram Viewer");
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add the HistogramComponent object to the frame
        appFrame.add(map);
        // Set the frame and its contents visible
        appFrame.setVisible(true);
    }
}
