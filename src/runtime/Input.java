package runtime;

import java.util.ArrayList;

import org.jbox2d.dynamics.Body;

public class Input {
	
	public void Rotate(ArrayList<Body> list, boolean clockwise, boolean stop) {
		float angVel = 1.0f;
		if(!clockwise)
			angVel = -1.0f * angVel;
		if(stop)
			angVel = 0.0f;
		
		for(Body body : list) {			
			body.setAngularVelocity(angVel);
		}
	}
}
