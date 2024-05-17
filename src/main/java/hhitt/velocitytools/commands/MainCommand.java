package hhitt.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.velocitytools.VelocityTools;
import hhitt.velocitytools.utils.MessageUtils;

public class MainCommand implements SimpleCommand {

    final ProxyServer proxy;
    final VelocityTools plugin;
    public MainCommand(ProxyServer proxy, VelocityTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        if(args.length == 0){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Wrong-Command-Usage").getString()));
            return;
        }


        if(args.length >= 0 && args[0].equalsIgnoreCase("reload")){

            if(!sender.hasPermission("velocitytools.reload") || !sender.hasPermission("velocitytools.admin")){
                sender.sendMessage(MessageUtils.MiniMessageParse(
                        plugin.getConfig().node("Messages", "No-Permission").getString()));
                return;
            }

            plugin.reloadConfig();
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Reload-Config").getString()));
            return;
        }

        if(args.length >= 0 && args[0].equalsIgnoreCase("help")){

            if(!sender.hasPermission("velocitytools.help") || !sender.hasPermission("velocitytools.admin")){
                sender.sendMessage(MessageUtils.MiniMessageParse(
                        plugin.getConfig().node("Messages", "No-Permission").getString()));
                return;
            }

            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Help").getString()));
        }


    }
}
