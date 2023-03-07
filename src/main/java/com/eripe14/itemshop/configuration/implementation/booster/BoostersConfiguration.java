package com.eripe14.itemshop.configuration.implementation.booster;

import com.eripe14.itemshop.configuration.ReloadableConfig;
import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;
import com.eripe14.itemshop.itemshop.booster.BoosterRepository;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.entity.Exclude;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BoostersConfiguration implements ReloadableConfig, BoosterRepository {

    @Description( { " ", "# Boosters configuration" })
    private final Map<String, BoosterConfiguration> boosters = Map.of(
            "x2", new BoosterConfiguration(
                    "x2",
                    100.0,
                    "booster_x2",
                    Duration.ofSeconds(30),
                    "itemshop.booster.2",
                    new ItemConfiguration(
                            11,
                            "&eBooster x2",
                            List.of("&7Kup ten booster aby", "&7podwoić swoje pieniądze!"),
                            List.of(ItemFlag.HIDE_ATTRIBUTES),
                            Material.LIME_CANDLE,
                            false
                    )
            ),
            "x4", new BoosterConfiguration(
                    "x4",
                    100.0,
                    "booster_x4",
                    Duration.ofSeconds(30),
                    "itemshop.booster.4",
                    new ItemConfiguration(
                            13,
                            "&eBooster x4",
                            List.of("&7Kup ten booster aby", "&7zwielokrotnić swoje pieniądze!"),
                            List.of(ItemFlag.HIDE_ATTRIBUTES),
                            Material.LIME_CANDLE,
                            false)
            )
    );

    @Exclude
    @Override
    public CompletableFuture<Optional<BoosterConfiguration>> getBooster(String name) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(this.boosters.get(name)));
    }

    @Exclude
    @Override
    public CompletableFuture<List<BoosterConfiguration>> getBoosters() {
        return CompletableFuture.supplyAsync(() -> List.copyOf(this.boosters.values()));
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "boosters.yml");
    }

}