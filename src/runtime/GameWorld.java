package runtime;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.JointType;
import org.lwjgl.util.Color;

public class GameWorld {

	private World world;
	
	// Player Controls
	public ArrayList<Body> player1PlatArrayList;
	public ArrayList<Body> player2PlatArrayList;
	
	public Body bucketBody;
	
	public World getWorld() {
		return world;
	}

	private void setWorld(World world) {
		this.world = world;
	}
	
	public ArrayList<Body> getPlayer1Plat() {
		return this.player1PlatArrayList;
	}
	
	public ArrayList<Body> getPlayer2Plat() {
		return this.player2PlatArrayList;
	}

	public GameWorld(){
		player1PlatArrayList = new ArrayList<>();
		player2PlatArrayList = new ArrayList<>();
	}

	public void initialize(Vec2 gravity, boolean sleep /*, LevelDef level*/){
		setWorld(new World(gravity, sleep));
		
		Vec2 pos = new Vec2((float)Game.projectionWidth / 2,(float) Game.projectionHeight / 2);
		
		/* Create Hoop */
		ShapeObjects.createComplex(world, 0.0f);

		//Spawn the Platforms
//		spawnPlatforms();
		
		ShapeObjects.createBlocker(world, new Vec2((float)Game.projectionWidth/2.0f + 8f, (float)Game.projectionHeight - 10 - 10f*(2)),1);
		ShapeObjects.createBlocker(world, new Vec2((float)Game.projectionWidth/2.0f + 8f, (float)Game.projectionHeight - 10 - 10f*(4)),2);

		
		// Make ground
/*		PolygonShape shape = new PolygonShape();
		shape.setAsBox(100, 100);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.userData = new FixtureUserData(Color.RED, false);
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position = new Vec2(0, -90f);
		bd.angle = 0;
		
		Body body = world.createBody(bd);
		body.createFixture(fd);
*/		
	}
	
	// Simulate the world one step
	public void step(){
		world.step(Game.timeStep, Game.velocityIterations, Game.positionIterations);
	}
	
	// Draw the world
	public void draw(){
		// Iterate over all the bodies in the world and draw each of their fixtures
		for (Body body = world.getBodyList(); body != null; body = body.getNext()){
			DrawingTool.drawBody(body);
		}
	}
	
	public void spawnBalls() {
		Random random = new Random();
		float velY, velX;
		
		velY = 90.0f;
		velX = (random.nextFloat()*10.0f) + 20.0f;
		Vec2 velocityLeftBallVec2 = new Vec2(velX, velY);
		Vec2 posLeftBallVec2 = new Vec2(10.0f, 20);
		ShapeObjects.createBall(world, 1, posLeftBallVec2, velocityLeftBallVec2);
		
		velX = (random.nextFloat()*20.0f) + 20.0f;
		Vec2 velocityRightBallVec2 = new Vec2(-velX, velY);
		Vec2 posRightBallVec2 = new Vec2(Game.projectionWidth - 10.0f, 20);
		ShapeObjects.createBall(world, 1, posRightBallVec2, velocityRightBallVec2);
	}
	
	public void spawnPlatforms() {
		/* Make Platforms */
		//ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 0f, (float)Game.projectionHeight - 10 - 10f));
		
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 8f, (float)Game.projectionHeight - 10 - 10f*(2)),2);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f - 8f, (float)Game.projectionHeight - 10 - 10f*(2)),1);
		
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 8f, (float)Game.projectionHeight - 10 - 10f*(3)),1);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f - 8f, (float)Game.projectionHeight - 10 - 10f*(3)),2);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 3*8f, (float)Game.projectionHeight - 10 - 10f*(3)),2);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f - 3*8f, (float)Game.projectionHeight - 10 - 10f*(3)),1);
		
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 8f, (float)Game.projectionHeight - 10 - 10f*(4)),2);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f - 8f, (float)Game.projectionHeight - 10 - 10f*(4)),1);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 3*8f, (float)Game.projectionHeight - 10 - 10f*(4)),1);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f - 3*8f, (float)Game.projectionHeight - 10 - 10f*(4)),2);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f + 5*8f, (float)Game.projectionHeight - 10 - 10f*(4)),2);
		ShapeObjects.createPlatform(world,new Vec2((float)Game.projectionWidth/2.0f - 5*8f, (float)Game.projectionHeight - 10 - 10f*(4)),1);	
	}
}
