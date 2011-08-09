package org.rob4001.iAuction;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import com.iConomy.iConomy;
import com.iConomy.system.Account;
import com.imdeity.utils.ChatTools;
import com.imdeity.utils.ItemType;

public class AuctionCommand implements CommandExecutor {

	private static final List<String> output = new ArrayList<String>();
	private iAuction plugin = null;
	
	public static Player auctionOwner = null;
    public static Player timerPlayer = null;
	private static Timer auctionTimer = null;
    private static int currentBid = 0;
    private static TimerTask auctionTT = null;
    public static int auctionTime = 0;
    public static boolean isAuction = false;
    public static Player winner = null;
    private static int auctionItemId = 0;
    private static short auctionItemDamage = 0;
    private static byte auction_item_byte = 0;
    private static int auctionItemAmount = 0;
    private static int auctionItemStarting = 0;
    private static double auctionItemBid = 0;
    private static boolean win = false;
    private int i;
	
	static {
	    output.add(ChatTools.formatTitle("Auction"));
    	output.add(ChatTools.formatCommand("", "/auction", "", "Checks current auction info."));
        output.add(ChatTools.formatCommand("Merchant", "/auction", "start [time] [item] [amount] [start price]", "Starts a new auction."));
        output.add(ChatTools.formatCommand("Merchant", "/auction", "end", "Ends your current auction."));
    	output.add(ChatTools.formatCommand("", "/auction", "bid [amount]", "Bids on an auction."));
	}
	
	public AuctionCommand(iAuction instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        if (sender instanceof Player) {
        	Player player = (Player) sender;
        	parseAuctionCommand(player, args);
            return true;
        } else {
            return false;
        }
    }
	
	private void parseAuctionCommand(Player player, String[] split) {
	    if (split.length == 0) {
	        if (isAuction) {
                String out = (ChatColor.GRAY +auctionOwner.getName()+
                        " has " + auctionItemAmount + " " +
                        ItemType.getFromID(auctionItemId, auction_item_byte).getName() +
                        " up for auction for " + auctionItemStarting +
                        ".00 Dei.");
                if (winner != null) {
                    out = (ChatColor.GRAY +winner.getName()+
                            " is winning the auction of " + auctionItemAmount + " " +
                            ItemType.getFromID(auctionItemId, auction_item_byte).getName() +
                            " for " + currentBid +
                            ".00 Dei.");
                }
                player.sendMessage(out);
	        } else {
	          warn(player, "There is no auction running at the moment.");
	        }
	        help(player);
	    } else if (split[0].equalsIgnoreCase("help") || split[0].equalsIgnoreCase("?")) {
	       for (String line : output)
                player.sendMessage(line);
	    } else if (split[0].equalsIgnoreCase("start") || split[0].equalsIgnoreCase("s")) {
            auctionStart(player, split);
	    } else if (split[0].equalsIgnoreCase("bid") || split[0].equalsIgnoreCase("b")) {
            auctionBid(player, split);
	    } else if (split[0].equalsIgnoreCase("end") || split[0].equalsIgnoreCase("e")) {
            auctionStop(player);
	    } else {
            
        }
	}

