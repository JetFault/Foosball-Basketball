
package runtime;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

// This line lets me say stuff like glOrtho() instead of GL11.glOrtho()
import static org.lwjgl.opengl.GL11.*;


public class Game {
	
	public static final Vec2 gravity = new Vec2(0, -50.0f);
	public static final int screenWidth = 1440;
	public static final int screenHeight = 900;

	public static final float aspectRatio = (float)screenWidth / (float)screenHeight;
	
	public static final float projectionHeight = 100;
	public static final float projectionWidth = aspectRatio * projectionHeight;
	
	public static final int drawRate = 60; // the number of frames to draw per second
	public static final long drawDuration = 1000 / drawRate; // The amount of time a frame should take to draw
	
	public static final int updateRate = 60; // The number of times per second to update the game world
	public static final long updateDuration = 1000 / updateRate; // The amount of time a physics update should take
	
	// Physics simulation parameters
	public static final float timeStep = 1f / updateRate;
	public static final int velocityIterations = 6;
	public static final int positionIterations = 2;
	
	public static final long gameStartTime = getTime();
	public static final GameWorld gameWorld = new GameWorld();
	
	/* 0->w 1->s 2->up 3->down 4->left 5->right 6->a 7->d 8->space*/
	public static boolean[] keyPressed = new boolean[9];
	
