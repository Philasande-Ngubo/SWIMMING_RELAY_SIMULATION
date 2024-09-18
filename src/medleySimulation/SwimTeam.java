//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import java.util.concurrent.CountDownLatch;
import medleySimulation.Swimmer.SwimStroke;

public class SwimTeam extends Thread {
	
	public static StadiumGrid stadium; //shared 
	private Swimmer [] swimmers;
	private int teamNo; //team number 

	//These latches sorts the swimmers by the orders of their strokes
	// and also makes other swimmers wait till their predecessors  finishes swimming
	CountDownLatch breastLatch = new CountDownLatch(1);
	CountDownLatch butterLatch = new CountDownLatch(1);
	CountDownLatch freeLatch = new CountDownLatch(1);

	
	public static final int sizeOfTeam=4;
	
	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr) {
		this.teamNo=ID;
		
		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);

		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+30); //range of speeds 
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s],this); //hardcoded speed for now
		}
	}

	public void celebrate(){
		try{
			swimmers[3].exitPool();
			while ( true){
				swimmers[0].dance();
				swimmers[1].dance();
				swimmers[2].dance();
				swimmers[3].dance();

			}
		}
		catch (Exception e){}
	}
	
	
	public void run() {
		try {	
			for(int s=0;s<sizeOfTeam; s++) { //start swimmer threads
				swimmers[s].start();
				
			}
			
			for(int s=0;s<sizeOfTeam; s++) swimmers[s].join();			//don't really need to do this;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

