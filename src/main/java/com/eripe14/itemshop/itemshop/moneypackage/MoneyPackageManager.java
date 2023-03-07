package com.eripe14.itemshop.itemshop.moneypackage;

import com.eripe14.itemshop.configuration.implementation.moneypackage.MoneyPackageConfiguration;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MoneyPackageManager {

    private final MoneyPackageRepository moneyPackageRepository;

    public MoneyPackageManager(MoneyPackageRepository moneyPackageRepository) {
        this.moneyPackageRepository = moneyPackageRepository;
    }

    public CompletableFuture<Optional<MoneyPackageConfiguration>> getMoneyPackage(String name) {
        return this.moneyPackageRepository.getMoneyPackage(name);
    }

    public CompletableFuture<Set<MoneyPackageConfiguration>> getMoneyPackages() {
        return this.moneyPackageRepository.getMoneyPackages();
    }

}