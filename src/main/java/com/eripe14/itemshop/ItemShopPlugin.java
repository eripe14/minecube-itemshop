package com.eripe14.itemshop;

import com.eripe14.itemshop.command.InvalidUsage;
import com.eripe14.itemshop.command.PermissionMessage;
import com.eripe14.itemshop.configuration.ConfigurationManager;
import com.eripe14.itemshop.configuration.implementation.MessageConfiguration;
import com.eripe14.itemshop.configuration.implementation.PluginConfiguration;
import com.eripe14.itemshop.configuration.implementation.booster.BoostersConfiguration;
import com.eripe14.itemshop.configuration.implementation.moneypackage.MoneyPackagesConfiguration;
import com.eripe14.itemshop.configuration.implementation.progressbar.ProgressBarsConfiguration;
import com.eripe14.itemshop.database.DatabaseManager;
import com.eripe14.itemshop.database.wrapper.UserOrmLite;
import com.eripe14.itemshop.database.wrapper.WalletOrmLite;
import com.eripe14.itemshop.hook.HookManager;
import com.eripe14.itemshop.hook.implementation.LuckPermsHook;
import com.eripe14.itemshop.hook.implementation.PlaceholderApiController;
import com.eripe14.itemshop.hook.implementation.PlaceholderApiHook;
import com.eripe14.itemshop.hook.implementation.VaultHook;
import com.eripe14.itemshop.itemshop.ItemShopCommand;
import com.eripe14.itemshop.itemshop.ItemShopInventory;
import com.eripe14.itemshop.itemshop.booster.BoosterHandlerImpl;
import com.eripe14.itemshop.itemshop.booster.BoosterInventory;
import com.eripe14.itemshop.itemshop.booster.BoosterManager;
import com.eripe14.itemshop.itemshop.moneypackage.MoneyPackageHandlerImpl;
import com.eripe14.itemshop.itemshop.moneypackage.MoneyPackageManager;
import com.eripe14.itemshop.itemshop.moneypackage.MoneyPackagesInventory;
import com.eripe14.itemshop.notification.NotificationAnnouncer;
import com.eripe14.itemshop.progressbar.ProgressBarManager;
import com.eripe14.itemshop.progressbar.transaction.ProgressBarTransactionService;
import com.eripe14.itemshop.purchase.PurchaseService;
import com.eripe14.itemshop.scheduler.BukkitSchedulerImpl;
import com.eripe14.itemshop.scheduler.Scheduler;
import com.eripe14.itemshop.user.UserRepository;
import com.eripe14.itemshop.util.LegacyColorProcessor;
import com.eripe14.itemshop.wallet.WalletCommand;
import com.eripe14.itemshop.wallet.WalletRepository;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import java.util.stream.Stream;

public class ItemShopPlugin extends JavaPlugin {

    private Scheduler scheduler;

    private ConfigurationManager configurationManager;
    private DatabaseManager databaseManager;

    private PluginConfiguration pluginConfiguration;
    private MessageConfiguration messageConfiguration;
    private BoostersConfiguration boostersConfiguration;
    private MoneyPackagesConfiguration moneyPackagesConfiguration;
    private ProgressBarsConfiguration progressBarsConfiguration;

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;
    private NotificationAnnouncer notificationAnnouncer;

    private UserRepository userRepository;
    private WalletRepository walletRepository;

    private HookManager hookManager;
    private Economy economy;
    private LuckPerms luckPerms;

    private PurchaseService purchaseService;

    private ProgressBarManager progressBarManager;
    private ProgressBarTransactionService progressBarTransactionService;

    private BoosterManager boosterManager;
    private BoosterHandlerImpl boosterHandler;

    private MoneyPackageManager moneyPackageManager;
    private MoneyPackageHandlerImpl moneyPackageHandler;

    private EventCaller eventCaller;

    private BoosterInventory boosterInventory;
    private MoneyPackagesInventory moneyPackagesInventory;
    private ItemShopInventory itemShopInventory;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        Stopwatch started = Stopwatch.createStarted();
        Server server = this.getServer();
        Logger logger = this.getLogger();

        this.scheduler = new BukkitSchedulerImpl(this);

