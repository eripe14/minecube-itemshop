package com.eripe14.itemshop.configuration.implementation.progressbar;

import com.eripe14.itemshop.progressbar.ProgressBar;
import net.dzikoysk.cdn.entity.Contextual;
import net.kyori.adventure.bossbar.BossBar;

@Contextual
public class ProgressBarConfiguration implements ProgressBar {

    private String name;

    private BossBar.Color color;

    private BossBar.Overlay overlay;

    private ProgressBarConfiguration() { }

    public ProgressBarConfiguration(String name, BossBar.Color color, BossBar.Overlay overlay) {
        this.name = name;
        this.color = color;
        this.overlay = overlay;
    }

    @Override
    public String bossBarName() {
        return this.name;
    }

    @Override
    public BossBar.Color bossBarColor() {
        return this.color;
    }

    @Override
    public BossBar.Overlay bossBarOverlay() {
        return this.overlay;
    }

}