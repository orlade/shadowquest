package World;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ExitZone
{
	/** X and Y coordinates of the zone (px) */
	private double x, y;
	/** X and Y coordinates of the zone (px) */
	private double width, height;
	/** The ID of the state this zone jumps to */
	private int targetId;
	
	/** Create a new exit zone based on an (X, Y) position (all in px)
	 * 
	 * @param x The x coordindate to create at
	 * @param y The y coordindate to create at
	 * @param width The widt of the zone
	 * @param height The height of the zone
	 * @param targetId The ID of the state the zone will jump to
	 */
	public ExitZone(double x, double y, double width, double height, int targetId)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.targetId = targetId;
	}

	/** Create a new exit zone based on tile indexes
	 * 
	 * @param x The x coordindate to create at
	 * @param y The y coordindate to create at
	 * @param width The widt of the zone
	 * @param height The height of the zone
	 * @param targetId The ID of the state the zone will jump to
	 */
	public ExitZone(int tx, int ty, double width, double height, int targetId)
	{
		this.x = tx * width;
		this.y = ty * height;
		this.width = width;
		this.height = height;
		this.targetId = targetId;
	}
	
    /** Render semi-transparent blue rect's over exit area
     * 
     * @param g The Slick graphics object, used for drawing.
     */
	public void renderMask(Graphics g, double cameraX, double cameraY)
	{ 
        g.setColor(new Color(0,0,1,0.5f));
        // Map is drawn before graphics are translated 
		g.fillRect((float)(getX()-cameraX), (float)(getY()-cameraY), (float)getWidth(), (float)getHeight());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExitZone [height=" + height + ", targetId=" + targetId
				+ ", width=" + width + ", x=" + x + ", y=" + y + "]";
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @return the targetId
	 */
	public int getTargetId() {
		return targetId;
	}

	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
}
