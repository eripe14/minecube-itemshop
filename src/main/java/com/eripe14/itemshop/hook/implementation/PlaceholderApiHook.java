package com.eripe14.itemshop.hook.implementation;

import com.eripe14.itemshop.hook.Hook;
import com.eripe14.itemshop.wallet.Wallet;
import com.eripe14.itemshop.wallet.WalletRepository;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlaceholderApiHook extends PlaceholderExpansion implements Hook {

    private final AsyncLoadingCache<UUID, Wallet> walletCache;

    public PlaceholderApiHook(WalletRepository walletRepository) {
        this.walletCache = Caffeine.newBuilder()
                .refreshAfterWrite(3, TimeUnit.SECONDS)
                .buildAsync(key -> walletRepository.findWallet(key).get(15, TimeUnit.SECONDS));
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("money")) {
            UUID uuid = player.getUniqueId();
            Wallet wallet = this.walletCache.synchronous().getIfPresent(uuid);

            if (wallet == null) {
                return "0";
            }

            return String.valueOf(wallet.getMoney());
        }

        return "Unknown placeholder";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "itemshop";
    }

    @Override
    public @NotNull String getAuthor() {
        return "eripe14";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public void initialize() {
        this.register();
    }

    @Override
    public String pluginName() {
        return "PlaceholderAPI";
    }

    public AsyncLoadingCache<UUID, Wallet> getWalletCache() {
        return this.walletCache;
    }

}