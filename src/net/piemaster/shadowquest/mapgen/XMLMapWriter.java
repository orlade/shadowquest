/*
 *  Tiled Map Editor, (c) 2004-2008
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 *  Bjorn Lindeijer <b.lindeijer@xs4all.nl>
 */

package net.piemaster.shadowquest.mapgen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

public class XMLMapWriter
{
	private static final int LAST_BYTE = 0x000000FF;

//	/**
//	 * Saves a map to an XML file.
//	 * 
//	 * @param map
//	 *            The map to write
//	 * @param filename
//	 *            The filename of the map file
//	 */
//	public static void writeMap(RawMap map, String filename) throws Exception
//	{
//		OutputStream os = new FileOutputStream(filename);
//		Writer writer = new OutputStreamWriter(os);
//		XMLWriter xmlWriter = new XMLWriter(writer);
//
//		xmlWriter.startDocument();
//		writeMap(map, xmlWriter);
//		xmlWriter.endDocument();
//
//		writer.flush();
//	}

	/**
	 * Converts the given raw map to an XML string.
	 * 
	 * @param map
	 *            The map to convert
	 */
	public static String mapToString(RawMap map) throws Exception
	{
		Writer writer = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(writer);

		xmlWriter.startDocument();
		writeMap(map, xmlWriter);
		xmlWriter.endDocument();
		
		String mapString = writer.toString();
		writer.flush();
		return mapString;
	}
	
	/**
	 * Writes a given map to a .tmx file (TileD XML data file) to be read in by
	 * Slick
	 * 
	 * @param map
	 *            The map to write
	 * @param w
	 *            The XML writer to write with
	 * @throws IOException
	 */
	private static void writeMap(RawMap map, XMLWriter w) throws IOException
	{
		// Write the XML doctype
		w.writeDocType("map", null, "http://mapeditor.org/dtd/1.0/map.dtd");

		// Write the map (root) element
		w.startElement("map");
		w.writeAttribute("version", "1.0");
		w.writeAttribute("orientation", "orthogonal");
		w.writeAttribute("width", map.getWidth());
		w.writeAttribute("height", map.getHeight());
		w.writeAttribute("tilewidth", map.getTileWidth());
		w.writeAttribute("tileheight", map.getTileHeight());

		// Write the tileset element
		writeTilesetReference("tileset.tsx", w);

		// Write the tile data
		writeTiles(map, w);

		// End map (root) element
		w.endElement();
	}

	/**
	 * Writes a reference to an external tileset into a XML document.
	 * 
	 * @param path
	 *            The path to the tileset
	 * @param w
	 *            The XML writer to use
	 * @throws IOException
	 */
	private static void writeTilesetReference(String path, XMLWriter w) throws IOException
	{
		// Write the tileset element
		w.startElement("tileset");
		w.writeAttribute("firstgid", 1);
		w.writeAttribute("source", path);
		w.endElement();
	}

	/**
	 * Writes this layer to an XMLWriter. This should be done <b>after</b> the
	 * first global ids for the tilesets are determined, in order for the right
	 * gids to be written to the layer data.
	 * 
	 * @param map
	 *            The map to write the tiles of
	 * @param w
	 *            The XML writer to use
	 * @throws IOException
	 */
	private static void writeTiles(RawMap map, XMLWriter w) throws IOException
	{
		// Write the layer element
		w.startElement("layer");
		w.writeAttribute("name", "Layer 0");
		w.writeAttribute("width", map.getWidth());
		w.writeAttribute("height", map.getHeight());
		
		// Write the data element
		w.startElement("data");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		OutputStream out;
		out = new GZIPOutputStream(baos);

		w.writeAttribute("encoding", "base64");
		w.writeAttribute("compression", "gzip");
		// System.out.println("WRITING!!!: ("+map.getWidth()+","+map.getHeight()+")");

		for (int y = 0; y < map.getHeight(); y++)
		{
			for (int x = 0; x < map.getWidth(); x++)
			{
				// Initialise gid to default value
				int gid = map.getTileId(x, y);

				out.write(gid & LAST_BYTE);
				out.write(gid >> 8 & LAST_BYTE);
				out.write(gid >> 16 & LAST_BYTE);
				out.write(gid >> 24 & LAST_BYTE);
			}
		}

		((GZIPOutputStream) out).finish();
		w.writeCDATA(new String(Base64.encodeBytes(baos.toByteArray())));

		// End the data element
		w.endElement();
		// End the layer element
		w.endElement();
	}
}
