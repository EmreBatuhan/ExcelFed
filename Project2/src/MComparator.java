import java.util.Comparator;

//This Comparator compares two players using skill level.If skills are equal arrival ,then times are compared.If they are also equal ids are compared.
public class MComparator implements Comparator<Player>{

	static double epsilon = 0.0000000000000001;
	
	public int compare(Player p1,Player p2) {
		
		if(Math.abs(p1.getSkill() - p2.getSkill()) < epsilon) {
			if(p1.getArrivalTime() < p2.getArrivalTime()) {
				return -1;
			}
			if(p1.getArrivalTime() - p2.getArrivalTime() < epsilon) {
				return p1.getId() - p2.getId();
			}
			return 1;
		}
		
		if(p1.getSkill() < p2.getSkill()) {
			return 1;
		}
		
		return -1;
	}
}
