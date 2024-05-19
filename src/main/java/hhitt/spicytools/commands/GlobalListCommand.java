package hhitt.spicytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import hhitt.spicytools.SpicyTools;
import hhitt.spicytools.utils.MessageUtils;

public class GlobalListCommand implements SimpleCommand {

    final ProxyServer proxy;
    final SpicyTools plugin;
    public GlobalListCommand(ProxyServer proxy, SpicyTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }
    @Override
    public void execute(Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        //Permission check
        if(!sender.hasPermission("spicytools.globalList") && !sender.hasPermission("spicytools.admin")){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "No-Permission").getString()
            ));
            return;
        }

        //Total proxy players
        String playerList = String.valueOf(proxy.getPlayerCount());
        String totalProxy = plugin.getConfig().node("Global-List", "Total").getString();
        String totalProxyFormatted = totalProxy.replace("%playercount%", playerList);
        sender.sendMessage(MessageUtils.MiniMessageParse(totalProxyFormatted));


        //For each server players
        for (RegisteredServer server : proxy.getAllServers()) {
            String playersOnServer = String.valueOf(server.getPlayersConnected().size());
            String playersPerServer = plugin.getConfig().node("Global-List", "Per-Server").getString();
            String playersPerServerFormatTed = playersPerServer.replace(
                    "%server%", server.getServerInfo().getName()).replace("%playercount%", playersOnServer);
            sender.sendMessage(MessageUtils.MiniMessageParse(playersPerServerFormatTed));

        }

    }
}
