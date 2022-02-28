import java.util.PriorityQueue;
import java.util.HashMap;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Locale;

public class TennisClub {

	static double epsilon = 0.0000000000000001;
	
	private double currentTime; // Also output 15 when the simulation is over
	
	private HashMap<Integer,Player> idToPlayer; // Since entries are given with player id in the input file turning ids to actual players is necessary
	
	private PriorityQueue<Entry> entries;
	private PriorityQueue<Occupation> occupations;
	
	private int freeMasseurs; //Since there is no difference among the masseurs they are stored as int
	private int freeCoaches;
	private PriorityQueue<Physiotherapist> freeTherapists; //Since every therapist has its own theraphy time they have their own class
	
	private PriorityQueue<Player> tQueue; // Training queue . If there are no available coaches , players will wait here.
	private PriorityQueue<Player> pQueue; // Physiotherapy queue. Sorting players is done via PComparator. TComparator for tQueue and MComparator for mQueue.
	private PriorityQueue<Player> mQueue; // Massage queue.
	
	//These attributes are set for output presentation.
	private int maxTQueue; // output 1
	private int maxPQueue; // output 2
	private int maxMQueue; // output 3
	
	private int totalTrainings; // output 4,7,10
	private int totalTherapies; // output 5,8,10
	private int totalMassages; // output 6,9
	
	private double totalTWaiting; // output 4,10
	private double totalPWaiting; // output 5,10
	private double totalMWaiting; // output 6
	
	private double totalTTime; // output 7,10
	private double totalPTime; // output 8,10
	private double totalMTime; // output 9
	
	private double maxPWaiting; // output 11
	private int maxPWaitPlayerId; //output 11
	
	private double minMWaiting; // output 12
	private int minMWaitPlayerId; // output 12
	
	private int invalid; // output 13
	private int canceled; // output 14
	
	public TennisClub() {
		currentTime = 0;
		idToPlayer = new HashMap<Integer,Player>();
		entries = new PriorityQueue<Entry>();
		occupations = new PriorityQueue<Occupation>();
		freeMasseurs = 0;
		freeCoaches = 0;
		freeTherapists = new PriorityQueue<Physiotherapist>();
		tQueue = new PriorityQueue<Player>(11,new TComparator());
		pQueue = new PriorityQueue<Player>(11,new PComparator());
		mQueue = new PriorityQueue<Player>(11,new MComparator());
		
	}
	
	public ArrayList<String> outputList() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(String.valueOf(maxTQueue));
		result.add(String.valueOf(maxPQueue));
		result.add(String.valueOf(maxMQueue));
		result.add(String.format(Locale.US,"%.3f",totalTWaiting / totalTrainings));
		result.add(String.format(Locale.US,"%.3f",totalPWaiting / totalTherapies));
		result.add(String.format(Locale.US,"%.3f",totalMWaiting / totalMassages));
		result.add(String.format(Locale.US,"%.3f",totalTTime / totalTrainings));
		result.add(String.format(Locale.US,"%.3f",totalPTime / totalTherapies));
		result.add(String.format(Locale.US,"%.3f",totalMTime / totalMassages));
		result.add(String.format(Locale.US,"%.3f",totalTWaiting / totalTrainings + totalTTime / totalTrainings + totalPWaiting / totalTherapies + totalPTime / totalTherapies));
		result.add(String.valueOf(maxPWaitPlayerId) +" "+String.format(Locale.US,"%.3f",maxPWaiting));
		
		//If minWaiting is added as -1 , since it is double it will be printed as "-1.000" which we don't want
		if(minMWaitPlayerId == -1) {
			result.add("-1 -1");
		}
		else {
			result.add((String.valueOf(minMWaitPlayerId) +" "+String.format(Locale.US,"%.3f",minMWaiting)));
		}
		
