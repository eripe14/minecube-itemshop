package com.eripe14.itemshop.progressbar.transaction;

import com.eripe14.itemshop.configuration.implementation.progressbar.ProgressBarConfiguration;
import com.eripe14.itemshop.progressbar.ProgressBarManager;
import com.eripe14.itemshop.progressbar.ProgressBarTask;
import com.eripe14.itemshop.user.User;
import com.eripe14.itemshop.user.UserRepository;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.UUID;

public class ProgressBarTransactionService {

    private final Plugin plugin;
    private final UserRepository userRepository;
    private final ProgressBarManager progressBarManager;
    private final MiniMessage miniMessage;
    private final AudienceProvider audienceProvider;

    public ProgressBarTransactionService(Plugin plugin, UserRepository userRepository, ProgressBarManager progressBarManager, MiniMessage miniMessage, AudienceProvider audienceProvider) {
        this.plugin = plugin;
        this.userRepository = userRepository;
        this.progressBarManager = progressBarManager;
        this.miniMessage = miniMessage;
        this.audienceProvider = audienceProvider;
    }

    public void execute(User user, String barName, Duration barDuration) {
        UUID userUuid = user.getUniqueId();

        ProgressBarTransaction transaction = new ProgressBarTransaction(userUuid, barName, barDuration);
        user.addTransaction(transaction);

        this.progressBarManager.getProgressBar(barName).thenAccept(progressBarOptional -> {
            if (progressBarOptional.isEmpty()) {
                return;
            }

            ProgressBarConfiguration progressBar = progressBarOptional.get();
            BossBar bar = BossBar.bossBar(
                    this.miniMessage.deserialize(progressBar.bossBarName()),
                    1.0f,
                    progressBar.bossBarColor(),
                    progressBar.bossBarOverlay()
            );

            new ProgressBarTask(bar, transaction, this.audienceProvider).runTaskTimer(this.plugin, 0L, 2L);
        });

        this.userRepository.update(user);
    }

}