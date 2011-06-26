package net.piemaster.shadowquest.world;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public interface WorldObject
{
	/**
	 * Render the object in the game world.
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g);

	/** Serialises the unit */
	@Override
	public String toString();

	/**
	 * @return the x
	 */
	public double getX();

	/**
	 * @return the y
	 */
	public double getY();

	/**
	 * @param the
	 *            x coord to set
	 */
	public void setX(double x);

	/**
	 * @param the
	 *            y coord to set
	 */
	public void setY(double y);

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @return the avatar
	 */
	public Image getAvatar();

	/**
	 * @return the physWidth
	 */
	public int getPhysWidth();

	/**
	 * @return the physHeight
	 */
	public int getPhysHeight();
}
