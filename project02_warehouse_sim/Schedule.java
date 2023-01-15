
/**
 *
 * @author Alexander Labell
 * @version 1.0
 */
public interface Schedule
{
    // Called each hour, allowing the object to perform an action.
    public void action();
    // Will store the object’s current information into a log file.
    public int log_status();
}
