package hhitt.velocitytools;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
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
        CommandMeta commandMeta = commandManager.metaBuilder("find")
                .aliases("whereis")
                .plugin(this)
                .build();
        SimpleCommand simpleCommand = new FindCommand(proxy);
        commandManager.register(commandMeta, simpleCommand);
    }
}
