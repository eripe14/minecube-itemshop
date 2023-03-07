package com.eripe14.itemshop.itemshop.moneypackage;

import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;
import com.eripe14.itemshop.configuration.implementation.MessageConfiguration;
import com.eripe14.itemshop.configuration.implementation.moneypackage.MoneyPackageConfiguration;
import com.eripe14.itemshop.notification.NotificationAnnouncer;
import com.eripe14.itemshop.scheduler.Scheduler;
import com.eripe14.itemshop.util.Legacy;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import panda.utilities.text.Formatter;

public class MoneyPackagesInventory {

    private final Scheduler scheduler;
    private final MoneyPackageManager moneyPackageManager;
    private final MoneyPackageHandler moneyPackageHandler;
    private final MiniMessage miniMessage;
    private final MessageConfiguration messageConfiguration;
    private final NotificationAnnouncer notificationAnnouncer;

    public MoneyPackagesInventory(
            Scheduler scheduler,
            MoneyPackageManager moneyPackageManager,
            MoneyPackageHandler moneyPackageHandler,
            MiniMessage miniMessage,
            MessageConfiguration messageConfiguration,
            NotificationAnnouncer notificationAnnouncer
    ) {
        this.scheduler = scheduler;
        this.moneyPackageManager = moneyPackageManager;
        this.moneyPackageHandler = moneyPackageHandler;
        this.miniMessage = miniMessage;
        this.messageConfiguration = messageConfiguration;
        this.notificationAnnouncer = notificationAnnouncer;
    }

    public void openInventory(Player player) {
        this.scheduler.async(() -> {
            MessageConfiguration.MoneyPackageSection moneyPackageSection = this.messageConfiguration.moneyPackageSection;
            Formatter formatter = new Formatter();

            Gui gui = Gui.gui()
                    .title(Legacy.RESET_ITALIC.append(this.miniMessage.deserialize("&aPaczki monet")))
                    .rows(6)
                    .disableAllInteractions()
                    .create();

            this.moneyPackageManager.getMoneyPackages().whenComplete((moneyPackages, moneyPackagesError) -> {
                if (moneyPackagesError != null) {
                    moneyPackagesError.printStackTrace();
                    return;
                }

                for (MoneyPackageConfiguration moneyPackage : moneyPackages) {
                    this.setItem(gui, moneyPackage.getItemInGui(), event -> {
                        this.moneyPackageHandler.executeMoneyPackage(player, moneyPackage).whenComplete((result, moneyPackageError) -> {
                            if (moneyPackageError != null) {
                                moneyPackageError.printStackTrace();
                                return;
                            }

                            if (!result.success()) {
                                this.notificationAnnouncer.sendMessage(player, moneyPackageSection.notEnoughMoney);
                                return;
                            }

                            formatter.register("{PACKAGE}", moneyPackage.getName());
                            formatter.register("{PRICE}", moneyPackage.getBuyPrice());
                            formatter.register("{AMOUNT}", result.money());

                            this.notificationAnnouncer.sendMessage(player, moneyPackageSection.packageBought, formatter);
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