import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Warehouse class, representing a warehouse which contains crates who have destinations. 
 * Warehouses can dispatch crates by having delivery trucks call the pickUp method, and 
 * deliveries can be received by Trucks using the deliver method.
 * @author Cordell Bonnieux
 */
public class Warehouse {
	private String name;
	private ArrayList<String> crates;
	private boolean matchName;
	private int errors;
	private Lock lock;
	
	/**
	 * Warehouse constructor
	 * @param n String name of Warehouse instance
	 * @param m boolean if crate name must match warehouse name
	 */
	public Warehouse(String n, boolean m) {
		if (n == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		this.name = n;
		this.matchName = m;
		this.crates = new ArrayList<String>();
		this.errors = 0;
		this.lock = new ReentrantLock();
	}
	
	/**
	 * Pick Up
	 * @param destination String the destination of the crate
	 * @param max int the max number of pickups
	 * @return ArrayList of Strings, the outgoing crates
	 * @throws InterruptedException if Thread sleep is interrupted
	 */
	public ArrayList<String> pickUp(String destination, int max) throws InterruptedException {
		if (destination == null) {
			throw new NullPointerException("destination cannot be null");
		} /*else if (!crates.contains(destination)) {
			throw new IllegalArgumentException("destination does not exist");
		} */else if (max < 1) {
			throw new IllegalArgumentException("integer parameter cannot be less than 1");
		}
		
		// wait until enough crates are ready
		boolean ready = false;
		while (!ready) {
			int order = 0;
			for (int i = 0; i < crates.size(); i++) {
				if (crates.get(i) == destination) {
					order++;
				}
			}
			if (order >= max) {
				ready = true;
			} else {
				Thread.sleep(500);
			}
		}
		
		// local variables
		ArrayList<String> outgoing = new ArrayList<String>();
		int removed = 0;
		int total = crates.size();
		
		// remove crates
		for (int i = 0; i < total; i++) {
			if (removed < max) {
				if (crates.get(i) == destination) {
					lock.lock();
					try {
						outgoing.add(crates.get(i));
						crates.remove(i);
						i--;
						total--;
						removed++;
					} finally {
						lock.unlock();
					}
				}	
			}
		}
		return outgoing;
	}
	
	/**
	 * Deliver
	 * @param delivery ArrayList of Strings, representing incoming crates
	 */
	public void deliver(ArrayList<String> delivery) {
		if (delivery == null) {
			throw new NullPointerException("delivery array list cannot be null");
		} else if (delivery.contains(null)) {
			throw new NullPointerException("delivery array list cannot contain null");
		}
		
		for (int i = 0; i < delivery.size(); i++) {
			if (matchName && name != delivery.get(i)) {
				errors++;
			}
			crates.add(delivery.get(i));
		}
	}
	
	/**
	 * Get Name 
	 * @return Warehouse name String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *  Get Crate Count
	 * @return number of crates int
	 */
	public int getCrateCount() {
		return crates.size();
	}
	
	/**
	 * to String
	 */
	@Override
	public String toString() {
		return "name: " + name + ", number of crates: " + getCrateCount() + ", number of errors: " + errors;
	}
}
