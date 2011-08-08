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
    private static int auction_item_byte = 0;
    private static int auctionItemAmount = 0;
    private static int auctionItemStarting = 0;
    private static double auctionItemBid = 0;
    private static boolean win = false;
    
    private int i;
	
	static {
    	output.add(ChatTools.formatTitle("iAuction"));
    	output.add(ChatTools.formatCommand("", "/auction", "", "Checks current auction info."));
    	output.add(ChatTools.formatCommand("Merchant", "/auction", "start [time] [item] [amount] [start price]", "Starts a new auction."));
    	output.add(ChatTools.formatCommand("Merchant", "/auction", "end", "Ends current auction"));
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
                        Items.name(auctionItemId, auction_item_byte).toLowerCase() +
                        " up for auction at " + auctionItemStarting +
                        ".00 Dei.");
                if (winner != null) {
                    out = (ChatColor.GRAY +winner.getName()+
                            " is winning the auction of " + auctionItemAmount + " " +
                            Items.name(auctionItemId, auction_item_byte).toLowerCase() +
                            " at " + currentBid +
                            ".00 Dei.");
                }
                player.sendMessage(out);
	        } else {
	          warn(player, "There is no auction running at the moment.");
	        }
	    } else if (split[0].equalsIgnoreCase("help") || split[0].equalsIgnoreCase("?")) {
            for (String line : output)
                player.sendMessage(line);
	    } else if (split[0].equalsIgnoreCase("start") || split[0].equalsIgnoreCase("s")) {
            auctionStart(player, split);
	    } else if (split[0].equalsIgnoreCase("bid") || split[0].equalsIgnoreCase("b")) {
            auctionBid(player, split);
	    } else if (split[0].equalsIgnoreCase("end") || split[0].equalsIgnoreCase("e")) {
            auctionStop(player);
	    } else if (split[0].equalsIgnoreCase("info") || split[0].equalsIgnoreCase("i")) {
            auctionInfo(null, player);
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
                    int id[] = {
                            -1, 0
                    };
                    int count = 0;
                    try {
                        id = Items.validate(msg[2]);
                    } catch (Exception e) {
                        warn(player, "Invalid item.");
                        return;
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
                        auctionItemId = id[0];
                        auction_item_byte = id[1];
                        currentBid = auctionItemStarting;
                        PlayerInventory inventory = player.getInventory();
                        if (auctionCheck(player, inventory, auctionItemId, auction_item_byte, auctionItemAmount, auctionTime, auctionItemStarting)) {
                            isAuction = true;
                            inventory.removeItem(new ItemStack[]{

                                    new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte) auction_item_byte))
                            });
                            final int interval = auctionTime;
                            
                            i = interval;

                            auctionTT = new TimerTask() {

                                double half = java.lang.Math.floor(i / 2);

                                @Override
                                public void run() {
                                    if (half <= 10) {
                                        if (i == interval || i == 10 || i == 3 || i == 2) {
                                            plugin.broadcast((ChatColor.GRAY + "" + i + " seconds left to bid!"));
                                        }
                                    } else {
                                        if (i == interval || i == half || i == 10 || i == 3 || i == 2) {
                                        	plugin.broadcast(ChatColor.GRAY + "" + i + " seconds left to bid!");
                                        }
                                    }
                                    if (i == 1) {
                                    	plugin.broadcast(ChatColor.GRAY + "" + i + " seconds left to bid!");
                                    }
                                    if (i == 0) {
                                        auctionStop(timerPlayer);
                                    }
                                    i--;
                                }
                            };
                            
                            plugin.broadcast(ChatColor.GRAY +auctionOwner.getName()+
                            		" put up " + auctionItemAmount + " " +
                            		Items.name(auctionItemId, auction_item_byte).toLowerCase() +
                            		" for auction at " + (auctionItemStarting) +
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
            warn(player, "You don't have perrmisions to start an auction!");
        }
    }
	
	public boolean auctionCheck(Player player, PlayerInventory inventory, int id, int data, int amount, int time, double price) {
	        if (time > 10) {
	            ItemStack stacks[] = inventory.getContents();
	            int size = 0;
	            for (int i = 0; i < stacks.length; i++)
	                if (stacks[i] != null && stacks[i].getTypeId() == id && (!Items.isDamageable(id) || stacks[i].getDurability() == 0))
	                    size += stacks[i].getAmount();

	            if (amount <= size) {
	                if (price >= 0.0D) {
	                    return true;
	                } else {
	                    warn(player, " The starting price has to be at least 0 dei!");
	                    return false;
	                }
	            } else {
	                warn(player, "You don't have enough "+Items.name(id, data)+" to do that!");
	                warn(player, "NOTE: You can't auction damaged tools.");
	                return false;
	            }
	        } else {
	            warn(player, "Time must be longer than 10 seconds!");
	            return false;
	        }
	    }

	public void auctionInfo(Server server, Player player) {
        if (server != null) {
           plugin.broadcast("Auctioned Item: " + Items.name(auctionItemId, auction_item_byte) + " ["+auctionItemId+"]");
           plugin.broadcast("Amount: "+auctionItemAmount);
           plugin.broadcast("Starting Price: " + auctionItemStarting+".00 Dei");
           plugin.broadcast("Owner: "+auctionOwner.getName());
       }
        if (player != null)
            if (isAuction) {
                String out = (ChatColor.GRAY +auctionOwner.getName()+
                        " has " + auctionItemAmount + " " +
                        Items.name(auctionItemId, auction_item_byte).toLowerCase() +
                        " up for auction at " + auctionItemStarting +
                        ".00 Dei.");
                if (winner != null) {
                    out = (ChatColor.GRAY +winner.getName()+
                            " is winning the auction of " + auctionItemAmount + " " +
                            Items.name(auctionItemId, auction_item_byte).toLowerCase() +
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
	            isAuction = false;
	            auctionTimer.cancel();
	            if (win) {
	                plugin.broadcast(ChatColor.GRAY+"Auction Ended - "+winner.getName()+" won "+auctionItemAmount+" "+Items.name(auctionItemId, auction_item_byte).toLowerCase());
	                winner.sendMessage(ChatColor.GREEN+"Enjoy your items!");
	                auctionOwner.sendMessage(ChatColor.GREEN+"Your items have been sold for "+currentBid+".00 Dei!");
	                iConomy.getAccount(winner.getName()).getHoldings().subtract(currentBid);
	                iConomy.getAccount(auctionOwner.getName()).getHoldings().add(currentBid);
	                if (Items.hasSpace(winner, auctionItemAmount)) {
	                    winner.getInventory().addItem(new ItemStack[]{
	                            new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte) auction_item_byte))
	                    });
	                } else {
	                    winner.sendMessage("You do not have enough Inventory space!");
	                    winner.sendMessage("The items have been dropped at your feet");
	                    winner.getWorld().dropItemNaturally(winner.getLocation(), new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte) auction_item_byte)));
	                }
	            } else {
	                plugin.broadcast(ChatColor.GRAY+"Auction ended with no bids.");
	                auctionOwner.sendMessage("Your items have been returned to you!");
	                if (Items.hasSpace(auctionOwner, auctionItemAmount)) {
	                    ItemStack[] itemss;
	                    if (auctionItemAmount > Material.getMaterial(auctionItemId).getMaxStackSize()) {
	                        ArrayList<ItemStack> it = new ArrayList<ItemStack>();
	                        int remainder = auctionItemAmount % Material.getMaterial(auctionItemId).getMaxStackSize();
	                        it.add(new ItemStack(auctionItemId, remainder, auctionItemDamage, Byte.valueOf((byte) auction_item_byte)));
	                        int stacks = auctionItemAmount - remainder;
	                        for (i = 1; i <= stacks; i++) {
	                            it.add(new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte) auction_item_byte)));
	                        }
	                        itemss = (ItemStack[]) it.toArray();
	                    } else {
	                        itemss = new ItemStack[]{new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte) auction_item_byte))};
	                    }
	                    auctionOwner.getInventory().addItem(itemss);
	                } else {
	                    auctionOwner.sendMessage("You do not have enough Inventory space!");
	                    auctionOwner.sendMessage("The items have been dropped at your feet");
	                    auctionOwner.getWorld().dropItemNaturally(auctionOwner.getLocation(), new ItemStack(auctionItemId, auctionItemAmount, auctionItemDamage, Byte.valueOf((byte) auction_item_byte)));
	                }
	            }
	            
	            writeToMySQL();
	            
	            auctionItemId = 0;
	            auctionItemAmount = 0;
	            auctionItemStarting = 0;
	            currentBid = 0;
	            auctionItemBid = 0;
	            winner = null;
	            auctionOwner = null;
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
	    if (iAuction.Permissions.has(player, "auction.bid")) {
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

	   
	private void writeToMySQL() {
	    String sql = "";
	    sql = ("INSERT INTO "+iAuction.database.tableName("log")+"("+
                " (`username`, `item_id`, `item_damage`, `item_count`, `win_username`, `win_price`, `auction_time`) " +
                " VALUES (?,?,?,?,?,?,now()) ");
	    iAuction.database.Write(sql, auctionOwner.getName(), auctionItemId, auctionItemDamage, auctionItemAmount, winner.getName(), currentBid);
	}
	
	public void warn(Player player, String msg) {
	    player.sendMessage(ChatColor.DARK_GRAY+"[Auction] " +ChatColor.RED + msg);
	}

	   
	public void help(Player player) {
	    player.sendMessage(ChatColor.YELLOW+"Use \"/auction ?\" to recieve help.");
	}
}
