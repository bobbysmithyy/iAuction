package org.rob4001.iAuction;

import org.bukkit.entity.Player;

import com.iConomy.system.Account;

public class ConfirmQuestionTask extends iAuctionQuestionTask {

    protected Player player;
    protected String message;
    protected int bid;
    protected Account acc;

    public ConfirmQuestionTask(Player player, String message) {

        this.player = player;
        this.message = message;
    }

    public ConfirmQuestionTask(Player player, String message, int bid, Account acc) {

        this.player = player;
        this.message = message;
        this.bid = bid;
        this.acc = acc;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

    public int getBid() {
        return this.bid;
    }
    
    public Account getAccount() {
        return this.acc;
    }
    
    @Override
    public void run() {
    }

}
