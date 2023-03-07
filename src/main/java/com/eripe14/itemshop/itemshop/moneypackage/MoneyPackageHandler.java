package com.eripe14.itemshop.itemshop.moneypackage;

import com.eripe14.itemshop.configuration.implementation.moneypackage.MoneyPackageConfiguration;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface MoneyPackageHandler {

    CompletableFuture<MoneyPackageResult> executeMoneyPackage(Player player, MoneyPackageConfiguration moneyPackage);

}