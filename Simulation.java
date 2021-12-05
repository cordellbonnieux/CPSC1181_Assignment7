import java.util.*;
/**
 * This class simulates a FactoryWarehouse producing goods for Warehouses, who receive said goods from Truck, who deliver them.
 * @author Cordell Bonnieux
 *
 */
public class Simulation {
	public static void main(String[] args) {
		
		boolean debug = true;
		// Factory Warehouse production
		final int PRODUCTION = 10; 
		// Truck capacity
		final int CAPACITY = 10;
		
		// create three warehouses
		Warehouse warehouse1 = new Warehouse("Warehouse One", true);
		Warehouse warehouse2 = new Warehouse("Warehouse Two", true);
		Warehouse warehouse3 = new Warehouse("Warehouse Three", true);
		
		// crate factory warehouse with warehouses as destination, with a production of 10
		ArrayList<String> destinations = new ArrayList<String>(Arrays.asList(warehouse1.getName(),warehouse2.getName(),warehouse3.getName()));
		FactoryWarehouse factory = new FactoryWarehouse("Factory", destinations, PRODUCTION);
		
		// create three trucks
		Truck truck1 = new Truck(factory, warehouse1, CAPACITY);
		Truck truck2 = new Truck(factory, warehouse2, CAPACITY);
		Truck truck3 = new Truck(factory, warehouse3, CAPACITY);
		
		// create threads for trucks
		Thread route1 = new Thread(truck1);
		Thread route2 = new Thread(truck2);
		Thread route3 = new Thread(truck3);
		
		// create factory thread
		Thread factoryOnShift = new Thread(factory);
		
		// start threads
		route1.start();
		route2.start();
		route3.start();
		factoryOnShift.start();
		
		while (debug) {
			printWait(factory, warehouse1, warehouse2, warehouse3, truck1, truck2, truck3);
			debug = (warehouse1.getCrateCount() >= PRODUCTION && warehouse2.getCrateCount() >= PRODUCTION && warehouse3.getCrateCount() >= PRODUCTION) ? false : true;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!debug) {
				printWait(factory, warehouse1, warehouse2, warehouse3, truck1, truck2, truck3);
			}
		}
		
		/*
		 * Thread.currentThread().interrupt()
		 * was not always shutting down the main thread/thread0 (only sometimes).
		 * So, I am interrupting them individually
		 */
		route1.interrupt();
		route2.interrupt();
		route3.interrupt();
		factoryOnShift.interrupt();
	}
	
	public static void printWait(FactoryWarehouse factory, Warehouse warehouse1, Warehouse warehouse2, Warehouse warehouse3, Truck truck1, Truck truck2, Truck truck3) {
		try {
			System.out.println(factory.toString());
			System.out.println(warehouse1.toString());
			System.out.println(warehouse2.toString());
			System.out.println(warehouse3.toString());
			System.out.println(truck1.toString());
			System.out.println(truck2.toString());
			System.out.println(truck3.toString());
			System.out.println("##########################################################");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}