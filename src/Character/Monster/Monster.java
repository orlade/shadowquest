/* 433-294 Object Oriented Software Development
 * RPG Game Monster
 * Author: Oliver Lade <orlade>
 */
package Character.Monster;

import java.util.EnumMap;

import org.newdawn.slick.SlickException;

import Character.Bonus;
import Character.Player;
import Character.Unit;
import Game.GameplayState;
import Game.Item;
import Game.RPG;
import World.RandomFactory;
import World.WorldGold;
import World.WorldMap;


public abstract class Monster extends Unit
{
	/** The scan range of the monster (how close targets must be to be detected) */
	private int detectionRange = 150;
	
    /** Create a new Monster object.
     * 
     * @param x The x coordinate of the monster's starting position.
     * @param y The y coordinate of the monster's starting position.
     * @throws SlickException
     */
    public Monster(double x, double y, int level)
    throws SlickException
    {
		// Initilialise the bonuses
		this.bonuses = new EnumMap<Bonus.Type, Double>(Bonus.Type.class);
		for(Bonus.Type type : Bonus.Type.values())
		{
			this.bonuses.put(type, 0.0);
		}
		
    	// Set the monster's position to the spawn position given
		this.x = x;
		this.y = y;
		
		// Set level and derive XP
		this.setLevel(level);
		this.setXp( (int)( level*level*level + 50 + Math.random()*20 ) );
		
		// Set up the monster's physical size
		this.physWidth = 34;
		this.physHeight = 44;
    }

	/** Update the player's state for a frame.
     * @author Oliver
     * 
     * @param delta Time passed since last frame (milliseconds).
     * @param gps The GameplayState, which acts as a facade. 
     */
    @Override
    public void update(int delta, GameplayState gps)
    {
    	// If dead, don't update
    	if(health <= 0)
    	{
    		this.die(gps);
    		return;
    	}
    	// Decrease the cooldown timer if necessary
    	if(this.cooldownTimer > 0)
    	{
    		this.cooldownTimer -= delta; 
			// If the attack is less than half cooled down, reset rotation
			if(this.getCooldownTimer() < this.getCooldown()/2)
			{
				this.getAvatar().rotate(-this.getAvatar().getRotation());
			}
    	}
    	
    	// Attempt to find a target to attack
    	Unit target = scanForTarget(gps);
    	if(target != null)
    	{
    		double distX = target.getX() - this.getX();
    		double distY = target.getY() - this.getY();
    		double distTotal = Math.sqrt(distX*distX + distY*distY);
    		
    		// Attack and return (don't move) if the player is within attacking range
    		if(Math.abs(distTotal) < this.attackRange)
    		{
   	    		this.attack(target);
    			return;
    		}
    		
    		double dx = (distX / distTotal) * speed * delta;
    		double dy = (distY / distTotal) * speed * delta;
    		double newX = getX() + dx;
    		double newY = getY() + dy;
    		
    		if(gps.getID() != RPG.GPSMAIN || !((WorldMap)gps.getMap()).isInTown(newX, newY))
    		{
    			this.move(newX, newY, gps);
    		}
    	}
    }

    /** Scan the area around the monster for targets to attack
     * @param gps The GameplayState, which holds the lists of units.
     * @return The target to be attacked.
     */
    Unit scanForTarget(GameplayState gps)
    {
    	// At present, the only target for a monster is the Player
    	Player p = gps.getPlayer();
    	
    	double distX = this.getX() - p.getX();
		double distY = this.getY() - p.getY();
    	double playerDist = Math.sqrt(distX*distX + distY*distY);
    	
    	// If the player is within detection range, set as target
    	if(playerDist < detectionRange)
    	{
    		return p;
    	}
    	return null;
    }
    
	@Override
    /** Kills the entity, removing it from the world (called from Unit.attack())
     * @param gps The GameplayState to remove the entity from.
     */
	public void die(GameplayState gps)
	{
		// Remove the monster from the world
		gps.getMonsters().remove(this);//(gps.getMonsters().indexOf(this), null);
		//gps.getDyingMonsters().add(this);
		
		// Create a corpse
		this.dropCorpse(gps);
		// Roll for an item drop
		dropItem(gps);
		// Debug print
		System.out.println(getName()+" was killed!");
	}
	
