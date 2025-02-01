package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.TradeMenu;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TradeItem extends FunctionalItem {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEntityEvent e) {
        use(game, player, e.getRightClicked());
    }

    @Override
    public void use(PEGame game, PEPlayer player, EntityDamageByEntityEvent e) {
        use(game, player, e.getEntity());
    }

    private void use(PEGame game, PEPlayer player, Entity clicked) {
        PEPlayer clickedPlayer = game.getPEPlayer(clicked.getName());
        if (clickedPlayer == null) {
            return;
        }

        if (!player.isPrisoner() || !clickedPlayer.isPrisoner()) {
            return;
        }

        Prisoner sender = (Prisoner) player;
        Prisoner target = (Prisoner) clickedPlayer;

        if (sender.hasBeenRequestedBy(target) && sender.isStillValidRequest()) {
            sender.clearRequest();
            target.clearRequest();
            new TradeMenu(target, sender);
            return;
        }

        target.sendRequest(sender);

        String senderName = sender.getName();
        String targetName = target.getName();

        MessageLanguageManager senderMessages = MessageLanguageManager.getInstanceByPlayer(senderName);
        BukkitMessageSender.sendChatMessage(sender, senderMessages.getTradeRequestSentMessage(targetName));

        int time = ConfigManager.getInstance().getTradeRequestTimeout();
        MessageLanguageManager targetMessages = MessageLanguageManager.getInstanceByPlayer(targetName);
        String tradeRequestReceivedMessage = targetMessages.getTradeRequestReceivedMessage(senderName, time);
        BukkitMessageSender.sendChatMessage(target, tradeRequestReceivedMessage);
    }

}
