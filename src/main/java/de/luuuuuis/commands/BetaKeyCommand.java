package de.luuuuuis.commands;

import de.luuuuuis.BetaKey;
import de.luuuuuis.MojangUUIDResolve;
import de.luuuuuis.SQL.KeyInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BetaKeyCommand extends Command {

    public BetaKeyCommand(String name) {
        super(name, "", "bk");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        KeyInfo keyInfo = new KeyInfo();

        if (!p.hasPermission("BetaKey.key")) {
            p.sendMessage(BetaKey.prefix + "§cYou have no authorization for this.");
            return;
        }

        if (args.length == 0) {
            p.sendMessage(BetaKey.prefix + "§bBetaKey Help");
            p.sendMessage("");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createKey\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createPermaKey\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey deleteKey [KEY]\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey add [NAME]\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey remove [NAME]\"");
            p.sendMessage("");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("createKey")) {
                String randomKey;
                randomKey = BetaKey.getRandomKey(36);
                keyInfo.createKey(randomKey);

                TextComponent msg = new TextComponent(BetaKey.prefix + "§7The §bKey §7is §e" + randomKey);
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lClick to copy the key!").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, randomKey));

                p.sendMessage(msg);


            } else if (args[0].equalsIgnoreCase("createPermaKey")) {

                String randomKey;
                randomKey = BetaKey.getRandomKey(33) + "t-P";
                keyInfo.createKey(randomKey);

                TextComponent msg = new TextComponent(BetaKey.prefix + "§7The §bKey §7is §e" + randomKey);
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lClick to copy the key!").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, randomKey));

                p.sendMessage(msg);


            } else if (args[0].equalsIgnoreCase("deleteKey")) {
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey deleteKey [KEY]\"");
            } else if (args[0].equalsIgnoreCase("add")) {
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey add [NAME]\"");
            } else if (args[0].equalsIgnoreCase("remove")) {
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey remove [NAME]\"");
            } else {
                p.sendMessage(BetaKey.prefix + "§bBetaKey Help");
                p.sendMessage("");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createKey\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createPermaKey\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey deleteKey [KEY]\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey add [NAME]\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey remove [NAME]\"");
                p.sendMessage("");
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("deletekey")) {
                String deleteKey = args[1];
                if (keyInfo.keyIsValid(deleteKey)) {
                    keyInfo.deleteKey(deleteKey);
                    p.sendMessage(BetaKey.prefix + "§7The &bkey §7was deleted");
                } else {
                    p.sendMessage(BetaKey.prefix + "§7The §bkey §7doesn't exist or was already redeemed");
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                String pNAME = args[1];
                p.sendMessage(BetaKey.prefix + "§7The player §e" + pNAME + " §7will be unlocked");
                String pUUID = MojangUUIDResolve.getUUIDResult(pNAME).getValue();
                if (!pUUID.equals("null")) {
                    if (!keyInfo.getAllowedList().contains(pUUID) || !keyInfo.alreadyAdded(pUUID)) {
                        keyInfo.addPlayer(pUUID);
                        keyInfo.getAllowedList().add(pUUID);
                        p.sendMessage(BetaKey.prefix + "§7The player §e" + pNAME + " §7was unlocked");
                    } else {
                        p.sendMessage(BetaKey.prefix + "§7The player §e" + pNAME + " §7is already unlocked");
                    }
                } else {
                    p.sendMessage(BetaKey.prefix + "§7The player §e" + pNAME + " §7doesn't exists");
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                String pNAME = args[1];
                String pUUID = MojangUUIDResolve.getUUIDResult(pNAME).getValue();
                if (keyInfo.getAllowedList().contains(pUUID)) {
                    keyInfo.deletePlayer(pUUID);
                    keyInfo.getAllowedList().remove(pUUID);
                    p.sendMessage(BetaKey.prefix + "§7The player §e" + pNAME + " §7was removed");
                } else {
                    p.sendMessage(BetaKey.prefix + "§7The player §e" + pNAME + " §7was never unlocked");
                }
            } else {
                p.sendMessage(BetaKey.prefix + "§bBetaKey Help");
                p.sendMessage("");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createKey\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createPermaKey\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey deleteKey [KEY]\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey add [NAME]\"");
                p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey remove [NAME]\"");
                p.sendMessage("");
            }
        } else {
            p.sendMessage(BetaKey.prefix + "§bBetaKey Help");
            p.sendMessage("");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createKey\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey createPermaKey\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey deleteKey [KEY]\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey add [NAME]\"");
            p.sendMessage(BetaKey.prefix + "Command: \"/BetaKey remove [NAME]\"");
            p.sendMessage("");
        }


    }

}
