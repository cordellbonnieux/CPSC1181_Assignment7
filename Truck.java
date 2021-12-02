import java.util.*;

/**
 * Truck, used to transport crates from Warehouse to Warehouse.
 * @author Cordell Bonnieux
 *
 */
public class Truck implements Runnable {

	private String name;
	private Warehouse source;
	private Warehouse destination;
	private int capacity;
	private ArrayList<String> crates; 

	/**
	 * Class Constructor
	 * @param source Warehouse - a warehouse to pick up from
	 * @param destination Warehouse - a warehouse to deliver to
	 * @param capacity int - the maximum capacity of this truck
	 */
	public Truck(Warehouse source, Warehouse destination, int capacity) {
		if (source == null || destination == null) {
			throw new NullPointerException("Warehouse params cannot be null");
		} else if (capacity < 0) {
			throw new IllegalArgumentException("capacity cannot be negative");
		}
		this.source = source;
		this.destination = destination;
		this.capacity = capacity;
		this.name = "Truck " + destination.getName();
		this.crates = new ArrayList<String>();
	}
	
	/**
	 * Run
	 * this Truck picks up this.capacity amount of crates from this.source and 
	 * delivers them to this.destination. Both destination and source are Warehouses.
	 */
	@Override
	public void run() {
		boolean working = true;
		while (working) {
			try {
				// pick up
				crates.addAll(source.pickUp(destination.getName(), capacity));
				// drive 
				Thread.sleep(1000);
				// deliver
				destination.deliver(crates);
				crates = new ArrayList<String>();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(this.name + "'s thread is shutting down.");
				working = false;
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
				System.out.println(this.name + "'s thread is shutting down.");
				working = false;
			}
		}
	}
	
	@Override
	public String toString() {
		return "Truck Name: " + this.name + ", crates: " + this.crates;
	}
}
