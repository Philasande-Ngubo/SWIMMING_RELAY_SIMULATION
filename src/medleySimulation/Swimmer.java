//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swimmer swimming a race
//Swimmers have one of four possible swim strokes: backstroke, breaststroke, butterfly and freestyle
package medleySimulation;

import java.awt.Color;

import java.util.Random;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

public class Swimmer extends Thread {
	
	public static StadiumGrid stadium; //shared 
	private FinishCounter finish; //shared
	
		
	GridBlock currentBlock;
	private Random rand;
	private int movingSpeed;
	private SwimTeam myTeam;
	
	private PeopleLocation myLocation;
	private int ID; //thread ID 
	private int team; // team ID
	private GridBlock start;
	private boolean dancin = true;

	private static CyclicBarrier startRace = new CyclicBarrier(10);
	private static AtomicBoolean shouldStartSwimSound = new AtomicBoolean(true);
	public static Audio swimSound = new Audio("swimming.wav",true);
	public static Random r = new Random();
	//Waits for all teams' backstrokes to start the race 

	public enum SwimStroke { 
		Backstroke(1,2.5,Color.black),
		Breaststroke(2,2.1,new Color(255,102,0)),
		Butterfly(3,2.55,Color.magenta),
		Freestyle(4,2.8,Color.red);
	    	
	     private final double strokeTime;
	     private final int order; // in minutes
	     private final Color colour;   

	     SwimStroke( int order, double sT, Color c) {
	            this.strokeTime = sT;
	            this.order = order;
	            this.colour = c;
	        }
	  
	        public int getOrder() {return order;}

	        public  Color getColour() { return colour; }
	    }  
	    private final SwimStroke swimStroke;
	
	//Constructor
	Swimmer( int ID, int t, PeopleLocation loc, FinishCounter f, int speed, SwimStroke s,SwimTeam myTeam) {
		this.swimStroke = s;
		this.myTeam = myTeam;
		this.ID=ID;
		movingSpeed=speed; //range of speeds for swimmers
		this.myLocation = loc;
		this.team=t;
		start = stadium.returnStartingBlock(team);
		finish=f;
		rand=new Random();
	}
	
	//getter
	public   int getX() { return currentBlock.getX();}	
	
	//getter
	public   int getY() {	return currentBlock.getY();	}
	
	//getter
	public   int getSpeed() { return movingSpeed; }

	
	public SwimStroke getSwimStroke() {
		return swimStroke;
	}

	//!!!You do not need to change the method below!!!
	//swimmer enters stadium area
	public void enterStadium() throws InterruptedException {
		currentBlock = stadium.enterStadium(myLocation);  //
		sleep(200);  //wait a bit at door, look around
	}
	
	//!!!You do not need to change the method below!!!
	//go to the starting blocks
	//printlns are left here for help in debugging
	public void goToStartingBlocks() throws InterruptedException {		
		int x_st= start.getX();
		int y_st= start.getY();
	//System.out.println("Thread "+this.ID + " has start position: " + x_st  + " " +y_st );
	// System.out.println("Thread "+this.ID + " at " + currentBlock.getX()  + " " +currentBlock.getY() );
	 while (currentBlock!=start) {
		//	System.out.println("Thread "+this.ID + " has starting position: " + x_st  + " " +y_st );
		//	System.out.println("Thread "+this.ID + " at position: " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep(movingSpeed*3);  //not rushing 
			currentBlock=stadium.moveTowards(currentBlock,x_st,y_st,myLocation); //head toward starting block
		//	System.out.println("Thread "+this.ID + " moved toward start to position: " + currentBlock.getX()  + " " +currentBlock.getY() );
		}
	System.out.println("-----------Thread "+this.ID + " at start " + currentBlock.getX()  + " " +currentBlock.getY() );
	}
	
	//!!!You do not need to change the method below!!!
	//dive in to the pool
	private void dive() throws InterruptedException {
		int x= currentBlock.getX();
		int y= currentBlock.getY();
		currentBlock=stadium.jumpTo(currentBlock,x,y-2,myLocation);
	}
	
