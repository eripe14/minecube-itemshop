package com.eripe14.itemshop.database.wrapper;

import com.eripe14.itemshop.database.DatabaseManager;
import com.eripe14.itemshop.progressbar.transaction.ProgressBarTransaction;
import com.eripe14.itemshop.scheduler.Scheduler;
import com.eripe14.itemshop.user.User;
import com.eripe14.itemshop.user.UserRepository;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import panda.std.Option;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UserOrmLite extends AbstractRepositoryOrmLite implements UserRepository {

    public UserOrmLite(DatabaseManager databaseManager, Scheduler scheduler) {
        super(databaseManager, scheduler);
    }

    @Override
    public CompletableFuture<Void> update(User owner) {
        return CompletableFuture.supplyAsync(() -> {
            for (ProgressBarTransaction transaction : owner.getTransactions()) {
                TransactionWrapper transactionWrapper = new TransactionWrapper(
                        transaction.getTransactionUniqueId(),
                        transaction.getOwnerUniqueId(),
                        transaction.getProgressBarName(),
                        transaction.getStartTime().toString(),
                        transaction.getDuration().toString()
                );

                this.awaitSave(TransactionWrapper.class, transactionWrapper);
            }

            return null;
        });
    }

    @Override
    public CompletableFuture<User> findUser(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ProgressBarTransaction> ownerTransactions = this.awaitAction(TransactionWrapper.class, dao -> Option.supplyThrowing(Throwable.class, () -> dao.queryBuilder()
                            .where()
                            .eq("owner_uuid", uniqueId)
                            .query()
                    ))
                    .map(wrappers -> wrappers.stream().map(TransactionWrapper::toProgressBarTransaction).toList())
                    .orElseGet(new ArrayList<>());

            return new User(uniqueId, ownerTransactions);
        });
    }

    @Override
    public CompletableFuture<Set<User>> getAllUsers() {
        return CompletableFuture.supplyAsync(() -> {
            Set<User> users = this.awaitAction(TransactionWrapper.class, dao -> Option.supplyThrowing(Throwable.class, () -> dao.queryBuilder()
                            .where()
                            .isNotNull("owner_uuid")
                            .query()
                    ))
                    .map(wrappers -> wrappers.stream().map(TransactionWrapper::toProgressBarTransaction).collect(Collectors.groupingBy(ProgressBarTransaction::getOwnerUniqueId)))
                    .map(map -> map.entrySet().stream().map(entry -> new User(entry.getKey(), entry.getValue())).collect(Collectors.toSet()))
                    .orElseGet(Set::of);

            return Set.copyOf(users);
        });
    }

    @DatabaseTable(tableName = "progress_bars")
    private static class TransactionWrapper {
        @DatabaseField(columnName = "uuid", id = true)
        private UUID boughtRankId;

        @DatabaseField(columnName = "owner_uuid")
        private UUID ownerUuid;

        @DatabaseField(columnName = "progress_bar_name")
        private String rank;

        @DatabaseField(columnName = "start_time")
        private String startTime;

        @DatabaseField(columnName = "duration")
        private String duration;

        public TransactionWrapper() {
        }

        public TransactionWrapper(UUID boughtRankId, UUID ownerUuid, String rank, String startTime, String duration) {
            this.boughtRankId = boughtRankId;
            this.ownerUuid = ownerUuid;
            this.rank = rank;
            this.startTime = startTime;
            this.duration = duration;
        }

        public ProgressBarTransaction toProgressBarTransaction() {
            return new ProgressBarTransaction(this.boughtRankId, this.ownerUuid, this.rank, Instant.parse(this.startTime), Duration.parse(this.duration));
        }
    }

    public static UserRepository create(DatabaseManager databaseManager, Scheduler scheduler) {
        try {
            TableUtils.createTableIfNotExists(databaseManager.connectionSource(), TransactionWrapper.class);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return new UserOrmLite(databaseManager, scheduler);
    }

}