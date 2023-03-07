package com.eripe14.itemshop.progressbar;

import com.eripe14.itemshop.configuration.implementation.progressbar.ProgressBarConfiguration;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ProgressBarManager {

    private final ProgressBarRepository progressBarRepository;

    public ProgressBarManager(ProgressBarRepository progressBarRepository) {
        this.progressBarRepository = progressBarRepository;
    }

    public CompletableFuture<Optional<ProgressBarConfiguration>> getProgressBar(String name) {
        return this.progressBarRepository.findProgressBar(name);
    }

    public CompletableFuture<Set<ProgressBarConfiguration>> getProgressBars() {
        return this.progressBarRepository.getProgressBars();
    }

}