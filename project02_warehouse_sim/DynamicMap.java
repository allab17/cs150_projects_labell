
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;

import java.util.*;

/**
 * Render the trucks and warehouses.
 *
 * @author Alexander Labell
 * @version 1.0
 */
public class DynamicMap extends JComponent
{
    private ArrayList<Render> items;
    public DynamicMap() {
        items = new ArrayList<Render>();
    }

    public void add(Render item) {
        items.add(item);
    }
    // Paints a histogram with three bins
    @Override
    public void paintComponent(Graphics g) {
        for(Render i : items) {
            i.draw(g);
        }
    }
}
