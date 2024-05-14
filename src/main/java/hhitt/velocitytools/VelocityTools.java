package hhitt.velocitytools;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.velocitytools.commands.AlertCommand;
import hhitt.velocitytools.commands.FindCommand;
import org.slf4j.Logger;

@Plugin(
        id = "velocitytools",
        name = "VelocityTools",
        version = "1.0",
        authors = "hhitt"
)
public class VelocityTools{

    private final ProxyServer proxy;

    @Inject
    public VelocityTools(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        CommandManager commandManager = proxy.getCommandManager();


        CommandMeta findCommandMeta = commandManager.metaBuilder("find")
                .aliases("whereis", "findplayer")
                .plugin(this)
                .build();
        SimpleCommand findCommand = new FindCommand(proxy);
        commandManager.register(findCommandMeta, findCommand);

        CommandMeta alertCommandMeta = commandManager.metaBuilder("alert")
                .aliases("broadcast")
                .plugin(this)
                .build();
        SimpleCommand alertCommand = new AlertCommand(proxy);
        commandManager.register(findCommandMeta, alertCommand);
    }
}
