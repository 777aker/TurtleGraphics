package basicallyDefault;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.Queue;

public class Turtle implements Runnable {

	//the constructor for this class, not sure what it 
	//will do yet, maybe just take in its id, which
	//will also be used as a seed of sorts
	int id;
	public Turtle(int id) {
		//ok, its not actually an id its gonna be something else
		this.id = id;
		repeat();
	}
	
	//so if repeat is called go fill it again by calling this sort of
	public void repeat() {
		switch(id) {
		case 0:
			tColor = new Color(10, 10, 10);
			break;
		case 1:
			one();
			break;
		case 2:
			two();
			break;
		case 3:
			three();
			break;
		case 4:
			four();
			break;
			default:
				break;
		}
	}
	
	//ok we also need the angle it is looking toward
	public double angle;
	//and we need the color the turtle is drawing
	Color tColor = Color.MAGENTA;
	//the queue of commands for the turtle to follow
	Queue<Integer> commandQueue = new LinkedList<>();
	//how much to do that command (thats good communication skills uhg)
	Queue<Integer> amountQueue = new LinkedList<>();
	//TODO use the fram size from tw instead of the screensize
	//the x and y position of the turtle
	double x = Toolkit.getDefaultToolkit().getScreenSize().width/2, 
			y = Toolkit.getDefaultToolkit().getScreenSize().height/2;
	Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	//list of int final commands so easier to understand but doesn't slow down code because ints
	final int FORWARD = 1, RIGHT = 2, LEFT = 3, COLOR = 4, REPEAT = 5;
	//movef is a variable to determine how many times we need to go forward
	int movef;
	//this is where all the magic will happen, sort of
	public void move() {
		if(movef > 0) {
			x += Math.cos(Math.toRadians(angle));
			y += Math.sin(Math.toRadians(angle));
			movef--;
		} else if(!commandQueue.isEmpty()) {
			switch(commandQueue.remove()) {
			case FORWARD:
				movef = amountQueue.remove();
				break;
			case RIGHT:
				//change the angle
				angle -= amountQueue.remove();
				break;
			case LEFT:
				//change the angle
				angle += amountQueue.remove();
				break;
			case COLOR:
				//change the color, colors are passed as 9 digits which represent rgb
				int rgb = amountQueue.remove();
				int blue = rgb%1000;
				rgb /= 1000;
				int green = rgb%1000;
				rgb /= 1000;
				int red = rgb;
				tColor = new Color(red, green, blue);
				break;
			case REPEAT:
				amountQueue.remove();
				repeat();
				break;
				default:
					System.out.println("OOF, i messed up the commands");
					break;
			}
		}
	}
	
	public void controlMove() {
		if(right) {
			angle += 1;
		}
		if(left) {
			angle -= 1; 
		}
		if(up) {
			x += Math.cos(Math.toRadians(angle));
			y += Math.sin(Math.toRadians(angle));
		}
		if(down) {
			x -= Math.cos(Math.toRadians(angle));
			y -= Math.sin(Math.toRadians(angle));
		}
	}
	
	//booleans for which keys are currently being held
	boolean right = false, left = false, up = false, down = false;
	//setters for which key is currently being held
	public void setRight(boolean r) {right = r;}
	public void setLeft(boolean l) {left = l;}
	public void setUp(boolean u) {up = u;}
	public void setDown(boolean d) {down = d;}
	//color variables
	int red = 10, green = 10, blue = 10;
	public void setColor(int r, int g, int b) {
		red += r;
		green += g;
		blue += b;
		if(red > 255)
			red = 0;
		if(green > 255)
			green = 0;
		if(blue > 255)
			blue = 0;
		tColor = new Color(red, green, blue);
	}
	
	//this is how an outside program can add commands to a turtle
	public void addCommand(int c, int a) {
		commandQueue.add(c);
		amountQueue.add(a);
	}
	
	//how much of a thick boi the turtle is
	int penWidth = 2;
	//each turtle is going to draw itself bc easy
	public void paintTurtle(Graphics g) {
		//so draw your color at your point
		g.setColor(tColor);
		//int x and y are doubles for best accuracy but
		//can only draw points as ints so casting x and y to int
		g.fillRect((int)x, (int)y, penWidth, penWidth);
	}
	
	//this is a variable to stop the thread, dont wanna waste power
	boolean run = true;
	//the speed of the turtle
	int speed = 3;
	//this is so we can thread the turtles and have
	//them all running around at the same time
	public void run() {
		//try to do this and if it fails go do the catch thing
		try {
			//just do this forever
			while(run) {
				//if its not a controllable turtle move normally
				if(id != 0) {
					move();
				} else {
					//this is a special method for moving control turtles
					controlMove();
				}
				//now we tell it to slow down bc we
				//dont need maximum speed computer can go thats excessive,
				//and looks ridiculous
				Thread.sleep(speed);
			}
		//so if something happened (which wont) tell me what happened
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getStackTrace());
		}
		
		
	}
	
	//this int keeps track of how many times the turtle has repeated its loop
		//just for fun math stuff and cool drawings
		int looped = 1;
		
		//if turtles id is 1 draw this set
		public void one() {
			for(int i = 0; i < 3; i++) {
				addCommand(FORWARD, 10*looped);
				addCommand(RIGHT, 120);
				int r = (int)(Math.random()*255);
				int g = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
				addCommand(COLOR, r*1000000 + g*1000 + b);
			}
			addCommand(LEFT, 120);
			for(int i = 0; i < 3; i++) {
				addCommand(FORWARD, 10*looped);
				addCommand(RIGHT, 120);
				int r = (int)(Math.random()*255);
				int g = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
				addCommand(COLOR, r*1000000 + g*1000 + b);
			}
			//reapeat the above
			addCommand(REPEAT, 0);
			looped++;
		}
		
		//ok, lets make another cool stuff
		public void two() {
			//gives the turtle a random spot on the screen and random color
			if(looped == 1) {
				int r = (int)(Math.random()*255);
				int g = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
				addCommand(COLOR, r*1000000 + g*1000 + b);
				x = (int)(Math.random()*SCREEN_SIZE.width);
				y = (int)(Math.random()*SCREEN_SIZE.height);
			}
			
			addCommand(FORWARD, looped);
			addCommand(RIGHT, 120);
			addCommand(FORWARD, looped);
			addCommand(RIGHT, 120);
			addCommand(FORWARD, looped);
			addCommand(RIGHT, 60);
			addCommand(FORWARD, looped);
			addCommand(RIGHT, looped);
			
			//repeat the stuff
			addCommand(REPEAT, 0);
			looped++;
		}
		
		//ok, another one
		public void three() {
			if(looped == 1) {
				int r = (int)(Math.random()*255);
				int g = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
				addCommand(COLOR, r*1000000 + g*1000 + b);
				x = (int)(Math.random()*SCREEN_SIZE.width);
				y = (int)(Math.random()*SCREEN_SIZE.height);
				angle = 0;
			}
			int ang = (int)(360/((int)(Math.random()*8)+2));
			int times = 360/ang;
			for(int i = 0; i < times; i++) {
				addCommand(FORWARD, looped*10);
				addCommand(RIGHT, ang);
				for(int f = 0; f < 3; f++) {
					addCommand(FORWARD, (int)Math.sqrt(looped)*10);
					addCommand(RIGHT, 120);
				}
			}
			addCommand(RIGHT, 180);
			addCommand(FORWARD, looped*10);
			addCommand(RIGHT, 180);
			
			//repeats
			addCommand(REPEAT, 0);
			looped++;
			
		}
	
		public void four() {
			
		}
		
}
