package com.spacechase0.minecraft.componentequipment;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class CELog
{
	public static void info( String str )
	{
		logger.info( str );
	}
	
	public static void fine( String str )
	{
		logger.fine( str );
	}
	
	public static void warning( String str )
	{
		logger.warning( str );
	}
	
	public static void severe( String str )
	{
		logger.severe( str );
	}
	
	private static Logger makeLogger()
	{
		Logger logger = Logger.getLogger( "SC0_ComponentEquipment" );
		//logger.setParent( FMLRelaunchLog.log.getLogger() );
		//logger.setLevel( Level.FINE );
		
		return logger;
	}
	
	private static final Logger logger = makeLogger();
}
