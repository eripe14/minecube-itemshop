package com.eripe14.itemshop.progressbar;

import net.kyori.adventure.bossbar.BossBar;

public interface ProgressBar {

    String bossBarName();

    BossBar.Color bossBarColor();

    BossBar.Overlay bossBarOverlay();

}