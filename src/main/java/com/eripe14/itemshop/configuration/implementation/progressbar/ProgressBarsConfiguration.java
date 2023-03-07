package com.eripe14.itemshop.configuration.implementation.progressbar;

import com.eripe14.itemshop.configuration.ReloadableConfig;
import com.eripe14.itemshop.progressbar.ProgressBarRepository;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.kyori.adventure.bossbar.BossBar;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Contextual
public class ProgressBarsConfiguration implements ReloadableConfig, ProgressBarRepository {

    @Description( { " ", "# Progress bars configuration" })
    private final Map<String, ProgressBarConfiguration> progressBars = Map.of(
            "booster_x2", new ProgressBarConfiguration(
                    "Booster x2",
                    BossBar.Color.PINK,
                    BossBar.Overlay.PROGRESS),
            "booster_x4", new ProgressBarConfiguration(
                    "Booster x4",
                    BossBar.Color.PURPLE,
                    BossBar.Overlay.PROGRESS)
    );

    @Override
    public CompletableFuture<Optional<ProgressBarConfiguration>> findProgressBar(String name) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(this.progressBars.get(name)));
    }

    @Override
    public CompletableFuture<Set<ProgressBarConfiguration>> getProgressBars() {
        return CompletableFuture.supplyAsync(() -> Set.copyOf(this.progressBars.values()));
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "progress-bars.yml");
    }

}