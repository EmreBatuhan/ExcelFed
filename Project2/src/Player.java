
public class Player {

	private int id;
	private int skill;
	private double trainingTime ; //This is the duration that the player has decided to take for the next training
	private double massageTime ; 
	private double lastArrivalTime ; //The last time player entered a queue. Not updated if the player directly takes service without waiting.
	private int totalMassage ; 
	private boolean busy; // If the player is waiting in a queue or taking a service ,then this value is true otherwise false. 
	private double totalMWait ; //This value is the total Massage queue waiting time of the player
	private double totalPWait ;
	
	
	public Player(int _id,int _skill) {
		id =_id;
		skill = _skill;
		trainingTime = 0;
		massageTime = 0;
		lastArrivalTime = -1;
		totalMassage = 0;
		busy = false;
		totalMWait = 0;
		totalPWait = 0;
	}
	
	public void setTrainingTime(double _trainingTime) {
		trainingTime = _trainingTime;
	}
	
	public void setMassageTime(double _massageTime) {
		massageTime = _massageTime;
	}
	
	public void setArrivalTime(double _arrivalTime) {
		lastArrivalTime = _arrivalTime;
	}
	
	public void setBusy(boolean _busy) {
		busy = _busy;
	}
	
	public void addTotalMassage() {
		totalMassage++;
	}
	
	public void addMWait(double waitTime) {
		totalMWait += waitTime;
	}
	
	public void addPWait(double waitTime) {
		totalPWait += waitTime;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSkill() {
		return skill;
	}
	
	public double getTrainingTime() {
		return trainingTime;
	}
	
	public double getMassageTime() {
		return massageTime;
	}
	
	public double getArrivalTime() {
		return lastArrivalTime;
	}
	
	public int getTotalMassage() {
		return totalMassage;
	}
	
	public double getTotalMWait() {
		return totalMWait;
	}
	
	public double getTotalPWait() {
		return totalPWait;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
}
