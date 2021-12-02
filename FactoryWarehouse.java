import java.util.*;
/**
 * Factory Warehouse, this class represents a warehouse which produces crates. These crates can be be used in tandem
 * with the parent class' methods in order to distribute them to other warehouses.
 * @author Cordell Bonnieux
 *
 */
public class FactoryWarehouse extends Warehouse implements Runnable {
	private ArrayList<String> destinations;
	private int produced;
	private int send;
	
	/**
	 * Class constructor
	 * @param n String - this warehouse's name
	 * @param m boolean - do the names of the crates need to match this warehouse's name to be stored here?
	 * @param p int - the number of crates produced 
	 * @param s int - the number of crates to be produced and sent
	 * @param d ArrayList<String> - a list of destination warehouses, which represent which warehouses crates are produced for
	 */
	public FactoryWarehouse(String name, ArrayList<String> destinations, int cratesToSend) {
		super(name, false);
		this.produced = 0;
		this.send = cratesToSend;
		this.destinations = destinations;
	}

	@Override
	public void run() {	
		boolean on = true;
		while (send > 0 && on) {
			int load = (send > 3) ? 3 : send;
			for (int i = 0; i < destinations.size(); i++) {
				// produce
				ArrayList<String> parcels = new ArrayList<String>();
				for (int y = 0; y < load; y++) {
					parcels.add(destinations.get(i));
					produced++;
				}
				// store and sleep for 5 sec
				//  add lock
				try {
					this.deliver(parcels);
					send -= load;
					Thread.sleep(5000);
				} catch (InterruptedException e)  {
					System.out.println(e.getMessage());
					System.out.println("Thread shutting down");
					on = false;
					i = destinations.size(); // exits production all together, omit to continue with other destinations
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				} finally {
					// release lock
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("name: %s, number of stored crates: %d, number of crates produced: %d", getName(), getCrateCount(), produced);
	}
}