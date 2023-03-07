package com.eripe14.itemshop.progressbar;

import com.eripe14.itemshop.configuration.implementation.progressbar.ProgressBarConfiguration;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ProgressBarRepository {

    CompletableFuture<Optional<ProgressBarConfiguration>> findProgressBar(String name);

    CompletableFuture<Set<ProgressBarConfiguration>> getProgressBars();

}