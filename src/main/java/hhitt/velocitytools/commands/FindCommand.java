package hhitt.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.velocitytools.VelocityTools;
import hhitt.velocitytools.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class FindCommand implements SimpleCommand {

    final ProxyServer proxy;
    final VelocityTools plugin;
    public FindCommand(ProxyServer proxy, VelocityTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        if(!sender.hasPermission("velocitytools.find") && !sender.hasPermission("velocitytools.admin")){
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


        //Get the player and servers
        String playerName = args[0];
        Optional<Player> optionalPlayer = proxy.getPlayer(playerName);

        //Check is player is valid and then send the message
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            String playerFoundMessage = plugin.getConfig().node("Find", "Player-Found").getString();
            String formattedMessage = playerFoundMessage
                    .replace("%player%", player.getUsername())
                    .replace("%server%", player.getCurrentServer().get().getServerInfo().getName());
            sender.sendMessage(MessageUtils.MiniMessageParse(formattedMessage));
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
