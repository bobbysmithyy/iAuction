package org.rob4001.iAuction;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

import com.imdeity.utils.ChatTools;

public class iAuctionPlayerListener extends PlayerListener {

    @SuppressWarnings("unused")
    private iAuction plugin;

    public iAuctionPlayerListener(iAuction instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String sql = "";
        sql = "SELECT * FROM " + iAuction.database.tableName("log")
                + " WHERE `username` = '" + player.getName()
                + "' ORDER BY `id` DESC";
        HashMap<Integer, ArrayList<String>> query = iAuction.database.Read(sql);

        if (!query.isEmpty() && query.get(1).get(4).equalsIgnoreCase("nothing here")
                && query.get(1).get(1).equalsIgnoreCase(player.getName())) {

            int auctionItemAmount = Integer.parseInt(query.get(1).get(3));
            int auctionItemId = Integer.parseInt(query.get(1).get(2));
            int mat = Material.getMaterial(auctionItemId).getMaxStackSize();
            int remainder = auctionItemAmount % mat;
            int stacks = auctionItemAmount / mat;
            ArrayList<ItemStack> it = new ArrayList<ItemStack>();
            if (auctionItemAmount >= mat) {
                it = new ArrayList<ItemStack>();
                for (int i = 0; i < stacks; i++) {
                    it.add(new ItemStack(auctionItemId, mat));
                }
                if (remainder > 0) {
                    it.add(new ItemStack(auctionItemId, remainder));
                }
            } else {
                it.add(new ItemStack(auctionItemId, auctionItemAmount));
            }

            if (Misc.hasSpace(player, stacks)) {

                for (ItemStack i : it) {
                    player.getInventory().addItem(i);
                }

                ChatTools
                        .formatAndSend(
                                "<option>Your items from the last auction were refunded",
                                "Auction", player);
            } else {
                ChatTools.formatAndSend(
                        "<option>You do not have enough Inventory space!",
                        "Auction", player);
                ChatTools.formatAndSend(
                        "<option>The items have been dropped at your feet",
                        "Auction", player);
                for (ItemStack i : it) {
                    player.getWorld()
                            .dropItemNaturally(player.getLocation(), i);
                }
            }
            
            int id  = 0;
            sql = "SELECT `id` FROM " + iAuction.database.tableName("log")
                    + " WHERE `username` = '" + player.getName()
                    + "' ORDER BY `id` DESC";
            query = iAuction.database.Read(sql);
            System.out.println(query);
            if (!query.get(1).get(0).isEmpty()) {
                id = Integer.parseInt(query.get(1).get(0));
            }
            
            sql = ("UPDATE " + iAuction.database.tableName("log") + " SET "
                    + " `win_username` = ?,  `win_price` = ? " + " WHERE `id` = "
                    + id + "");
            iAuction.database.Write(sql, "Items Returned", -1);
        }
    }
    
    

}
