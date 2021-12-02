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
	protected Lock lock;
	
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
		} else if (max < 1) {
			throw new IllegalArgumentException("integer parameter cannot be less than 1");
		}
		
		//System.out.println("first step! " + this.getName());
		
		// wait until enough crates are ready
//		boolean ready = false;
//		while (!ready) {
//			int order = 0;
//			for (int i = 0; i < crates.size(); i++) {
//				if (crates.get(i) == destination) {
//					order++;
//				}
//			}
//			if (order >= max) {
//				ready = true;
//			} else {
//				lock.lock();
//				try {
//					Thread.sleep(500);
//				} finally {
//					lock.unlock();
//				}
//				
//			}
//		}
		
		System.out.println("first step! " + this.getName());
		
		// local variables
		ArrayList<String> outgoing = new ArrayList<String>();
		ArrayList<Integer> outIndex = new ArrayList<Integer>();
		
		int removed = 0;
		boolean waiting = true;
		
		while (waiting) {
			int ready = 0;
			lock.lock();
			try {
				for (int i = 0; i < crates.size(); i++ ) {
					if (crates.get(i) == destination) {
						ready++;
						outIndex.add(i);
					}
				}
			} finally {
				lock.unlock();
			}

			if (ready >= max) {
				waiting = false;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				
			}
		}
		
		lock.lock();
		try {
			for (int i = 0; i < max; i++) {
				outgoing.add(crates.get(outIndex.get(i)));
			}
			crates.removeAll(outgoing);
			
			
//			for (int i = 0; i < crates.size(); i++) {
//				if (removed < max) {
//					if (crates.get(i) == destination) {
//						outgoing.add(crates.get(i));
//						crates.remove(i);
//						removed++;
//					}	
//				}
//			}	
		} finally {
			lock.unlock();
		}

		
		
		System.out.println("last step! " + this.getName());
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
			lock.lock();
			try {
				crates.add(delivery.get(i));
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
