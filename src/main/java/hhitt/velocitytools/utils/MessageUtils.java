package hhitt.velocitytools.utils;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageUtils {

    public static TextComponent MiniMessageParse (String message) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return (TextComponent) miniMessage.deserialize(message);
    }

}
