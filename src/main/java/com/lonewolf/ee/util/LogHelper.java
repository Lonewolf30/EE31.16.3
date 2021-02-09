package com.lonewolf.ee.util;

import com.lonewolf.ee.reference.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import java.io.File;

public class LogHelper
{
	private static Logger logger = LogManager.getLogger();
	
	private static boolean info = true;
	private static boolean warn = true;
	private static boolean error = true;
	
	public static void info(Marker marker, String log, Object args)
	{
		if (info)
			logger.info(marker, log, args);
	}
	
	public static void info(String log, Object args)
	{
		if (info)
			logger.info(Reference.marker, log, args);
	}
	
	public static void info(Marker marker, String log, Object[] args)
	{
		if (info)
			logger.info(marker, log, args);
	}
	
	public static void info(Marker marker, String log)
	{
		if (info)
			logger.info(marker, log);
	}
	
	public static void info(String log)
	{
		if (info)
			logger.info(Reference.marker, log);
	}
	
	public static void info(String log, Object... args)
	{
		if (info)
			logger.info(Reference.marker, log, args);
	}
	
	public static void warn(String log)
	{
		if (warn)
			logger.warn(Reference.marker, log);
	}
	
	public static void warn(String log, Object object)
	{
		if (warn)
			logger.warn(Reference.marker, log, object);
	}
	
	public static void warn(Marker marker, String log)
	{
		if (warn)
			logger.warn(marker, log);
	}
	
	public static void warn(Marker marker, String log, Object object)
	{
		if (warn)
			logger.warn(marker, log, object);
	}
	
	public static void warn(Marker marker, String log, Object[] object)
	{
		if (warn)
			logger.warn(marker, log, object);
	}
	
	public static void error(String s, Object object)
	{
		if (error)
			logger.warn(Reference.marker, s, object);
	}
}
