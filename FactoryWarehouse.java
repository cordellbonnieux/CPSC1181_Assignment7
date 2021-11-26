import java.util.*;

public class FactoryWarehouse extends Warehouse implements Runnable {
	private ArrayList<String> destinations;
	private int produced;
	private int send;
	
	public FactoryWarehouse(String n, boolean m, int p, int s, ArrayList<String> d) {
		super(n, m);
		this.produced = p;
		this.send = s;
		this.destinations = d;
	}

	@Override
	public void run() {	
		boolean on = true;
		while (send > 0 && on) {
			int load = (send > 3) ? 3 : send;
			for (int i = 0; i < destinations.size(); i++) {
				ArrayList<String> parcels = new ArrayList<String>();
				for (int y = 0; y < load; y++) {
					parcels.add(destinations.get(i));
					produced++;
				}
				try {
					this.deliver(parcels);
					send -= load;
					Thread.sleep(5000);
				} catch (InterruptedException e)  {
					System.out.println(e.getMessage());
					System.out.println("Thread shutting down");
					on = false;
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("name: %s, number of stored crates: %d, number of crates produced: %d", getName(), getCrateCount(), produced);
	}
}
/*
* make 3 for A
* send A's
* make 3 for B 
* send B's
* make 3 for A
* etc
* until it reaches this.send's value
*
*Exercise 3 the truck will need an arraylist of trucks 
* it is not mentioned in the assignment pdf
*
*/