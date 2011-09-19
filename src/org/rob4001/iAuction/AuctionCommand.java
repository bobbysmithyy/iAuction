package org.rob4001.iAuction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import ca.xshade.bukkit.questioner.Questioner;
import ca.xshade.questionmanager.Option;
import ca.xshade.questionmanager.Question;

import com.iConomy.iConomy;
import com.iConomy.system.Account;
import com.imdeity.utils.ChatTools;
import com.imdeity.utils.ItemType;

public class AuctionCommand implements CommandExecutor {

    private static final List<String> output = new ArrayList<String>();

    private static iAuction plugin = null;

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
    private static boolean win = false;
    private static int i;

    static {
        output.add(ChatTools.formatTitle("Auction"));
        output.add(ChatTools.formatCommand("", "/auction", "",
                "Checks current auction info."));
        output.add(ChatTools.formatCommand("Merchant", "/auction",
                "start [time] [item] [amount] [start price]",
                "Starts a new auction."));
        output.add(ChatTools.formatCommand("Merchant", "/auction", "end",
                "Ends your current auction."));
        output.add(ChatTools.formatCommand("", "/auction", "bid [amount]",
                "Bids on an auction."));
    }

    public AuctionCommand(iAuction instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String args[]) {
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
                String out = ("<option><white>"
                        + auctionOwner.getName()
                        + "<gray> has <yellow>"
                        + auctionItemAmount
                        + " "
                        + ItemType.getFromID(auctionItemId, auction_item_byte)
                                .getName() + "<gray> up for <yellow>"
                        + auctionItemStarting + ".00 Dei.");
                if (winner != null) {
                    out = ("<option><white>"
                            + winner.getName()
                            + "<gray> is winning <yellow>"
                            + auctionItemAmount
                            + " "
                            + ItemType.getFromID(auctionItemId,
                                    auction_item_byte).getName()
                            + "<gray> for <yellow>" + currentBid + ".00 Dei.");
                }
                ChatTools.formatAndSend(out, "Auction", player);
            } else {
                warn(player, "There is no auction running at the moment.");
            }
            help(player);
        } else if (split[0].equalsIgnoreCase("help")
                || split[0].equalsIgnoreCase("?")) {
            for (String line : output)
                player.sendMessage(line);
        } else if (split[0].equalsIgnoreCase("start")
                || split[0].equalsIgnoreCase("s")) {
            auctionStart(player, split);
        } else if (split[0].equalsIgnoreCase("bid")
                || split[0].equalsIgnoreCase("b")) {
            auctionBid(player, split);
        } else if (split[0].equalsIgnoreCase("end")
                || split[0].equalsIgnoreCase("e")) {
            auctionStop(player);
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
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    int maxTime = iAuctionSettings.getMaxTime();
                    if (maxTime != 0 && auctionTime > maxTime) {
                        warn(player,
                                ("Too long! Maximum time is " + maxTime + "."));
                        return;
                    }
                    int id[] = { -1, 0 };
                    int count = 0;

                    try {
                        if (msg[2].contains(":")) {
                            String[] split = msg[2].split(":");
                            id[0] = Integer.valueOf(split[0]);
                            id[1] = Integer.valueOf(split[1]);
                        } else if (msg[2].contains(";")) {
                            String[] split = msg[2].split(";");
                            id[0] = Integer.valueOf(split[0]);
                            id[1] = Integer.valueOf(split[1]);
                        } else {
                            id[0] = Integer.valueOf(msg[2]);
                        }
                    } catch (Exception ex) {
                        warn(player, ("Item has to be an ID number"));
                        return;
                    }

                    if (id[0] == -1 || id[0] == 0) {
                        warn(player, "Invalid item.");
                        return;
                    }
                    boolean falseItem = false;
                    ItemStack aitemstack[];
                    int k = (aitemstack = player.getInventory().getContents()).length;
                    for (int i = 0; i < k; i++) {
                        ItemStack item = aitemstack[i];
                        if (item != null && item.getTypeId() == id[0]) {
                            MaterialData data = item.getData();
                            if (id[1] != 0) {
                                if (data.getData() == Byte.valueOf("" + id[1])) {
                                    auction_item_byte = Byte
                                            .valueOf("" + id[1]);
                                } else {
                                    falseItem = true;
                                }

                            }
                        }
                    }

                    k = (aitemstack = player.getInventory().getContents()).length;
                    for (int j = 0; j < k; j++) {
                        ItemStack item = aitemstack[j];
                        if (item != null && item.getTypeId() == id[0]) {
                            MaterialData data = item.getData();
                            if (id[1] != 0) {
                                if (data.getData() == (byte) id[1]
                                        && item.getDurability() == auctionItemDamage)
                                    count++;
                            }
                        }
                    }

                    if (auctionItemAmount >= count && !falseItem) {
                        try {
                            auctionItemId = id[0];
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        currentBid = auctionItemStarting;
                        PlayerInventory inventory = player.getInventory();
                        if (auctionCheck(player, inventory, auctionItemId,
                                auction_item_byte, auctionItemAmount,
                                auctionTime, auctionItemStarting)) {

                            this.confirmStart(player);

                        }
                    } else {
                        warn(player, "You don't have enough "
                                + ItemType.getFromID(id[0], id[1]).getName()
                                + " to do that!");
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

    public boolean auctionCheck(Player player, PlayerInventory inventory,
            int id, int data, int amount, int time, double price) {
        if (iAuctionSettings.isLogging()) {
            String lvl = "";
            lvl = "SELECT `level` FROM `"
                    + iAuctionSettings.getMySQLDatabaseTable()
                    + "` WHERE `username` = '" + player.getName()
                    + "' && `class` = '" + iAuctionSettings.getMySQLClassName()
                    + "';";
            int level = iAuction.database.GetInt(lvl);
            if (!player.isOp()) {
                if (level > 0) {
                    String cnt = "";
                    cnt = "SELECT COUNT(*) AS count FROM "
                            + iAuction.database.tableName("log")
                            + " WHERE auction_time > (SUBDATE(NOW(), INTERVAL 1 DAY)) && `username` = '"
                            + player.getName() + "';";

                    int count = iAuction.database.GetInt(cnt);
                    if (level == 1
                            && count >= iAuctionSettings.maxAuctionsLevelOne()) {
                        warn(player,
                                "You can only use the auctions "
                                        + iAuctionSettings
                                                .maxAuctionsLevelOne()
                                        + " times per day for your class. Try again tomorrow.");
                        return false;
                    } else if (level == 2
                            && count >= iAuctionSettings.maxAuctionsLevelTwo()) {
                        warn(player,
                                "You can only use the auctions "
                                        + iAuctionSettings
                                                .maxAuctionsLevelTwo()
                                        + " times per day for your class. Try again tomorrow.");
                        return false;
                    } else if (level == 3
                            && count >= iAuctionSettings
                                    .maxAuctionsLevelThree()) {
                        warn(player,
                                "You can only use the auctions "
                                        + iAuctionSettings
                                                .maxAuctionsLevelThree()
                                        + " times per day for your class. Try again tomorrow.");
                        return false;
                    }
                } else {
                    warn(player, ("Your not a Merchant!"));
                    return false;
                }
            }
        }
        if (time > iAuctionSettings.getMinTime()) {
            ItemStack stacks[] = inventory.getContents();
            int size = 0;
            for (int i = 0; i < stacks.length; i++)
                if (stacks[i] != null
                        && stacks[i].getTypeId() == id
                        && (!Misc.isDamageable(id) || stacks[i].getDurability() == 0))
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
                warn(player,
                        "You don't have enough "
                                + ItemType.getFromID(auctionItemId,
                                        auction_item_byte).getName()
                                + " to do that!");
                return false;
            }
        } else {
            warn(player,
                    "Time must be longer than " + iAuctionSettings.getMinTime()
                            + " seconds!");
            return false;
        }
    }

    public void auctionInfo(Player player) {
        if (player != null)
            if (isAuction) {
                String out = ("<option><white>"
                        + auctionOwner.getName()
                        + "<gray> has <yellow>"
                        + auctionItemAmount
                        + " "
                        + ItemType.getFromID(auctionItemId, auction_item_byte)
                                .getName() + "<gray> up for <yellow>"
                        + auctionItemStarting + ".00 Dei.");
                if (winner != null) {
                    out = ("<option><white>"
                            + winner.getName()
                            + "<gray> is winning <yellow>"
                            + auctionItemAmount
                            + " "
                            + ItemType.getFromID(auctionItemId,
                                    auction_item_byte).getName()
                            + "<gray> for <yellow>" + currentBid + ".00 Dei.");
                }
                ChatTools.formatAndSend(out, "Auction", player);
            } else {
                warn(player, "No auctions in session at the moment!");
            }
    }

    public void auctionStop(Player player) {
        if (iAuction.Permissions.has(player, "auction.end")
                || player == auctionOwner || player.isOp()) {
            endAuction();
        } else {
            warn(player, "You have no permissions to stop that auction!");
        }
    }
    

    public void auctionBid(Player player, String msg[]) {
        if (iAuction.Permissions.has(player, "auction.bid") || player.isOp()) {
            if (msg.length == 2) {
                if (player != null && isAuction) {
                    String name = player.getName();
                    Account acc = iConomy.getAccount(name);
                    if (auctionOwner.getName().equalsIgnoreCase(
                            player.getName())) {
                        ChatTools
                                .formatAndSend(
                                        "<option>You can't bid on your own auction silly",
                                        "Auction", player);
                        return;
                    }
                    int bid;
                    try {
                        bid = Integer.parseInt(msg[1]);
                    } catch (Exception ex) {
                        warn(player, " Invalid syntax.");
                        help(player);
                        return;
                    }
                    if (bid <= acc.getHoldings().balance()) {
                        if (isAuction) {
                            if (bid > currentBid) {
                                win = true;
                                currentBid = bid;
                                winner = player;
                                plugin.broadcast(
                                        "<subheader><white>"
                                                + player.getName()
                                                + " <gray>bid <yellow>"
                                                + bid
                                                + ".00 Dei <gray>on <white>"
                                                + auctionItemAmount
                                                + " "
                                                + ItemType.getFromID(
                                                        auctionItemId,
                                                        auction_item_byte)
                                                        .getName() + ".", "");
                                if (iAuctionSettings.getAntiSnipe())
                                    i += iAuctionSettings.getAntiSnipeValue();

                            } else {
                                warn(player, "Your bid was too low.");
                            }
                        } else {
                            warn(player,
                                    "There is no auction running at the moment.");
                        }
                    } else {
                        warn(player, "You don't have enough money!");
                        warn(player, "Your current balance is: "
                                + acc.getHoldings().balance() + " Dei.");
                    }
                } else {
                    warn(player, "You can't bid on your own auction!");
                }
            } else {
                warn(player, " Invalid syntax.");
                help(player);
            }
        } else {
            warn(player, "You don't have permissions to bid an auction!");
        }
    }

    private void writeToMySQL() {
        String sql = "";
        sql = ("INSERT INTO "
                + iAuction.database.tableName("log")
                + " "
                + " (`username`, `item_id`, `item_count`, `win_username`, `win_price`, `auction_time`) " + " VALUES (?,?,?,?,?,now());");

        iAuction.database.Write(sql, auctionOwner.getName(), auctionItemId,
                auctionItemAmount, "nothing here", -1);
    }

    public static int getId() {
        String sql = "";
        sql = "SELECT `id` FROM " + iAuction.database.tableName("log")
                + " WHERE `username` = '" + auctionOwner.getName()
                + "' ORDER BY `id` DESC LIMIT 1";
        HashMap<Integer, ArrayList<String>> i = iAuction.database.Read(sql);
        if (!i.get(1).get(0).isEmpty()) {
            return Integer.parseInt(i.get(1).get(0));
        }
        return -1;
    }

    public static void updateMySQL(boolean hasWon) {
        String sql = "";
        sql = ("UPDATE " + iAuction.database.tableName("log") + " SET "
                + " `win_username` = ?, `win_price` = ? " + " WHERE `id` = "
                + getId() + "");
        if (hasWon) {
            iAuction.database.Write(sql, winner.getName(), currentBid);
        } else {
            iAuction.database.Write(sql, "", currentBid);
        }
    }

    public void warn(Player player, String msg) {
        ChatTools.formatAndSend("<option><red>" + msg, "Auction", player);
    }

    public void help(Player player) {
        ChatTools
                .formatAndSend(
                        "<option><yellow>Use \"/auction ?\" to see a list of commands.",
                        "Auction", player);
    }

    public void startCommand(Player player) {
        PlayerInventory inventory = player.getInventory();
        isAuction = true;
        inventory.removeItem(new ItemStack[] { new ItemStack(auctionItemId,
                auctionItemAmount, auctionItemDamage, auction_item_byte) });

        int interval = auctionTime;

        i = interval;
        if (i != 0) {
            auctionTT = new TimerTask() {

                double three = Math.floor(i * .75);
                double half = Math.floor(i / 2);
                double quarter = Math.floor(i / 4);
                double eighth = Math.floor(i / 8);

                @Override
                public void run() {
                    if (i > 0) {
                        if (i == 1) {
                            plugin.broadcast("<subheader><gray>" + i
                                    + " second left to bid!", "");
                        } else if (i == three || i == half || i == quarter
                                || i == eighth || i == 3 || i == 2) {
                            plugin.broadcast("<subheader><gray>" + i
                                    + " seconds left to bid!", "");
                        }
                    } else {
                        auctionStop(timerPlayer);
                    }
                    i--;
                }
            };
        }

        plugin.broadcast("<option><white>"
                + auctionOwner.getName()
                + "<gray> put <yellow>"
                + auctionItemAmount
                + " "
                + ItemType.getFromID(auctionItemId, auction_item_byte)
                        .getName() + "<gray> up for <yellow>"
                + auctionItemStarting + ".00 Dei.", "Auction");

        auctionTimer = new Timer();
        auctionTimer.scheduleAtFixedRate(auctionTT, 0L, 1000L);
        writeToMySQL();
    }

    public void confirmStart(Player player) {
        String phrase = "Are you sure you want to auction off "
                + auctionItemAmount
                + " "
                + ItemType.getFromID(auctionItemId, auction_item_byte)
                        .getName() + "?";

        Plugin test = plugin.getServer().getPluginManager()
                .getPlugin("Questioner");

        if (test != null && test instanceof Questioner && test.isEnabled()) {
            Questioner questioner = (Questioner) test;
            questioner.loadClasses();

            List<Option> options = new ArrayList<Option>();
            options.add(new Option("yes", new ConfirmQuestionTask(player,
                    phrase) {
                @Override
                public void run() {
                    startCommand(player);
                }
            }));
            options.add(new Option("no",
                    new ConfirmQuestionTask(player, phrase)));
            Question question = new Question(player.getName(), phrase, options);
            try {
                plugin.appendQuestion(questioner, question);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public static void endAuction() {
        if (isAuction) {
            isAuction = false;
            auctionTime = 0;
            auctionTimer.cancel();
            int mat = Material.getMaterial(auctionItemId).getMaxStackSize();
            if (win) {
                plugin.broadcast(
                        "<option><white>"
                                + winner.getName()
                                + "<gray> won <yellow>"
                                + auctionItemAmount
                                + " "
                                + ItemType.getFromID(auctionItemId,
                                        auction_item_byte).getName()
                                + "<gray> for <yellow>" + currentBid
                                + ".00 Dei.", "Auction");

                ChatTools.formatAndSend("<option><green>Enjoy your items!",
                        "Auction", winner);
                ChatTools.formatAndSend(
                        "<option><green>Your items have been sold for "
                                + currentBid + ".00 Dei!", "Auction",
                        auctionOwner);

                iConomy.getAccount(winner.getName()).getHoldings()
                        .subtract(currentBid);
                iConomy.getAccount(auctionOwner.getName()).getHoldings()
                        .add(currentBid);
                int remainder = auctionItemAmount % mat;
                int stacks = auctionItemAmount / mat;
                ArrayList<ItemStack> it = new ArrayList<ItemStack>();
                if (auctionItemAmount >= mat) {
                    it = new ArrayList<ItemStack>();
                    for (i = 0; i < stacks; i++) {
                        it.add(new ItemStack(auctionItemId, mat,
                                auctionItemDamage, auction_item_byte));
                    }
                    if (remainder > 0) {
                        it.add(new ItemStack(auctionItemId, remainder,
                                auctionItemDamage, auction_item_byte));
                    }
                } else {
                    it.add(new ItemStack(auctionItemId, auctionItemAmount,
                            auctionItemDamage, auction_item_byte));
                }

                if (Misc.hasSpace(winner, stacks)) {

                    for (ItemStack i : it) {
                        winner.getInventory().addItem(i);
                    }

                } else {
                    ChatTools
                            .formatAndSend(
                                    "<subheader>You do not have enough Inventory space!",
                                    "", winner);
                    ChatTools
                            .formatAndSend(
                                    "<subheader>The items have been dropped at your feet",
                                    "", winner);
                    for (ItemStack i : it) {
                        winner.getWorld().dropItemNaturally(
                                winner.getLocation(), i);
                    }
                }
                if (iAuctionSettings.isLogging()) {
                    updateMySQL(true);
                }
                winner = null;
            } else {
                plugin.broadcast("<option>Ended with no bids.", "Auction");
                ChatTools.formatAndSend(
                        "<subheader>Your items have been returned to you!",
                        "", auctionOwner);

                int remainder = auctionItemAmount % mat;
                int stacks = auctionItemAmount / mat;

                ArrayList<ItemStack> it = new ArrayList<ItemStack>();
                if (auctionItemAmount >= mat) {
                    it = new ArrayList<ItemStack>();
                    for (i = 0; i < stacks; i++) {
                        it.add(new ItemStack(auctionItemId, mat,
                                auctionItemDamage, auction_item_byte));
                    }
                    if (remainder > 0) {
                        it.add(new ItemStack(auctionItemId, remainder,
                                auctionItemDamage, auction_item_byte));
                    }
                } else {
                    it.add(new ItemStack(auctionItemId, auctionItemAmount,
                            auctionItemDamage, auction_item_byte));
                }

                if (Misc.hasSpace(auctionOwner, stacks)) {

                    for (ItemStack i : it) {
                        auctionOwner.getInventory().addItem(i);
                    }

                } else {
                    ChatTools
                            .formatAndSend(
                                    "<subheader>You do not have enough Inventory space!",
                                    "", auctionOwner);
                    ChatTools
                            .formatAndSend(
                                    "<subheader>The items have been dropped at your feet",
                                    "", auctionOwner);
                    for (ItemStack i : it) {
                        auctionOwner.getWorld().dropItemNaturally(
                                auctionOwner.getLocation(), i);
                    }
                }
                if (iAuctionSettings.isLogging()) {
                    updateMySQL(false);
                }
            }
            auctionItemId = 0;
            auctionItemAmount = 0;
            auctionItemStarting = 0;
            currentBid = 0;
            auctionOwner = null;
            win = false;
        } else {
            System.out.println("[Auction] No auctions in session at the moment!");
            return;
        }
    }
}
