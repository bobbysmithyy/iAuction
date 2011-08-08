// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 19-05-11 17:44:33
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   iAuction.java

package org.rob4001.iAuction;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.ensifera.animosity.craftirc.CraftIRC;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.imdeity.utils.*;

import org.bukkit.Server;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

// Referenced classes of package org.minr.Zaraza107.iAuction:
//            iProperty, Items

public class iAuction extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");
    public static MySQLConnection database = null;
    private static AuctionCommand ac = null;
    
    public static Server server;
    public static PermissionHandler Permissions;
    public static iProperty Item;
    public static HashMap<String, String> items;
    public Channel c;
    public CraftIRC craftIRC;
    
    public iAuction() {
        AuctionCommand.isAuction = false;
        ac = new AuctionCommand(this);
    }

    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        server = getServer();
        Item = new iProperty("items.db");
        setupItems();
        loadSettings();
        enablePermissions();
        getCommand("auction").setExecutor(new AuctionCommand(this));
        out("Version "+pdfFile.getVersion()+" Enabled");
    }

    public void onDisable() {
        if (AuctionCommand.isAuction) {
            ac.auctionStop(AuctionCommand.timerPlayer);
        }
        out("Disabled!");
    }

    public void setupItems() {
        Map<?, ?> mappedItems = null;
        items = new HashMap<String, String>();
        try {
            mappedItems = Item.returnMap();
        } catch (Exception ex) {
            out("Could not open items.db!");
        }
        if (mappedItems != null) {
            for (Iterator<?> iterator = mappedItems.keySet().iterator(); iterator.hasNext(); ) {
                Object item = iterator.next();
                String left = (String) item;
                String right = (String) mappedItems.get(item);
                String id = left.trim();
                if (id.matches("[0-9]+") || id.matches("[0-9]+,[0-9]+")) {
                    if (right.contains(",")) {
                        String synonyms[] = right.split(",");
                        String itemName = synonyms[0].replaceAll("\\s", "");
                        items.put(id, itemName);
                        for (int i = 1; i < synonyms.length; i++) {
                            itemName = synonyms[i].replaceAll("\\s", "");
                            items.put(itemName, id);
                        }

                    } else {
                        String itemName = right.replaceAll("\\s", "");
                        items.put(id, itemName);
                    }
                } else {
                    String itemName = left.replaceAll("\\s", "");
                    id = right.trim();
                    items.put(itemName, id);
                }
            }

        }
    }

    public void enablePermissions() {
        Plugin p = server.getPluginManager().getPlugin("Permissions");
        if (p != null) {
            if (!p.isEnabled())
                server.getPluginManager().enablePlugin(p);
            Permissions = ((Permissions) p).getHandler();
            out("Permissions support enabled!");
        } else {
        	out("Permissions system is enabled but could not be loaded!");
        }
    }

    public void enableHeroChat() {
        Plugin p = server.getPluginManager().getPlugin("HeroChat");
        if (p != null) {
            if (!p.isEnabled())
                server.getPluginManager().enablePlugin(p);
            ChannelManager cm = ((HeroChat) p).getChannelManager();
            c = cm.getChannel(iAuctionSettings.getHeroChatChannelName());
            if (c.getName().equalsIgnoreCase(iAuctionSettings.getHeroChatChannelName()) || c.getNick().equalsIgnoreCase(iAuctionSettings.getHeroChatChannelName())) {
            	out("Herochat system has been enabled properly!");
            } else {
            	out("The channel specified does not exist.");
            }

        } else {
        	out("HeroChat system is enabled but could not be loaded!");
        }
    }

    public void enableCraftIRC() {
    	Plugin p = server.getPluginManager().getPlugin("CraftIRC");
    	if (p != null) {
    	    try {
                out("CraftIRC found, enabling support...");
                craftIRC = (CraftIRC) p;
            } catch (ClassCastException ex) {
                out("Unable to link to CraftIRC!");
                ex.printStackTrace();
            }
        } else {
            out("CraftIRC not detected!");
        }
    }
    
    public void broadcast(String msg) {
        if (iAuctionSettings.isEnabledHeroChat()) {
            c.sendMessage("", msg, c.getMsgFormat(), c.getPlayers(), false, true);
        } else {
            server.broadcastMessage((new StringBuilder("")).append(msg).toString());
        }
        if (iAuctionSettings.isEnabledCraftIRC()) {
        	craftIRC.sendMessageToTag((new StringBuilder("[Auction] ")).append(msg.replaceAll("�[0-9a-f]", "")).toString(), iAuctionSettings.getCraftIRCTag());
        }
    }
  
    public void out(String message) {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName()+ "] " + message);
    }
   
   	public String getRootFolder() {
    		if (this != null)
    			return this.getDataFolder().getPath();
    		else
    			return "";
    	}
    
    public boolean loadSettings() {
		try {
			FileMgmt.checkFolders(new String[]{
					getRootFolder(),
					getRootFolder() + FileMgmt.fileSeparator() + "settings"});
			iAuctionSettings.loadConfig(getRootFolder() + FileMgmt.fileSeparator() + "settings" + FileMgmt.fileSeparator() + "config.yml", "/config.yml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return true;
	}
    
}
