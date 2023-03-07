package com.eripe14.itemshop.configuration.implementation.booster;

import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;
import com.eripe14.itemshop.itemshop.booster.Booster;
import net.dzikoysk.cdn.entity.Contextual;

import java.time.Duration;

@Contextual
public class BoosterConfiguration implements Booster {

    private String name;

    private double price;

    private String progressBarName;

    private Duration boosterDuration;

    private String permission;

    private ItemConfiguration itemInGui;

    private BoosterConfiguration() { }

    public BoosterConfiguration(String name, double price, String progressBarName, Duration boosterDuration, String permission, ItemConfiguration itemInGui) {
        this.name = name;
        this.price = price;
        this.progressBarName = progressBarName;
        this.boosterDuration = boosterDuration;
        this.permission = permission;
        this.itemInGui = itemInGui;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public String getProgressBarName() {
        return this.progressBarName;
    }

    @Override
    public Duration getBoosterDuration() {
        return this.boosterDuration;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public ItemConfiguration getItemInGui() {
        return this.itemInGui;
    }

}