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
	
	public static void main(String []args) {
		//initialize the window and start the program
		TurtleWindow tw = new TurtleWindow();
	}
	
	//we are going to get the size of the screen and store it here 
	final Dimension SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	//this is the window creation and setting the default properties
	public TurtleWindow() {
		//this basically just sets the window to be full screen
		this.setSize(SCREEN_SIZE);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setResizable(false);
		//whenever a key is pressed do stuff in this class
		this.addKeyListener(new KeyListener());
		//handles mouse input and what to do
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseListener());
	}
	
	//list of turtles
	ArrayList<Turtle> turtles = new ArrayList<Turtle>();
	//all the turtles threads in a list so that we can stop all of them
	//when delete all turtles or something is called
	ArrayList<Thread> turtleThreads = new ArrayList<Thread>();
	public void makeTurtle(int id) {
		Turtle newTurt = new Turtle(id);
		turtles.add(newTurt);
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
		}
		//call each turtles little draw thing
		for(Turtle t: turtles)
			t.paintTurtle(g);
		mouseTurtle.paintTurtle(g);
		//call paint again bc we dont wanna just draw one thing
		//and give up
		repaint();
	}
	
	//list of int final commands so easier to understand but doesn't slow down code because ints
	final int FORWARD = 1, RIGHT = 2, LEFT = 3, COLOR = 4;
	
	public class KeyListener extends KeyAdapter {
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
			}
		}
	}

	//the mouse following turtle
	Turtle mouseTurtle = new Turtle(0);
	boolean first = true;
	public class MouseListener extends MouseAdapter {
		//this is when the mouse is moved do a thing
		public void mouseMoved(MouseEvent m) {
			//so now if the mouse moved we are gonna follow it because thats cool
			//using math
			int newx = m.getX();
			int newy = m.getY();
			//TODO fix this, it no work for some reason
			double distance = Math.sqrt(((newx-(int)mouseTurtle.x)^(2))+(newy-(int)mouseTurtle.y)^(2));
			
			mouseTurtle.addCommand(FORWARD, (int)distance);
		}
		//this is when the mouse is pressed do a thing
		public void mousePressed(MouseEvent m) {
			
		}
	}
	
}
