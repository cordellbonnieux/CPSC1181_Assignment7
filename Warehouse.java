import java.util.ArrayList;

public class Warehouse {
	private String name;
	private ArrayList<String> crates;
	private boolean matchName;
	private int errors;
	
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
			throw new IllegalArgumentException("destination cannot be null");
		} else if (!crates.contains(destination)) {
			throw new IllegalArgumentException("destination does not exist");
		} else if (max < 1) {
			throw new IllegalArgumentException("integer parameter cannot be less than 1");
		}
		
		// local variables
		ArrayList<String> outgoing = new ArrayList<String>();
		int removed = 0;
		int total = crates.size();
		
		// wait till a crate is ready
		while (!crates.contains(destination)) {
			Thread.sleep(500);
		}
		
		// remove crates
		for (int i = 0; i < total; i++) {
			if (removed < max) {
				if (crates.get(i) == destination) {
					outgoing.add(crates.get(i));
					crates.remove(i);
					i--;
					total--;
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
			throw new IllegalArgumentException("delivery array list cannot be null");
		} else if (delivery.contains(null)) {
			throw new IllegalArgumentException("delivery array list cannot contain null");
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
