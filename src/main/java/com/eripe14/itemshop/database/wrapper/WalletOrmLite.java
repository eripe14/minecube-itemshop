package com.eripe14.itemshop.database.wrapper;

import com.eripe14.itemshop.database.DatabaseManager;
import com.eripe14.itemshop.scheduler.Scheduler;
import com.eripe14.itemshop.wallet.Wallet;
import com.eripe14.itemshop.wallet.WalletRepository;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import panda.std.Option;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WalletOrmLite extends AbstractRepositoryOrmLite implements WalletRepository {

    public WalletOrmLite(DatabaseManager databaseManager, Scheduler scheduler) {
        super(databaseManager, scheduler);
    }

    @Override
    public CompletableFuture<Wallet> findWallet(UUID owner) {
        return CompletableFuture.supplyAsync(() -> {
            Option<Double> money = this.awaitAction(WalletWrapper.class, dao -> Option.supplyThrowing(Throwable.class, () -> dao.queryBuilder()
                    .where()
                    .eq("owner", owner)
                    .queryForFirst()
            )).map(WalletWrapper::toWallet).map(Wallet::getMoney);

            if (money.isPresent()) {
                return new Wallet(owner, money.get());
            }

            return new Wallet(owner, 0);
        });
    }

    @Override
    public CompletableFuture<Boolean> update(UUID owner, double money) {
        return CompletableFuture.supplyAsync(() -> {
            WalletWrapper walletWrapper = new WalletWrapper(owner, money);
            this.awaitSave(WalletWrapper.class, walletWrapper);

            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> addMoney(UUID owner, double money) {
        return CompletableFuture.supplyAsync(() -> {
            this.findWallet(owner).whenComplete((wallet, error) -> {
                if (error != null) {
                    error.printStackTrace();
                    return;
                }

                this.update(owner, wallet.getMoney() + money);
            });

            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeMoney(UUID owner, double money) {
        return CompletableFuture.supplyAsync(() -> {
            Option<Double> moneyOption = this.awaitAction(WalletWrapper.class, dao -> Option.supplyThrowing(Throwable.class, () -> dao.queryBuilder()
                    .where()
                    .eq("owner", owner)
                    .queryForFirst()
            )).map(WalletWrapper::toWallet).map(Wallet::getMoney);

            if (moneyOption.isPresent()) {
                double currentMoney = moneyOption.get();

                if (currentMoney < money) {
                    return false;
                }

                this.update(owner, currentMoney - money);
                return true;
            }

            return true;
        });
    }

    @DatabaseTable(tableName = "wallets")
    private static class WalletWrapper {
        @DatabaseField(columnName = "owner", id = true)
        private UUID owner;

        @DatabaseField(columnName = "money")
        private double money;

        public WalletWrapper() {
        }

        public WalletWrapper(UUID owner, double money) {
            this.owner = owner;
            this.money = money;
        }

        public static WalletWrapper fromWallet(Wallet wallet) {
            return new WalletWrapper(wallet.getOwner(), wallet.getMoney());
        }

        public Wallet toWallet() {
            return new Wallet(this.owner, this.money);
        }
    }

    public static WalletRepository create(DatabaseManager databaseManager, Scheduler scheduler) {
        try {
            TableUtils.createTableIfNotExists(databaseManager.connectionSource(), WalletWrapper.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new WalletOrmLite(databaseManager, scheduler);
    }

}