package net.piemaster.shadowquest.character.villager;

/* 433-294 Object Oriented Software Development
 * RPG Villager NPC
 * Author: Oliver Lade <orlade>
 */

import java.util.EnumMap;

import net.piemaster.shadowquest.GameplayState;
import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Corpse;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Villager extends Unit
{
	/** The distance at which the villager will interact with the player */
	protected float interactDist = 55;
	/**
	 * Flag marking whether the player is interacting with the villager (to stop
	 * repeats)
	 */
	protected boolean interacting = false;
	/** The maximum time a dialogue box will last (ms) */
	protected int dialogueLife = 4000;
	/** The time until the dialogue box disappears (ms) */
	protected int dialogueTimer = 0;
	/** The string to be spoken upon interaction */
	protected String dialogueText = "";

	/** The height of the dialogue bar for interaction */
	protected int dialogueBarHeight = 16;
	/** The width of the dialogue bar for interaction */
	protected int dialogueBarWidth;

	/**
	 * Create a new Villager object
	 * 
	 * @param x
	 *            The x coordinates of the starting position
	 * @param y
	 *            The y coordinates of the starting position
	 * @param name
	 *            The villager's name
	 * @param avatar
	 *            The Image used to represent the villager
	 * @throws SlickException
	 */
	public Villager(double x, double y, String name, Image avatar) throws SlickException
	{
		// Initilialise the bonuses
		this.bonuses = new EnumMap<Bonus.Type, Double>(Bonus.Type.class);
		for (Bonus.Type type : Bonus.Type.values())
		{
			this.bonuses.put(type, 0.0);
		}

		// Set the Villager's details
		this.x = x;
		this.y = y;
		this.name = name;
		this.avatar = avatar;
		this.physWidth = 34;
		this.physHeight = 44;
		this.health = this.maxHealth;
	}

	@Override
	/** Update the villager's state for a frame.
	 * 
	 * @param delta Time passed since last frame (milliseconds).
	 * @param gps The GameplayState, which acts as a facade. 
	 */
	public void update(int delta, GameplayState gps)
	{
		// Handle villager death
		if (this.health < 0)
		{
			this.die(gps);
			return;
		}
		// Decrease the cooldown timer if necessary
		if (this.cooldownTimer > 0)
		{
			this.cooldownTimer -= delta;
		}
		// Decrease the cooldown timer if necessary
		if (this.dialogueTimer > 0)
		{
			this.dialogueTimer -= delta;
		}
		else
		{
			this.dialogueText = "";
		}

		// Check for interaction
		if (GameplayState.distanceTo(this, gps.getPlayer()) < interactDist)
		{
			if (!this.interacting)
			{
				this.interact(gps.getPlayer());
			}
		}
		else
		{
			if (this.interacting)
			{
				this.interacting = false;
			}
		}
	}

	@Override
	/** Kills the villager and removes them from the game world
	 * 
	 * @param gps The GameplayState from which to remove the villager
	 */
	public void die(GameplayState gps)
	{
		// Create a corpse
		Image corpseAvatar = getAvatar();
		corpseAvatar.rotate(-90);
		gps.getCorpses().add(new Corpse(getX(), getY(), corpseAvatar));
		// Debug print
		System.out.println(this.getName() + " died!");
	}

	/**
	 * Render the villager's dialogue box (if necessary)
	 * 
	 * @param g
	 *            The graphics device to draw with
	 */
	@Override
	public void renderSecondary(Graphics g)
	{
		super.renderSecondary(g);
		if (!dialogueText.isEmpty())
		{
			this.renderDialogueBox(g);
		}
	}

	/**
	 * Renders a floating health box above the unit with the unit's name and
	 * health representation
	 * 
	 * @param g
	 *            The graphics object to draw with.
	 * @param world
	 *            The game world, which holds the fonts to write with.
	 */
	public void renderDialogueBox(Graphics g)
	{
		// Colours for drawing
		Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp

		// Set the width of the health bar based on the unit's name
		this.dialogueBarWidth = Math.max(70, g.getFont().getWidth(this.getDialogueText()) + 6);

		// Draw the health bar
		float barX = (float) this.getX() - dialogueBarWidth / 2;
		float barY = (float) this.getY() - this.getPhysHeight() / 2 - healthBarHeight
				- dialogueBarHeight - 5;

		g.setColor(BAR_BG);
		g.fillRect(barX, barY, dialogueBarWidth, dialogueBarHeight);

		// Draw name text (in white)
		g.setColor(VALUE);
		float textX = barX + (dialogueBarWidth - g.getFont().getWidth(this.getDialogueText())) / 2;
		float textY = barY + (dialogueBarHeight - g.getFont().getHeight(this.getDialogueText()))
				/ 2;
		g.drawString(this.getDialogueText(), textX, textY);
	}

	/**
	 * The action to take when interacting with another Unit (usually the
	 * player)
	 * 
	 * @param target
	 *            The target to interact with
	 */
	public abstract void interact(Unit target);

	/**
	 * Sets the required fields for NPC dialogue given a string to say
	 * 
	 * @param text
	 *            The message to speak
	 */
	public void setDialogue(String text)
	{
		this.dialogueText = text;
		this.dialogueTimer = this.dialogueLife;
		this.interacting = true;
	}

	/**
	 * Gets the minimum damage done by the unit.
	 * 
	 * @return The minimum damage done by the unit
	 */
	@Override
	public double getMinDamage()
	{
		return 0;
	}

	/**
	 * Gets the maximum damage done by the unit.
	 * 
	 * @return The maximum damage done by the unit
	 */
	@Override
	public double getMaxDamage()
	{
		return 0;
	}

	/**
	 * @return the interactDist
	 */
	public float getInteractDist()
	{
		return interactDist;
	}

	/**
	 * @return the interacting
	 */
	public boolean isInteracting()
	{
		return interacting;
	}

	/**
	 * @return the dialogueLife
	 */
	public int getDialogueLife()
	{
		return dialogueLife;
	}

	/**
	 * @return the dialogueTimer
	 */
	public int getDialogueTimer()
	{
		return dialogueTimer;
	}

	/**
	 * @return the dialogueText
	 */
	public String getDialogueText()
	{
		return dialogueText;
	}

	/**
	 * @return the dialogueBarHeight
	 */
	public int getDialogueBarHeight()
	{
		return dialogueBarHeight;
	}

	/**
	 * @return the dialogueBarWidth
	 */
	public int getDialogueBarWidth()
	{
		return dialogueBarWidth;
	}
}
