import java.util.Comparator;

//This Comparator compares two players using trainingTime.If TrainingTimes are equal ,then arrival times are compared.If they are also equal ids are compared.
public class PComparator implements Comparator<Player>{

	static double epsilon = 0.0000000000000001;
	
	public int compare(Player p1,Player p2) {
		
		if(Math.abs(p1.getTrainingTime() - p2.getTrainingTime()) < epsilon) {
			if(Math.abs(p1.getArrivalTime() - p2.getArrivalTime()) < epsilon) {
				return p1.getId() - p2.getId();
			}
			if(p1.getArrivalTime() < p2.getArrivalTime()) {
				return -1;
			}
			return 1;
		}
		
		if(p1.getTrainingTime() < p2.getTrainingTime()) {
			return 1;
		}
		
		return -1;
	}
}
