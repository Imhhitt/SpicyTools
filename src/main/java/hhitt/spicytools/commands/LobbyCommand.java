package hhitt.spicytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import hhitt.spicytools.SpicyTools;
import hhitt.spicytools.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LobbyCommand implements SimpleCommand {

    final ProxyServer proxy;
    final SpicyTools plugin;
    public LobbyCommand(ProxyServer proxy, SpicyTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        if(!(sender instanceof Player)){

            return;
        }

        if(!sender.hasPermission("spicytools.lobby") && !sender.hasPermission("spicytools.admin")){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "No-Permission").getString()));
            return;
        }

        Player player = (Player) sender;

        //Gets the lobby defined at config.yml
        String lobbyServer = plugin.getConfig().node("Lobby", "Lobby-Server").getString();


        //Check if the player is alredy connected to lobby server
        if(lobbyServer.equalsIgnoreCase(player.getCurrentServer().get().getServerInfo().getName())){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Lobby", "Already-Connected").getString()));
            return;
        }


        //Get all the servers
        List<String> serverNames = new ArrayList<>();
        proxy.getAllServers().forEach(server -> serverNames.add(server.getServerInfo().getName()));

        //If the server of config.yml is a valid one:
        if(!serverNames.contains(lobbyServer)){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Server-Not-Found").getString()));

            return;
        }

        //Gets the server and send the player

        Optional<RegisteredServer> lobbyServerRegisted = proxy.getServer(lobbyServer);
        player.sendMessage(MessageUtils.MiniMessageParse(
                plugin.getConfig().node("Lobby", "Pre-Connect-Message").getString()));

        player.createConnectionRequest(lobbyServerRegisted.get()).fireAndForget();

        player.sendMessage(MessageUtils.MiniMessageParse(
                plugin.getConfig().node("Lobby", "Post-Connect-Message").getString()));

    }
}
