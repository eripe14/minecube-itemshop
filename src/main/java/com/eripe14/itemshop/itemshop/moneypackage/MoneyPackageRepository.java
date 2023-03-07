package com.eripe14.itemshop.itemshop.moneypackage;

import com.eripe14.itemshop.configuration.implementation.moneypackage.MoneyPackageConfiguration;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface MoneyPackageRepository {

    CompletableFuture<Optional<MoneyPackageConfiguration>> getMoneyPackage(String name);

    CompletableFuture<Set<MoneyPackageConfiguration>> getMoneyPackages();

}