package geste;

public class StampedCoord  {
	int x, y;
	long timeStamp;
	
	public StampedCoord(int x, int y) {
		this.x = x;
		this.y = y; 
	}

	public StampedCoord(int x, int y, long t) {
		this(x, y);
		timeStamp = t;
	}

	public StampedCoord copy() {
		StampedCoord p = new StampedCoord(this.x, this.y);
		return p;
	}

	public double distance(StampedCoord p2) {
		double dx = p2.x - x, dy = p2.y - y;
		return Math.sqrt(dx*dx + dy*dy);
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
