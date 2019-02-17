package de.luuuuuis.commands;

import de.luuuuuis.BetaKey;
import de.luuuuuis.MojangUUIDResolve;
import de.luuuuuis.MySQL.KeyInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BetaKeyCmd extends Command {

    public BetaKeyCmd(String name) {
        super(name, "", "bk");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (!p.hasPermission("BetaKey.key")) {
            p.sendMessage(BetaKey.getPrefix() + "§cYou have no authorization for this.");
            return;
        }

        KeyInfo keyInfo = new KeyInfo();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                String rndKey = BetaKey.getRandomKey(36);
                keyInfo.createKey(rndKey);

                TextComponent msg = new TextComponent(BetaKey.getPrefix() + "§7The §bKey §7is §e" + rndKey + " §b[Click to copy]");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lClick to copy the key!").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, rndKey));

                p.sendMessage(msg);
                return;
            } else if (args[0].equalsIgnoreCase("list")) {
                p.sendMessage(BetaKey.getPrefix() + "Players:");
                keyInfo.getAllowedNameList().forEach(players -> p.sendMessage("§7> " + players));
                p.sendMessage("");
                p.sendMessage(BetaKey.getPrefix() + "Keys:");
                keyInfo.getKeyList().forEach(keys -> {
                    TextComponent msg = new TextComponent("§7> " + keys);
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lClick to copy the key!").create()));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, keys));

                    p.sendMessage(msg);
                });
                p.sendMessage("");
                return;
            }

            p.sendMessage(BetaKey.getPrefix() + "§bBetaKey Help");
            p.sendMessage("");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey list");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey create");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey create PERMA");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey delete <KEY>");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey add <NAME>");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey remove <NAME>");
            p.sendMessage("");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create") && args[1].equalsIgnoreCase("perma")) {
                String rndKey = BetaKey.getRandomKey(33) + "t-P";
                keyInfo.createKey(rndKey);

                TextComponent msg = new TextComponent(BetaKey.getPrefix() + "§7The §bKey §7is §e" + rndKey + " §b[Click to copy]");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lClick to copy the key!").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, rndKey));

                p.sendMessage(msg);
                return;
            } else if (args[0].equalsIgnoreCase("delete")) {
                keyInfo.deleteKey(args[1]);
                p.sendMessage(BetaKey.getPrefix() + "This key has been shredded!");
            } else if (args[0].equalsIgnoreCase("add")) {
                String uuid = MojangUUIDResolve.getUUIDResult(args[1]).getValue();
                keyInfo.addPlayer(uuid, args[1]);
                p.sendMessage(BetaKey.getPrefix() + "§e" + args[1] + " §7has been added!");
            } else if (args[0].equalsIgnoreCase("remove")) {
                String uuid = MojangUUIDResolve.getUUIDResult(args[1]).getValue();
                keyInfo.removePlayer(uuid, args[1]);
                p.sendMessage(BetaKey.getPrefix() + "§e" + args[1] + " §7has been removed!");
            }
        } else {
            p.sendMessage(BetaKey.getPrefix() + "§bBetaKey Help");
            p.sendMessage("");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey list");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey create");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey create PERMA");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey delete <KEY>");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey add <NAME>");
            p.sendMessage(BetaKey.getPrefix() + "/BetaKey remove <NAME>");
            p.sendMessage("");
        }

    }
}
