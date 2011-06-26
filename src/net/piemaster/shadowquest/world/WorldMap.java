package net.piemaster.shadowquest.world;

import org.newdawn.slick.SlickException;

public class WorldMap extends Map
{
	/** The position of the town in the world */
	private int townTileStartX = 7;
	private int townTileStartY = 7;
	private int townTilesWidth = 7;
	private int townTilesHeight = 4;

	/**
	 * The TiledMap object representing the world map
	 * 
	 * @param mapFile
	 *            The filepath to the .tmx map file
	 * @param mapFolder
	 *            The folder containing the map information
	 * @throws SlickException
	 */
	public WorldMap(String mapFile, String mapFolder) throws SlickException
	{
		super(mapFile, mapFolder);
	}

	/**
	 * Returns whether a point in the game world is the town or not
	 * 
	 * @param x
	 *            The x coordinate to test
	 * @param y
	 *            The y coordinate to test
	 */
	public boolean isInTown(double x, double y)
	{
		int tx = (int) x / getTileWidth();
		int ty = (int) y / getTileHeight();

		if (tx >= this.townTileStartX
				&& tx < townTileStartX + townTilesWidth
				&& ty >= this.townTileStartY
				&& ty < townTileStartY + townTilesHeight
				&& !(tx == townTileStartX && ty == townTileStartY)
				&& !(tx == townTileStartX + townTilesWidth - 1 && ty == townTileStartY)
				&& !(tx == townTileStartX && ty == townTileStartY + townTilesHeight - 1)
				&& !(tx == townTileStartX + townTilesWidth - 1 && ty == townTileStartY
						+ townTilesHeight - 1))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
