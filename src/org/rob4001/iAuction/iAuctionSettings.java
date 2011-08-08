package org.rob4001.iAuction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imdeity.utils.FileMgmt;

import org.bukkit.util.config.Configuration;

@SuppressWarnings("unused")
public class iAuctionSettings {
	private static Configuration config;
	private static Configuration language;
	
	public static void loadConfig(String filepath, String defaultRes) throws IOException {
		File file = FileMgmt.CheckYMLexists(filepath, defaultRes);
		if (file != null) {
				
			// read the config.yml into memory
			config = new Configuration(file);
			config.load();
			file = null;
		}	
	}
	
	// This will read the language entry in the config.yml to attempt to load custom languages
	// if the file is not found it will load the default from resource
	public static void loadLanguage (String filepath, String defaultRes) throws IOException {		
		
		String defaultName = filepath + FileMgmt.fileSeparator() + getString("language");
		
		File file = FileMgmt.CheckYMLexists(defaultName, defaultRes);
		if (file != null) {
				
			// read the (language).yml into memory
			language = new Configuration(file);
			language.load();
			file = null;	
			
		}
	}
	
	///////////////////////////////////
	
	// Functions to pull data from the config and language files


    private static String[] parseString(String str) {
		return parseSingleLineString(str).split("@");
	}
	
	public static String parseSingleLineString(String str) {
		return str.replaceAll("&", "\u00A7");
	}
	
	public static Boolean getBoolean(String root){
        return config.getBoolean(root.toLowerCase(), true);
    }
    private static Double getDouble(String root){
        return config.getDouble(root.toLowerCase(), 0);
    }
    private static Integer getInt(String root){
        return config.getInt(root.toLowerCase(), 0);
    }
    private static Long getLong(String root){
        return Long.parseLong(getString(root).trim());
    }
    
    /*
     * Public Functions to read data from the Configuration
     * and Language data
     * 
     * 
     */
    
    public static String getString(String root){
        return config.getString(root.toLowerCase());
    }
    public static String getLangString(String root){
        return parseSingleLineString(language.getString(root.toLowerCase()));
    }
    
 // read a comma delimited string into an Integer list
	public static List<Integer> getIntArr(String root) {
		
		String[] strArray = getString(root.toLowerCase()).split(",");
		List<Integer> list = new ArrayList<Integer>();
		if (strArray != null) {
		for (int ctr=0; ctr < strArray.length; ctr++)
			if (strArray[ctr] != null)
				list.add(Integer.parseInt(strArray[ctr]));
		}	
		return list;
	}
	
	// read a comma delimited string into a trimmed list.
	public static List<String> getStrArr(String root) {
		
		String[] strArray = getString(root.toLowerCase()).split(",");
		List<String> list = new ArrayList<String>();
		if (strArray != null) {
		for (int ctr=0; ctr < strArray.length; ctr++)
			if (strArray[ctr] != null)
				list.add(strArray[ctr].trim());
		}
		return list;
	}
	
    ///////////////////////////////////
    
	
	public static String getMySQLServerAddress() {
		return getString("mysql.SERVER_ADDRESS");
	}
	
	public static int getMySQLServerPort() {
		return getInt("mysql.SERVER_PORT");
	}
	
	public static String getMySQLDatabaseName() {
		return getString("mysql.DATABASE_NAME");
	}
	
	public static String getMySQLUsername() {
		return getString("mysql.USERNAME");
	}
	
	public static String getMySQLPassword() {
		return getString("mysql.PASSWORD");
	}
	
	public static String getMySQLDatabaseTablePrefix() {
		return getString("mysql.DATABASE_TABLE_PREFIX");
	}
	
	///////////////////////


	public static int getMaxTime() {
		return getInt("auction.MAXIMUM_TIME");
	}

	public static String getHeroChatChannelName() {
		return getString("auction.HEROCHAT_CHANNEL_NAME");
	}
	
	public static boolean isEnabledHeroChat() {
		return getBoolean("auction.HEROCHAT_ENABLED");
	}

	public static boolean isEnabledCraftIRC() {
		return getBoolean("auction.CRAFTIRC_ENABLED");
	}
	
	public static String getCraftIRCTag() {
		return getString("auction.CRAFTIRC_TAG");
	}
//
//	public static ChatColor getWarningColor() {
//		return ChatColor.valueOf(getString("auction.WARNING_COLOR"));
//	}
//	
//	public static ChatColor getAuctionStatusColor() {
//		return ChatColor.valueOf(getString("auction.STATUS_COLOR"));
//	}
//	
//	public static ChatColor getAuctionTimerColor() {
//		return ChatColor.valueOf(getString("auction.TIME_COLOR"));
//	}
	
	public static boolean getAntiSnipe() {
		return getBoolean("auction.ENABLE_ANTISNIPE");
	}
	
	public static int getAntiSnipeValue() {
		return getInt("auction.ANTISNIPE_VALUE");
	}
}
