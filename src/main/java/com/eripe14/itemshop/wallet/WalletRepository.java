package com.eripe14.itemshop.wallet;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WalletRepository {

    CompletableFuture<Wallet> findWallet(UUID owner);

    CompletableFuture<Boolean> update(UUID owner, double money);

    CompletableFuture<Boolean> addMoney(UUID owner, double money);

    CompletableFuture<Boolean> removeMoney(UUID owner, double money);

}