package geste;

public class StampedCoord  {
	int x, y;
	long timeStamp;
	String label;
	
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
		p.label = this.label;
		p.timeStamp = this.timeStamp;
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

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label == null ? "p" : label;
	}
}
