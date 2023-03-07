package com.eripe14.itemshop.configuration.implementation.moneypackage;

import com.eripe14.itemshop.configuration.ReloadableConfig;
import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;
import com.eripe14.itemshop.itemshop.moneypackage.MoneyPackageRepository;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MoneyPackagesConfiguration implements ReloadableConfig, MoneyPackageRepository {

    @Description( { " ", "# Money packages configuration" })
    private final Map<String, MoneyPackageConfiguration> moneyPackages = Map.of(
            "starter", new MoneyPackageConfiguration(
                    "starter",
                    100.0,
                    90.0,
                    110.0,
                    new ItemConfiguration(
                            11,
                            "&eStarter",
                            List.of(
                                    "&7Kup ten pakiet za 100zł, aby",
                                    "&7otrzymać gwarantowane 90 monet!",
                                    "&7Możesz wygrać aż 110 monet!"
                            ),
                            List.of(ItemFlag.HIDE_ATTRIBUTES),
                            Material.LIME_CANDLE,
                            false
                    )
            ),
            "premium", new MoneyPackageConfiguration(
                    "premium",
                    300.0,
                    250.0,
                    500.0,
                    new ItemConfiguration(
                            13,
                            "&ePremium",
                            List.of(
                                    "&7Kup ten pakiet za 300zł, aby",
                                    "&7otrzymać gwarantowane 250 monet!",
                                    "&7Możesz wygrać aż 500 monet!"
                            ),
                            List.of(ItemFlag.HIDE_ATTRIBUTES),
                            Material.RED_CANDLE,
                            false
                    )
            ),
            "vip", new MoneyPackageConfiguration(
                    "vip",
                    500.0,
                    400.0,
                    1000.0,
                    new ItemConfiguration(
                            15,
                            "&eVIP",
                            List.of(
                                    "&7Kup ten pakiet za 500zł, aby",
                                    "&7otrzymać gwarantowane 400 monet!",
                                    "&7Możesz wygrać aż 1000 monet!"
                            ),
                            List.of(ItemFlag.HIDE_ATTRIBUTES),
                            Material.BLUE_CANDLE,
                            false
                    )
            )
    );

    @Override
    public CompletableFuture<Optional<MoneyPackageConfiguration>> getMoneyPackage(String name) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(this.moneyPackages.get(name)));
    }

    @Override
    public CompletableFuture<Set<MoneyPackageConfiguration>> getMoneyPackages() {
        return CompletableFuture.supplyAsync(() -> Set.copyOf(this.moneyPackages.values()));
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "moneypackages.yml");
    }

}