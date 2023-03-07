package com.eripe14.itemshop.itemshop.booster;

import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;
import com.eripe14.itemshop.configuration.implementation.MessageConfiguration;
import com.eripe14.itemshop.configuration.implementation.booster.BoosterConfiguration;
import com.eripe14.itemshop.notification.NotificationAnnouncer;
import com.eripe14.itemshop.scheduler.Scheduler;
import com.eripe14.itemshop.util.DurationUtil;
import com.eripe14.itemshop.util.Legacy;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import panda.utilities.text.Formatter;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BoosterInventory {

    private final Scheduler scheduler;
    private final BoosterManager boosterManager;
    private final BoosterHandlerImpl boosterHandler;
    private final MiniMessage miniMessage;
    private final MessageConfiguration messageConfiguration;
    private final NotificationAnnouncer notificationAnnouncer;

    public BoosterInventory(
            Scheduler scheduler,
            BoosterManager boosterManager,
            BoosterHandlerImpl boosterHandler,
            MiniMessage miniMessage,
            MessageConfiguration messageConfiguration,
            NotificationAnnouncer notificationAnnouncer
    ) {
        this.scheduler = scheduler;
        this.boosterManager = boosterManager;
        this.boosterHandler = boosterHandler;
        this.miniMessage = miniMessage;
        this.messageConfiguration = messageConfiguration;
        this.notificationAnnouncer = notificationAnnouncer;
    }

    public void openInventory(Player player) {
        this.scheduler.async(() -> {
            MessageConfiguration.BoosterSection boosterSection = this.messageConfiguration.boosterSection;
            Formatter formatter = new Formatter();

            UUID uuid = player.getUniqueId();

            Gui gui = Gui.gui()
                    .title(Legacy.RESET_ITALIC.append(this.miniMessage.deserialize("&aBoostery")))
                    .rows(6)
                    .disableAllInteractions()
                    .create();

            Optional<Duration> remainingBoosterTimeOptional = this.boosterHandler.getRemainingBoosterTime(uuid);

            CompletableFuture<List<BoosterConfiguration>> boostersCompletableFuture = this.boosterManager.getBoosters();

            boostersCompletableFuture.whenComplete((boosters, boostersError) -> {
                if (boostersError != null) {
                    boostersError.printStackTrace();
                    return;
                }

                for (BoosterConfiguration booster : boosters) {
                    this.setItem(gui, booster.getItemInGui(), event -> {
                        if (remainingBoosterTimeOptional.isPresent()) {
                            Duration remainingBoosterTime = remainingBoosterTimeOptional.get();

                            formatter.register("{TIME}", DurationUtil.format(remainingBoosterTime));
                            this.notificationAnnouncer.sendMessage(player, boosterSection.boosterDelay, formatter);

                            gui.close(player);
                            return;
                        }

                        this.boosterHandler.executeBooster(player, booster).whenComplete((result, boosterError) -> {
                            if (boosterError != null) {
                                boosterError.printStackTrace();
                                return;
                            }

                            if (!result) {
                                this.notificationAnnouncer.sendMessage(player, boosterSection.notEnoughMoney);
                                return;
                            }

                            formatter.register("{PRICE}", booster.getPrice());
                            formatter.register("{BOOSTER}", booster.getName());

                            this.notificationAnnouncer.sendMessage(player, boosterSection.boosterBought, formatter);
                            gui.close(player);
                        });
                    });
                }
            });

            this.scheduler.sync(() -> gui.open(player));
        });
    }

    private void setItem(Gui gui, ItemConfiguration item, GuiAction<InventoryClickEvent> action) {
        gui.setItem(item.slot, item.asGuiItem(action));
    }

    private Component color(String text) {
        return Legacy.RESET_ITALIC.append(this.miniMessage.deserialize(text));
    }

}