//M. M. Kuttel 2024 mkuttel@gmail.com
//
package medleySimulation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.swing.JPanel;

//CS2 - DO NOT CHANGE ANY OF THIS CLASS! (except perhaps to uncomment grid for debugging)

public class StadiumView extends JPanel implements Runnable {
	
		private static final long serialVersionUID = 1L;
		private PeopleLocation[] pplLocations; //array of the locations of the patrons
		private int numPeople;  //total number in the simulation

		private int wIncr; //width of each block
		private int hIncr; //height of each block
		private int maxY; //maximum Y  for the grid
		private int maxX; //Maximum X for the grid
		private int endPool; //where pool ends, starting block position.
		
		private final int xBorder=5;
		private final int yBorder = 5;
		

		private static Color [] laneColours = {Color.green,
		                                       Color.blue,Color.blue,Color.blue,
		                                       Color.yellow,Color.yellow,Color.yellow,
		                                       Color.blue,Color.blue,Color.blue,
		                                       Color.green};
		private final Color water = new Color(200,255,255);
		StadiumGrid grid; //shared grid
		
		StadiumView(PeopleLocation[] people,  StadiumGrid grid) { //constructor
			this.pplLocations=people; 
			numPeople = people.length;
			this.grid = grid;
			this.maxY = grid.getMaxY();
		    this.maxX= grid.getMaxX();
		    this.endPool = StadiumGrid.start_y;
		    
		    int width = getWidth();
		    int height = getHeight();
		    wIncr= width/(maxX+xBorder*2); 
		    hIncr= height/(maxY+yBorder*2);
		}
		
		//paint the picture constantly
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
		    int width = getWidth();
		    int height = getHeight();
		    wIncr= width/(maxX+xBorder*2); 
		    hIncr= height/(maxY+yBorder*2);

		    g.setColor(Color.darkGray); //background
		    g.drawRect(0,0,width,height);
		    
		    g.setColor(water);
		    g.fillRect(xBorder*wIncr,yBorder*hIncr,(maxX)*wIncr,(endPool)*hIncr); //water 
		    g.setColor(Color.white);
		    g.fillRect(xBorder*wIncr,(yBorder+endPool)*hIncr,(maxX)*wIncr,(maxY-endPool)*hIncr); //concrete
		    
		    //draw top and bottom edge
		    g.setColor(Color.lightGray);
		    g.fillRect(wIncr*xBorder, (yBorder-1)*hIncr, wIncr*(maxX), hIncr);
		    g.fillRect(wIncr*xBorder, (endPool+yBorder)*hIncr, wIncr*(maxX), hIncr*1);
		    g.setColor(Color.black);
		    
		    //draw grid lines  - uncomment to see where grid is
		  /*  g.setColor(Color.gray);
		    for (int i=0;i<=maxX;i++)  //columns 
		    		g.drawLine((i+xBorder)*wIncr, hIncr*(yBorder), (i+xBorder)*wIncr, (endPool+yBorder)*hIncr); //- leave space at bottom
		    for (int i=0;i<=endPool;i++) //rows 
		    		g.drawLine(wIncr*xBorder, (i+yBorder)*hIncr, (maxX+xBorder)*wIncr, (i+yBorder)*hIncr); //- leave space at sides
	       */
		    
		    //draw lane lines
		    g2.setStroke(new BasicStroke(3));
		    int lane=0, i=0;
		    for ( i=0;i<maxX;i+=5)  { //columns 
		    	    g.setColor(laneColours[lane]);
		    	    lane++;
		    	    g.drawLine((i+xBorder)*wIncr, hIncr*yBorder, (i+xBorder)*wIncr, (endPool+yBorder)*hIncr); //- leave space at bottom
				    g.setColor(Color.white);
				    g.fillRect((i+2+xBorder)*wIncr,(endPool+yBorder)*hIncr,wIncr, hIncr); //draw starting blocks outside pool	   
		    }
    	    g.setColor(laneColours[lane]);
    	    g.drawLine((i+xBorder)*wIncr, hIncr*yBorder, (i+xBorder)*wIncr, (endPool+yBorder)*hIncr); //draw last one

		   //draw the ovals representing people in middle of grid block
			int x,y;
			 g.setFont(new Font("Helvetica", Font.BOLD, hIncr/2));
			 		 
			 //patrons
		    for ( i=0;i<numPeople;i++){	    	
		    		if (pplLocations[i].inPool()) {
			    		g.setColor(pplLocations[i].getColor());
			    		x= (pplLocations[i].getX()+xBorder)*wIncr;
			    		y= (pplLocations[i].getY()+yBorder)*hIncr;
			    		g.fillOval(x+wIncr, y , wIncr, hIncr);
			    		//g.drawString(pplLocations[i].getID()+"",x+wIncr/4, y+wIncr/4);
		    		}
		    } 		    
		   }
	
		public int getEndPool() {
			return endPool;
		}

		public void setEndPool(int endPool) {
			this.endPool = endPool;
		}

		//the thread just continually redraws the image
		public void run() {
			while (true) {
				repaint();
			}
		}

	}


