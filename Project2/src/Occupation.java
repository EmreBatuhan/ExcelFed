
//Occupations represent the activity time players and the workers.
//If there is an occupation with a player and a worker , both of them are busy until the end.
//If the worker is coach or masseur then freeCoaches/freeMasseurs are lowered, 
//else the worker is therapist then therapist is taken out from the Physiotherapists queue  
public class Occupation implements Comparable<Occupation>{

	private Player player;
	private double endTime;
	private String type; // there are 3 types : "t" for Training , "p" for Physiotherapy , "m" for Massage  
	private Physiotherapist therapist; // If the type is p then it is crucial to store the therapist
	
	static double epsilon = 0.0000000000000001;
	
	// Constructor that will be used for "t" and "m" types
	public Occupation(Player _player,double _endTime,String _type) {
		player = _player;
		endTime = _endTime;
		type = _type;
		therapist = null;
	}
	
	//Constructor that will be used for "p" type
	public Occupation(Player _player,double _endTime,String _type,Physiotherapist _therapist) {
		player = _player;
		endTime = _endTime;
		type = _type;
		therapist = _therapist;
	}
	
	public int compareTo(Occupation other) {
		
		if(Math.abs(endTime - other.endTime) < epsilon) {
			return player.getId() - other.player.getId();
		}
		
		if(endTime < other.endTime) {
			return -1;
		}
		return 1;
	}

	public Player getPlayer() {
		return player;
	}

	public double getEndTime() {
		return endTime;
	}

	public String getType() {
		return type;
	}	
	
	public Physiotherapist getTherapist() {
		return therapist;
	}
}

