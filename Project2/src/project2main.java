import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;


import java.util.ArrayList;


public class project2main {
	
	public static void main(String[] args) {
			
			//File reading and printing part has been taken from the Project1 FILE_IO_EXAMPLE document.
			
			
			//Read input file (first argument)	
			String inputFileName = args[0];
			File myInputFile = new File(inputFileName);
			
			Scanner myReader;
			
			try {
				myReader = new Scanner(myInputFile).useLocale(Locale.US);
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
				
			
			//Write output file (second argument)
			String outputFileName = args[1];
			File myOutputFile = new File(outputFileName);
			
			PrintStream outstream;
			try {
				if (myOutputFile.createNewFile()) {
			        System.out.println("File created: " + outputFileName);
			    } 
			      
			    outstream = new PrintStream(outputFileName); 
			      
		    } catch (IOException e) {
		      e.printStackTrace();
		      myReader.close();
		      return;
		    }
			
			//Reading input data
			TennisClub tc = new TennisClub();
			
			//Reading players
			int n = myReader.nextInt(); 
			for(int i=0;i < n; i++) {
				int id = myReader.nextInt();
				int skill = myReader.nextInt();
				
				Player player = new Player(id,skill);
				tc.addPlayer(player);
			}
			
			//Reading arrivals
			int arrivals = myReader.nextInt();
			for(int i=0;i<arrivals;i++) {
				String type = myReader.next();
				int id = myReader.nextInt();
				double plannedTime = myReader.nextDouble();
				double duration = myReader.nextDouble();
				
				Entry entry = new Entry(tc.getIdToPlayer().get(id),plannedTime,duration,type);
				tc.addEntry(entry);
			}
			
			//Reading physiotherapists
			int therapistNum = myReader.nextInt();
			for(int i=0;i < therapistNum;i++) {
				Physiotherapist therapist = new Physiotherapist(i,myReader.nextDouble());
				tc.addTherapist(therapist);
			}
			
			//Reading training coaches and masseur
			int coachNum = myReader.nextInt();
			int masseurNum = myReader.nextInt();
			
			tc.addCoaches(coachNum);
			tc.addMasseurs(masseurNum);
			
			myReader.close();
			//Input reading is done. Starting to process the data.
			while(tc.updateTime() == false) {}
			
			//Data proccessing is done. Printing the output
			ArrayList<String> outputList = tc.outputList();
			for(String str : outputList) {
				outstream.println(str);
			}
			
			outstream.close();
			
	}

}

