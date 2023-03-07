package com.eripe14.itemshop.itemshop.moneypackage;

import com.eripe14.itemshop.configuration.implementation.moneypackage.MoneyPackageConfiguration;
import com.eripe14.itemshop.purchase.PurchaseService;
import com.eripe14.itemshop.wallet.WalletRepository;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MoneyPackageHandlerImpl implements MoneyPackageHandler {

    private final Random random;
    private final PurchaseService purchaseService;
    private final WalletRepository walletRepository;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;


    public MoneyPackageHandlerImpl(PurchaseService purchaseService, WalletRepository walletRepository, AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
        this.random = new Random();
        this.purchaseService = purchaseService;
        this.walletRepository = walletRepository;
    }

    @Override
    public CompletableFuture<MoneyPackageResult> executeMoneyPackage(Player player, MoneyPackageConfiguration moneyPackage) {
        UUID uuid = player.getUniqueId();

        return this.walletRepository.removeMoney(uuid, moneyPackage.getBuyPrice()).thenApply(result -> {
            if (!result) {
                return new MoneyPackageResult(false, 0.0);
            }

            double randomReward = this.getRoundedNumber(this.getRandomAmount(moneyPackage));


            this.purchaseService.depositMoney(player, randomReward);

            return new MoneyPackageResult(true, randomReward);
        });
    }

    private double getRandomAmount(MoneyPackageConfiguration moneyPackage) {
        return this.random.nextDouble(moneyPackage.getMaxReward() - moneyPackage.getMinReward()) + moneyPackage.getMinReward();
    }

    private double getRoundedNumber(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}