	public static long lastTimeFired = 0;
	public static long lastTimeSwitched = 0;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			DisplayMode dm = new DisplayMode(Game.screenWidth, Game.screenHeight);
			Display.setDisplayMode(dm);
			Display.create();
		}
		catch (LWJGLException e) {
			System.err.println(e.getMessage());
		}
		
		// Set up projection matrix for 2d world!
		setupProjection();
		
		gameWorld.initialize(gravity, false);
		
		long start;
		long end;
		
		// OH HEY IT'S THE MAIN GAME LOOP
		while (!Display.isCloseRequested()) {
			start = getTime();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			if(getGameTime() % 200 == 0) {
				gameWorld.spawnBalls();
			}
			
/*			if(getGameTime() % 300 == 0) {
				if (gameWorld.bucketBody.getAngularVelocity() == 0.1f) {
					gameWorld.bucketBody.setAngularVelocity(-0.1f);
				}
				else {
					gameWorld.bucketBody.setAngularVelocity(0.1f);
				}
			}
	*/		
			// Simulate physics world
			gameWorld.step();
			
			// Draw physics objects
			gameWorld.draw();
			
			// Draw non physics objects
			
			pollInput();
			
			Display.update();
			
			end = getTime();
			long frameDuration = end - start;
			
			// Sleep for any extra time.
			if (frameDuration < drawDuration){
				try {
					Thread.sleep(drawDuration - frameDuration);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		Display.destroy();
	}
	
    public static void pollInput() {
    	
    	if(Mouse.isButtonDown(0)) {
    		long delay = 250;
    		if((getTime() - lastTimeFired) > delay) {
    			lastTimeFired = getTime();
    			int x = Mouse.getX();
    			int y = Mouse.getY();

    			float spawnX = 10.0f;
    			float spawnY = 20.0f;

    			//velY = 90.0f;
    			//velX = (random.nextFloat()*10.0f) + 20.0f;

    			float velX = (float)x - spawnX;
    			float velY = (float)y - spawnY;

    			Vec2 velocityLeftBallVec2 = new Vec2(velX, velY);
    			Vec2 posLeftBallVec2 = new Vec2(spawnX, spawnY);
    			ShapeObjects.createBall(gameWorld.getWorld(), 1, posLeftBallVec2, velocityLeftBallVec2);    			
    		}   		
    	}
    	
    	
    	while(Keyboard.next()) {
    		if(Keyboard.getEventKeyState()) {
    			if(Keyboard.getEventKey() == Keyboard.KEY_UP) {
    				System.out.println("Up Arrow Pressed!");
    				keyPressed[2] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
    				System.out.println("Down Arrow Pressed!");
    				keyPressed[3] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
    				System.out.println("Left Arrow Pressed!");
    				keyPressed[4] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
    				System.out.println("Right Arrow Pressed!");
    				keyPressed[5] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_W) {
    				System.out.println("Key A Pressed!");
    				keyPressed[0] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_S) {
    				System.out.println("Key D Pressed!");
    				keyPressed[1] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_A) {
    				System.out.println("Key A Pressed!");
    				keyPressed[6] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_D) {
    				System.out.println("Key D Pressed!");
    				keyPressed[7] = true;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
    				System.out.println("Key Space Pressed!");
    				
    	    		Body b1 = gameWorld.player1PlatArrayList.get(0);
    	    		Body b2 = gameWorld.player2PlatArrayList.get(0);
    	    		gameWorld.player1PlatArrayList.remove(0);
    	    		gameWorld.player2PlatArrayList.remove(0);
    	    		gameWorld.player1PlatArrayList.add(b2);
    	    		gameWorld.player2PlatArrayList.add(b1);
    	    		
    	    		if (gameWorld.player1PlatArrayList.get(0).getFixtureList().getUserData() == ShapeObjects.blueData){
    	        		gameWorld.player1PlatArrayList.get(0).getFixtureList().setUserData(ShapeObjects.redData);
    	        		gameWorld.player2PlatArrayList.get(0).getFixtureList().setUserData(ShapeObjects.blueData);
    	    		}
    	    		else {
    	    			gameWorld.player1PlatArrayList.get(0).getFixtureList().setUserData(ShapeObjects.blueData);
    	        		gameWorld.player2PlatArrayList.get(0).getFixtureList().setUserData(ShapeObjects.redData);
    				}
    				
    				keyPressed[8] = true;
    			}
    		}
    		else {
    			if(Keyboard.getEventKey() == Keyboard.KEY_UP) {
    				System.out.println("Up Arrow Released!");
    				keyPressed[2] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
    				System.out.println("Down Arrow Released!");
    				keyPressed[3] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
    				System.out.println("Left Arrow Pressed!");
    				keyPressed[4] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
    				System.out.println("Right Arrow Pressed!");
    				keyPressed[5] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_W) {
    				System.out.println("Key W Pressed!");
    				keyPressed[0] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_S) {
    				System.out.println("Key S Pressed!");
    				keyPressed[1] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_A) {
    				System.out.println("Key A Pressed!");
    				keyPressed[6] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_D) {
    				System.out.println("Key D Pressed!");
    				keyPressed[7] = false;
    			}
    			else if(Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
    				System.out.println("Key Space Pressed!");
    				keyPressed[8] = false;
    			}
    		}
    	}
    	
    	/* Player 1 */
    	if(keyPressed[0]) {
    		Rotate(gameWorld.player1PlatArrayList,true,false);
    	}
    	else if(keyPressed[1]) {
    		Rotate(gameWorld.player1PlatArrayList,false,false);
    	}
    	else {
    		Rotate(gameWorld.player1PlatArrayList,false,true);
    	}
    	
    	/* Player 1 */
    	if(keyPressed[6]) {
    		Move(gameWorld.player1PlatArrayList,true,false);
    	}
    	else if(keyPressed[7]) {
    		Move(gameWorld.player1PlatArrayList,false,false);
    	}
    	else {
    		Move(gameWorld.player1PlatArrayList,false,true);
    	}
    	
    	/* Player 2 */
    	if(keyPressed[2]) {
    		Rotate(gameWorld.player2PlatArrayList,true,false);
    	}
    	else if(keyPressed[3]) {
    		Rotate(gameWorld.player2PlatArrayList,false,false);
    	}
    	else {
    		Rotate(gameWorld.player2PlatArrayList,false,true);
    	}
    	
    	/* Player 2 */
    	if(keyPressed[4]) {
    		Move(gameWorld.player2PlatArrayList,true,false);
    	}
    	else if(keyPressed[5]) {
    		Move(gameWorld.player2PlatArrayList,false,false);
    	}
    	else {
    		Move(gameWorld.player2PlatArrayList,false,true);
    	}

    	if(keyPressed[8]) {


    	} 
    }

	public static void Rotate(ArrayList<Body> list, boolean clockwise, boolean stop) {
		float angVel = 3.0f;
		if(!clockwise)
			angVel = -1.0f * angVel;
		if(stop)
			angVel = 0.0f;
		
		for(Body body : list) {			
			body.setAngularVelocity(angVel);
		}
	}
	
	public static void Move(ArrayList<Body> list, boolean left, boolean stop) {
		float x = 35f;
		float y = 0;
		
		if(left)
			x = -1 * x;
		if(stop)
			x = 0.0f;
		
		Vec2 vel = new Vec2(x, y);
		
		for(Body body : list) {			
			body.setLinearVelocity(vel);
		}
	}
	
	// Gets the current system time
	public static long getTime(){
		return System.nanoTime() / 1000000;
	}
	
	// Returns the amount of time that has passed since the game started
	public static long getGameTime(){
		return getTime() - gameStartTime;
	}
	
	private static void setupProjection() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, projectionWidth, 0, projectionHeight, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glLoadIdentity();
	}
}
