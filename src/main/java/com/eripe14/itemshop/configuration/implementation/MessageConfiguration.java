package com.eripe14.itemshop.configuration.implementation;

import com.eripe14.itemshop.configuration.ReloadableConfig;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;

import java.io.File;

public class MessageConfiguration implements ReloadableConfig {

    @Description({ " ", "# Wrong command usage" })
    public WrongUsage wrongUsage = new WrongUsage();

    @Description({ " ", "# Wallet section" })
    public WalletSection walletSection = new WalletSection();

    @Description({ " ", "# Booster section" })
    public BoosterSection boosterSection = new BoosterSection();

    @Description({ " ", "# Money package section" })
    public MoneyPackageSection moneyPackageSection = new MoneyPackageSection();

    @Contextual
    public static class WrongUsage {
        public String invalidUsage = "&4Wrong command usage &8>> &7{COMMAND}.";

        public String invalidUsageHeader = "&cWrong command usage!";

        public String invalidUsageEntry = "&8 >> &7";

        public String noPermission = "&4You don't have permission to perform this command.";

        public String cantFindPlayer = "&4Can not find that player!";

        public String onlyForPlayer = "&4Command only for players!";
    }

    @Contextual
    public static class WalletSection {
        public String updateWalletError = "&4Error while updating wallet!";

        public String wallet = "&aYour wallet: &7{MONEY}zł";

        public String setWallet = "&7You have set &a{TARGET} &7wallet to &a{AMOUNT}&7!";
        public String setWalletTarget = "&7Your wallet has been set to &a{AMOUNT}&7!";

        public String addWallet = "&7You have added &a{AMOUNT} &7to &a{TARGET}&7!";
        public String addWalletTarget = "&a{AMOUNT} &7added to your wallet&7!";

        public String removeWallet = "&7You have removed &a{AMOUNT} &7from &a{TARGET}&7!";
        public String removeWalletTarget = "&a{AMOUNT} &7removed from your wallet&7!";

        public String getWallet = "&7{TARGET} &7wallet is &a{AMOUNT}&7!";
    }

    @Contextual
    public static class BoosterSection {
        public String notEnoughMoney = "&4You don't have enough money to buy this booster!";

        public String boosterBought = "&7You have bought &a{BOOSTER} &7booster for &a{PRICE}zł&7!";

        public String boosterDelay = "&4You can't buy this booster yet! You must wait &e{TIME}&4!";
    }

    @Contextual
    public static class MoneyPackageSection {
        public String notEnoughMoney = "&4You don't have enough money to buy this package!";

        public String packageBought = "&7You have bought &a{PACKAGE} &7package for &a{PRICE}zł&7! You have received &a{AMOUNT}&7!";
    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

}