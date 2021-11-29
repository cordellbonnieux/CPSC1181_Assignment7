import java.util.ArrayList;
import java.util.Arrays;

public class Simulation {
	public static void main(String[] args) {
		// create three warehouses
		Warehouse one = new Warehouse("Warehouse One", true);
		Warehouse two = new Warehouse("Warehouse Two", true);
		Warehouse three = new Warehouse("Warehouse Three", true);
		
		// crate factory warehouse with warehouses as destination, with a production of 10
		ArrayList<String> destinations = new ArrayList<String>(Arrays.asList(one.getName(),two.getName(),three.getName()));
		FactoryWarehouse factory = new FactoryWarehouse("Factory", destinations, 10);
		
		System.out.println(destinations);
	}
}