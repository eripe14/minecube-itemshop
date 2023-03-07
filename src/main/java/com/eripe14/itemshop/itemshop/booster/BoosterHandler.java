package com.eripe14.itemshop.itemshop.booster;

import com.eripe14.itemshop.configuration.implementation.booster.BoosterConfiguration;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface BoosterHandler {

    CompletableFuture<Boolean> executeBooster(Player player, BoosterConfiguration boosterConfiguration);

}