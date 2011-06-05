// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 19-05-11 17:44:33
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   iAuction.java

package org.rob4001.iAuction;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.iConomy.*;
import com.iConomy.system.Holdings;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.File;
import java.util.*;



import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

// Referenced classes of package org.minr.Zaraza107.iAuction:
//            iProperty, Items

public class iAuction extends JavaPlugin
{

    public iAuction()
    {
        isAuction = false;
        win = false;
        permissionsEnabled = false;
        groupManagerEnabled = false;
    }

    public void onEnable()
    {
        server = getServer();
        PluginDescriptionFile pdfFile = getDescription();
        System.out.println((new StringBuilder("[")).append(pdfFile.getName()).append("] (version ").append(pdfFile.getVersion()).append(") is enabled!").toString());
        enableiConomy();
        Item = new iProperty("items.db");
        setupItems();
        setupSettings();
    }

    public void onDisable()
    {
        PluginDescriptionFile pdfFile = getDescription();
        System.out.println((new StringBuilder("[iAuction] version ")).append(pdfFile.getVersion()).append(" is disabled.").toString());
    }

    public void setupItems()
    {
        Map<?, ?> mappedItems = null;
        items = new HashMap<String, String>();
        try
        {
            mappedItems = Item.returnMap();
        }
        catch(Exception ex)
        {
            System.out.println("[iAuction] could not open items.db!");
        }
        if(mappedItems != null)
        {
            for(Iterator<?> iterator = mappedItems.keySet().iterator(); iterator.hasNext();)
            {
                Object item = iterator.next();
                String left = (String)item;
                String right = (String)mappedItems.get(item);
                String id = left.trim();
                if(id.matches("[0-9]+") || id.matches("[0-9]+,[0-9]+"))
                {
                    if(right.contains(","))
                    {
                        String synonyms[] = right.split(",");
                        String itemName = synonyms[0].replaceAll("\\s", "");
                        items.put(id, itemName);
                        for(int i = 1; i < synonyms.length; i++)
                        {
                            itemName = synonyms[i].replaceAll("\\s", "");
                            items.put(itemName, id);
                        }

                    } else
                    {
                        String itemName = right.replaceAll("\\s", "");
                        items.put(id, itemName);
                    }
                } else
                {
                    String itemName = left.replaceAll("\\s", "");
                    id = right.trim();
                    items.put(itemName, id);
                }
            }

        }
    }

    public void setupSettings()
    {
        String path = (new StringBuilder("plugins")).append(File.separator).append("iAuction").append(File.separator).toString();
        (new File(path)).mkdir();
        Settings = new iProperty((new StringBuilder(String.valueOf(path))).append("iAuction.settings").toString());
        maxTime = Settings.getInt("maximal-time", 0);
        hcChannelName = Settings.getString("herochat-channel-name", "global");
        hcEnabled = Settings.getBoolean("enable-herochat", false);
        
        if(hcEnabled)
        {
        	
   
                enableHeroChat();
            
            
        }
        String perm = Settings.getString("permission-plugin", "permissions");
        if(perm.equalsIgnoreCase("permissions"))
            enablePermissions();
        else
        if(perm.equalsIgnoreCase("groupmanager"))
            enableGroupManager();
        else
            System.out.println("[iAuction] WARNING! No permission system enabled!");
        tagColor = ChatColor.valueOf(Settings.getString("tag-color", "yellow").toUpperCase()).toString();
        warningColor = ChatColor.valueOf(Settings.getString("warning-color", "red").toUpperCase()).toString();
        auctionStatusColor = ChatColor.valueOf(Settings.getString("auction-status-color", "dark_green").toUpperCase()).toString();
        auctionTimeColor = ChatColor.valueOf(Settings.getString("auction-time-color", "dark_aqua").toUpperCase()).toString();
        helpMainColor = ChatColor.valueOf(Settings.getString("help-main-color", "yellow").toUpperCase()).toString();
        helpCommandColor = ChatColor.valueOf(Settings.getString("help-command-color", "aqua").toUpperCase()).toString();
        helpOrColor = ChatColor.valueOf(Settings.getString("help-or-color", "blue").toUpperCase()).toString();
        helpObligatoryColor = ChatColor.valueOf(Settings.getString("help-obligator-color", "dark_red").toUpperCase()).toString();
        helpOptionalColor = ChatColor.valueOf(Settings.getString("help-optional-color", "light_purple").toUpperCase()).toString();
        infoPrimaryColor = ChatColor.valueOf(Settings.getString("info-primary-color", "blue").toUpperCase()).toString();
        infoSecondaryColor = ChatColor.valueOf(Settings.getString("info-secondary-color", "aqua").toUpperCase()).toString();
        tag = (new StringBuilder(String.valueOf(tagColor))).append("[AUCTION] ").toString();
    }

