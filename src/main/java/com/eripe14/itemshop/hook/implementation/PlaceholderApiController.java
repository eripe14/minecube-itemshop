package com.eripe14.itemshop.hook.implementation;

import com.eripe14.itemshop.wallet.Wallet;
import com.eripe14.itemshop.wallet.WalletUpdateEvent;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlaceholderApiController implements Listener {

    private final AsyncLoadingCache<UUID, Wallet> visualUserCache;

    public PlaceholderApiController(AsyncLoadingCache<UUID, Wallet> visualUserCache) {
        this.visualUserCache = visualUserCache;
    }

    @EventHandler
    void onVisualUserJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        this.visualUserCache.synchronous().refresh(uuid);
    }

    @EventHandler
    void onVisualUserQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        this.visualUserCache.synchronous().invalidate(uuid);
    }

    @EventHandler
    void onVisualRankUpdate(WalletUpdateEvent event) {
        UUID uuid = event.getUuid();

        this.visualUserCache.synchronous().refresh(uuid);
    }

}