	public void auctionStart(Player player, String msg[]) {
        if (iAuction.Permissions.has(player, "auction.start") || player.isOp()) {
            if (!isAuction) {
                if (msg.length == 5) {
                    auctionOwner = player;
                    timerPlayer = player;
                    try {
                        auctionTime = Integer.parseInt(msg[1]);
                        auctionItemAmount = Integer.parseInt(msg[3]);
                        auctionItemStarting = Integer.parseInt(msg[4]);
                    } catch (NumberFormatException ex) {
                        warn(player, "Invalid syntax.");
                        help(player);
                        return;
                    }
                    int maxTime = iAuctionSettings.getMaxTime();
                    if (maxTime != 0 && auctionTime > maxTime) {
                        warn(player, ("Too long! Maximum time is "+maxTime+"."));
                        return;
                    }
                    int id[] = {-1, 0};
                    int count = 0;
                        
                    if (msg[2].contains(":") ) {
                        String[] split = msg[2].split(":");
                        id[0] = Integer.valueOf(split[0]);
                        id[1] = Integer.valueOf(split[1]);
                    } else if (msg[2].contains(";") ) {
                        String[] split = msg[2].split(";");
                        id[0] = Integer.valueOf(split[0]);
                        id[1] = Integer.valueOf(split[1]);
                    } else {
                        id[0] = Integer.valueOf(msg[2]);
                    }
                    
                    if (id[0] == -1 || id[0] == 0) {
                        warn(player, "Invalid item.");
                        return;
                    }
                    ItemStack aitemstack[];
                    int k = (aitemstack = player.getInventory().getContents()).length;
                    for (int i = 0; i < k; i++) {
                        ItemStack item = aitemstack[i];
                        if (item != null && item.getTypeId() == id[0]) {
                            MaterialData data = item.getData();
                            if (id[1] != 0) {
                                if (data.getData() == (byte) id[1])
                                    auctionItemDamage = item.getDurability();
                            } else {
                                auctionItemDamage = item.getDurability();
                            }
                        }
                    }

                    k = (aitemstack = player.getInventory().getContents()).length;
                    for (int j = 0; j < k; j++) {
                        ItemStack item = aitemstack[j];
                        if (item != null && item.getTypeId() == id[0]) {
                            MaterialData data = item.getData();
                            if (id[1] != 0) {
                                if (data.getData() == (byte) id[1] && item.getDurability() == auctionItemDamage)
                                    count++;
                            } else if (item.getDurability() == auctionItemDamage)
                                count++;
                        }
                        count = 1;
                    }

                    if (auctionItemAmount >= count) {
                        try {
                            auctionItemId = id[0]; 
                            auction_item_byte = Byte.valueOf(""+id[1]);
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                        currentBid = auctionItemStarting;
                        PlayerInventory inventory = player.getInventory();
                        if (auctionCheck(player, inventory, auctionItemId, auction_item_byte, auctionItemAmount, auctionTime, auctionItemStarting)) {
                            isAuction = true;
                            inventory.removeItem(new ItemStack[]{
                                    new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, auction_item_byte) });
                            final int interval = auctionTime;
                            
                            i = interval;

                            auctionTT = new TimerTask() {

                                double half = Math.floor(i / 2);
                                double quarter = Math.floor(i / 4);
                                double eighth = Math.floor(i / 8);
                                
                                @Override
                                public void run() {
                                    if (i>0) {
                                        if (i == 1) {
                                            plugin.broadcast(ChatColor.GRAY + "" + i + " second left to bid!");
                                        } else if (i == interval || i == half || i == quarter || i == eighth || i == 3 || i == 2) {
                                        	plugin.broadcast(ChatColor.GRAY + "" + i + " seconds left to bid!");
                                        }
                                    } else {
                                        auctionStop(timerPlayer);
                                    }
                                    i--;
                                }
                            };
                            
                            plugin.broadcast(ChatColor.GOLD +auctionOwner.getName()+
                                    " put up " + auctionItemAmount + " " +
                                    ItemType.getFromID(auctionItemId, auction_item_byte).getName() +
                                    " up for auction for " + auctionItemStarting +
                                    ".00 Dei.");
                           
                            auctionTimer = new Timer();
                            auctionTimer.scheduleAtFixedRate(auctionTT, 0L, 1000L);

                        }
                    } else {
                        warn(player, "Sorry but you have only "+count+" of that item.");
                    }
                } else {
                    warn(player, "Invalid syntax.");
                    help(player);
                }
            } else {
                warn(player, "There is already an auction running!");
            }
        } else {
            warn(player, "You don't have permission to start an auction!");
        }
    }
	
	public boolean auctionCheck(Player player, PlayerInventory inventory, int id, int data, int amount, int time, double price) {	        
	    if (iAuctionSettings.isLogging()) {
	        String lvl = "";
	        lvl = "SELECT `level` FROM `"+iAuctionSettings.getMySQLDatabaseTable()+"` WHERE `username` = '"+player.getName()+"' && `class` = '"+iAuctionSettings.getMySQLClassName()+"';";
	        int level = iAuction.database.GetInt(lvl);
	        if (!player.isOp()) {
	            if (level > 0) {
	                String cnt = "";
	                cnt = "SELECT COUNT(*) AS count FROM "+iAuction.database.tableName("log")+" WHERE auction_time > (SUBDATE(NOW(), INTERVAL 1 DAY)) && `username` = '"+player.getName()+"';";

	                int count = iAuction.database.GetInt(cnt);
	                if (level == 1 && count>=iAuctionSettings.maxAuctionsLevelOne()) {
	                    warn(player, "You can only use the auctions "+iAuctionSettings.maxAuctionsLevelOne()+" times per day for your class. Try again tomorrow.");
	                    return false;
	                } else if (level == 2 && count>=iAuctionSettings.maxAuctionsLevelTwo()) {
	                    warn(player, "You can only use the auctions "+iAuctionSettings.maxAuctionsLevelTwo()+" times per day for your class. Try again tomorrow.");
	                    return false;
	                } else if (level == 3 && count>=iAuctionSettings.maxAuctionsLevelThree()) {
	                    warn(player, "You can only use the auctions "+iAuctionSettings.maxAuctionsLevelThree()+" times per day for your class. Try again tomorrow.");
	                    return false;
	                }
	            } else {
	                return false;
	            }
	        }
	    }
	    if (time > iAuctionSettings.getMinTime()) {
	        ItemStack stacks[] = inventory.getContents();
	        int size = 0;
	        for (int i = 0; i < stacks.length; i++)
	            if (stacks[i] != null && stacks[i].getTypeId() == id && (!Misc.isDamageable(id) || stacks[i].getDurability() == 0))
	                size += stacks[i].getAmount();
	        List<Integer> restrictions = iAuctionSettings.getRestrictedItems();
	        for (int i : restrictions) {
	            if (id == i) {
	                warn(player, "Dont be stupid, you cant sell that!");
	                return false;
	            }
	        }
	        if (amount <= size) {
	            if (price >= 0.0D) {
	                return true;
	            } else {
	                warn(player, "The starting price has to be at least 0 Dei!");
	                return false;
	            } 
	        } else {
	            warn(player, "You don't have enough "+ItemType.getFromID(auctionItemId, auction_item_byte).getName()+" to do that!");
	            return false;
	        }
	    } else {
	        warn(player, "Time must be longer than "+iAuctionSettings.getMinTime()+" seconds!");
	        return false;
	    }
	}