		result.add(String.valueOf(invalid));
		result.add(String.valueOf(canceled));
		result.add(String.format(Locale.US,"%.3f",currentTime));
		return result;
	}
	
	public HashMap<Integer,Player> getIdToPlayer(){
		return idToPlayer;
	}
	
	public void addPlayer(Player player) {
		idToPlayer.put(player.getId(),player);
	}
	
	public void addMasseurs(int quantity) {
		freeMasseurs += quantity;
	}
	
	public void addCoaches(int quantity) {
		freeCoaches += quantity;
	}
	
	public void addTherapist(Physiotherapist therapist) {
		freeTherapists.add(therapist);
	}
	
	public void addTQueue(Player player) {		
		//Arrival time is the time when the player entered the queue. Player will hold this information for later.
		player.setArrivalTime(currentTime);
		tQueue.add(player);
	}
	
	public void addPQueue(Player player) {
		player.setArrivalTime(currentTime);
		pQueue.add(player);
	}
	
	public void addMQueue(Player player) {
		player.setArrivalTime(currentTime);
		mQueue.add(player);
	}
	
	public boolean updateTime() {
		//This method is the main method. In this part we decide which time to jump next. 
		//It is decided by looking for the minimum plannedTime value in the entries and minimum endTime value in the occupations.
		
		
		// Updating output attributes
		if(tQueue.size() > maxTQueue) {
			maxTQueue = tQueue.size();
		}
		if(pQueue.size() > maxPQueue) {
			maxPQueue = pQueue.size();
		}
		if(mQueue.size() > maxMQueue) {
			maxMQueue = mQueue.size();
		}
		
		
		if(entries.peek() == null && occupations.peek() == null ) {
			//When this part has been reached it means the simulation is over
			//These two methods are for output and they are searching for overall extremes so they are called here.
			this.checkMinMassage();
			this.checkMaxTherapy();
			return true;
		}
		
		if(entries.peek() == null) {
			currentTime = occupations.peek().getEndTime();
			while(occupations.peek() != null && Math.abs(occupations.peek().getEndTime() - currentTime)< epsilon) {
				Occupation occupation = occupations.poll();
				this.endOccupation(occupation);
			}
			return false;
		}
		
		if(occupations.peek() == null) {
			currentTime = entries.peek().getPlannedTime();
			while(entries.peek() != null && Math.abs(entries.peek().getPlannedTime() - currentTime) < epsilon) {
				Entry entry = entries.poll();
				this.makeEntry(entry);
			}
			return false;
		}
		
		if(Math.abs(entries.peek().getPlannedTime() - occupations.peek().getEndTime()) < epsilon ) {
			currentTime = entries.peek().getPlannedTime();
			while(occupations.peek() != null && Math.abs(occupations.peek().getEndTime() - currentTime) < epsilon) {
				Occupation occupation = occupations.poll();
				this.endOccupation(occupation);
			}
			
			while(entries.peek() != null && Math.abs(entries.peek().getPlannedTime() - currentTime) < epsilon) {
				Entry entry = entries.poll();
				this.makeEntry(entry);
			}
			return false;
		}
		
		if(entries.peek().getPlannedTime() < occupations.peek().getEndTime()) {
			currentTime = entries.peek().getPlannedTime();
			while(entries.peek() != null && Math.abs(entries.peek().getPlannedTime() - currentTime) < epsilon) {
				Entry entry = entries.poll();				
				this.makeEntry(entry);
			}
			return false;
		}
		
		if(entries.peek().getPlannedTime() > occupations.peek().getEndTime()) {
			
			currentTime = occupations.peek().getEndTime();
			while(occupations.peek() != null && Math.abs(occupations.peek().getEndTime() - currentTime) < epsilon) {
				Occupation occupation = occupations.poll();
				this.endOccupation(occupation);
			}
			return false;
		}
		return false;
	}
	
	public void makeEntry(Entry entry) {
		// This entry's planned time has come so this entry's player will enter a queue or a service.
		
		Player player = entry.getPlayer();
		if(player.isBusy() && (player.getTotalMassage() > 2 && entry.getType().equals("m"))) {
			//In this part the entry should be canceled but also invalid.
			//In this situation we take this as invalid.
			invalid++;
			return;
		}
		
		if(player.isBusy()) { 
			// It is decided here if the entry will be canceled or not.
			canceled++;
			return;
		}
		
		player.setBusy(true);
		
		if(entry.getType().equals("t")) {
			
			player.setTrainingTime(entry.getDuration());
			if(freeCoaches > 0) {
				//If the code has reached here it means player went directly for a coach without waiting 
				freeCoaches--;
				this.doTraining(player, entry.getDuration());
				return;
			}
			//All coaches are occupied so player goes to TQueue
			this.addTQueue(player);
			return;
		}
		if(entry.getType().equals("p")) {
			if(freeTherapists.peek() != null) {
				Physiotherapist therapist = freeTherapists.poll();
				this.doTherapy(player,therapist);
				return;
			}
			this.addPQueue(player);
			return;
		}
		if(entry.getType().equals("m")) {
			
			if(player.getTotalMassage() > 2) {
				//It is decided here if the entry is invalid or not.
				invalid++;
				player.setBusy(false);
				return;
			}
			
			
			player.setMassageTime(entry.getDuration());
			if(freeMasseurs > 0) {
				freeMasseurs--;
				this.doMassage(player,entry.getDuration());
				return;
			}
			this.addMQueue(player);
			return;
		}
	}
	
	public void endOccupation(Occupation occupation) {
		// This occupation's end time has come so this occupation will end.
		Player player = occupation.getPlayer();
		
		player.setBusy(false);
		
		if(occupation.getType().equals("t")) {
			
			//Since the training of this player has finished right now lets make an entry and immediately make that entry
			Entry entry = new Entry(player,currentTime,0,"p");
			this.makeEntry(entry);
			
			if(tQueue.peek() != null) {
				//Someone is waiting so we take them directly.
				Player player2 = tQueue.poll();
				
				this.doTraining(player2, player2.getTrainingTime());
			}
			else {	
				// No one in queue so coach is now free
				freeCoaches++;
			}
			return;
			
		}
		if(occupation.getType().equals("p")) {
			if(pQueue.peek() != null) {
				Player player2 = pQueue.poll();
				this.doTherapy(player2,occupation.getTherapist());
			}
			else {
				freeTherapists.add(occupation.getTherapist());
			}
			return;
		}
		if(occupation.getType().equals("m")) {
			if(mQueue.peek() != null) {
				Player player2 = mQueue.poll();
				this.doMassage(player2,player2.getMassageTime());
			}
			else {
				freeMasseurs++;
			}
			return;
		}
	}
	
	public void addEntry(Entry entry) {
		entries.add(entry);
	}
	
	public void addOccupation(Occupation occupation) {
		occupations.add(occupation);
	}
	
	public void doTraining(Player player,double trainingTime) {
		//This player is doing Training and will be busy until endtime = currentTime + trainingTime
		Occupation occ = new Occupation(player,currentTime + trainingTime,"t");
		occupations.add(occ);
		
		// Updating output attributes
		totalTrainings++;
		totalTTime += trainingTime;
		if(Math.abs(player.getArrivalTime() + 1) < epsilon) {
			return; // if ArrivalTime is -1 then the player did not enter queue so it didnt wait.
		}
		totalTWaiting += currentTime - player.getArrivalTime();
		player.setArrivalTime(-1);
		
	}
	
	public void doTherapy(Player player,Physiotherapist therapist) {
		Occupation occ = new Occupation(player,currentTime + therapist.getServiceTime(),"p",therapist);
		occupations.add(occ);
		
		// Updating output attributes
		totalTherapies++;
		totalPTime += therapist.getServiceTime();
		if(Math.abs(player.getArrivalTime() + 1) < epsilon) {
			return; 
		}
		player.addPWait(currentTime - player.getArrivalTime());
		totalPWaiting += currentTime - player.getArrivalTime();
		
		player.setArrivalTime(-1);
	}
	
	public void doMassage(Player player,double massageTime) {
		Occupation occ = new Occupation(player,currentTime + massageTime,"m");
		occupations.add(occ);
		player.addTotalMassage();
		
		// Updating output attributes
		totalMassages++;
		totalMTime += massageTime;
		if(Math.abs(player.getArrivalTime() + 1)< epsilon) {				
			return; 		
		}
		player.addMWait(currentTime - player.getArrivalTime());
		totalMWaiting += currentTime - player.getArrivalTime();
		player.setArrivalTime(-1);
	}
	
	public void checkMaxTherapy() {
		// This method checks maximum waiting time among the players .
		maxPWaiting = 0;
		maxPWaitPlayerId = 0;
		for(int playerId : idToPlayer.keySet()) {
			Player player = idToPlayer.get(playerId);
			if(player.getTotalPWait() > maxPWaiting) {
				maxPWaiting = player.getTotalPWait();
				maxPWaitPlayerId = playerId;
				continue;
			}
			if(Math.abs(player.getTotalPWait() - maxPWaiting) < epsilon && playerId < maxPWaitPlayerId) {
				maxPWaiting = player.getTotalPWait();
				maxPWaitPlayerId = playerId;
			}
		}
	}
	public void checkMinMassage() {
		// This method checks minimum waiting time among the players that got 3 massages.
		minMWaiting = -1;
		minMWaitPlayerId = -1;
		for(int playerId : idToPlayer.keySet()) {
			Player player = idToPlayer.get(playerId);
			if(player.getTotalMassage() == 3) {
				if(Math.abs(minMWaiting + 1) < epsilon || player.getTotalMWait() <minMWaiting) {
					minMWaiting = player.getTotalMWait();
					minMWaitPlayerId = playerId;
					continue;
				}
				if(Math.abs(player.getTotalMWait() - minMWaiting) < epsilon && playerId < minMWaitPlayerId) {
					minMWaiting = player.getTotalMWait();
					minMWaitPlayerId = playerId;
				}
				
			}
		}
	}
	
	
}
