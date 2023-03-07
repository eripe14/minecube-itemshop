package com.eripe14.itemshop.progressbar;

import com.eripe14.itemshop.progressbar.transaction.ProgressBarTransaction;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class ProgressBarTask extends BukkitRunnable {

    private final BossBar bar;
    private final ProgressBarTransaction transaction;
    private final AudienceProvider audienceProvider;
    private double progress = 1.0;

    public ProgressBarTask(BossBar bar, ProgressBarTransaction transaction, AudienceProvider audienceProvider) {
        this.bar = bar;
        this.transaction = transaction;
        this.audienceProvider = audienceProvider;
    }

    @Override
    public void run() {
        UUID uuid = this.transaction.getOwnerUniqueId();
        Audience player = this.audienceProvider.player(uuid);

        Duration duration = this.transaction.getDuration();

        Instant start = this.transaction.getStartTime();
        Instant now = Instant.now();
        Instant end = start.plus(duration);

        double change = 1.0 / duration.toMillis() * 100;

        this.progress -= change;

        if (this.progress <= 0 || now.isAfter(end)) {
            player.hideBossBar(this.bar);
            player.sendMessage(Component.text("Zakończono"));
            this.cancel();
            return;
        }

        this.bar.progress((float) this.progress);

        player.showBossBar(this.bar);
    }
}