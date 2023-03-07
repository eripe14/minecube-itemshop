package com.eripe14.itemshop.user;

import com.eripe14.itemshop.progressbar.transaction.ProgressBarTransaction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {

    private final UUID uuid;
    private final Map<String, ProgressBarTransaction> transactions;

    public User(UUID uuid, List<ProgressBarTransaction> transactions) {
        this.uuid = uuid;
        this.transactions = transactions
                .stream()
                .collect(Collectors.toMap(ProgressBarTransaction::getProgressBarName, progressBarTransaction -> progressBarTransaction));
    }

    public boolean hasProgressBar(String progressBarName) {
        return this.transactions.containsKey(progressBarName);
    }

    public void addTransaction(ProgressBarTransaction progressBarTransaction) {
        this.transactions.put(progressBarTransaction.getProgressBarName(), progressBarTransaction);
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public Collection<ProgressBarTransaction> getTransactions() {
        return Collections.unmodifiableCollection(this.transactions.values());
    }

}