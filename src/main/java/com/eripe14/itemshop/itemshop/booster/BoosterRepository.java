package com.eripe14.itemshop.itemshop.booster;

import com.eripe14.itemshop.configuration.implementation.booster.BoosterConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BoosterRepository {

    CompletableFuture<Optional<BoosterConfiguration>> getBooster(String name);

    CompletableFuture<List<BoosterConfiguration>> getBoosters();

}