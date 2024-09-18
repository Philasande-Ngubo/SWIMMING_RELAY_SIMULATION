//M. M. Kuttel 2024 mkuttel@gmail.com
// GridBlock class to represent a block in the grid.
// only one thread at a time "owns" a GridBlock - this must be enforced

package medleySimulation;


public class GridBlock {
	
	private int isOccupied; 
	
	private final boolean isStart;  //is this a starting block?
	private int [] coords; // the coordinate of the block.
	
	GridBlock(boolean startBlock) throws InterruptedException {
		isStart=startBlock;
		isOccupied= -1;
	}
	
	GridBlock(int x, int y, boolean startBlock) throws InterruptedException {
		this(startBlock);
		coords = new int [] {x,y};
	}
	
	public  synchronized int getX() {return coords[0];}  
	
	public synchronized  int getY() {return coords[1];}
	
	
	
	//Get a block
	public synchronized boolean get(int threadID) throws InterruptedException {
		if (isOccupied==threadID) return true; //thread Already in this block
		if (isOccupied>=0) return false; //space is occupied
		isOccupied= threadID;  //set ID to thread that had block
		return true;
	}
		
	
	//release a block
	public synchronized void release() {
		isOccupied= -1;
	}
	

	//is a bloc already occupied?
	public boolean occupied() {
		if(isOccupied==-1) return false;
		return true;
	}
	
	
	//is a start block
	public boolean isStart() {
		return isStart;	
	}

}
