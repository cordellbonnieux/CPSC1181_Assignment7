import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
	private Lock lock;

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
		this.lock = new ReentrantLock();
	}
	
	@Override
	public void run() {
		boolean working = true;
		while (working) {
			// pick up crates
			lock.lock();
			try {
				System.out.println("trying to pick up");
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
				lock.unlock();
			}	
			// drive 
			lock.lock();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println(this.name + " sleep (driving time) interupted.\nThread shutting down.");
				working = false;
			} finally {
				lock.unlock();
			}
			// deliver crates
			lock.lock();
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
				lock.unlock();
			}
		}
	}
	
	@Override
	public String toString() {
		return "Truck Name: " + this.name + ", crates: " + this.crates;
	}
}
