// Simple class to record when someone has crossed the line first and wins
package medleySimulation;

public class FinishCounter {
	private boolean firstAcrossLine; //flag
	private int winner; //who won
	private int winningTeam; //counter for patrons who have left the club
	
	FinishCounter() { 
		firstAcrossLine= true;//no-one has won at start
		winner=-1; //no-one has won at start
		winningTeam=-1; //no-one has won at start
	}
		
	//This is called by a swimmer when they touch the fnish line
	public synchronized void finishRace(int swimmer, int team) {
		boolean won =false;
		if(firstAcrossLine) {
			firstAcrossLine=false;
			won = true;
			}
		if (won) {
			winner=swimmer;
			winningTeam=team;
		}
	}
	
	//Has race been won?
	public boolean isRaceWon() {
		return !firstAcrossLine;
	}

	public int getWinner() { return winner; }
	
	public int getWinningTeam() { return winningTeam;}
}
