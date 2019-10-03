package basicallyDefault;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class TurtleWindow extends JFrame {
	
	public static TurtleWindow tw = new TurtleWindow();
	
	public static void main(String []args) {
		//this doesnt have anything which kinda makes me uncomfortable
	}
	
	//we are going to get the size of the screen and store it here 
	final Dimension SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	//whether or not im testing
	boolean testing = false;
	//used for testing so the whole screen isnt covered
	Dimension testingSize = new Dimension();
	//this is the window creation and setting the default properties
	public TurtleWindow() {

		testingSize.width = SCREEN_SIZE.width/2;
		testingSize.height = SCREEN_SIZE.height/2;
		//so setting window size and whether its maximized and stuff
		//if testing then wont be max size and fullscreen
		//it will most likely cause errors if the order of these is changed so dont change
		if(testing) {
			this.setSize(testingSize);
		} else {
			this.setSize(SCREEN_SIZE);
		}
		this.setUndecorated(true);
		this.setVisible(true);
		if(testing) {
			
		} else {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		this.setResizable(false);
		//whenever a key is pressed do stuff in this class
		this.addKeyListener(new KeyListener());
		//handles mouse input and what to do
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseListener());
		
		//creating mouse turtle and making it work
		turtles.add(mouseTurtle);
		turtleThreads.add(mouseThread);
		mouseThread.start();
		
	}
	
	//the mouse following turtle
	Turtle mouseTurtle = new Turtle(-1);
	Thread mouseThread = new Thread(mouseTurtle);
	
	//list of turtles
	ArrayList<Turtle> turtles = new ArrayList<Turtle>();
	//all the turtles threads in a list so that we can stop all of them
	//when delete all turtles or something is called
	ArrayList<Thread> turtleThreads = new ArrayList<Thread>();
	//we also need a seperate turtle list for the key events so
	//when a key is pressed it doesn't go through every turtle
	//just the controllable ones
	ArrayList<Turtle> controlTurtles = new ArrayList<Turtle>();
	public void makeTurtle(int id) {
		Turtle newTurt = new Turtle(id);
		turtles.add(newTurt);
		if(id == 0)
			controlTurtles.add(newTurt);
		Thread newThread = new Thread(newTurt);
		turtleThreads.add(newThread);
		newThread.start();
	}
	
	//whether the background needs to be updated or not
	//true by default so it draws the background at start up
	boolean changeBG = true;
	//variable for what color the background should be
	Color backgroundColor = Color.DARK_GRAY;
	//this is what puts stuff on the screen
	public void paint(Graphics g) {
		//if the background needs to be updated, then do
		if(changeBG) {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);
			changeBG = false;
			g.setColor(Color.white);
			g.drawString("Press 1, 2, or 3 or hold.", 10, 20);
			g.drawString("Press spacebar to create a controllable turtle.", 10, 40);
			g.drawString("Use arrows to control and - = to change color.", 10, 60);
			g.drawString("Press del to clear screen.", 10, 80);
			g.drawString("Press escape to quit.", 10, 100);
		}
		//call each turtles little draw thing
		for(Turtle t: turtles)
			t.paintTurtle(g);
		//call paint again bc we dont wanna just draw one thing
		//and give up
		repaint();
	}
	
	//list of int final commands so easier to understand but doesn't slow down code because ints
	final int FORWARD = 1, RIGHT = 2, LEFT = 3, COLOR = 4;
	
	public class KeyListener extends KeyAdapter {
		//ok so, you can only switch by likes ints and chars so i figured out the keycode for everything
		//I now realize that I could use a map so I might try that
		
		
		//this is called whenever a key is pressed
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			//so the switches are based
			//off a table of values for keyCodes
			//bc switch is dumb and wont let
			//me do e.VK_DELETE or other stuff :P
			//this is delete all the turtles if delete is pressed
			case 127:
				for(Turtle t: turtles)
					t.run = false;
				turtles.clear();
				controlTurtles.clear();
				changeBG = true;
				break;
			//if escape is pressed immediately terminate the program
			case 27:
				System.exit(0);
				break;
			//if one was pressed (not on numpad) do a thing
			case 49:
				makeTurtle(1);
				break;
			//if two was pressed (not on numpad) do a thing
			case 50:
				makeTurtle(2);
				break;
			//if three is pressed
			case 51:
				makeTurtle(3);
				break;
			//if four is passed
			case 52:
				makeTurtle(4); 
				break;
			//if m is pressed get mouseTurtle going
			case 77:
				//turtles.add(mouseTurtle);
				//turtleThreads.add(mouseThread);
				//mouseThread.start();
				break;
			//So lets add some key commands bc thats even cooler
			//first to make a new controllable turle press space
			case 32:
				makeTurtle(0);
				break;
			//arrow right
			case 39:
				for(Turtle t: controlTurtles)
					t.setRight(true);
				break;
			//arrow left
			case 37:
				for(Turtle t: controlTurtles)
					t.setLeft(true);
				break;
			//arrow up
			case 38:
				for(Turtle t: controlTurtles)
					t.setUp(true);
				break;
			//arrow down
			case 40:
				for(Turtle t: controlTurtles)
					t.setDown(true);
				break;
			//equal sign will do some color changes and minus
			case 61:
				for(Turtle t: controlTurtles)
					t.setColor(50, 10, 50);
				break;
			case 189:
				for(Turtle t: controlTurtles)
					t.setColor(-5, -5, -5);
				break;
			}
		}
		
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) {
			//arrow right
			case 39:
				for(Turtle t: controlTurtles)
					t.setRight(false);
				break;
			//arrow left
			case 37:
				for(Turtle t: controlTurtles)
					t.setLeft(false);
				break;
			//arrow up
			case 38:
				for(Turtle t: controlTurtles)
					t.setUp(false);
				break;
			//arrow down
			case 40:
				for(Turtle t: controlTurtles)
					t.setDown(false);
				break;
			}
		}
		
	}

	boolean first = true;
	public class MouseListener extends MouseAdapter {
		//this is when the mouse is moved do a thing
		public void mouseMoved(MouseEvent m) {
			//so now if the mouse moved we are gonna follow it because thats cool
			//using math
			// TODO 
			/*
			int newx = m.getX();
			int newy = m.getY();
			double turty = mouseTurtle.y;
			double turtx = mouseTurtle.x;
			//TODO optimize this mouse follow stuff, its currently awful
			//so this gets the distance from one point to the next
			double distance = Math.sqrt((newx-(int)turtx)^(2)+(newy-(int)turty)^(2));
			//this gets the angle to the next distance
			if(!Double.isNaN(distance)) {
				//mouseTurtle.angle = Math.atan((newy-(int)mouseTurtle.y)/(newx-(int)mouseTurtle.x));
				double angle, ydis, xdis;
				
				if(turty > newy) {
					ydis = turty - newy;
					if(turtx > newx) {
						
					} else if(newx > turtx) {
						
					} else {
						
					}
				} else if(newy > turty) {
					ydis = newy - turty;
					if(turtx > newx) {
						
					} else if(newx > turtx) {
						
					} else {
						
					}
				} else {
					if(turtx > newx) {
						
					} else if(newx > turtx) {
						
					}
				}
				
				mouseTurtle.addCommand(FORWARD, (int)distance);
			}*/
		}
		//this is when the mouse is pressed do a thing
		public void mousePressed(MouseEvent m) {
			
		}
	}
	
}
