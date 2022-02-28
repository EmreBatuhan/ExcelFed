import java.lang.Math;

//Entries are basically plans that the players make.When a player decides to enter a service , they decide plannedTime and duration.
//When the planned time arrives ,the player applies for the service.In result the player enters a queue or directly get the service
public class Entry implements Comparable<Entry>{

	private Player player;
	private double plannedTime;
	private double duration;
	private String type;
	
	static double epsilon = 0.0000000000000001;
	
	public Entry(Player _player, double _plannedTime ,double _duration, String _type) {
		player = _player;
		plannedTime = _plannedTime;
		duration = _duration;
		type = _type;
	}
	
	public int compareTo(Entry other) {
		
		if(Math.abs(plannedTime - other.plannedTime) < epsilon) {
			return player.getId() - other.player.getId();
		}
		
		if(plannedTime < other.plannedTime) {
			return -1;
		}
		
		return 1;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public double getPlannedTime() {
		return plannedTime;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public String getType() {
		return type;
	}
	
	
	
	
}
