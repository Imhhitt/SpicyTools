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
import java.util.stream.Collectors;

public class SendCommand implements SimpleCommand {

    private final ProxyServer proxy;
    private final SpicyTools plugin;

    public SendCommand(ProxyServer proxy, SpicyTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        //Permission check
        if(!sender.hasPermission("spicytools.send") && !sender.hasPermission("spicytools.admin")){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "No-Permission").getString()
            ));
            return;
        }

        //The command need 2 arguments at least
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Wrong-Command-Usage").getString()));
            return;
        }

        String playerName = args[0];
        String serverName = args[1];


        //If the player is not valid
        Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
        if (!optionalPlayer.isPresent()) {
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Player-Not-Found").getString().replace(
                            "%player%", playerName)));
            return;
        }

        //If the name of the server is wrong
        Player player = optionalPlayer.get();
        Optional<RegisteredServer> optionalServer = proxy.getServer(serverName);
        if (!optionalServer.isPresent()) {
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Server-Not-Found").getString().replace(
                            "%server%", serverName)));
            return;
        }

        //Check if the player is already connected to the server
        if(args[1].equalsIgnoreCase(player.getCurrentServer().get().getServerInfo().getName())){

            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Already-Connected").getString()));
            return;
        }

        //Send the player to the server
        RegisteredServer server = optionalServer.get();
        player.createConnectionRequest(server).fireAndForget();

        sender.sendMessage(MessageUtils.MiniMessageParse(
                plugin.getConfig().node("Messages", "Player-Sender").getString().replace(
                        "%player%", playerName).replace("%server%", serverName)));

        player.sendMessage(MessageUtils.MiniMessageParse(
                plugin.getConfig().node("Messages", "Player-Sent").getString().replace(
                        "%player%", playerName).replace("%server%", serverName)));
    }


    //Arguments suggestion
    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            return proxy.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return proxy.getAllServers().stream()
                    .map(server -> server.getServerInfo().getName())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

    }
}
