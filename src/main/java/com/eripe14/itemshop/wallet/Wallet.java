package com.eripe14.itemshop.wallet;

import java.util.UUID;

public class Wallet {
    private final UUID owner;
    private final double money;

    public Wallet(UUID owner, double money) {
        this.owner = owner;
        this.money = money;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public double getMoney() {
        return this.money;
    }
}