	//!!!You do not need to change the method below!!!
	//swim there and back
	private int direction(){
		int results = r.nextInt(2)+1;
		if (dancin){
			dancin = false;
		}
		else{
			dancin = true;
		}
		return results * ( r.nextBoolean() ? 1 : -1 );
	}
	public void dance(){
			try{
				currentBlock=stadium.moveTowards(currentBlock,getX()+direction(),getY()+direction(),myLocation);
				sleep(((int) (movingSpeed*swimStroke.strokeTime))+200);
			}
			catch (Exception e){}


	}
	private void swimRace() throws InterruptedException {
		int x= currentBlock.getX();
		while((boolean) ((currentBlock.getY())!=0)) {
			currentBlock=stadium.moveTowards(currentBlock,x,0,myLocation);
			//System.out.println("Thread "+this.ID + " swimming " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep((int) (movingSpeed*swimStroke.strokeTime)); //swim
			System.out.println("Thread "+this.ID + " swimming  at speed" + movingSpeed );	
		}

		while((boolean) ((currentBlock.getY())!=(StadiumGrid.start_y-1))) {
			currentBlock=stadium.moveTowards(currentBlock,x,StadiumGrid.start_y,myLocation);
			//System.out.println("Thread "+this.ID + " swimming " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep((int) (movingSpeed*swimStroke.strokeTime));  //swim
		}
		
	}
	
	//!!!You do not need to change the method below!!!
	//after finished the race
	public void exitPool() throws InterruptedException {		
		int bench=stadium.getMaxY()-swimStroke.getOrder(); 			 //they line up
		int lane = currentBlock.getX()+1;//slightly offset
		currentBlock=stadium.moveTowards(currentBlock,lane,currentBlock.getY(),myLocation);
	   while (currentBlock.getY()!=bench) {
		 	currentBlock=stadium.moveTowards(currentBlock,lane,bench,myLocation);
			sleep(movingSpeed*3);  //not rushing 
		}
	}
	
	public void run(){
		try {
			
			//Swimmer arrives
			sleep(movingSpeed+(rand.nextInt(10))); //arriving takes a while
			myLocation.setArrived();
			if (this.swimStroke.order == 2){
				myTeam.breastLatch.await();
				myTeam.breastLatch = new CountDownLatch(1);

			}
			// swimmers wait for their predecors to enter first
			//then re-instatiate the latches to be re-used for the relay action 

			if (this.swimStroke.order == 3){
				myTeam.butterLatch.await();
				myTeam.butterLatch = new CountDownLatch(1);

			}
			if (this.swimStroke.order == 4){
				myTeam.freeLatch.await();
				myTeam.freeLatch = new CountDownLatch(1);

			}
			enterStadium();	

			// a swimmer notifies the next swimmer to enter
			if (this.swimStroke.order == 1){
				myTeam.breastLatch.countDown();

			}

			if (this.swimStroke.order == 2){
				myTeam.butterLatch.countDown();

			}
			if (this.swimStroke.order == 3){
				myTeam.freeLatch.countDown();

			}
			
			goToStartingBlocks();

			// swimmers wait for their predecors to finish their swim lap
			if (this.swimStroke.order == 2){
				myTeam.breastLatch.await();

			}

			if (this.swimStroke.order == 3){
				myTeam.butterLatch.await();

			}
			if (this.swimStroke.order == 4){
				myTeam.freeLatch.await();

			}
			try{
			if (this.swimStroke.order == 1){startRace.await();
					synchronized (this.swimSound){
					if (shouldStartSwimSound.get()){
						shouldStartSwimSound.set(false);
						swimSound.play();

					}
				}

				}
			}

			catch(Exception e){}

			new Audio("dive.wav",false).play();
			dive();
				
			swimRace();
			if(swimStroke.order==4) {
				finish.finishRace(ID, team); // fnishline
			}
			else {
				//System.out.println("Thread "+this.ID + " done " + currentBlock.getX()  + " " +currentBlock.getY() );
				// swimmers notifies the next swimmer to enter the pool and start their round
				if (this.swimStroke.order == 1){
					myTeam.breastLatch.countDown();

				}

				if (this.swimStroke.order == 2){
					myTeam.butterLatch.countDown();

				}
				if (this.swimStroke.order == 3){
				myTeam.freeLatch.countDown();

				}
		
				exitPool();//if not last swimmer leave pool
			}
			
		} catch (InterruptedException e1) {  //do nothing
		} 
	}
	
}
