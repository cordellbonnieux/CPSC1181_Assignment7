import java.util.ArrayList;

public class Truck implements Runnable {

	private String name;
	private Warehouse source;
	private Warehouse destination;
	private int capacity;
	private ArrayList<String> crates; 

	/**
	 * Class Constructor
	 * @param source
	 * @param destination
	 * @param capacity
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
	
	@Override
	public void run() {
		boolean working = true;
		while (working) {
			// pick up crates
			try {
				crates.addAll(source.pickUp(destination.getName(), capacity));
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println(this.name + " pick up interrupted.\nThread is shutting down.");
				working = false;
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
				System.out.println("Destination is null.\nThread is shutting down.");
				working = false;
			} finally {
				// do something
			}	
			// drive 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println(this.name + " sleep (driving time) interupted.\nThread shutting down.");
				working = false;
			}
			// deliver crates
			try {
				destination.deliver(crates);
				crates = new ArrayList<String>();
			} /*catch (InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println(this.name + " delivery interrupted.\nThread is shutting down.");
				working = false;
			} */catch (NullPointerException e) {
				System.out.println(e.getMessage());
				System.out.println("Crates is null.\nThread is shutting down.");
				working = false;
			} finally {
				// do something
			}
		}
		
	}
}
