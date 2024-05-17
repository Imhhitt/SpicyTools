package hhitt.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import hhitt.velocitytools.VelocityTools;
import hhitt.velocitytools.utils.MessageUtils;

import java.util.Collection;
import java.util.stream.Collectors;

public class GlobalListCommand implements SimpleCommand {

    final ProxyServer proxy;
    final VelocityTools plugin;
    public GlobalListCommand(ProxyServer proxy, VelocityTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }
    @Override
    public void execute(Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        if(!sender.hasPermission("velocitytools.globallist") || !sender.hasPermission("velocitytools.admin")){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "No-Permission").getString()));
            return;
        }

        String playerList = String.valueOf(proxy.getPlayerCount());
        String totalProxy = plugin.getConfig().node("Global-List", "Total").getString();
        String totalProxyFormatted = totalProxy.replace("%playercount%", playerList);
        sender.sendMessage(MessageUtils.MiniMessageParse(totalProxyFormatted));


        for (RegisteredServer server : proxy.getAllServers()) {
            String playersOnServer = String.valueOf(server.getPlayersConnected().size());
            String playersPerServer = plugin.getConfig().node("Global-List", "Per-Server").getString();
            String playersPerServerFormatTed = playersPerServer.replace(
                    "%server%", server.getServerInfo().getName()).replace("%playercount%", playersOnServer);
            sender.sendMessage(MessageUtils.MiniMessageParse(playersPerServerFormatTed));

        }

    }
}
