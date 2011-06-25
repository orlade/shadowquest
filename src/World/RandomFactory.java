package World;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Character.Bonus;
import Game.Item;

public abstract class RandomFactory
{
	public static WorldWeapon createWeapon(double x, double y, int mlvl)
	throws SlickException
	{
		Image avatar = new Image("assets/items/sword.png");
		double minDamage = Math.random() * mlvl * 5;
		double maxDamage = (minDamage+1) + Math.random() * mlvl * 2;
		
		// Generate bonuses
		ArrayList<Bonus> bonuses = new ArrayList<Bonus>(0);
		String suffix = "";
		int numBonuses = Math.max(0, (int)(Math.random() * 4 - (3.0/mlvl)));
		if(numBonuses > 0)
		{
			for(int i=0; i < numBonuses; i++)
			{
				bonuses.add(generateBonus(mlvl));
			}
			suffix = getBonusSuffix(bonuses.get(0));
		}
		
		// Determine the name (and hence avatar)
		String name;
		if(maxDamage < 20)
		{
			name = "Short Sword";
		}
		else if(maxDamage < 40)
		{
			name = "Long Sword";
		}
		else
		{
			name = "Claymore";
		}
		
		if(!suffix.isEmpty())
		{
			name += " " + suffix;
		}
		
		// Create and return the item
		return new WorldWeapon(x, y, name, avatar, bonuses, minDamage, maxDamage);
	}
	
	public static WorldAccessory createAccessory(double x, double y, int mlvl)
	throws SlickException
	{
		Image avatar = new Image("assets/items/amulet.png");
		
		// Generate bonuses
		ArrayList<Bonus> bonuses = new ArrayList<Bonus>(0);
		String suffix = "";
		int numBonuses = Math.max(1, (int)(Math.random() * 4 - (3.0/mlvl)));
		if(numBonuses > 0)
		{
			for(int i=0; i < numBonuses; i++)
			{
				bonuses.add(generateBonus(mlvl));
			}
			suffix = getBonusSuffix(bonuses.get(0));
		}
		
		// Determine the name (and hence avatar)
		String name;
		switch((int)(Math.random()*3))
		{
		case 0:
			name = "Amulet";
			break;
		case 1:
			name = "Ring";
			break;
		case 2:
			name = "Necklace";
			break;
		default:
			name = "Amulet";
		}
		
		if(!suffix.isEmpty())
		{
			name += " " + suffix;
		}
		
		// Create and return the item
		return new WorldAccessory(x, y, name, avatar, bonuses);
	}
	
	public static WorldArmour createArmour(double x, double y, int mlvl)
	throws SlickException
	{
		Image avatar = new Image("assets/items/elven_shield.png");
		int armourRating = (int)(2*mlvl + Math.random() * mlvl * 4);
		
		// Generate bonuses
		ArrayList<Bonus> bonuses = new ArrayList<Bonus>(0);
		String suffix = "";
		int numBonuses = Math.max(0, (int)(Math.random() * 4 - (3.0/mlvl)));
		if(numBonuses > 0)
		{
			for(int i=0; i < numBonuses; i++)
			{
				bonuses.add(generateBonus(mlvl));
			}
			suffix = getBonusSuffix(bonuses.get(0));
		}
		
		// Determine the name (and hence avatar)
		String name;
		if(armourRating < 10)
		{
			name = "Leather Armour";
		}
		else if(armourRating < 30)
		{
			name = "Chain Mail";
		}
		else
		{
			name = "Plate Mail";
		}
		
		if(!suffix.isEmpty())
		{
			name += " " + suffix;
		}
		
		// Create and return the item
		return new WorldArmour(x, y, name, Item.Type.ARMOUR, avatar, bonuses, armourRating);
	}
	