    public boolean isDebugging(Player player)
    {
        if(debugees.containsKey(player))
            return ((Boolean)debugees.get(player)).booleanValue();
        else
            return false;
    }

    public void setDebugging(Player player, boolean value)
    {
        debugees.put(player, Boolean.valueOf(value));
    }

    public void enableiConomy()
    {
        Plugin p = server.getPluginManager().getPlugin("iConomy");
        if(p != null)
        {
            if(!p.isEnabled())
                server.getPluginManager().enablePlugin(p);
            iConomy = (iConomy)p;
            
        } else
        {
            System.out.println("[iAuction] WARNING! iConomy not detected!");
        }
    }

    public void enablePermissions()
    {
        Plugin p = server.getPluginManager().getPlugin("Permissions");
        if(p != null)
        {
            if(!p.isEnabled())
                server.getPluginManager().enablePlugin(p);
            Permissions = ((Permissions)p).getHandler();
            permissionsEnabled = true;
            System.out.println("[iAuction] Permissions support enabled!");
        } else
        {
            System.out.println("[iAuction] Permissions system is enabled but could not be loaded!");
        }
    }

    public void enableGroupManager()
    {
        Plugin p = server.getPluginManager().getPlugin("GroupManager");
        if(p != null)
        {
            if(!p.isEnabled())
                server.getPluginManager().enablePlugin(p);
            GroupManager gm = (GroupManager)p;
            groupManager = gm;
            groupManagerEnabled = true;
            System.out.println("[iAuction] GroupManager support enabled!");
        } else
        {
            System.out.println("[iAuction] GroupManager system is enabled but could not be loaded!");
        }
    }

