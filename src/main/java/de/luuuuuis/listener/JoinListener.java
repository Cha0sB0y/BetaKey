package de.luuuuuis.listener;

import de.luuuuuis.BetaKey;
import de.luuuuuis.MySQL.KeyInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(LoginEvent e) {
        String uuid = e.getConnection().getUniqueId().toString();

        KeyInfo keyInfo = new KeyInfo();

        if (!keyInfo.getAllowedList().contains(uuid)) {
            e.setCancelReason(BetaKey.getKickReason());
            e.setCancelled(true);
        }


    }
}
