package com.eripe14.itemshop.itemshop.moneypackage;

import com.eripe14.itemshop.configuration.implementation.ItemConfiguration;

public interface MoneyPackage {

    String getName();

    double getBuyPrice();

    double getMinReward();

    double getMaxReward();

    ItemConfiguration getItemInGui();

}