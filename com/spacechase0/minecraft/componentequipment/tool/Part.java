package com.spacechase0.minecraft.componentequipment.tool;

import java.util.ArrayList;
import java.util.List;

public class Part
{
	public static void addType( String type )
	{
		types.add( type );
	}
	
	public static String[] getTypes()
	{
		return types.toArray( new String[] {} );
	}
	
	private static List< String > types = new ArrayList< String >();
	
	static
	{
		addType( "handle" );
		addType( "binding" );
		
		addType( "axeHead" );
		addType( "hoeHead" );
		addType( "pickaxeHead" );
		addType( "shovelHead" );
		
		addType( "swordHead" );
		
		addType( "bowHead" );
		//addType( "bowHandle" );
		
		addType( "helm" );
		addType( "chest" );
		//addType( "arm" );
		//addType( "belt" );
		addType( "leggings" );
		addType( "boots" );
		
		addType( "paxelHead" );
	}
}