    /** Creates an item in the world.
     * @param gps The GameplayState to create the item in.
     */
	public void dropItem(GameplayState gps)
	{
		// Roll for gold drop
		double rand = Math.random();
		if(rand < 0.5)
		{
			double dropX = this.getX() + Math.random() * this.getPhysWidth() * (1-Math.round(Math.random())*2);
			double dropY = this.getY() + Math.random() * this.getPhysHeight() * (1-Math.round(Math.random())*2);
			// If the drop location is blocked, drop at monster position
			if(gps.getMap().isTileBlocked(dropX, dropY))
			{
				System.out.println("Drop location ["+dropX+", "+dropY+"] blocked! Dropping at current position");
				dropX = this.getX();
				dropY = this.getY();
			}
			int amount = (int)(10 + (Math.random() * 10 * this.getLevel()) * gps.getPlayer().getGoldDrop());
			try
			{
				gps.spawnWorldItem( new WorldGold(dropX, dropY, amount) );
			}
			// Handle image load failure
			catch(SlickException e)
			{
				System.out.println(e.getStackTrace());
			}
		}
		
		// Roll for item drop
		rand = Math.random();
		if(rand < gps.getPlayer().getItemDrop())
		{
			double dropX = this.getX() + Math.random() * this.getPhysWidth() * (1-Math.round(Math.random())*2);
			double dropY = this.getY() + Math.random() * this.getPhysHeight() * (1-Math.round(Math.random())*2);
			// If the drop location is blocked, drop at monster position
			if(gps.getMap().isTileBlocked(dropX, dropY))
			{
				System.out.println("Drop location ["+dropX+", "+dropY+"] blocked! Dropping at current position");
				dropX = this.getX();
				dropY = this.getY();
			}
			
			// Watch for SlickException loading Image for avatar
			try
			{
				// Choose a random number between 0 and 4 inclusive
				switch((int)(Math.random()*10))
				{
				// More gold
				case 0:
					int amount = (int)(10 + (Math.random() * 10 * this.getLevel()) * gps.getPlayer().getGoldDrop());
					gps.spawnWorldItem( new WorldGold(dropX, dropY, amount) );
					break;
				// Weapon
				case 1:
				case 2:
					gps.spawnWorldItem(RandomFactory.createWeapon(dropX, dropY, this.getLevel()));
					break;
				// Armour (of random type)
				case 3:
				case 4:
				case 5:
				case 6:
					gps.spawnWorldItem( RandomFactory.createArmourGeneral(
						dropX, dropY, this.getLevel(),
						Item.chooseRandomArmourType() ) );
					break;
				// Accessory
				case 7:
					gps.spawnWorldItem(RandomFactory.createAccessory(dropX, dropY, this.getLevel()));
					break;
				// Consumable
				case 8:
				case 9:
					gps.spawnWorldItem(RandomFactory.createConsumable(dropX, dropY, this.getLevel()));
					break;
				// Default is a consumable
				default:
					gps.spawnWorldItem(RandomFactory.createConsumable(dropX, dropY, this.getLevel()));
					break;
				}
			}
			// Handle image load failure
			catch(SlickException e)
			{
				System.out.println(e.getStackTrace());
			}
		}
	}
	
	/** Gets the minimum damage done by the unit.
	 * 
	 * @return The minimum damage done by the unit
	 */
	@Override
	public double getMinDamage()
	{
		double damage = this.getStrength()/4 * (this.getStrength()+100)/100;
		damage *= (1 + this.getBonuses().get(Bonus.Type.PCDAMAGE));
		damage += this.getBonuses().get(Bonus.Type.DAMAGE);
		return damage;
	}
	
	/** Gets the maximum damage done by the unit.
	 * 
	 * @return The maximum damage done by the unit
	 */
	@Override
	public double getMaxDamage()
	{
		double damage = this.getStrength()/2 * (this.getStrength()+100)/100;
		damage *= (1 + this.getBonuses().get(Bonus.Type.PCDAMAGE));
		damage += this.getBonuses().get(Bonus.Type.DAMAGE);
		return damage;
	}

	/**
	 * @return the detectionRange
	 */
	public int getDetectionRange() {
		return detectionRange;
	}
}