    public void enableHeroChat()
    {
        Plugin p = server.getPluginManager().getPlugin("HeroChat");
        if(p != null)
        {
            if(!p.isEnabled())
                server.getPluginManager().enablePlugin(p);
            ChannelManager cm= ((HeroChat)p).getChannelManager();
            c = cm.getChannel(hcChannelName);
            if (c.getName().equalsIgnoreCase(hcChannelName) || c.getNick().equalsIgnoreCase(hcChannelName)){
            	System.out.println("[iAuction]Herochat system has enabled properly!");
            }else{
            	System.out.println("[iAuction]Your channel spesified does not exist");
            }
           
        } else
        {
            System.out.println("[iAuction] HeroChat system is enabled but could not be loaded!");
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
    {
        String commandName = cmd.getName();
        if((sender instanceof Player) && commandName.equalsIgnoreCase("auction"))
        {
            try
            {
                if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
                    auctionHelp((Player)sender);
                else
                if(args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("s"))
                    auctionStart((Player)sender, args);
                else
                if(args[0].equalsIgnoreCase("bid") || args[0].equalsIgnoreCase("b"))
                    auctionBid((Player)sender, args);
                else
                if(args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("e"))
                    auctionStop((Player)sender);
                else
                if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i"))
                    auctionInfo(null, (Player)sender);
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                return false;
            }
            return true;
        } else
        {
            return false;
        }
    }

    public void auctionHelp(Player player)
    {
        String or = (new StringBuilder(String.valueOf(helpOrColor))).append("|").toString();
        player.sendMessage((new StringBuilder(String.valueOf(tagColor))).append(" -----[ ").append(auctionStatusColor).append("Auction Help").append(tagColor).append(" ]----- ").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpCommandColor))).append("/auction help").append(or).append(helpCommandColor).append("?").append(helpMainColor).append(" - Returns this").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpCommandColor))).append("/auction start").append(or).append(helpCommandColor).append("s").append(helpObligatoryColor).append(" <time> <item> <amount> <starting price>").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpMainColor))).append("Starts an auction for ").append(helpObligatoryColor).append("<time> ").append(helpMainColor).append("seconds with ").append(helpObligatoryColor).append("<amount>").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpMainColor))).append("of ").append(helpObligatoryColor).append("<item> ").append(helpMainColor).append("for ").append(helpObligatoryColor).append("<starting price>").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpCommandColor))).append("/auction bid").append(or).append(helpCommandColor).append("b").append(helpObligatoryColor).append(" <bid> ").append(helpOptionalColor).append("(maximum bid)").append(helpMainColor).append(" - Bids the auction.").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpMainColor))).append("If you set a ").append(helpOptionalColor).append("(maximum bid) ").append(helpMainColor).append("and the ").append(helpObligatoryColor).append("<bid> ").append(helpMainColor).append("is greater than the").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpMainColor))).append("current, you will outbid that bid if it is lower than your maximum.").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpCommandColor))).append("/auction end").append(or).append(helpCommandColor).append("e").append(helpMainColor).append(" - Ends current auction.").toString());
        player.sendMessage((new StringBuilder(String.valueOf(helpCommandColor))).append("/auction info").append(or).append(helpCommandColor).append("i").append(helpMainColor).append(" - Returns auction information.").toString());
    }

    public void warn(Player player, String msg)
    {
        player.sendMessage((new StringBuilder(String.valueOf(tag))).append(warningColor).append(msg).toString());
    }

    public void help(Player player)
    {
        player.sendMessage((new StringBuilder(String.valueOf(warningColor))).append("Use \"/auction ?\" to recieve help.").toString());
    }

    public void broadcast(String msg)
    {if(hcEnabled)
    {
    	c.sendMessage(tag, msg,c.getMsgFormat(),c.getPlayers(),false,true);
    	//c.sendMessage(tag, msg);
    }else{
        server.broadcastMessage((new StringBuilder(String.valueOf(tag))).append(msg).toString());
    }
    }

    public void auctionStart(Player player, String msg[])
    {
        if(!permissionsEnabled && !groupManagerEnabled || Permissions.has(player, "auction.start"))
        {
            if(!isAuction)
            {
                if(msg.length == 5)
                {
                    auctionOwner = player;
                    timerPlayer = player;
                    try
                    {
                        auctionTime = Integer.parseInt(msg[1]);
                        auctionItemAmount = Integer.parseInt(msg[3]);
                        auctionItemStarting = Double.parseDouble(msg[4]);
                    }
                    catch(NumberFormatException ex)
                    {
                        warn(player, "Invalid syntax.");
                        help(player);
                        return;
                    }
                    if(maxTime != 0 && auctionTime > maxTime)
                    {
                        warn(player, (new StringBuilder("Too long! Maximal time is ")).append(maxTime).toString());
                        return;
                    }
                    int id[] = {
                        -1, 0
                    };
                    int count = 0;
                    try
                    {
                        id = Items.validate(msg[2]);
                    }
                    catch(Exception e)
                    {
                        warn(player, "Invalid item.");
                        return;
                    }
                    if(id[0] == -1 || id[0] == 0)
                    {
                        warn(player, "Invalid item.");
                        return;
                    }
                    ItemStack aitemstack[];
                    int k = (aitemstack = player.getInventory().getContents()).length;
                    for(int i = 0; i < k; i++)
                    {
                        ItemStack item = aitemstack[i];
                        if(item != null && item.getTypeId() == id[0])
                        {
                            MaterialData data = item.getData();
                            if(id[1] != 0)
                            {
                                if(data.getData() == (byte)id[1])
                                    auctionItemDamage = item.getDurability();
                            } else
                            {
                                auctionItemDamage = item.getDurability();
                            }
                        }
                    }

                    k = (aitemstack = player.getInventory().getContents()).length;
                    for(int j = 0; j < k; j++)
                    {
                        ItemStack item = aitemstack[j];
                        if(item != null && item.getTypeId() == id[0])
                        {
                            MaterialData data = item.getData();
                            if(id[1] != 0)
                            {
                                if(data.getData() == (byte)id[1] && item.getDurability() == auctionItemDamage)
                                    count++;
                            } else
                            if(item.getDurability() == auctionItemDamage)
                                count++;
                        }
                        count = 1;
                    }

                    if(auctionItemAmount >= count)
                    {
                        auctionItemId = id[0];
                        auction_item_byte = id[1];
                        currentBid = auctionItemStarting;
                        PlayerInventory inventory = player.getInventory();
                        if(auctionCheck(player, inventory, auctionItemId, auction_item_byte, auctionItemAmount, auctionTime, auctionItemStarting))
                        {
                            isAuction = true;
                            inventory.removeItem(new ItemStack[] {

                                new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte)auction_item_byte))
                            });
                            final int interval = auctionTime;
                            final iAuction plug = this;
                            
                            auctionTT = new TimerTask() {
                            	int i = interval;
                            	double half = java.lang.Math.floor(i / 2);
                            	iAuction pl = plug;

                            	@Override
                            	public void run() {
                            	if (half <= 10) {
                            	if (i == interval || i == 10 || i == 3 || i == 2) {
                            	broadcast(auctionTimeColor + i + " seconds left to bid!");
                            	}
                            	} else {
                            	if (i == interval || i == half || i == 10 || i == 3 || i == 2) {
                            	broadcast(auctionTimeColor + i + " seconds left to bid!");
                            	}
                            	}
                            	if (i == 1) {
                            	broadcast(auctionTimeColor + i + " seconds left to bid!");
                            	}
                            	if (i == 0) {
                            	pl.auctionStop(timerPlayer);
                            	}
                            	i--;
                            	}
                            	};
                            	broadcast(auctionStatusColor + "Auction Started!");
                            	auctionInfo(server, null);

                            	auctionTimer = new Timer();
                            	auctionTimer.scheduleAtFixedRate(auctionTT, 0L, 1000L);
                            
                        }
                    } else
                    {
                        warn(player, (new StringBuilder("Sorry but you have only ")).append(auctionTimeColor).append(count).append(warningColor).append(" of that item.").toString());
                    }
                } else
                {
                    warn(player, "Invalid syntax.");
                    help(player);
                }
            } else
            {
                warn(player, "There is already an auction running!");
            }
        } else
        {
            warn(player, "You don't have perrmisions to start an auction!");
        }
    }

    public boolean auctionCheck(Player player, PlayerInventory inventory, int id, int data, int amount, int time, double price)
    {
        if(time > 10)
        {
            ItemStack stacks[] = inventory.getContents();
            int size = 0;
            for(int i = 0; i < stacks.length; i++)
                if(stacks[i] != null && stacks[i].getTypeId() == id && (!Items.isDamageable(id) || stacks[i].getDurability() == 0))
                    size += stacks[i].getAmount();

            if(amount <= size)
            {
                if(price >= 0.0D)
                {
                    return true;
                } else
                {
                    warn(player, " The starting price has to be at least 0!");
                    return false;
                }
            } else
            {
                warn(player, (new StringBuilder("You don't have enough ")).append(Items.name(id, data)).append(" to do that!").toString());
                warn(player, "NOTE: You can't auction damaged tools.");
                return false;
            }
        } else
        {
            warn(player, "Time must be longer than 10 seconds!");
            return false;
        }
    }

    public void auctionInfo(Server server, Player player)
    {
        if(server != null)
        {
            broadcast((new StringBuilder(String.valueOf(infoPrimaryColor))).append("Auctioned Item: ").append(infoSecondaryColor).append(Items.name(auctionItemId, auction_item_byte)).append(infoPrimaryColor).append(" [").append(infoSecondaryColor).append(auctionItemId).append(infoPrimaryColor).append("]").toString());
            broadcast((new StringBuilder(String.valueOf(infoPrimaryColor))).append("Amount: ").append(infoSecondaryColor).append(auctionItemAmount).toString());
            broadcast((new StringBuilder(String.valueOf(infoPrimaryColor))).append("Starting Price: ").append(infoSecondaryColor).append(com.iConomy.iConomy.format(auctionItemStarting)).toString());
            broadcast((new StringBuilder(String.valueOf(infoPrimaryColor))).append("Owner: ").append(infoSecondaryColor).append(auctionOwner.getDisplayName()).toString());
        }
        if(player != null)
            if(isAuction)
            {
                player.sendMessage((new StringBuilder(String.valueOf(tagColor))).append("-----[ ").append(auctionStatusColor).append("Auction Information").append(tagColor).append(" ]-----").toString());
                player.sendMessage((new StringBuilder(String.valueOf(tag))).append(infoPrimaryColor).append("Auctioned Item: ").append(infoSecondaryColor).append(Items.name(auctionItemId, auction_item_byte)).append(infoPrimaryColor).append(" [").append(infoSecondaryColor).append(auctionItemId).append(infoPrimaryColor).append("]").toString());
                player.sendMessage((new StringBuilder(String.valueOf(tag))).append(infoPrimaryColor).append("Amount: ").append(infoSecondaryColor).append(auctionItemAmount).toString());
                player.sendMessage((new StringBuilder(String.valueOf(tag))).append(infoPrimaryColor).append("Current bid: ").append(infoSecondaryColor).append(com.iConomy.iConomy.format(currentBid)).toString());
                player.sendMessage((new StringBuilder(String.valueOf(tag))).append(infoPrimaryColor).append("Owner: ").append(infoSecondaryColor).append(auctionOwner.getDisplayName()).toString());
                if(winner != null)
                    player.sendMessage((new StringBuilder(String.valueOf(tag))).append(infoPrimaryColor).append("Current Winner: ").append(infoSecondaryColor).append(winner.getDisplayName()).toString());
            } else
            {
                warn(player, "No auctions in session at the moment!");
            }
    }

    public void auctionStop(Player player)
    {
        if((permissionsEnabled || groupManagerEnabled) && Permissions.has(player, "auction.end") || player == auctionOwner || player.isOp())
        {
            if(isAuction)
            {
                isAuction = false;
                auctionTimer.cancel();
                if(win)
                {
                    
                    broadcast((new StringBuilder(String.valueOf(auctionStatusColor))).append("-- Auction Ended -- Winner [ ").append(winner.getDisplayName()).append(auctionStatusColor).append(" ] -- ").toString());
                    winner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("Enjoy your items!").toString());
                    auctionOwner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("Your items have been sold for ").append(com.iConomy.iConomy.format(currentBid)).append("!").toString());
                    
                    Holdings balance = com.iConomy.iConomy.getAccount(winner.getName()).getHoldings();
                    balance.subtract(currentBid);
                    balance = com.iConomy.iConomy.getAccount(auctionOwner.getName()).getHoldings();
                    balance.add(currentBid);
                    if(Items.hasSpace(winner, auctionItemAmount)){
                        winner.getInventory().addItem(new ItemStack[] {
                            new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte)auction_item_byte))
                        });}
                        else{
                        	winner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("You do not have enough Inventory space!").toString());
                        	winner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("They have been dropped at your feet").toString());
                        	winner.getWorld().dropItemNaturally(winner.getLocation(),new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte)auction_item_byte)));
                        
                        }
                } else
                {
                    broadcast((new StringBuilder(String.valueOf(auctionStatusColor))).append("-- Auction ended with no bids --").toString());
                    auctionOwner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("Your items have been returned to you!").toString());
                    if(Items.hasSpace(auctionOwner, auctionItemAmount)){
                    auctionOwner.getInventory().addItem(new ItemStack[] {
                        new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte)auction_item_byte))
                    });}
                    else{
                    	auctionOwner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("You do not have enough Inventory space!").toString());
                    	auctionOwner.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("They have been dropped at your feet").toString());
                    	auctionOwner.getWorld().dropItemNaturally(auctionOwner.getLocation(),new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte)auction_item_byte)));
                    
                    }
                    
                }
                auctionItemId = 0;
                auctionItemAmount = 0;
                auctionItemStarting = 0.0D;
                currentBid = 0.0D;
                auctionItemBid = 0.0D;
                winner = null;
                auctionOwner = null;
                win = false;
            } else
            {
                warn(player, "No auctions in session at the moment!");
                return;
            }
        } else
        {
            warn(player, "You have no perrmisions to stop that auction!");
        }
    }

    public void auctionBid(Player player, String msg[])
    {
        if(!permissionsEnabled && !groupManagerEnabled || Permissions.has(player, "auction.bid"))
        {
            if(msg.length == 2 || msg.length == 3)
            {
                if(player != auctionOwner)
                {
                    String name = player.getName();
                    Holdings acc = com.iConomy.iConomy.getAccount(name).getHoldings();
                    double bid;
                    double sbid;
                    try
                    {
                        bid = Double.parseDouble(msg[1]);
                        if(msg.length == 2)
                            sbid = 0.0D;
                        else
                            sbid = Double.parseDouble(msg[2]);
                    }
                    catch(Exception ex)
                    {
                        warn(player, " Invalid syntax.");
                        help(player);
                        return;
                    }
                    if(bid <= acc.balance() && sbid <= acc.balance())
                    {
                        if(isAuction)
                        {
                            if(bid > currentBid)
                            {
                            	
                                win = true;
                                if(bid > auctionItemBid)
                                {
                                    currentBid = bid;
                                    auctionItemBid = sbid;
                                    winner = player;
                                    broadcast((new StringBuilder(String.valueOf(auctionStatusColor))).append("Bid raised to ").append(auctionTimeColor).append(com.iConomy.iConomy.format(bid)).append(auctionStatusColor).append(" by ").append(auctionTimeColor).append(player.getDisplayName()).toString());
                                } else
                                {
                                    player.sendMessage((new StringBuilder(String.valueOf(tag))).append(auctionStatusColor).append("You have been outbid by ").append(auctionTimeColor).append(winner.getDisplayName()).append(auctionStatusColor).append("'s secret bid!").toString());
                                    broadcast((new StringBuilder(String.valueOf(auctionStatusColor))).append("Bid raised! Currently stands at: ").append(auctionTimeColor).append(com.iConomy.iConomy.format(bid + 1.0D)).toString());
                                    currentBid = bid + 1.0D;
                                }
                            	
                            	
                            } else
                            {
                                warn(player, "Your bid was too low.");
                            }
                        } else
                        {
                            warn(player, "There is no auction running at the moment.");
                        }
                    } else
                    {
                        warn(player, "You don't have enough money!");
                        warn(player, (new StringBuilder("Your current balance is: ")).append(com.iConomy.iConomy.format(acc.balance())).toString());
                    }
                } else
                {
                    warn(player, "You can't bid on your own auction!");
                }
            } else
            {
                warn(player, " Invalid syntax.");
                help(player);
            }
        } else
        {
            warn(player, "You don't have perrmisions to bid an auction!");
        }
    }

    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static Server server;
    private Timer auctionTimer;
    private TimerTask auctionTT;
    public boolean isAuction;
    public int auctionTime;
    public Player auctionOwner;
    public Player winner;
    private int auctionItemId;
    private short auctionItemDamage;
    private int auction_item_byte;
    private int auctionItemAmount;
    private double auctionItemStarting;
    private double auctionItemBid;
    private boolean win;
    private double currentBid;
    public Player timerPlayer;
    public String tag;
    public static iConomy iConomy;
    public static PermissionHandler Permissions;
    public static GroupManager groupManager;
    private boolean permissionsEnabled;
    private boolean groupManagerEnabled;
    private int maxTime;
    private boolean hcEnabled;
    private static String hcChannelName;
    private String tagColor;
    private String warningColor;
    private String auctionStatusColor;
    private String auctionTimeColor;
    private String helpMainColor;
    private String helpCommandColor;
    private String helpOrColor;
    private String helpObligatoryColor;
    private String helpOptionalColor;
    private String infoPrimaryColor;
    private String infoSecondaryColor;
    public static iProperty Item;
    public static HashMap<String, String> items;
    public static iProperty Settings;
    public Channel c;


}
