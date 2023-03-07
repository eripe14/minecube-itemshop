package com.eripe14.itemshop.wallet;

import com.eripe14.itemshop.EventCaller;
import com.eripe14.itemshop.configuration.implementation.MessageConfiguration;
import com.eripe14.itemshop.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

import java.util.UUID;

@Route(name = "wallet")
public class WalletCommand {

    private final MessageConfiguration messageConfiguration;
    private final NotificationAnnouncer notificationAnnouncer;
    private final WalletRepository walletRepository;
    private final EventCaller eventCaller;

    public WalletCommand(MessageConfiguration messageConfiguration, NotificationAnnouncer notificationAnnouncer, WalletRepository walletRepository, EventCaller eventCaller) {
        this.messageConfiguration = messageConfiguration;
        this.notificationAnnouncer = notificationAnnouncer;
        this.walletRepository = walletRepository;
        this.eventCaller = eventCaller;
    }

    @Execute
    @Permission("itemshop.wallet")
    void wallet(Player player) {
        MessageConfiguration.WalletSection walletSection = this.messageConfiguration.walletSection;
        Formatter formatter = new Formatter();

        UUID uuid = player.getUniqueId();

        this.walletRepository.findWallet(uuid).whenComplete((wallet, findError) -> {
            if (findError != null) {
                findError.printStackTrace();
                return;
            }

            formatter.register("{MONEY}", wallet.getMoney());

            this.notificationAnnouncer.sendMessage(player, walletSection.wallet, formatter);
        });
    }

    @Execute(route = "set")
    @Permission("itemshop.wallet.set")
    void set(Player player, @Arg Player target, @Arg @Name("amount") double amount) {
        MessageConfiguration.WalletSection walletSection = this.messageConfiguration.walletSection;
        Formatter formatter = new Formatter()
                .register("{TARGET}", target.getName())
                .register("{AMOUNT}", amount);

        UUID targetUuid = target.getUniqueId();

        this.walletRepository.update(targetUuid, amount).whenComplete((result, updateError) -> {
            if (updateError != null) {
                updateError.printStackTrace();
                return;
            }

            if (!result) {
                this.notificationAnnouncer.sendMessage(player, walletSection.updateWalletError, formatter);
                return;
            }

            this.eventCaller.callEvent(new WalletUpdateEvent(targetUuid));

            this.notificationAnnouncer.sendMessage(player, walletSection.setWallet, formatter);
            this.notificationAnnouncer.sendMessage(target, walletSection.setWalletTarget, formatter);
        });
    }

    @Execute(route = "add")
    @Permission("itemshop.wallet.add")
    void add(Player player, @Arg Player target, @Arg @Name("amount") double amount) {
        MessageConfiguration.WalletSection walletSection = this.messageConfiguration.walletSection;
        Formatter formatter = new Formatter()
                .register("{TARGET}", target.getName())
                .register("{AMOUNT}", amount);

        UUID targetUuid = target.getUniqueId();

        this.walletRepository.addMoney(targetUuid, amount).whenComplete((result, addError) -> {
            if (addError != null) {
                addError.printStackTrace();
                return;
            }

            if (!result) {
                this.notificationAnnouncer.sendMessage(player, walletSection.updateWalletError, formatter);
                return;
            }

            this.eventCaller.callEvent(new WalletUpdateEvent(targetUuid));

            this.notificationAnnouncer.sendMessage(player, walletSection.addWallet, formatter);
            this.notificationAnnouncer.sendMessage(target, walletSection.addWalletTarget, formatter);
        });
    }

    @Execute(route = "remove")
    @Permission("itemshop.wallet.remove")
    void remove(Player player, @Arg Player target, @Arg @Name("amount") double amount) {
        MessageConfiguration.WalletSection walletSection = this.messageConfiguration.walletSection;
        Formatter formatter = new Formatter()
                .register("{TARGET}", target.getName())
                .register("{AMOUNT}", amount);

        UUID targetUuid = target.getUniqueId();

        this.walletRepository.removeMoney(targetUuid, amount).whenComplete((result, removeError) -> {
            if (removeError != null) {
                removeError.printStackTrace();
                return;
            }

            if (!result) {
                this.notificationAnnouncer.sendMessage(player, walletSection.updateWalletError, formatter);
                return;
            }

            this.eventCaller.callEvent(new WalletUpdateEvent(targetUuid));

            this.notificationAnnouncer.sendMessage(player, walletSection.removeWallet, formatter);
            this.notificationAnnouncer.sendMessage(target, walletSection.removeWalletTarget, formatter);
        });
    }

    @Execute(route = "get")
    @Permission("itemshop.wallet.get")
    void get(Player player, @Arg Player target) {
        MessageConfiguration.WalletSection walletSection = this.messageConfiguration.walletSection;
        Formatter formatter = new Formatter()
                .register("{TARGET}", target.getName());

        UUID targetUuid = target.getUniqueId();

        this.walletRepository.findWallet(targetUuid).whenComplete((wallet, findError) -> {
            if (findError != null) {
                findError.printStackTrace();
                return;
            }

            formatter.register("{AMOUNT}", wallet.getMoney());

            this.notificationAnnouncer.sendMessage(player, walletSection.getWallet, formatter);
        });
    }

}