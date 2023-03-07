package com.eripe14.itemshop.itemshop.booster;

import com.eripe14.itemshop.configuration.implementation.booster.BoosterConfiguration;
import com.eripe14.itemshop.progressbar.transaction.ProgressBarTransactionService;
import com.eripe14.itemshop.user.UserRepository;
import com.eripe14.itemshop.wallet.WalletRepository;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BoosterHandlerImpl implements BoosterHandler {

    private final LuckPerms luckPerms;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ProgressBarTransactionService progressBarTransactionService;
    private final Plugin plugin;
    private final Server server;

    public BoosterHandlerImpl(LuckPerms luckPerms, WalletRepository walletRepository, UserRepository userRepository, ProgressBarTransactionService progressBarTransactionService, Plugin plugin, Server server) {
        this.luckPerms = luckPerms;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.progressBarTransactionService = progressBarTransactionService;
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public CompletableFuture<Boolean> executeBooster(Player player, BoosterConfiguration boosterConfiguration) {
        UUID uuid = player.getUniqueId();

        return this.walletRepository.removeMoney(uuid, boosterConfiguration.getPrice()).whenComplete((result, removeError) -> {
            if (removeError != null) {
                removeError.printStackTrace();
                return;
            }

            if (!result) {
                CompletableFuture.completedFuture(false);
                return;
            }

            this.userRepository.findUser(uuid).whenComplete((user, findError) -> {
                if (findError != null) {
                    findError.printStackTrace();
                    return;
                }

                this.progressBarTransactionService.execute(user, boosterConfiguration.getProgressBarName(), boosterConfiguration.getBoosterDuration());
            });

            this.addTempPermission(uuid, boosterConfiguration);
            CompletableFuture.completedFuture(true);
        });
    }

    public Optional<Duration> getRemainingBoosterTime(UUID uuid) {
        User user = this.luckPerms.getUserManager().getUser(uuid);

        if (user == null) {
            return Optional.empty();
        }

        Collection<Node> nodes = user.getNodes();

        for (Node node : nodes) {
            if (node == null) {
                return Optional.empty();
            }

            if (!node.getKey().startsWith("itemshop.booster.")) {
                return Optional.empty();
            }

            return Optional.ofNullable(node.getExpiryDuration());
        }

        return Optional.empty();
    }

    private void addTempPermission(UUID uuid, BoosterConfiguration boosterConfiguration) {
        this.luckPerms.getUserManager().modifyUser(uuid, user -> {
            user.data().add(Node.builder(boosterConfiguration.getPermission())
                    .expiry(boosterConfiguration.getBoosterDuration())
                    .build());
        });
    }

}