package hhitt.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.velocitytools.VelocityTools;
import hhitt.velocitytools.utils.MessageUtils;
import net.kyori.adventure.title.TitlePart;

import java.util.Collection;
import java.util.stream.Collectors;

public class BroadcastCommand implements SimpleCommand {


    final ProxyServer proxy;
    final VelocityTools plugin;
    public BroadcastCommand(ProxyServer proxy, VelocityTools plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {

        //Get the necessary info while the command is runned

        //Gets the sender
        CommandSource sender = invocation.source();
        //Gets the message to send and converting it
        String[] args = invocation.arguments();

        if(!sender.hasPermission("velocitytools.broadcast") && !sender.hasPermission("velocitytools.admin")){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "No-Permission").getString()
            ));
            return;
        }

        //Check if the command has arguments. If no, it is wrong!
        if(args.length == 0){
            sender.sendMessage(MessageUtils.MiniMessageParse(
                    plugin.getConfig().node("Messages", "Wrong-Command-Usage").getString()
            ));
            return;
        }

        //Build the args into a String.
        Collection<Player> allPlayers = proxy.getAllPlayers().stream()
                .filter(source -> source instanceof Player)
                .map(source -> (Player) source)
                .collect(Collectors.toList());

        StringBuilder alert = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            alert.append(args[i]);
            if (i < args.length - 1) {
                alert.append(" ");
            }
        }

        //Detect the mode of alert at config.yml
        if(plugin.getConfig().node("Broadcast", "Mode").getString().equalsIgnoreCase("Message")){


            //Get all players and then send them the message by chat, also print on console
            allPlayers.forEach(player -> player.sendMessage(MessageUtils.MiniMessageParse(alert.toString())));
            //Also sent to console
            proxy.getConsoleCommandSource().sendMessage(MessageUtils.MiniMessageParse(alert.toString()));




        } else if (plugin.getConfig().node("Broadcast", "Mode").getString().equalsIgnoreCase("Title")) {



            //Get all players and then send them the message by Title, also print on console
            for (Player player : proxy.getAllPlayers()){

                //If is set as a Title at broadcast config:
                if(plugin.getConfig().node("Broadcast", "While-Title").getString().equalsIgnoreCase("Title")){
                    player.sendTitlePart(TitlePart.TITLE, MessageUtils.MiniMessageParse(alert.toString()));
                    player.sendTitlePart(TitlePart.SUBTITLE, MessageUtils.MiniMessageParse(""));
                    //If is set as a Subtitle at broadcast config:
                } else if(plugin.getConfig().node("Broadcast", "While-Title").getString().equalsIgnoreCase("Subtitle")){
                    player.sendTitlePart(TitlePart.TITLE, MessageUtils.MiniMessageParse(""));
                    player.sendTitlePart(TitlePart.SUBTITLE, MessageUtils.MiniMessageParse(alert.toString()));
                }



            }

            //Also sent to console
            proxy.getConsoleCommandSource().sendMessage(MessageUtils.MiniMessageParse(alert.toString()));



        } else if (plugin.getConfig().node("Broadcast", "Mode").getString().equalsIgnoreCase("ActionBar")){


            for (Player player : proxy.getAllPlayers()){

                //Send ActionBar
                player.sendActionBar(MessageUtils.MiniMessageParse(alert.toString()));

                //Also sent to console
                proxy.getConsoleCommandSource().sendMessage(MessageUtils.MiniMessageParse(alert.toString()));


            }

        }



    }


}
