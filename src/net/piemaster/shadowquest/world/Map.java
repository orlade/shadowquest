package net.piemaster.shadowquest.world;

import net.piemaster.shadowquest.RPG;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public abstract class Map
{
	/** Width of a tile (px) */
	private final int tileWidth = 72;
	/** Height of a tile (px) */
	private final int tileHeight = 72;

	/** The height of the map (px) */
	private int mapHeight;
	/** The width of the map (px) */
	private int mapWidth;

	// /** The tile indexes of the dungeon entrance tile */
	// protected int entranceX, entranceY;
	// /** The tile indexes of the dungeon exit tile */
	// protected int exitX, exitY;
	// /** A list of the exit zones on the map */
	// protected ArrayList<ExitZone> exits = new ArrayList<ExitZone>(0);
	protected ExitZone entrance = null;
	protected ExitZone exit = null;

	/** Blocking tile mask */
	private boolean blocked[][];
	/** Flag to display debug blocking mask */
	private boolean bRenderBlockMask = false;
	/** Flag to display debug exit mask */
	private boolean bRenderExitMask = false;

	/** The TiledMap object representing the world map */
	private TiledMap map = null;

	/**
	 * The TiledMap object representing the world map
	 * 
	 * @param mapFile
	 *            The filepath to the .tmx map file
	 * @param mapFolder
	 *            The folder containing the map information
	 * @throws SlickException
	 */
	public Map(String mapFile, String mapFolder) throws SlickException
	{
		// Set up the map details
		this.map = new TiledMap(mapFile, mapFolder);

		this.mapHeight = this.map.getWidth() * this.tileWidth;
		this.mapWidth = this.map.getHeight() * this.tileHeight;

		// Build a collision map from the tilemap's "block" property
		this.buildCollisionMap();
	}

	/**
	 * Create a new map from an existing TileD map
	 * 
	 * @param map
	 *            The TiledMap to use
	 */
	public Map(TiledMap map)
	{
		// Set up the map details
		this.map = map;

		this.mapHeight = this.map.getWidth() * this.tileWidth;
		this.mapWidth = this.map.getHeight() * this.tileHeight;

		// Build a collision map from the tilemap's "block" property
		this.buildCollisionMap();
	}

	/** Build a collision map from the tilemap's "block" property */
	private void buildCollisionMap()
	{
		blocked = new boolean[map.getHeight()][map.getWidth()];
		for (int x = 0; x < map.getWidth(); x++)
		{
			for (int y = 0; y < map.getHeight(); y++)
			{
				int tileID = map.getTileId(x, y, 0);
				String value = map.getTileProperty(tileID, "block", "0");
				blocked[y][x] = (value.equals("1") ? true : false);
			}
		}
	}

	/**
	 * Render the map at the camera's co-ordinates (pre-graphics translation)
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 * @param cameraX
	 *            The camera's X coordinate (from World)
	 * @param cameraY
	 *            The camera's Y coordinate (from World)
	 */
	public void render(Graphics g, double cameraX, double cameraY)
	{
		// Calculate the offset to the next tile (needed by TiledMap.render())
		int tileOffsetX = (int) -(cameraX % tileWidth);
		int tileOffsetY = (int) -(cameraY % tileHeight);

		// Calculate the index of the leftmost tile that is being displayed
		int tileIndexX = (int) (cameraX / tileWidth);
		int tileIndexY = (int) (cameraY / tileHeight);

		// Calculate how many tiles need to be rendered from the topleftmost
		int visX = (RPG.SCREENWIDTH - tileOffsetX) / tileWidth + 1;
		int visY = (RPG.SCREENHEIGHT - tileOffsetY) / tileHeight + 1;

		// Render the map at the calculated position
		this.getMap().render(tileOffsetX, tileOffsetY, tileIndexX, tileIndexY, visX, visY);

		// DEBUG: Render the blocking mask
		if (isbRenderBlockMask())
		{
			renderBlockMask(g, tileOffsetX, tileOffsetY, tileIndexX, tileIndexY, visX, visY);
		}

		// DEBUG: Render the exit mask
		if (isbRenderExitMask())
		{
			if (entrance != null)
				entrance.renderMask(g, cameraX, cameraY);
			if (exit != null)
				exit.renderMask(g, cameraX, cameraY);
		}
	}

	/**
	 * Render semi-transparent red rect's over blocked tiles, from block mask
	 * 
	 * @param g
	 *            The Slick graphics object, used for drawing.
	 * @param tileOffsetX
	 *            The tilemap's X offset
	 * @param tileOffsetY
	 *            The tilemap's Y offset
	 * @param tileIndexX
	 *            The tilemap's horizontal starting tile index
	 * @param tileIndexY
	 *            The tilemap's vertical starting tile index
	 * @param visX
	 *            The number of tiles being rendered horizontally
	 * @param visY
	 *            The number of tiles being rendered vertically
	 */
	private void renderBlockMask(Graphics g, int tileOffsetX, int tileOffsetY, int tileIndexX,
			int tileIndexY, int visX, int visY)
	{
		// Debug blocking areas
		g.setColor(new Color(1, 0, 0, 0.5f));
		for (int xp = 0; xp < getMap().getWidth(); xp++)
		{
			for (int yp = 0; yp < getMap().getHeight(); yp++)
			{
				if ((xp >= tileIndexX) && (yp >= tileIndexY) && xp < tileIndexX + visX
						&& yp < tileIndexY + visY)
				{
					if (isTileBlocked(xp, yp))
					{
						g.fillRect(tileOffsetX + (xp * tileWidth) - (tileIndexX * tileWidth),
								tileOffsetY + (yp * tileHeight) - (tileIndexY * tileHeight),
								tileWidth, tileHeight);
					}
				}
			}
		}
	}

	/**
	 * Test whether a given bounding box is intersecting a blocked tile.
	 * 
	 * @param x
	 *            The x coordinate of the centre of the box being tested.
	 * @param y
	 *            The y coordinate of the centre of the box being tested.
	 * @param width
	 *            The width of the box being tested.
	 * @param height
	 *            The height of the box being tested.
	 * @param game
	 *            The parent Game object used as a facade.
	 */
	public boolean isBoxBlocked(double x, double y, int width, int height)
	{
		// Find the tile each corner of the box sits in
		int atx1 = (int) (x - width / 2) / this.getTileWidth();
		int atx2 = (int) (x + width / 2) / this.getTileWidth();
		int aty1 = (int) (y - height / 2) / this.getTileHeight();
		int aty2 = (int) (y + height / 2) / this.getTileHeight();

		// If the given points are outside the bounds, return blocked
		if (atx1 < 0 || aty1 < 0 || atx2 >= getMapWidthTiles() || aty2 >= getMapHeightTiles())
		{
			return true;
		}

		// Test is these tiles have their "block" property set to "1", return
		// result
		if (this.isTileBlocked(atx1, aty1))
			return true;
		if (this.isTileBlocked(atx1, aty2))
			return true;
		if (this.isTileBlocked(atx2, aty1))
			return true;
		if (this.isTileBlocked(atx2, aty2))
			return true;
		return false;
	}

	/**
	 * Returns whether a tile is a block tile or not, based on the block mask
	 * 
	 * @param x
	 *            The column of the tile
	 * @param y
	 *            The row of the tiles
	 */
	public boolean isTileBlocked(int tx, int ty)
	{
		// If outside bounds, return blocked
		if (ty >= blocked.length || tx >= blocked[ty].length)
			return true;

		return blocked[ty][tx];
	}

	/**
	 * Returns whether a world position is on a blocked tile
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public boolean isTileBlocked(double x, double y)
	{
		// Calculate tile indexes
		int tx = (int) (x / this.getTileWidth());
		int ty = (int) (y / this.getTileHeight());

		// If outside bounds, return blocked
		if (ty >= blocked.length || tx >= blocked[ty].length)
			return true;

		return blocked[ty][tx];
	}

	/**
	 * Returns whether a point in the game world is within an exit zone
	 * 
	 * @param x
	 *            The x coordinate to test
	 * @param y
	 *            The y coordinate to test
	 */
	public ExitZone getCurrentExit(double x, double y)
	{
		if (entrance != null && x > entrance.getX() && x < entrance.getX() + entrance.getWidth()
				&& y > entrance.getY() && y < entrance.getY() + entrance.getHeight())
		{
			return entrance;
		}
		else if (exit != null && x > exit.getX() && x < exit.getX() + exit.getWidth()
				&& y > exit.getY() && y < exit.getY() + exit.getHeight())
		{
			return exit;
		}
		return null;
	}

	/**
	 * @return the tileWidth
	 */
	public int getTileWidth()
	{
		return tileWidth;
	}

	/**
	 * @return the tileHeight
	 */
	public int getTileHeight()
	{
		return tileHeight;
	}

	/**
	 * @return the mapHeight (px)
	 */
	public int getMapHeight()
	{
		return mapHeight;
	}

	/**
	 * @return the mapWidth (px)
	 */
	public int getMapWidth()
	{
		return mapWidth;
	}

	/**
	 * @return the mapHeight (in tiles)
	 */
	public int getMapHeightTiles()
	{
		return (mapHeight / tileHeight);
	}

	/**
	 * @return the mapWidth (in tiles)
	 */
	public int getMapWidthTiles()
	{
		return (mapWidth / tileWidth);
	}

	/**
	 * @return the entrance
	 */
	public ExitZone getEntrance()
	{
		return entrance;
	}

	/**
	 * @return the exit
	 */
	public ExitZone getExit()
	{
		return exit;
	}

	/**
	 * @param entrance
	 *            the entrance to set
	 */
	public void setEntrance(ExitZone entrance)
	{
		this.entrance = entrance;
	}

	/**
	 * @param exit
	 *            the exit to set
	 */
	public void setExit(ExitZone exit)
	{
		this.exit = exit;
	}

	/**
	 * @return the blocked
	 */
	public boolean[][] getBlocked()
	{
		return blocked;
	}

	/**
	 * @return the bRenderBlockMask
	 */
	public boolean isbRenderBlockMask()
	{
		return bRenderBlockMask;
	}

	/**
	 * @return the bRenderExitMask
	 */
	public boolean isbRenderExitMask()
	{
		return bRenderExitMask;
	}

	/** Toggles whether the block mask is rendered */
	public void toggleRenderBlockMask()
	{
		this.bRenderBlockMask = !this.bRenderBlockMask;
	}

	/** Toggles whether the block mask is rendered */
	public void toggleRenderExitMask()
	{
		this.bRenderExitMask = !this.bRenderExitMask;
	}

	/**
	 * @return the map
	 */
	public TiledMap getMap()
	{
		return map;
	}
}
