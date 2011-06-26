package net.piemaster.shadowquest.world;

/* 433-294 Object Oriented Software Development
 * RPG Villager NPC
 * Author: Oliver Lade <orlade>
 */

import java.util.ArrayList;

import net.piemaster.shadowquest.GameplayState;
import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class WorldItem extends Item implements WorldObject
{
	/** The item's position in the world */
	protected double x, y;

	/** The physical width of the avatar (trimmed avatar width) */
	protected int physWidth;
	/** The physical height of the avatar (trimmed avatar height) */
	protected int physHeight;

	/** The width of the floating object bar */
	public float itemBarWidth;
	/** The height of the floating object bar */
	public final float itemBarHeight = 16;
	/** Whether to render the item bar when ALT not held */
	public boolean forceDisplayItemBar = false;

	/**
	 * Create an new item in the world
	 * 
	 * @param x
	 *            The x coordinates of the starting position
	 * @param y
	 *            The y coordinates of the starting position
	 * @param name
	 *            The item's name
	 * @param avatar
	 *            The Image used to represent the item
	 * @param bonuses
	 *            The list of bonuses to be applied to the item
	 * @throws SlickException
	 */
	public WorldItem(double x, double y, String name, Type type, Image avatar,
			ArrayList<Bonus> bonuses) throws SlickException
	{
		super(name, type, avatar, bonuses);
		// Set the item's details
		this.x = x;
		this.y = y;
		this.physWidth = 34;
		this.physHeight = 44;
	}

	/**
	 * Render the object in the game world.
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g)
	{
		this.getAvatar().draw((float) (getX() - this.getAvatar().getWidth() / 2),
				(float) (getY() - this.getAvatar().getHeight() / 2));
	}

	/**
	 * Render the item bar (if applicable) in the game world.
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 * @param gps
	 *            Facade for the player
	 */
	public void renderSecondary(Graphics g, GameplayState gps)
	{
		if (gps.isShowItemBars() || gps.getPlayer().getPickupCandidate() != null
				&& gps.getPlayer().getPickupCandidate().equals(this))
		{
			this.renderItemBar(g);
		}
	}

	/**
	 * Renders a floating box above the item with the item's name.
	 * 
	 * @param g
	 *            The graphics object to draw with.
	 */
	public void renderItemBar(Graphics g)
	{
		// Colours for drawing
		Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
		Color LABEL = new Color(0.9f, 0.9f, 0.4f); // Gold
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp

		// Set the width of the health bar based on the unit's name
		this.itemBarWidth = Math.max(70, g.getFont().getWidth(this.getName()) + 6);

		// Draw the health bar
		float barX = (float) this.getX() - itemBarWidth / 2;
		float barY = (float) this.getY() - this.getPhysHeight() / 2 - itemBarHeight - 5;

		g.setColor(BAR_BG);
		g.fillRect(barX, barY, itemBarWidth, itemBarHeight);

		// Draw name text
		if (!this.getBonuses().isEmpty())
		{
			g.setColor(LABEL);
		}
		else
		{
			g.setColor(VALUE);
		}
		float textX = barX + (itemBarWidth - g.getFont().getWidth(this.getName())) / 2;
		float textY = barY + (itemBarHeight - g.getFont().getHeight(this.getName())) / 2;
		g.drawString(this.getName(), textX, textY);
	}

	/**
	 * Removes the item from the game world
	 * 
	 * @param gps
	 *            The GameplayState from which to remove the item
	 */
	public void die(GameplayState gps)
	{
		gps.getWorldItems().remove(this);
	}

	// GETTERS
	/** @return The player's x coordinate */
	public double getX()
	{
		return x;
	}

	/** @return The player's y coordinate */
	public double getY()
	{
		return y;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y)
	{
		this.y = y;
	}

	/**
	 * @return the physWidth
	 */
	public int getPhysWidth()
	{
		return physWidth;
	}

	/**
	 * @return the physHeight
	 */
	public int getPhysHeight()
	{
		return physHeight;
	}

	/**
	 * @return the forceDisplayItemBar
	 */
	public boolean isForceDisplayItemBar()
	{
		return forceDisplayItemBar;
	}

	/**
	 * @param forceDisplayItemBar
	 *            the forceDisplayItemBar to set
	 */
	public void setForceDisplayItemBar(boolean forceDisplayItemBar)
	{
		this.forceDisplayItemBar = forceDisplayItemBar;
	}
}