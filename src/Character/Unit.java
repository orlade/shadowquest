package Character;
/* 433-294 Object Oriented Software Development
 * RPG Game Unit
 * Author: Oliver Lade <orlade>
 */

import java.util.EnumMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import World.WorldObject;

import Game.GameplayState;
import Inventory.Inventory;
import Inventory.InventoryArmour;
import Inventory.InventoryItem;

public abstract class Unit implements WorldObject
{
	/** The item's position in the world */
	protected double x, y;
	/** The unit's name */
	protected String name;
	
	/** Whether to enable noclip for the unit */
	protected boolean noclip = false;
	/** Whether to enable god mode for the unit */
	protected boolean god = false;

	// STATS
	/** 100% of the unit's health */
	protected double maxHealth = 100;
	/** The unit's current health */
	protected double health = maxHealth;
	/** 100% of the unit's energy */
	protected double maxEnergy = 100;
	/** The unit's current energy */
	protected double energy = maxEnergy;
	
	/** The unit's strength, determines damage */
	protected double strength;
	/** The unit's defence, determines damage taken and blocking */
	protected double defence;
	
	/** The minimum time between attacks (ms) */
	protected int cooldown = 1000;
	/** The time until the next attack can be made (ms) */
	protected int cooldownTimer = 0;
	
	/** The minimum time between HP regens (ms) */
	protected int hpRegenCooldown = 2000;
	/** The time until the next regen of HP (ms) */
	protected int hpRegenTimer = 0;
	/** The amount of HP healed per regen */
	protected double hpRegenAmount = 0;
	
	/** The unit's movement speed */
	protected double speed = 0.25;
	/** The range in which the unit can attack enemies */
	protected int attackRange = 50;
	
	/** The amount of experience the unit has */
	protected int xp = 0;
	/** The unit's level */
	protected int level = 1;

	// ITEMS
	/** The inventory held by the unit */
	protected Inventory inventory = new Inventory();
	/** The equipment held by the unit */
	protected Equipment equipment = new Equipment(this);
	
	/** Mapping of types of bonuses to the unit's current value for that bonus */
	protected EnumMap<Bonus.Type, Double> bonuses; 
	
	// RENDERING
	/** The Image object representing the object in the world */
	protected Image avatar = null;
	/** The physical width of the avatar (trimmed avatar width) */
	protected int physWidth;
	/** The physical height of the avatar (trimmed avatar height) */
	protected int physHeight;
	/** Whether to flip the avatar horizontally when rendering */
	protected boolean movingLeft = false;

	/** The width of the floating health bar */
	protected float healthBarWidth;
	/** The height of the floating health bar */
	protected final float healthBarHeight = 16;

	/** Attack another unit, dealing damage and resetting the cooldown timer
	 * 
	 * @param target The unit to attack
	 */
	public void attack(Unit target)
	{
		if(cooldownTimer <= 0)
		{
			// If the attack is not dodged
			double hitChance = 100 * 2 * (double)this.getLevel() / (this.getLevel() + target.getLevel());
			if( (Math.random()*100) > 100 - hitChance - this.getBonuses().get(Bonus.Type.ACCURACY))
			{
				// Calculate how much damage to do
				double min, max, damage;

				min = this.getMinDamage();
				max = this.getMaxDamage();
				damage = min + (int)(Math.random() * (max-min));

				// Roll for critical hit
				if(Math.random() > 0.9)
				{
					damage *= 2;
					System.out.println("BIFF! ZOT!");
				}

				// Adjust for armour
				damage -= Math.sqrt(target.getArmourRating());
				// Deal the damage
				target.damage(damage);
				
				// Debug print
				System.out.println("POW! ["+this.getName()+ " attacked "+target.getName()+" for "+damage+" dmg]");
				
				if(movingLeft)
				{
					this.getAvatar().rotate(-10);
				}
				else
				{
					this.getAvatar().rotate(10);
				}
			}
			else
			{
				System.out.println(target.getName()+" dodged "+this.getName()+"'s attack!");
			}
			
			// Reduce cooldown
			this.cooldownTimer = this.cooldown;
		}
	}
	
