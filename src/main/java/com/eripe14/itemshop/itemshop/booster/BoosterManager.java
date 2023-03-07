package com.eripe14.itemshop.itemshop.booster;

import com.eripe14.itemshop.configuration.implementation.booster.BoosterConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BoosterManager {

    private final BoosterRepository boosterRepository;

    public BoosterManager(BoosterRepository boosterRepository) {
        this.boosterRepository = boosterRepository;
    }

    public CompletableFuture<Optional<BoosterConfiguration>> getBooster(String name) {
        return this.boosterRepository.getBooster(name);
    }

    public CompletableFuture<List<BoosterConfiguration>> getBoosters() {
        return this.boosterRepository.getBoosters();
    }

}