	/** Generates a piece of armour of the given type
	 * 
	 * @param x The x coord to spawn
	 * @param y The y coord to spawn
	 * @param mlvl The level of the monster dropping the item
	 * @param type The Item type to drop
	 * @return A reference to the dropped item
	 * @throws SlickException
	 */
	public static WorldArmour createArmourGeneral(double x, double y, int mlvl, Item.Type type)
	throws SlickException
	{
		// Determine armour rating
		int armourRating;
		switch(type)
		{
		case ARMOUR:
		case SHIELD:
			armourRating = (int)(2*mlvl + Math.random() * mlvl * 4);
			break;
		case HELMET:
		case GLOVES:
		case BOOTS:
			armourRating = (int)(1*mlvl + Math.random() * mlvl * 2);
			break;
		default:
			armourRating = (int)(1*mlvl + Math.random() * mlvl * 2);
			break;
		}
		
		// Determine the name (and hence avatar)
		String name = "UNDEFINED";
		if(armourRating < 10)
		{
			switch(type)
			{
			case ARMOUR:	name = "Leather Armour";	break;
			case SHIELD:	name = "Buckler";			break;
			case HELMET:	name = "Cap";				break;
			case GLOVES:	name = "Fur Gloves";		break;
			case BOOTS:		name = "Fur Boots";			break;
			}
			
		}
		else if(armourRating < 30)
		{
			switch(type)
			{
			case ARMOUR:	name = "Chain Mail";		break;
			case SHIELD:	name = "Large Shield";		break;
			case HELMET:	name = "Helm";				break;
			case GLOVES:	name = "Iron Gloves";		break;
			case BOOTS:		name = "Iron Boots";		break;
			}
		}
		else
		{
			switch(type)
			{
			case ARMOUR:	name = "Plate Mail";		break;
			case SHIELD:	name = "Tower Shield";		break;
			case HELMET:	name = "Crown";				break;
			case GLOVES:	name = "Gauntlets";			break;
			case BOOTS:		name = "Greaves";			break;
			}
		}
		
		// Determine the avatar
		Image avatar;
		switch(type)
		{
		case ARMOUR:
		case SHIELD:
		case HELMET:
		case GLOVES:
		case BOOTS:
			avatar = new Image("assets/items/elven_shield.png");
			break;
		default:
			avatar = new Image("assets/items/elven_shield.png");
			break;
		}
		
		// Determine bonuses and suffix
		ArrayList<Bonus> bonuses = generateBonuses(mlvl);
		if(!bonuses.isEmpty())
		{
			String suffix = getBonusSuffix(bonuses.get(0));
			name += " " + suffix;
		}
		
		// Create and return the item
		return new WorldArmour(x, y, name, type, avatar, bonuses, armourRating);
	}
	
	/** Creates a randomised consumable based on the dropper's level
	 * 
	 * @param x The x coord to spawn at
	 * @param y The y coord to spawn at
	 * @param mlvl The level of the dropper
	 * @return A reference to the dropped item
	 * @throws SlickException
	 */
	public static WorldConsumable createConsumable(double x, double y, int mlvl)
	throws SlickException
	{
		Image avatar = new Image("assets/items/potion.png");
		
		// Determine the properties and name
		String name;
		ArrayList<Bonus> bonus = new ArrayList<Bonus>(1);
		switch((int)(Math.random()*3))
		{
		case 0:
			name = "Health Potion";
			bonus.add(new Bonus(Bonus.Type.HP, 25));
			break;
		case 1:
			name = "Energy Potion";
			bonus.add(new Bonus(Bonus.Type.MP, 25));
			break;
		case 2:
			name = "Scroll of Bartel";
			bonus.add(new Bonus(Bonus.Type.STRENGTH, 5));
			break;
		default:
			name = "Health Potion";
			bonus.add(new Bonus(Bonus.Type.HP, 25));
			break;
		}
		
		// Create and return the item
		return new WorldConsumable(x, y, name, avatar, bonus);
	}
	