	/** Update the unit's state in the game world */
	public abstract void update(int delta, GameplayState gps);
	
    /** Handles movement of the player once the destination is chosen
     * 
     * @param newX The x coordinate of the proposed destination.
     * @param newY The x coordinate of the proposed destination.
     * @param gps The GameplayState that holds the map for map checks.
     */
    public void move(double newX, double newY, GameplayState gps)
    {
		// Check for collisions only if clipping is on
		if(!noclip)
		{
			// Move the unit only if the new tile is unblocked
			if( !gps.getMap().isBoxBlocked(newX, y, getPhysWidth(), getPhysHeight()) &&
				!gps.isUnitBlocked(this, newX, y) )
			{
				// Check if the player has changed direction
				double dirX = (newX - this.getX());
				
				// If last move was to the left and now moving right
				if(movingLeft && dirX > 0)
				{
					movingLeft = false;
					// Set the avatar back to unflipped
					this.setAvatar(this.getAvatar().getFlippedCopy(true, false));
				}
				// If last move was to the right and now moving left
				else if(!movingLeft && dirX < 0)
				{
					movingLeft = true;
					// Set the avatar back to unflipped
					this.setAvatar(this.getAvatar().getFlippedCopy(true, false));
				}
				
				// Update position
				x = newX;
			}
			// DEBUG
			else
			{
				//System.out.println(getName()+" IS BLOCKED ON X ["+x+" -> "+newX+"]");
			}
			if( !gps.getMap().isBoxBlocked(x, newY, physWidth, physHeight) &&
				!gps.isUnitBlocked(this, x, newY) )
			{
				// Update position
				y = newY;
			}
			// DEBUG
			else
			{
				//System.out.println(getName()+" IS BLOCKED ON Y ["+y+" -> "+newY+"]");
			}
		}
		else
		{
			// Update position
			x = newX;
			y = newY;
		}
    }

	/** Render the unit in the game world, reflecting his new state.
	 * @param g The Slick graphics object, used for drawing.
	 */
	@Override
	public void render(Graphics g)
	{
		// Draw the player at his real world co-ordinates (translated graphics)
		if(movingLeft)
		{
			this.getAvatar().draw(
					(float) (getX() - this.getAvatar().getWidth()/2),
					(float) (getY() - this.getAvatar().getHeight()/2) );
		}
		else
		{
			this.getAvatar().draw(
					(float)getX() - this.getAvatar().getWidth()/2,
					(float)getY() - this.getAvatar().getHeight()/2 );
		}
	}
	
	/** Render non-avatar details (health bar)
	 * 
	 * @param g The graphics device to draw with
	 */
	public void renderSecondary(Graphics g)
	{
		if(!(this instanceof Player))
		{
			renderHealthBox(g);
		}
		
		/*
		//DEBUG: Render the unit's physical bounding box
		g.setColor(new Color(0,1,0,0.5f));
        // Map is drawn before graphics are translated 
		g.fillRect((float)(getX()-getPhysWidth()/2), (float)(getY()-getPhysHeight()/2),
				(float)getPhysWidth(), (float)getPhysHeight());
		*/
	}

	/** Renders a floating health box above the unit with the unit's name and health representation
	 * @param g The graphics object to draw with.
	 */
	public void renderHealthBox(Graphics g)
	{
		// Colours for drawing
		//Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
		Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
		Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

		// Font for drawing
		//g.setFont(world.getLabelFont());
		// Set the width of the health bar based on the unit's name
		this.healthBarWidth = Math.max(70, g.getFont().getWidth(this.getName()) + 6);

		// Draw the health bar
		float barX = (float)this.getX() - healthBarWidth/2;
		float barY = (float)this.getY() - this.getPhysHeight()/2 - healthBarHeight - 5;

		g.setColor(BAR_BG);
		g.fillRect(barX, barY, healthBarWidth, healthBarHeight );
		g.setColor(BAR);
		g.fillRect(barX, barY, (healthBarWidth * ((float)getHealth() / getMaxHealth())), healthBarHeight );

		// Draw name text (in white)
		g.setColor(VALUE);
		float textX = barX + ( healthBarWidth - g.getFont().getWidth(this.getName()) ) / 2;
		float textY = barY + ( healthBarHeight - g.getFont().getHeight(this.getName()) ) / 2;
		g.drawString(this.getName(), textX, textY);
	}

