package com.eripe14.itemshop.progressbar;

import com.eripe14.itemshop.itemshop.booster.BoosterHandlerImpl;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public class ProgressBarController implements Listener {

    private final ProgressBarManager progressBarManager;
    private final ProgressBarRepository repository;
    private final BoosterHandlerImpl boosterHandler;
    private final Server server;
    private final Plugin plugin;

    public ProgressBarController(ProgressBarManager progressBarManager, ProgressBarRepository repository, BoosterHandlerImpl boosterHandler, Server server, Plugin plugin) {
        this.progressBarManager = progressBarManager;
        this.repository = repository;
        this.boosterHandler = boosterHandler;
        this.server = server;
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Optional<Duration> remainingDurationOptional = this.boosterHandler.getRemainingBoosterTime(uuid);

        if (remainingDurationOptional.isEmpty()) {
            return;
        }

        Duration remainingDuration = remainingDurationOptional.get();

      //  this.repository.findProgressBar(uuid).whenComplete((progressBar, throwable) -> {
        //            if (throwable != null) {
        //                throwable.printStackTrace();
        //                return;
        //            }
        //
        //            this.progressBarManager.showProgressBar(player, progressBar);
        //
        //            ProgressBarTask progressBarTask = new ProgressBarTask(progressBar, this.progressBarManager);
        //
        //            this.server.getScheduler().runTaskTimer(this.plugin, progressBarTask, 20L, 20L);
        //        });
    }


}