package com.eripe14.itemshop.itemshop.booster;

import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;

import java.time.Duration;

public interface Booster {

    String getName();

    double getPrice();

    String getProgressBarName();

    Duration getBoosterDuration();

    String getPermission();

    ItemConfiguration getItemInGui();

}