	/** Generates a list of bonuses, depending on the dropper's level
	 * 
	 * @param mlvl The level of the dropper
	 * @return The list of bonuses
	 */
	public static ArrayList<Bonus> generateBonuses(int mlvl)
	{
		// Generate bonuses
		ArrayList<Bonus> bonuses = new ArrayList<Bonus>(0);
		int numBonuses = Math.max(0, (int)(Math.random() * 4 - (3.0/mlvl)));
		if(numBonuses > 0)
		{
			for(int i=0; i < numBonuses; i++)
			{
				bonuses.add(generateBonus(mlvl));
			}
			return bonuses;
		}
		else
		{
			return Bonus.EMPTY_BONUS;
		}
	}
	
	/** */
	private static Bonus generateBonus(int mlvl)
	{
		// Determine bonus type
		int type = (int)(Math.random() * 10 + 1);
		
		// Determine bonus value
		Bonus bonus;
		switch(type)
		{
		// Damage
		case 1:
			bonus = new Bonus(Bonus.Type.DAMAGE, 0.2 * getFairValue(mlvl));
			break;
		// Max HP
		case 2:
			bonus = new Bonus(Bonus.Type.MAXHP, 0.3 * getFairValue(mlvl));
			break;
		// Speed
		case 3:
			bonus = new Bonus(Bonus.Type.SPEED, 0.05 * Math.max(1, getFairValue(mlvl)/10));
			break;
		// Cooldown
		case 4:
			bonus = new Bonus(Bonus.Type.COOLDOWN, 25 * Math.max(1, getFairValue(mlvl)/10));
			break;
		// Item drop
		case 5:
			bonus = new Bonus(Bonus.Type.ITEMDROP, 0.05 * Math.max(1, getFairValue(mlvl)/20) );
			break;
		// Gold drop
		case 6:
			bonus = new Bonus(Bonus.Type.GOLDDROP, 0.05 * Math.max(1, getFairValue(mlvl)/20) );
			break;
		// HP regen
		case 7:
			bonus = new Bonus(Bonus.Type.HPREGEN, 0.05 * Math.max(1, getFairValue(mlvl)/10) );
			break;
		// Strength
		case 8:
			bonus = new Bonus(Bonus.Type.STRENGTH, getFairValue(mlvl)/2 );
			break;
		// Accuracy
		case 9:
			bonus = new Bonus(Bonus.Type.ACCURACY, 0.5 * Math.max(1, getFairValue(mlvl)/5) );
			break;
		// % Damage
		case 10:
			bonus = new Bonus(Bonus.Type.PCDAMAGE, 0.05 * Math.max(1, getFairValue(mlvl)/20) );
			break;
		default:
			bonus = new Bonus(Bonus.Type.MAXHP, getFairValue(mlvl));
			break;
		}
		return bonus;
	}
	
	/** Returns some scalar value that represents the value of the monster given its mlvl
	 * This vlue is used to multiply bonus factors and determine bonus values.
	 * @param mlvl The level of the slain monster
	 * @return The fair value of the monster
	 */
	private static int getFairValue(int mlvl)
	{
		double value = 10;
		for(int i=0; i < mlvl; i++)
		{
			value = value + 10 + 0.1 * value;
		}
		return (int)value + (int)(Math.random()*10);
	}
	
	private static String getBonusSuffix(Bonus bonus)
	{
		String suffix = "";
		switch(bonus.getType())
		{
		// Damage
		case DAMAGE:
			suffix = "of Rooke";
			break;
		// Max HP
		case MAXHP:
			suffix = "of Selwood";
			break;
		// Speed
		case SPEED:
			suffix = "of Wojcinski";
			break;
		// Cooldown
		case COOLDOWN:
			suffix = "of Varcoe";
			break;
		// Item drop
		case ITEMDROP:
			suffix = "of Ablett";
			break;
		// Gold drop
		case GOLDDROP:
			suffix = "of Judd";
			break;
		// HP regen
		case HPREGEN:
			suffix = "of Corey";
			break;
		// Strength
		case STRENGTH:
			suffix = "of Bartel";
			break;
		// Accuracy
		case ACCURACY:
			suffix = "of Chapman";
			break;
		// % Damage
		case PCDAMAGE:
			suffix = "of Mooney";
			break;
		default:
			suffix = "";
			break;
		}
		return suffix;
	}
}
