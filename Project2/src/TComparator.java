import java.util.Comparator;
import java.lang.Math;

//This Comparator compares two players using arrival time.If arrival times are equal ids are compared.
public class TComparator implements Comparator<Player>{

	static double epsilon = 0.0000000000000001;
	
	public int compare(Player p1,Player p2) {
		if(Math.abs(p1.getArrivalTime() - p2.getArrivalTime()) < epsilon) {
			return p1.getId() - p2.getId();
		}
		if(p1.getArrivalTime() < p2.getArrivalTime()) {
			return -1;
		}
		return 1;
	}
}
