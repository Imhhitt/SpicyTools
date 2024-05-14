package hhitt.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.velocitytools.utils.MessageUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AlertCommand implements SimpleCommand {

    final ProxyServer proxy;
    public AlertCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }
    @Override
    public void execute(Invocation invocation) {

        //Get the necessary info while the command is runned

        //Gets the sender
        CommandSource sender = invocation.source();

        //Gets the message to send and converting it
        String[] args = invocation.arguments();
        String alert = Arrays.toString(args);

        //Gets all online players and senf them the message
        Collection<Player> allPlayers = proxy.getAllPlayers().stream()
                .filter(source -> source instanceof Player)
                .map(source -> (Player) source)
                .collect(Collectors.toList());

        allPlayers.forEach(player -> player.sendMessage(MessageUtils.MiniMessageParse(alert)));

        //Also send the message to console
        proxy.getConsoleCommandSource().sendMessage(MessageUtils.MiniMessageParse(alert));


    }
}
