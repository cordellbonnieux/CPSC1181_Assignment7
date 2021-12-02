import java.util.*;
import java.util.concurrent.locks.Condition;
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
	private Condition busy;
	
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
		this.busy = lock.newCondition();
	}
	
	/**
	 * Pick Up
	 * This method removes param max number of crates from this.crates
	 * and returns them. The method will wait to finish until param max
	 * number of param destination crates are inside this.crates.
	 * @param destination String the destination of the crate
	 * @param max int the max number of pickups
	 * @return ArrayList of Strings, the outgoing crates
	 * @throws InterruptedException if Thread sleep is interrupted
	 */
	public ArrayList<String> pickUp(String destination, int max) throws InterruptedException {
		if (destination == null) {
			throw new NullPointerException("destination cannot be null");
		} else if (max < 1) {
			throw new IllegalArgumentException("integer parameter cannot be less than 1");
		}
		lock.lock();	
		ArrayList<String> outgoing = new ArrayList<String>();
		boolean waiting = true;
		try {
			while (waiting) {
				int ready = 0;
				for (int i = 0; i < crates.size(); i++ ) {
					if (crates.get(i).contentEquals(destination) && ready < max) {
						ready++;
						outgoing.add(crates.get(i));
					}
				}
				if (ready == max) {
					waiting = false;
					crates.removeAll(outgoing);
				} else {
					outgoing = new ArrayList<String>();
					busy.await();
				}
			}
		} catch (InterruptedException e) {
			System.out.println(destination + "'s pickup thread is shutting down.");
			Thread.currentThread().interrupt();
			
		} finally {
			lock.unlock();
		}
		return outgoing;
	}
	
	/**
	 * Deliver
	 * Receives a delivery of crates, the param string's contents are stored
	 * into this.crates data member.
	 * @param delivery ArrayList of Strings, representing incoming crates
	 */
	public void deliver(ArrayList<String> delivery) {
		if (delivery.equals(null)) {
			throw new NullPointerException("delivery array list cannot be null");
		} else if (delivery.contains(null)) {
			throw new NullPointerException("delivery array list cannot contain null");
		}
		for (int i = 0; i < delivery.size(); i++) {
			if (matchName && name != delivery.get(i)) {
				errors++;
			}
			lock.lock();
			try {
				crates.add(delivery.get(i));
				busy.signalAll();
			} finally {
				lock.unlock();
			}
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