        this.configurationManager = new ConfigurationManager(this.getDataFolder());
        this.pluginConfiguration = this.configurationManager.load(new PluginConfiguration());
        this.messageConfiguration = this.configurationManager.load(new MessageConfiguration());
        this.boostersConfiguration = this.configurationManager.load(new BoostersConfiguration());
        this.moneyPackagesConfiguration = this.configurationManager.load(new MoneyPackagesConfiguration());
        this.progressBarsConfiguration = this.configurationManager.load(new ProgressBarsConfiguration());

        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();
        this.notificationAnnouncer = new NotificationAnnouncer(this.audienceProvider, this.miniMessage);

        try {
            this.databaseManager = new DatabaseManager(this.pluginConfiguration, logger, this.getDataFolder());
            this.databaseManager.connect();

            this.walletRepository = WalletOrmLite.create(this.databaseManager, this.scheduler);
            this.userRepository = UserOrmLite.create(this.databaseManager, this.scheduler);
        } catch (Exception exception) {
            exception.printStackTrace();

            logger.severe("Can't connect to database! Disabling plugin...");

            server.getPluginManager().disablePlugin(this);
        }

        this.hookManager = new HookManager(server, logger);

        PlaceholderApiHook placeholderApiHook = new PlaceholderApiHook(this.walletRepository);
        this.hookManager.initialize(placeholderApiHook);

        LuckPermsHook luckPermsHook = new LuckPermsHook(server);
        this.hookManager.initialize(luckPermsHook);
        this.luckPerms = luckPermsHook.getLuckPerms();

        VaultHook vaultHook = new VaultHook(server);
        this.hookManager.initialize(vaultHook);
        this.economy = vaultHook.getEconomy();

        this.purchaseService = new PurchaseService(this.economy);

        this.progressBarManager = new ProgressBarManager(this.progressBarsConfiguration);
        this.progressBarTransactionService = new ProgressBarTransactionService(this, this.userRepository, this.progressBarManager, this.miniMessage, this.audienceProvider);

        this.boosterManager = new BoosterManager(this.boostersConfiguration);
        this.boosterHandler = new BoosterHandlerImpl(
                this.luckPerms,
                this.walletRepository,
                this.userRepository,
                this.progressBarTransactionService,
                this,
                server
        );

        this.moneyPackageManager = new MoneyPackageManager(this.moneyPackagesConfiguration);
        this.moneyPackageHandler = new MoneyPackageHandlerImpl(this.purchaseService, this.walletRepository, this.audienceProvider, this.miniMessage);

        this.eventCaller = new EventCaller(server);

        this.boosterInventory = new BoosterInventory(
                this.scheduler,
                this.boosterManager,
                this.boosterHandler,
                this.miniMessage,
                this.messageConfiguration,
                this.notificationAnnouncer
        );

        this.moneyPackagesInventory = new MoneyPackagesInventory(
            this.scheduler,
            this.moneyPackageManager,
            this.moneyPackageHandler,
            this.miniMessage,
            this.messageConfiguration,
            this.notificationAnnouncer
        );

        this.itemShopInventory = new ItemShopInventory(
                this.miniMessage,
                this.boosterInventory,
                this.moneyPackagesInventory);

        this.liteCommands = LiteBukkitFactory.builder(server, "itemshop")
                .argument(Player.class, new BukkitPlayerArgument<>(this.getServer(), this.messageConfiguration.wrongUsage.onlyForPlayer))

                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(this.messageConfiguration.wrongUsage.onlyForPlayer))

                .commandInstance(new WalletCommand(this.messageConfiguration, this.notificationAnnouncer, this.walletRepository, this.eventCaller))
                .commandInstance(new ItemShopCommand(this.itemShopInventory))

                .invalidUsageHandler(new InvalidUsage(this.notificationAnnouncer, this.messageConfiguration))
                .permissionHandler(new PermissionMessage(this.messageConfiguration, this.notificationAnnouncer))

                .register();

        Stream.of(
                new PlaceholderApiController(placeholderApiHook.getWalletCache())
        ).forEach(plugin -> this.getServer().getPluginManager().registerEvents(plugin, this));

        //this.scheduler.timerSync(this.progressBarTask, Duration.ZERO, Duration.ofSeconds(1));

        long elapsed = started.elapsed().toMillis();
        this.getLogger().info("Successfully loaded ItemShop in " + elapsed + "ms");
    }

    @Override
    public void onDisable() {
        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }

        if (this.liteCommands != null) {
            this.liteCommands.getPlatform().unregisterAll();
        }
    }

}