//M. M. Kuttel 2024 mkuttel@gmail.com
// Simple Thread class to update the display of a text field
package medleySimulation;

import java.awt.Color;

import javax.swing.JLabel;

import java.util.concurrent.atomic.AtomicBoolean;

//You don't need to change this class
public class CounterDisplay  implements Runnable {
	
	private FinishCounter results;
	private JLabel win;
	private static AtomicBoolean shouldStartCelebration = new AtomicBoolean(true);
		
	CounterDisplay(JLabel w, FinishCounter score) {
        this.win=w;
        this.results=score;
    }
	
	public void run() { //this thread just updates the display of a text field
        while (true) {
        	//test changes colour when the race is won
        	if (results.isRaceWon()) {
        		win.setForeground(Color.RED);
               	win.setText("Winning Team: " + results.getWinningTeam() + "!!"); 
               	synchronized (this){
               		if (shouldStartCelebration.get()) {
               			Swimmer.swimSound.pause();
		        		new Audio("celebrate.wav",true).play();
		        		shouldStartCelebration.set(false);
		        		MedleySimulation.teams[results.getWinningTeam()].celebrate();
		           }
		    	}
        	}
        	else {
        		win.setForeground(Color.BLACK);
        		win.setText("------"); 
        	}	
        }
    }
}
