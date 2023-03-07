package com.eripe14.itemshop.configuration.implementation.moneypackage;

import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;
import com.eripe14.itemshop.itemshop.moneypackage.MoneyPackage;
import net.dzikoysk.cdn.entity.Contextual;

@Contextual
public class MoneyPackageConfiguration implements MoneyPackage {

    private String name;

    private double buyPrice;

    private double minReward;

    private double maxReward;

    private ItemConfiguration itemInGui;

    private MoneyPackageConfiguration() { }

    public MoneyPackageConfiguration(String name, double buyPrice, double minReward, double maxReward, ItemConfiguration itemInGui) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.minReward = minReward;
        this.maxReward = maxReward;
        this.itemInGui = itemInGui;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getBuyPrice() {
        return this.buyPrice;
    }

    @Override
    public double getMinReward() {
        return this.minReward;
    }

    @Override
    public double getMaxReward() {
        return this.maxReward;
    }

    @Override
    public ItemConfiguration getItemInGui() {
        return this.itemInGui;
    }
}