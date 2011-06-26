package net.piemaster.shadowquest;

import java.util.EnumMap;

import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Player;
import net.piemaster.shadowquest.inventory.InventoryArmour;
import net.piemaster.shadowquest.inventory.InventoryItem;
import net.piemaster.shadowquest.inventory.InventoryWeapon;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GUI
{
	/** The GUI's reference to the player, on which its display depends. */
	private Player player;

	/** An Image object containing the status panel */
	private Image panel = new Image("assets/panel.png");
	/** An Image object containing the inventory panel */
	private Image invPanel = new Image("assets/invpanel.png");
	/** The image of the select cursor box in the inventory panel */
	private Image selectBox = new Image("assets/select_box.png");

	/** Player status panel height (px) */
	private final int PANELHEIGHT = 70;
	/** Player inventory panel width (px) */
	public static final int INVPANELWIDTH = 275;
	// /** Player inventory panel height (px) */
	// private final int INVPANELHEIGHT = 531;

	/** Length of one edge of a (square) icon */
	private final int iconSize = 50;
	/** The number of icons per row in the Inventory panel */
	private final int iconsPerRow = 5;
	/** The total number of rows present in inventory storage */
	private final int iconRows = 4;
	/** The time (ms) between autoscrolling movements */
	private int invScrollCooldown = 300;
	/** The time (ms) left until another horizontal autoscroll can occur */
	private int invScrollHorizTimer = 0;
	/** The time (ms) left until another vertical autoscroll can occur */
	private int invScrollVerticalTimer = 0;

	/** Currently selected panel slot index */
	private double invSelectIndex = 0;

	// COLOURS
	/** Gold label colour */
	Color colourLabel = new Color(0.9f, 0.9f, 0.4f);
	/** White text colour */
	Color colourValue = new Color(1.0f, 1.0f, 1.0f);
	/** Black bar background colour */
	Color colourBarBg = new Color(0.0f, 0.0f, 0.0f, 0.8f);
	/** Red HP bar colour */
	Color colourBarHp = new Color(0.8f, 0.0f, 0.0f, 0.8f);
	/** Blue MP bar colour */
	Color colourBarMp = new Color(0.0f, 0.0f, 0.8f, 0.8f);
	/** Yellow XP bar colour */
	Color colourBarXp = new Color(1.0f, 1.0f, 0.3f, 0.8f);

	/**
	 * Create a GUI object
	 * 
	 * @throws SlickException
	 */
	public GUI(Player player) throws SlickException
	{
		this.player = player;
	}

	/**
	 * Update the timed elements of the GUI.
	 * 
	 * @param delta
	 *            The time (ms) since the last frame
	 */
	public void update(int delta)
	{
		if (invScrollHorizTimer >= 0)
		{
			invScrollHorizTimer -= delta;
		}
		if (invScrollVerticalTimer >= 0)
		{
			invScrollVerticalTimer -= delta;
		}
	}

	/**
	 * Renders the player's status panel.
	 * 
	 * @param g
	 *            The current Slick graphics context
	 * @param p
	 *            The player whose status should be displayed
	 */
	public void renderStatusPanel(Graphics g)
	{
		// Variables for layout
		String text; // Text to display
		int text_x, text_y; // Coordinates to draw text
		int bar_x, bar_y; // Coordinates to draw rectangles
		int bar_width, bar_height; // Size of rectangle to draw
		int hp_bar_width; // Size of red (HP) rectangle
		int mp_bar_width; // Size of blue (MP) rectangle
		int xp_bar_width; // Size of yellow (XP) rectangle

		float health_percent; // Player's health, as a percentage
		float energy_percent; // Player's energy, as a percentage
		float xp_percent; // Player's xp, as a percentage to next level

		// Panel background image
		this.panel.draw(0, RPG.SCREENHEIGHT - PANELHEIGHT);

		// Display the player's health
		text_x = 15;
		text_y = RPG.SCREENHEIGHT - PANELHEIGHT + 15;
		g.setColor(colourLabel);
		g.drawString("Health:", text_x, text_y);
		text = Math.max(0, player.getHealth()) + "/" + player.getMaxHealth();

		bar_x = 90;
		bar_y = RPG.SCREENHEIGHT - PANELHEIGHT + 13;
		bar_width = 90;
		bar_height = 20;
		health_percent = (float) Math.max(0, player.getHealth()) / player.getMaxHealth();
		hp_bar_width = (int) (bar_width * health_percent);
		text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
		g.setColor(colourBarBg);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);
		g.setColor(colourBarHp);
		g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
		g.setColor(colourValue);
		g.drawString(text, text_x, text_y);

		// Display the player's level
		text_x = 200;
		g.setColor(colourLabel);
		g.drawString("Level:", text_x, text_y);
		text_x += 60;
		text = Integer.toString(player.getLevel());
		g.setColor(colourValue);
		g.drawString(text, text_x, text_y);

		// Display the player's name
		text_x += 40;
		text_y -= 5;
		g.setColor(colourLabel);
		g.drawString(player.getName().toUpperCase(), text_x, text_y);
		text_y += 5;

		// Display the player's damage
		text_x += 80;
		g.setColor(colourLabel);
		g.drawString("Damage:", text_x, text_y);
		text_x += 120;
		text = (int) player.getMinDamage() + "-" + (int) player.getMaxDamage();
		g.setColor(colourValue);
		g.drawString(text, text_x - g.getFont().getWidth(text), text_y);

		// Display the pleyer's cooldown
		text_x += 30;
		g.setColor(colourLabel);
		g.drawString("Rate:", text_x, text_y);
		text_x += 60;
		text = Integer.toString(player.getCooldown());
		g.setColor(colourValue);
		g.drawString(text, text_x, text_y);

		// Display the player's health
		text_x = 15;
		text_y = RPG.SCREENHEIGHT - PANELHEIGHT + 39;
		g.setColor(colourLabel);
		g.drawString("Energy:", text_x, text_y);
		text = player.getEnergy() + "/" + player.getMaxEnergy();

		bar_x = 90;
		bar_y = RPG.SCREENHEIGHT - PANELHEIGHT + 37;
		bar_width = 90;
		bar_height = 20;
		energy_percent = (player.getEnergy() / player.getMaxEnergy());
		mp_bar_width = (int) (bar_width * energy_percent);
		text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
		g.setColor(colourBarBg);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);
		g.setColor(colourBarMp);
		g.fillRect(bar_x, bar_y, mp_bar_width, bar_height);
		g.setColor(colourValue);
		g.drawString(text, text_x, text_y);

		// Display the player's xp
		text_x = 200;
		g.setColor(colourLabel);
		g.drawString("XP:", text_x, text_y);
		text_x += 97;
		text = Integer.toString(player.getXp()) + "/" + player.getLevelXps()[player.getLevel()];

		bar_x = 240;
		bar_width = 120;
		xp_percent = (float) (player.getXp() - player.getLevelXps()[player.getLevel() - 1])
				/ (player.getLevelXps()[player.getLevel()] - player.getLevelXps()[player.getLevel() - 1]);
		xp_bar_width = (int) (bar_width * xp_percent);

		text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
		g.setColor(colourBarBg);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);
		g.setColor(colourBarXp);
		g.fillRect(bar_x, bar_y, xp_bar_width, bar_height);
		g.setColor(colourValue);
		g.drawString(text, text_x, text_y);

		// Display the player's gold
		text_x = 380;
		g.setColor(colourLabel);
		g.drawString("Gold:", text_x, text_y);
		text_x += 120;
		text = Integer.toString(player.getGold());
		g.setColor(colourValue);
		g.drawString(text, text_x - g.getFont().getWidth(text), text_y);
	}

	/**
	 * Renders the player's status panel.
	 * 
	 * @param g
	 *            The current Slick graphics context
	 * @param p
	 *            The player, whose inventory should be displayed
	 */
	public void renderInventoryPanel(Graphics g)
	{
		// Declare variables
		float initInvX = RPG.SCREENWIDTH - INVPANELWIDTH + 13;
		float initInvY = 149;
		float initEquipX = initInvX + 6;
		float initEquipY = 10;

		// Draw the Inventory Panel
		this.invPanel.draw(RPG.SCREENWIDTH - INVPANELWIDTH, 0);

		// Display the player's inventory
		int i = 0;
		for (InventoryItem item : player.getInventory().getItems())
		{
			float invX = initInvX + (i % iconsPerRow) * (iconSize + 1);
			float invY = initInvY + (i / iconsPerRow) * iconSize;
			// Render the item to (inv_x, inv_y)
			item.render(g, invX, invY);
			i++;
		}

		// Display the player's equipment
		// Start with the weapon and armour
		i = 0;
		EnumMap<Item.Type, InventoryItem> items = player.getEquipment().getItems();
		for (Item.Type type : Item.Type.values())
		{
			if (items.get(type) != null)
			{
				// Set the default values for the equip coords (the weapon box)
				float equipX = initEquipX;
				float equipY = initEquipY;

				if (type == Item.Type.WEAPON)
				{
					// Values are default
				}
				else if (type == Item.Type.SHIELD)
				{
					equipX = initEquipX + 124;
				}
				else if (type == Item.Type.HELMET)
				{
					equipX = initEquipX + 64;
				}
				else if (type == Item.Type.BOOTS)
				{
					equipY = initEquipY + 69;
				}
				else if (type == Item.Type.ARMOUR)
				{
					equipX = initEquipX + 64;
					equipY = initEquipY + 69;
				}
				else if (type == Item.Type.GLOVES)
				{
					equipX = initEquipX + 124;
					equipY = initEquipY + 69;
				}
				// Not an equippable item
				else
				{
					continue;
				}
				// Render the item to (equipX, equipY)
				items.get(type).render(g, equipX, equipY);
			}
			i++;
		}

		// Display the accessories
		float accScale = 0.6f;
		i = 0;
		for (InventoryItem acc : player.getEquipment().getAccessories())
		{
			if (acc != null)
			{
				float equipX = initEquipX + 179 + (i % 2) * (iconSize * accScale + 3);
				float equipY = initEquipY + 16 + (i / 2) * (iconSize * accScale + 2);
				// Render the item to (equipX, equipY)
				acc.render(g, equipX, equipY, accScale);
			}
			i++;
		}

		// Draw the selection box
		float selectX = initInvX + ((int) invSelectIndex % iconsPerRow) * (iconSize + 0.2f);
		float selectY = initInvY + ((int) invSelectIndex / iconsPerRow) * (iconSize + 0.25f);
		g.setColor(colourValue);
		this.selectBox.draw(selectX, selectY);

		// If the selected slot contains an item, draw the item details
		InventoryItem item = null;
		if (invSelectIndex < player.getInventory().getItems().size())
		{
			item = player.getInventory().getItems().get((int) invSelectIndex);
		}

		if (item != null)
		{
			float scale = 2.5f;
			float imgX = initInvX + 55 - scale * (item.getAvatar().getWidth() / 2);
			float imgY = 450 - scale * (item.getAvatar().getHeight() / 2);
			item.render(g, imgX, imgY, scale);

			// Write the item name
			float textX = initInvX + (INVPANELWIDTH - 20) / 2
					- g.getFont().getWidth(item.getName()) / 2;
			float textY = 361;
			// If the item has bonuses:
			if (!item.getBonuses().isEmpty())
			{
				// Set colour to gold for the name
				g.setColor(colourLabel);
			}
			else
			{
				// Set colour to white for the name
				g.setColor(colourValue);
			}
			g.drawString(item.getName(), textX, textY);

			// Write the type
			textX = initInvX + 120;
			textY = 380;
			g.setColor(colourValue);
			g.drawString("Type: " + toTitleCase(item.getType().toString()), textX, textY);

			// Write the relevant stat, if applicable
			g.setColor(colourValue);
			String statText = "";
			// Weapon -> Damage
			if (item.getType().equals(Item.Type.WEAPON))
			{
				statText = "Damage: " + (int) ((InventoryWeapon) (item)).getMinDamage() + "-"
						+ (int) ((InventoryWeapon) (item)).getMaxDamage();
			}
			// Armour -> Armour Rating
			else if (item.getType() == Item.Type.ARMOUR || item.getType() == Item.Type.SHIELD
					|| item.getType() == Item.Type.HELMET || item.getType() == Item.Type.GLOVES
					|| item.getType() == Item.Type.BOOTS)
			{
				statText = "Armour: " + ((InventoryArmour) (item)).getArmourRating();
			}
			// If there was a relevant start, write it
			if (!statText.isEmpty())
			{
				textY += 20;
				g.drawString(statText, textX, textY);
			}

			// Write bonus properties
			if (!item.getBonuses().isEmpty())
			{
				// Write the Properties header
				textY += 20;
				g.setColor(colourLabel);
				g.drawString("Item Properties:", textX, textY);
				textY += 5;
				g.setColor(colourValue);
				// Write each property
				for (Bonus bonus : item.getBonuses())
				{
					textY += 16;
					String text = toTitleCase(bonus.toString());
					g.drawString(text, textX, textY);
				}
			}
		}
	}

	/**
	 * Simply takes a string an returns it in title case
	 * 
	 * @param in
	 *            The string to convert
	 * @return The input string in title case
	 */
	private String toTitleCase(String in)
	{
		String title = "";
		for (int j = 0; j < in.length(); j++)
		{
			String next = in.substring(j, j + 1);
			if (j == 0)
				title += next.toUpperCase();
			else
				title += next.toLowerCase();
		}
		return title;
	}

	/** Increments the selected inventory slot index */
	public void moveInvSelectIndex(char dir, int size)
	{
		if (dir == 'l')
		{
			if ((int) invSelectIndex % iconsPerRow == 0)
			{
				// Wrap off left
				invSelectIndex += iconsPerRow - 1;
			}
			else
			{
				// Move left
				invSelectIndex--;
			}
			this.invScrollHorizTimer = this.invScrollCooldown;
		}
		else if (dir == 'r')
		{
			if ((invSelectIndex + 1) % iconsPerRow == 0)
			{
				invSelectIndex -= iconsPerRow - 1;
			}
			else
			{
				invSelectIndex++;
			}
			this.invScrollHorizTimer = this.invScrollCooldown;
		}
		else if (dir == 'u')
		{
			if (invSelectIndex >= iconsPerRow)
			{
				invSelectIndex -= iconsPerRow;
			}
			this.invScrollVerticalTimer = this.invScrollCooldown;
		}
		else if (dir == 'd')
		{
			if (invSelectIndex <= iconsPerRow * (iconRows - 1) - 1)
			{
				invSelectIndex += iconsPerRow;
			}
			this.invScrollVerticalTimer = this.invScrollCooldown;
		}

	}

	/**
	 * @return Whether the horizontal cooldown timer has expired
	 */
	public boolean isInvScrollHorizCool()
	{
		return invScrollHorizTimer < 0;
	}

	/**
	 * @return Whether the vertical cooldown timer has expired
	 */
	public boolean isInvScrollVerticalCool()
	{
		return invScrollVerticalTimer < 0;
	}

	/**
	 * @return the invSelectIndex
	 */
	public double getInvSelectIndex()
	{
		return invSelectIndex;
	}
}
