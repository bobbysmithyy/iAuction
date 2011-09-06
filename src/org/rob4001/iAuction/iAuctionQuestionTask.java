package org.rob4001.iAuction;

import ca.xshade.bukkit.questioner.BukkitQuestionTask;

public abstract class iAuctionQuestionTask extends BukkitQuestionTask {
    protected iAuction iAuction;

     public void setiAuction(iAuction instance) {
        this.iAuction = instance;
    }

    @Override
    public abstract void run();
}