	/** Handle death of the unit, called from update() if health < 0
	 * @param gps The GameplayState to remove the unit from
	 */
	public abstract void die(GameplayState gps);
	
	/** Creates a corpse of the unit at the unit's current position
	 * @param gps The GameplayState to add the corpse to
	 */
	public void dropCorpse(GameplayState gps)
	{
		// Create a corpse
		Image corpseAvatar = getAvatar().copy();
		if(this.movingLeft)
		{
			corpseAvatar.rotate(-corpseAvatar.getRotation() + 90);
		}
		else
		{
			corpseAvatar.rotate(-corpseAvatar.getRotation() - 90);
		}
		
		// Add corpse to GPS
		gps.getCorpses().add(new Corpse(getX(), getY(), corpseAvatar));
	}
	
	/** Gets the minimum damage done by the unit.
	 * 
	 * @return The minimum damage done by the unit
	 */
	public abstract double getMinDamage();
	/** Gets the maximum damage done by the unit.
	 * 
	 * @return The maximum damage done by the unit
	 */
	public abstract double getMaxDamage();
	
	/** Adjust the value of one of the unit's bonuses
	 * 
	 * @param type The bonus to adjust
	 * @param amount How much to adjust the bonus by
	 */
	public void adjustBonus(Bonus.Type type, double amount)
	{
		this.getBonuses().put(type, this.getBonuses().get(type) + amount);
		// If decreasing max HP/MP, ensure neither is above 100%
		if(type == Bonus.Type.MAXHP && getHealth() > getMaxHealth())
		{
			setHealth(getMaxHealth());
		}
		else if(type == Bonus.Type.MAXMP && getEnergy() > getMaxEnergy())
		{
			setEnergy(getMaxEnergy());
		}
		System.out.println(getName()+" adjusted "+type+" by "+amount);
	}
	
