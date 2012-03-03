package runtime;

import org.lwjgl.util.ReadableColor;

public class FixtureUserData {
	public ReadableColor color;
	public boolean filled = false;
	public boolean sensor = false;
	
	public FixtureUserData(ReadableColor color, boolean filled){
		this.color = color;
		this.filled = filled;
	}
	
	public FixtureUserData(ReadableColor color, boolean filled, boolean sensor){
		this.color = color;
		this.filled = filled;
		this.sensor = sensor;
	}
}
