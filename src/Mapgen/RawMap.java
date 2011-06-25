package Mapgen;

public class RawMap
{
	/** Dimensions of the map (in tiles) */
	int width, height;
	/** Dimensions of the tiles (in px) */
	int tileWidth, tileHeight;
	/** The map grid */
	int[][] mapGrid;
	
	/** Create a new RawMap from a grid of tile IDs
	 * 
	 * @param mapGrid The grid to create from
	 */
	public RawMap(int[][] mapGrid, int tileWidth, int tileHeight)
	{
		this.mapGrid = mapGrid;
		this.width = mapGrid[0].length;
		this.height = mapGrid.length;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	/** Gets the ID of the tile at the given coordinates
	 * 
	 * @param x The x coord
	 * @param y The y coord
	 * @return The tile ID
	 */
	public int getTileId(int x, int y)
	{
		int id = 0;
		try
		{
			id = mapGrid[y][x];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		return id;
	}
	
	/** Serialises the raw map data to a printable form */
	@Override
	public String toString()
	{
		String result = "";

		for(int y=0; y < getHeight(); y++)
		{
			for(int x=0; x < getWidth(); x++)
			{
				result += getMapGrid()[y][x];
			}
			result += "\n";
		}
		return result;
	}
	
	/** Serialises the raw map data to a printable form */
	public String toGraphicString()
	{
		String result = "";

		for(int y=0; y < getHeight(); y++)
		{
			for(int x=0; x < getWidth(); x++)
			{
				switch(getMapGrid()[y][x])
				{
				case 2: result += "#"; break;
				case 3: result += "."; break;
				default: result += "?"; break;
				}
			}
			result += "\n";
		}
		return result;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the tileWidth
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * @return the tileHeight
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * @return the mapGrid
	 */
	public int[][] getMapGrid() {
		return mapGrid;
	}
}
