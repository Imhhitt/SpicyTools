package hhitt.spicytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.spicytools.SpicyTools;
import hhitt.spicytools.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements SimpleCommand {

    final ProxyServer proxy;
    final SpicyTools plugin;
    public MainCommand(ProxyServer proxy, SpicyTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        //The command need 1 argument at least
        if(args.length == 0){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Wrong-Command-Usage").getString()));
            return;
        }


        //Reload config command
        if(args.length >= 0 && args[0].equalsIgnoreCase("reload")){

            if(!sender.hasPermission("spicytools.reload") || !sender.hasPermission("spicytools.admin")){
                sender.sendMessage(MessageUtils.MiniMessageParse(
                        plugin.getConfig().node("Messages", "No-Permission").getString()));
                return;
            }

            plugin.reloadConfig();
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Reload-Config").getString()));
            return;
        }

        //Help command
        if(args.length >= 0 && args[0].equalsIgnoreCase("help")){

            if(!sender.hasPermission("spicytools.help") || !sender.hasPermission("spicytools.admin")){
                sender.sendMessage(MessageUtils.MiniMessageParse(
                        plugin.getConfig().node("Messages", "No-Permission").getString()));
                return;
            }

            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Help").getString()));
        }


    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
            List<String> suggestions = new ArrayList<>();
            suggestions.add("reload");
            suggestions.add("help");
        return suggestions;
    }
}
