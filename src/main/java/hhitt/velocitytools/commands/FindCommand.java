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

        if(args.length == 0){
            sender.sendPlainMessage("no se usa así");
            return;
        }

        if(sender.hasPermission("valocitytools.find")){


            if(proxy.getAllPlayers().contains(args[0])){

                Optional<Player> playerToFind = proxy.getPlayer(args[0]);

                sender.sendPlainMessage("The player" + args[0] + "is on the server" + playerToFind.get().getCurrentServer());

            }
        } else{
            sender.sendPlainMessage("jodete, no tienes perms");
        }



    }


}
