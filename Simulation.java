import java.util.*;

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
		
		route1.start();
		route2.start();
		route3.start();
		factoryOnShift.start();
		
		/* 
		 * FOR DEBUGGING ONLY, remove before handing in: Print out the toString for all 7 Trucks and
		 * Warehouses and then sleep for 1 second
		 */
	
		
		System.out.println(destinations);
	}

}