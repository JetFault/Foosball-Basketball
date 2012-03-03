package runtime;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import org.jbox2d.common.MathUtils;

public class ShapeObjects {
	
	public static FixtureUserData redData, blueData;
	
	public static void createPlatform(World world, Vec2 pos, int player) {
		PolygonShape platform = new PolygonShape();
		platform.setAsBox(5, 0.3f);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = platform;
		fd.friction = 0.1f;
		fd.restitution = 0.8f;
		
		ReadableColor color = ReadableColor.WHITE;
		if(player == 1) {
			color = ReadableColor.RED;
		}
		else if(player == 2) {
			color = ReadableColor.BLUE;
		}		
		fd.userData = new FixtureUserData(color, true);
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.KINEMATIC;
		bd.position = pos;
		bd.angle = 0;
		
		Body body = world.createBody(bd);
		body.createFixture(fd);
		
		if(player == 1) {
			Game.gameWorld.getPlayer1Plat().add(body);
		}
		else if(player == 2) {
			Game.gameWorld.getPlayer2Plat().add(body);
		}
	}
	
	public static void createBlocker(World world, Vec2 pos, int player) {
		PolygonShape platform = new PolygonShape();
		platform.setAsBox(8, 0.3f);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = platform;
		fd.friction = 0.1f;
		fd.restitution = 0.8f;
		
		ReadableColor color = ReadableColor.WHITE;
		if(player == 1) {
			color = ReadableColor.RED;
		}
		else if(player == 2) {
			color = ReadableColor.BLUE;
		}		
		fd.userData = new FixtureUserData(color, true);
		if (player == 1) redData = (FixtureUserData)fd.userData;
		if (player == 2) blueData = (FixtureUserData)fd.userData;
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.KINEMATIC;
		bd.position = pos;
		bd.angle = 0;
		
		Body body = world.createBody(bd);
		body.createFixture(fd);
		
		if(player == 1) {
			Game.gameWorld.getPlayer1Plat().add(body);
		}
		else if(player == 2) {
			Game.gameWorld.getPlayer2Plat().add(body);
		}
	}
	
	public static void createBall(World world, float radius, Vec2 pos, Vec2 velocity) {
		CircleShape circles = new CircleShape();
		circles.m_radius = radius;
		
		FixtureDef fd = new FixtureDef();
		fd.shape = circles;
		fd.friction = 0.2f;
		fd.restitution = 0.4f;
		fd.density = 10.0f;
		fd.userData = new FixtureUserData(Color.WHITE, true);
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position = pos;
		bd.linearVelocity = velocity;
		bd.fixedRotation = false;
	
		Body body = world.createBody(bd);
		body.createFixture(fd);		
	}

	public static void createComplex(World world, float angle) {
		float midX = Game.projectionWidth/2.0f;
		float midY = Game.projectionHeight/2.0f;
		Vec2 midpt = new Vec2(midX,midY-25.0f);	
		
		float height = 10.0f;
		float width = 40.0f;
		float fatness = 0.3f;
				
		//Make Anchor		
		PolygonShape anchor = new PolygonShape();
		anchor.setAsBox(0.1f, 0.1f);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = anchor;
		//fDef.friction = 0.5f;
		fDef.userData = new FixtureUserData(Color.WHITE, false);
		
		BodyDef bDef = new BodyDef();
		//bDef.type = BodyType.KINEMATIC;
		Vec2 anchorTip = new Vec2(midpt.x, midpt.y + height);
		bDef.position.set(anchorTip);
		Body bodyAnchor = world.createBody(bDef);
		bodyAnchor.createFixture(fDef);
		
		//Make Hoop		
		PolygonShape middle = new PolygonShape();
		middle.setAsBox(fatness, height);
		
		PolygonShape bottomLeft = new PolygonShape();
		bottomLeft.setAsBox(width/2, fatness, new Vec2(0.0f-width/2,-height), 0.0f);
		
		PolygonShape bottomRight = new PolygonShape();
		bottomRight.setAsBox(width/2, fatness, new Vec2(0.0f+width/2,-height), 0.0f);
		
		float sideX = width;
		float sideY = height * (2.0f/3.0f);
		
		PolygonShape left = new PolygonShape();
		left.setAsBox(fatness, height/3.0f, new Vec2(-sideX,-sideY), 0.0f);
		
		PolygonShape right = new PolygonShape();
		right.setAsBox(fatness, height/3.0f, new Vec2(sideX,-sideY), 0.0f);

		BodyDef bd = new BodyDef();
		bd.type = BodyType.KINEMATIC;
		bd.position.set(midpt);
		bd.angle = angle * MathUtils.DEG2RAD;
		bd.fixedRotation = false;
		Body bodyHoop = world.createBody(bd);
		
		//bodyHoop.setAngularVelocity(5.0f);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = middle;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.userData = new FixtureUserData(Color.WHITE, true);		
		bodyHoop.createFixture(fd);
		
		fd.shape = bottomLeft;	
		fd.userData = new FixtureUserData(Color.RED, true);
		bodyHoop.createFixture(fd);
		fd.shape = left;
		bodyHoop.createFixture(fd);
		
		fd.shape = bottomRight;		
		fd.userData = new FixtureUserData(Color.BLUE, true);
		bodyHoop.createFixture(fd);		
		fd.shape = right;
		bodyHoop.createFixture(fd);		
		
		
		//Make Joint
		RevoluteJointDef rjd = new RevoluteJointDef();
		rjd.initialize(bodyAnchor, bodyHoop, anchorTip);
		rjd.collideConnected = true;
		rjd.lowerAngle = -90 * MathUtils.DEG2RAD;
		rjd.upperAngle = 90 * MathUtils.DEG2RAD;
		rjd.maxMotorTorque = 50.0f;
		rjd.enableLimit = true;
		rjd.enableMotor = false;
		world.createJoint(rjd);
		
		
		Game.gameWorld.bucketBody = bodyHoop;
	}
	
}