	/** Gets the total armour rating of the unit, the sum of all their equipped armour
	 * 
	 * @return The unit's total armour rating
	 */
	public int getArmourRating()
	{
		int result = 0;
		for(InventoryItem item : this.getEquipment().getItems().values())
		{
			// If it's a piece of armour, get its armour rating
			if(item instanceof InventoryArmour)
			{
				result += ((InventoryArmour)item).getArmourRating();
			}
		}
		return result;
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
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the maxHealth
	 */
	public int getMaxHealth() {
		return (int)(maxHealth + bonuses.get(Bonus.Type.MAXHP));
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return (int)(health);
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return (int)(energy);
	}

	/**
	 * @return the maxEnergy
	 */
	public int getMaxEnergy() {
		return (int)(maxEnergy + bonuses.get(Bonus.Type.MP));
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(double health) {
		this.health = health;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	/** Deal damage to the unit
	 * 	@param damage Amount of health to subtract
	 */
	public void damage(double damage)
	{
		if(!this.isGod())
			this.health -= damage;
	}

	/** Give health to the unit
	 * 
	 * @param amount Amount of health to give to the unit
	 */
	public void heal(double amount)
	{
		System.out.println("HEALING: HP = "+this.getHealth()+", MAX = "+this.getMaxHealth()+", AMOUNT = "+amount);
		if(this.getHealth() + amount > this.getMaxHealth())
		{
			this.setHealth(this.getMaxHealth());
		}
		else
		{
			this.health += amount;
		}
	}
	
	/** Give energy to the unit
	 * 
	 * @param amount Amount of energy to give to the unit
	 */
	public void recharge(double amount)
	{
		this.energy += amount;
		if(this.getEnergy() > this.getMaxEnergy())
		{
			this.setEnergy(this.getMaxEnergy());
		}
	}

	/**
	 * @return the strength
	 */
	public int getStrength() {
		return (int)(strength + bonuses.get(Bonus.Type.STRENGTH));
	}

	/**
	 * @return the defence
	 */
	public int getDefence() {
		return (int)(defence + bonuses.get(Bonus.Type.DEFENCE));
	}
	
	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed * (1 + bonuses.get(Bonus.Type.SPEED));
	}

	/**
	 * @return the cooldown
	 */
	public int getCooldown() {
		return Math.max((int)(cooldown - bonuses.get(Bonus.Type.COOLDOWN)), 50);
	}

	/**
	 * @return the hpRegenCooldown
	 */
	public int getHpRegenCooldown() {
		return hpRegenCooldown;
	}

	/**
	 * @return the hpRegenAmount
	 */
	public double getHpRegenAmount() {
		return hpRegenAmount + bonuses.get(Bonus.Type.HPREGEN);
	}

	/**
	 * @param cooldown the cooldown to add
	 */
	public void increaseCooldown(int cooldown) {
		this.cooldown += cooldown;
	}

	/**
	 * @return the cooldownTimer
	 */
	public int getCooldownTimer() {
		return cooldownTimer;
	}

	/**
	 * @return the hpRegenTimer
	 */
	public int getHpRegenTimer() {
		return hpRegenTimer;
	}

	/**
	 * @return the attackRange
	 */
	public double getAttackRange() {
		return attackRange + bonuses.get(Bonus.Type.RANGE);
	}

	/**
	 * @return the avatar
	 */
	@Override
	public Image getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the physWidth
	 */
	public int getPhysWidth() {
		return physWidth;
	}

	/**
	 * @return the physHeight
	 */
	public int getPhysHeight() {
		return physHeight;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * @return the bonuses
	 */
	public EnumMap<Bonus.Type, Double> getBonuses() {
		return bonuses;
	}

	/**
	 * @return the xp
	 */
	public int getXp() {
		return xp;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * @param level the level to set
	 */
    public void setLevel(int level)
    {
		if(level > 0)
		{
			this.level = level;
		}
		else
		{
			this.level = 1;
		}
	}
    
	/**
	 * @param level the XP to set
	 */
    public void setXp(int xp)
    {
		if(xp > 0)
		{
			this.xp = xp;
		}
		else
		{
			this.xp = 1;
		}
	}

	/**
	 * @param maxHealth the maxHealth to add
	 */
	public void increaseMaxHealth(double maxHealth) {
		this.maxHealth += maxHealth;
	}

	/**
	 * @param maxEnergy the maxEnergy to add
	 */
	public void increaseMaxEnergy(double maxEnergy) {
		this.maxEnergy += maxEnergy;
	}

	/**
	 * @param strength the strength to add
	 */
	public void increaseStrength(double strength) {
		this.strength += strength;
	}

	/**
	 * @param speed the speed to add
	 */
	public void increaseSpeed(double speed) {
		this.speed += speed;
	}

	/**
	 * @param attackRange the attackRange to set
	 */
	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	/**
	 * @return the noclip state
	 */
	public boolean isNoclip() {
		return noclip;
	}

	/**
	 * @param noclip the noclip value to set
	 */
	public void setNoclip(boolean noclip) {
		this.noclip = noclip;
	}
	
	/** Toggles whether the unit is clip tested or not */
	public void toggleNoclip()
	{
		this.noclip = !this.noclip;
	}
	
	/**
	 * @return the god
	 */
	public boolean isGod() {
		return god;
	}
	
	/** Toggles whether the unit has god mode or not */
	public void toggleGod()
	{
		this.god = !this.god;
	}
}
