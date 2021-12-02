import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation {
	public static void main(String[] args) {
		// create three warehouses
		Warehouse warehouse1 = new Warehouse("Warehouse One", true);
		Warehouse warehouse2 = new Warehouse("Warehouse Two", true);
		Warehouse warehouse3 = new Warehouse("Warehouse Three", true);
		
		// crate factory warehouse with warehouses as destination, with a production of 10
		ArrayList<String> destinations = new ArrayList<String>(Arrays.asList(warehouse1.getName(),warehouse2.getName(),warehouse3.getName()));
		FactoryWarehouse factory = new FactoryWarehouse("Factory", destinations, 10);
		
		// create three trucks
		Truck truck1 = new Truck(factory, warehouse1, 10);
		Truck truck2 = new Truck(factory, warehouse2, 10);
		Truck truck3 = new Truck(factory, warehouse3, 10);
		
		// create threads for trucks
		Thread route1 = new Thread(truck1);
		Thread route2 = new Thread(truck2);
		Thread route3 = new Thread(truck3);
		
		// create factory thread
		Thread factoryOnShift = new Thread(factory);
		
		//create a status printer
		//CurrentStatus status = new CurrentStatus(warehouse1, warehouse2, warehouse3, factory, truck1, truck2, truck3);
		//Thread printer = new Thread(status);
		
		//printer.start();
		route1.start();
		route2.start();
		route3.start();
		factoryOnShift.start();
		
		/* 
		 * FOR DEBUGGING ONLY, remove before handing in: Print out the toString for all 7 Trucks and
		 * Warehouses and then sleep for 1 second
		 */
		//Lock lock = new Lock();
		while (true) {
			//lock.lock();
			try {
				System.out.println(factory.toString());
				System.out.println(warehouse1.toString());
				System.out.println(warehouse2.toString());
				System.out.println(warehouse3.toString());
				System.out.println(truck1.toString());
				System.out.println(truck2.toString());
				System.out.println(truck3.toString());
				System.out.println("#############################");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				//lock.unlock();
			}
		}
	
		
		//System.out.println(destinations);
	}
	
	// this should just be a method
	public static class CurrentStatus implements Runnable {
		private Warehouse w1;
		private Warehouse w2;
		private Warehouse w3;
		private FactoryWarehouse factory;
		private Truck t1;
		private Truck t2;
		private Truck t3;
		private Lock lock;
		
		public CurrentStatus (Warehouse w1, Warehouse w2, Warehouse w3, FactoryWarehouse f, Truck t1, Truck t2, Truck t3) {
			this.w1 = w1;
			this.w2 = w2;
			this.w3 = w3;
			this.factory = f;
			this.t1 = t1;
			this.t2 = t2;
			this.t3 = t3;
			this.lock = new ReentrantLock();
			
		}

		@Override
		public void run() {
			while (true) {
				lock.lock();
				try {
					System.out.println(factory.toString());
					System.out.println(w1.toString());
					System.out.println(w2.toString());
					System.out.println(w3.toString());
					System.out.println(t1.toString());
					System.out.println(t2.toString());
					System.out.println(t3.toString());
					System.out.println("#############################");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
	}

}