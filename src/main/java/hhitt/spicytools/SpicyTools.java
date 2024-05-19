package hhitt.spicytools;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import hhitt.spicytools.commands.*;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Plugin(
        id = "spicytools",
        name = "SpicyTools",
        version = "1.0",
        authors = "hhitt"
)
public class SpicyTools {

    private final ProxyServer proxy;
    private final Logger logger;
    private ConfigurationNode config;

    @Inject
    public SpicyTools(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        //Config management on start
        config = createConfig("SpicyTools", "config.yml");
        if (config != null) {
            reloadConfig();
        }


        //Build the commands
        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta findCommandMeta = commandManager.metaBuilder("find")
                .aliases("whereis")
                .plugin(this)
                .build();
        SimpleCommand findCommand = new FindCommand(proxy, this);
        commandManager.register(findCommandMeta, findCommand);

        CommandMeta alertCommandMeta = commandManager.metaBuilder("alert")
                .aliases("warning")
                .plugin(this)
                .build();
        SimpleCommand alertCommand = new AlertCommand(proxy, this);
        commandManager.register(alertCommandMeta, alertCommand);

        CommandMeta broadcastCommandMeta = commandManager.metaBuilder("broadcast")
                .aliases("broadcast")
                .plugin(this)
                .build();
        SimpleCommand broadcastCommand = new BroadcastCommand(proxy, this);
        commandManager.register(broadcastCommandMeta, broadcastCommand);

        CommandMeta globalListCommandMeta = commandManager.metaBuilder("globallist")
                .aliases("glist")
                .plugin(this)
                .build();
        SimpleCommand globalListCommand = new GlobalListCommand(proxy, this);
        commandManager.register(globalListCommandMeta, globalListCommand);

        CommandMeta mainCommandMeta = commandManager.metaBuilder("spicytools")
                .aliases("stools")
                .plugin(this)
                .build();
        SimpleCommand mainCommand = new MainCommand(proxy, this);
        commandManager.register(mainCommandMeta, mainCommand);

        CommandMeta catchCommandMeta = commandManager.metaBuilder("catch")
                .aliases("catchplayer")
                .plugin(this)
                .build();
        SimpleCommand catchCommand = new CatchCommand(proxy, this);
        commandManager.register(catchCommandMeta, catchCommand);

        CommandMeta sendCommandMeta = commandManager.metaBuilder("send")
                .aliases("sendtoserver")
                .plugin(this)
                .build();
        SimpleCommand sendCommand = new SendCommand(proxy, this);
        commandManager.register(sendCommandMeta, sendCommand);
    }


    public ConfigurationNode getConfig() {
        return config;
    }


    /*
    Thanks to https://github.com/AddisionS for send me the createConfig method ;))
     */

    private ConfigurationNode createConfig(String folderName, String fileName) {
        Path pluginFolder = Paths.get("plugins", folderName);

        try {
            if (!Files.exists(pluginFolder)) {
                Files.createDirectories(pluginFolder);
            }

            Path configFile = pluginFolder.resolve(fileName);
            if (!Files.exists(configFile)) {
                Files.copy(getClass().getResourceAsStream("/" + fileName), configFile);
                logger.info("Created " + fileName + " from resources.");
            }

            ConfigurationLoader<?> loader = YamlConfigurationLoader.builder()
                    .path(configFile)
                    .build();

            return loader.load();
        } catch (IOException e) {
            logger.info("Failed to create or load configuration: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public ConfigurationNode reloadConfig() {
        try {
            Path configFile = Paths.get("plugins", "SpicyTools", "config.yml");
            if (!Files.exists(configFile)) {
                logger.warning("Configuration file does not exist.");
                return null;
            }

            ConfigurationLoader<?> loader = YamlConfigurationLoader.builder()
                    .path(configFile)
                    .build();

            config = loader.load();
            logger.info("Configuration reloaded successfully.");
            return config;
        } catch (IOException e) {
            logger.warning("Failed to reload configuration: " + e.getMessage());
            return null;
        }
    }

}