
//Since every therapist has its own theraphy time they have their own class
public class Physiotherapist implements Comparable<Physiotherapist>{
	
	private int id;
	private double serviceTime;
	
	public Physiotherapist(int _id ,double _serviceTime) {
		id = _id;
		serviceTime = _serviceTime;
	}
		
	public int compareTo(Physiotherapist other) {
		return  id - other.id ;
	}
	
	public int getId() {
		return id;
	}
	
	public double getServiceTime() {
		return serviceTime;
	}
	
	
}