	public void auctionInfo(Server server, Player player) {
        if (server != null) {
           plugin.broadcast("Auctioned Item: " + ItemType.getFromID(auctionItemId, auction_item_byte).getName());
           plugin.broadcast("Amount: "+auctionItemAmount);
           plugin.broadcast("Starting Price: " + auctionItemStarting+".00 Dei");
           plugin.broadcast("Owner: "+auctionOwner.getName());
       }
        if (player != null)
            if (isAuction) {
                String out = (ChatColor.GRAY +auctionOwner.getName()+
                        " has " + auctionItemAmount + " " +
                        ItemType.getFromID(auctionItemId, auction_item_byte).getName() +
                        " up for auction at " + auctionItemStarting +
                        ".00 Dei.");
                
                if (winner != null) {
                    out = (ChatColor.GRAY +winner.getName()+
                            " is winning the auction of " + auctionItemAmount + " " +
                            ItemType.getFromID(auctionItemId, auction_item_byte).getName() +
                            " at " + currentBid +
                            ".00 Dei.");
                }
                player.sendMessage(out);
            } else {
	            warn(player, "No auctions in session at the moment!");
	        }
	    } 

	public void auctionStop(Player player) {
	    if (iAuction.Permissions.has(player, "auction.end") || player == auctionOwner || player.isOp()) {
	        if (isAuction) {
	            auctionTimer.cancel();
	            int mat = Material.getMaterial(auctionItemId).getMaxStackSize();
	            if (win) {
	                plugin.broadcast(ChatColor.GOLD+"Auction Ended - "+winner.getName()+" won "+auctionItemAmount+" "+ItemType.getFromID(auctionItemId, auction_item_byte).getName() + " for "+currentBid+".00 Dei.");
	                winner.sendMessage(ChatColor.GREEN+"Enjoy your items!");
	                auctionOwner.sendMessage(ChatColor.GREEN+"Your items have been sold for "+currentBid+".00 Dei!");
	                iConomy.getAccount(winner.getName()).getHoldings().subtract(currentBid);
	                iConomy.getAccount(auctionOwner.getName()).getHoldings().add(currentBid);
	                int remainder = auctionItemAmount % mat;
                    int stacks = auctionItemAmount / mat;
                    ArrayList<ItemStack> it = new ArrayList<ItemStack>();
                    if (auctionItemAmount >= mat) {
                        it = new ArrayList<ItemStack>();
                        for (i = 0; i < stacks; i++) {
                            it.add(new ItemStack(auctionItemId, mat, auctionItemDamage,auction_item_byte));
                        }
                        it.add(new ItemStack(auctionItemId, remainder, auctionItemDamage, auction_item_byte));
                    } else {
                        it.add(new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, auction_item_byte));
                    }
                    
                    if (Misc.hasSpace(auctionOwner, stacks)) {
                        
                        for (ItemStack i : it) {
                            auctionOwner.getInventory().addItem(i);
                        }
                        
                    } else {
                        auctionOwner.sendMessage("You do not have enough Inventory space!");
                        auctionOwner.sendMessage("The items have been dropped at your feet");
                        for (ItemStack i : it) {
                            auctionOwner.getWorld().dropItemNaturally(auctionOwner.getLocation(), i);
                        }
                    }
	                if (iAuctionSettings.isLogging()) { writeToMySQL(true); }
	            } else {
	                plugin.broadcast(ChatColor.GRAY+"Auction ended with no bids.");
	                auctionOwner.sendMessage("Your items have been returned to you!");
	               
	                int remainder = auctionItemAmount % mat;
                    int stacks = auctionItemAmount / mat;
	                
                    ArrayList<ItemStack> it = new ArrayList<ItemStack>();
                    if (auctionItemAmount >= mat) {
                        it = new ArrayList<ItemStack>();
                        for (i = 0; i < stacks; i++) {
                            it.add(new ItemStack(auctionItemId, mat, auctionItemDamage,auction_item_byte));
                        }
                        it.add(new ItemStack(auctionItemId, remainder, auctionItemDamage, auction_item_byte));
                    } else {
                        it.add(new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, auction_item_byte));
                    }
                    
                    if (Misc.hasSpace(auctionOwner, stacks)) {
	                    
	                    for (ItemStack i : it) {
	                        auctionOwner.getInventory().addItem(i);
	                    }
	                    
	                } else {
	                    auctionOwner.sendMessage("You do not have enough Inventory space!");
	                    auctionOwner.sendMessage("The items have been dropped at your feet");
	                    for (ItemStack i : it) {
	                        auctionOwner.getWorld().dropItemNaturally(auctionOwner.getLocation(), i);
	                    }
	                }
	                if (iAuctionSettings.isLogging()) { writeToMySQL(false); }
	            }    
	            auctionItemId = 0;
	            auctionItemAmount = 0;
	            auctionItemStarting = 0;
	            currentBid = 0;
	            auctionItemBid = 0;
	            winner = null;
	            auctionOwner = null;
	            isAuction = false;
	            win = false;
	        } else {
	            warn(player, "No auctions in session at the moment!");
	            return;
	        }
	    } else {
	        warn(player, "You have no perrmisions to stop that auction!");
	    }
	}

	public void auctionBid(Player player, String msg[]) {
	    if (iAuction.Permissions.has(player, "auction.bid") || player.isOp()) {
	        if (msg.length == 2 || msg.length == 3) {
	            if (player != auctionOwner) {
	                String name = player.getName();
	                Account acc = iConomy.getAccount(name);
	                int bid;
	                int sbid;
	                try {
	                    bid = Integer.parseInt(msg[1]);
	                    if (msg.length == 2)
	                        sbid = 0;
	                    else
	                        sbid = Integer.parseInt(msg[2]);
	                } catch (Exception ex) {
	                    warn(player, " Invalid syntax.");
	                    help(player);
	                    return;
	                }
	                if (bid <= acc.getHoldings().balance() && sbid <= acc.getHoldings().balance()) {
	                    if (isAuction) {
	                        if (bid > currentBid) {
	                            win = true;
	                            if (bid > auctionItemBid) {
	                                currentBid = bid;
	                                auctionItemBid = sbid;
	                                winner = player;
	                                plugin.broadcast("Bid raised to "+bid+".00 Dei by "+player.getName());
	                                if (iAuctionSettings.getAntiSnipe()) i += iAuctionSettings.getAntiSnipeValue();
	                            } else {
	                                player.sendMessage("You have been outbid by "+winner.getName()+"'s secret bid!");
	                                plugin.broadcast("Bid raised! Currently stands at: "+(bid + 1.0D)+".00 Dei!");
	                                currentBid = bid + 1;
	                            }

	                        } else {
	                            warn(player, "Your bid was too low.");
	                        }
	                    } else {
	                        warn(player, "There is no auction running at the moment.");
	                    }
	                } else {
	                    warn(player, "You don't have enough money!");
	                    warn(player, "Your current balance is: "+acc.getHoldings().balance()+ " Dei.");
	                }
	            } else {
	                warn(player, "You can't bid on your own auction!");
	            }
	        } else {
	            warn(player, " Invalid syntax.");
	            help(player);
	        }
	    } else {
	        warn(player, "You don't have perrmisions to bid an auction!");
	    }
	}
	   
	private void writeToMySQL(boolean hasWon) {
	    String sql = "";
	    sql = ("INSERT INTO "+iAuction.database.tableName("log")+" "+
                " (`username`, `item_id`, `item_count`, `win_username`, `win_price`, `auction_time`) " +
                " VALUES (?,?,?,?,?,now());");
	    if (hasWon) {
	        iAuction.database.Write(sql, auctionOwner.getName(), auctionItemId, auctionItemAmount, winner.getName(), currentBid);
	    } else {
	        iAuction.database.Write(sql, auctionOwner.getName(), auctionItemId, auctionItemAmount, "none", currentBid);
	    }
	}
	
	public void warn(Player player, String msg) {
	    player.sendMessage(ChatColor.DARK_GRAY+"[Auction] " +ChatColor.RED + msg);
	}
	   
	public void help(Player player) {
	    player.sendMessage(ChatColor.DARK_GRAY+"[Auction] " +ChatColor.YELLOW+"Use \"/auction ?\" to recieve help.");
	}
	
	
    
}
