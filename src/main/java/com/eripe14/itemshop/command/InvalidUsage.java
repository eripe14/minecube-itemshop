package com.eripe14.itemshop.command;

import com.eripe14.itemshop.configuration.implementation.MessageConfiguration;
import com.eripe14.itemshop.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

import java.util.List;

public class InvalidUsage implements InvalidUsageHandler<CommandSender> {

    private final NotificationAnnouncer notificationAnnouncer;
    private final MessageConfiguration messageConfiguration;

    public InvalidUsage(NotificationAnnouncer notificationAnnouncer, MessageConfiguration messageConfiguration) {
        this.notificationAnnouncer = notificationAnnouncer;
        this.messageConfiguration = messageConfiguration;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {
        List<String> schematics = schematic.getSchematics();

        Formatter formatter = new Formatter()
                .register("{COMMAND}", schematics.get(0));

        if (schematics.size() == 1) {
            this.notificationAnnouncer.sendMessage(sender, this.messageConfiguration.wrongUsage.invalidUsage, formatter);
            return;
        }

        this.notificationAnnouncer.sendMessage(sender, this.messageConfiguration.wrongUsage.invalidUsageHeader);

        for (String schema : schematics) {
            this.notificationAnnouncer.sendMessage(sender, this.messageConfiguration.wrongUsage.invalidUsageEntry + schema);
        }
    }

}