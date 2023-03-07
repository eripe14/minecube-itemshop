package com.eripe14.itemshop.progressbar.transaction;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class ProgressBarTransaction {

    private final UUID transactionUniqueId;
    private final UUID ownerUniqueId;
    private final String progressBarName;
    private final Instant startTime;
    private final Duration duration;

    public ProgressBarTransaction(UUID ownerUniqueId, String progressBarName, Duration duration) {
        this.transactionUniqueId = UUID.randomUUID();
        this.ownerUniqueId = ownerUniqueId;
        this.progressBarName = progressBarName;
        this.startTime = Instant.now();
        this.duration = duration;
    }

    public ProgressBarTransaction(UUID transactionUniqueId, UUID ownerUniqueId, String progressBarName, Instant instant, Duration duration) {
        this.transactionUniqueId = transactionUniqueId;
        this.ownerUniqueId = ownerUniqueId;
        this.progressBarName = progressBarName;
        this.startTime = instant;
        this.duration = duration;
    }

    public UUID getTransactionUniqueId() {
        return this.transactionUniqueId;
    }

    public UUID getOwnerUniqueId() {
        return this.ownerUniqueId;
    }

    public String getProgressBarName() {
        return this.progressBarName;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

}