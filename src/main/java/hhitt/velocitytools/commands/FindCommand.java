package hhitt.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.List;
import java.util.Optional;

public class FindCommand implements SimpleCommand {

    final ProxyServer proxy;

    public FindCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {

        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if(proxy.getAllPlayers().contains(args[0])){

            Optional<Player> playerToFind = proxy.getPlayer(args[0]);

            sender.sendPlainMessage("The player" + args[0] + "is on the server" + playerToFind.get().getCurrentServer());

        }

    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("command.test");
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

}
