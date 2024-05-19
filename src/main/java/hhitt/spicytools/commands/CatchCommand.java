package hhitt.spicytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import hhitt.spicytools.SpicyTools;
import hhitt.spicytools.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CatchCommand implements SimpleCommand {

    final ProxyServer proxy;
    final SpicyTools plugin;
    public CatchCommand(ProxyServer proxy, SpicyTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }
    @Override
    public void execute(Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        //The sender must be a player
        if(!(sender instanceof Player)){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Must-Be-Player").getString()));
            return;
        }

        Player catcher = (Player) sender;
        if(!sender.hasPermission("spicytools.catch") && !sender.hasPermission("spicytools.admin")){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "No-Permission").getString()
            ));
            return;
        }

        //Check if the command has arguments. If no, it is wrong!
        if(args.length == 0){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Wrong-Command-Usage").getString()));
            return;
        }


        //Get player and server
        String playerName = args[0];
        Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();

            //Check if the player is already connected to the server of catcher
            if(((Player) sender).getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(
                    player.getCurrentServer().get().getServerInfo().getName())){

                sender.sendMessage(MessageUtils.MiniMessageParse(
                        plugin.getConfig().node("Messages", "Already-Connected").getString()));
                return;
            }

            //Gets the servers and send the player to them
            Optional<ServerConnection> optionalServer = catcher.getCurrentServer();
            if (optionalServer.isPresent()) {

                RegisteredServer server = optionalServer.get().getServer();
                player.createConnectionRequest(server).fireAndForget();

                String senderMessage = plugin.getConfig().node("Catch", "Sender").getString();
                String formattedSenderMessage = senderMessage.replace("%player%", player.getUsername());
                sender.sendMessage(MessageUtils.MiniMessageParse(formattedSenderMessage));

                String caughtMessage = plugin.getConfig().node("Catch", "Caught-Player").getString();
                String formattedCaughtMessage = caughtMessage
                        .replace("%player%", player.getUsername())
                        .replace("%server%", player.getCurrentServer().get().getServerInfo().getName());
                sender.sendMessage(MessageUtils.MiniMessageParse(formattedCaughtMessage));


            } else {


                sender.sendMessage(MessageUtils.MiniMessageParse(
                        plugin.getConfig().node("Messages", "Server-Not-Found").getString()));
            }

        } else {
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Player-Not-Found").getString()));
        }


    }

    //Arguments suggestion
    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            return proxy.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
