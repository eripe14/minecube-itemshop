package com.eripe14.itemshop.itemshop;

import com.eripe14.itemshop.itemshop.booster.BoosterInventory;
import com.eripe14.itemshop.itemshop.moneypackage.MoneyPackagesInventory;
import com.eripe14.itemshop.util.Legacy;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ItemShopInventory {

    private final MiniMessage miniMessage;
    private final BoosterInventory boosterInventory;
    private final MoneyPackagesInventory moneyPackagesInventory;

    public ItemShopInventory(MiniMessage miniMessage, BoosterInventory boosterInventory, MoneyPackagesInventory moneyPackagesInventory) {
        this.miniMessage = miniMessage;
        this.boosterInventory = boosterInventory;
        this.moneyPackagesInventory = moneyPackagesInventory;
    }

    public void openInventory(Player player) {
        Gui gui = Gui.gui()
                .title(Legacy.RESET_ITALIC.append(this.miniMessage.deserialize("ItemShop")))
                .rows(6)
                .disableAllInteractions()
                .create();

        for (int i = 9; i < 18; i++) {
            gui.setItem(i, ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                    .name(this.miniMessage.deserialize(""))
                    .asGuiItem(event -> event.setCancelled(true)));
        }

        GuiItem head = ItemBuilder.skull()
                .name(this.color("&aItemShop"))
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQxZDhjNTA0ODY3OTMzNzAzZGEzNmVjMTFmNTM4ZjYyNjVmOTg0NDFkODgxZWFjNDhlZjJjNDkzNGMxYiJ9fX0=")
                .lore(this.color(
                        "&7ItemShop serwery, możesz tutaj zakupić rangi, dodatki...",
                        "&7...paczki z monetami i wiele więcej.",
                        "",
                        "&eKliknij, aby przeglądać!"
                ))
                .asGuiItem(event -> {
                });

        GuiItem glass = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(this.color(""))
                .lore(this.color(""))
                .asGuiItem(event -> {
                });

        //Item do kategorii paczki z monetami.
        GuiItem money = ItemBuilder.from(Material.YELLOW_SHULKER_BOX)
                .name(this.color("&aPaczki z monetami &8(1k, 5k...)"))
                .lore(this.color(
                        "&7Brakuje ci monet? Zakup paczkę...",
                        "&7...monet.",
                        "&7",
                        "&fCeny paczek zaczynają się od &62 zł&f!",
                        "",
                        "&eKliknij, aby przejść!"
                ))
                .asGuiItem(event -> this.moneyPackagesInventory.openInventory(player));

        //Item do kategorii rangi premium.
        GuiItem rank = ItemBuilder.from(Material.TOTEM_OF_UNDYING)
                .name(this.color("&aRangi premium &8(Vip...)"))
                .lore(this.color(
                        "&7Zakup range premium, aby odblokować...",
                        "&7...dostęp do przywilejów.",
                        "&7",
                        "&fCeny rang zaczynają się od &62 zł&f!",
                        "",
                        "&eKliknij, aby przejść!"
                ))
                .asGuiItem(event -> {
                    //Player player = (Player) event.getWhoClicked();
                    //                    rankCategory.openInventory(player); //metoda do przechodzenia.
                });

        //Item do kategorii magiczne skrzynie.
        GuiItem chest = ItemBuilder.from(Material.CHEST)
                .name(this.color("&aMagiczne skrzynki"))
                .lore(this.color(
                        "&7Zakup magiczne skrzynki i wylosuj...",
                        "&7...niesamowite dodatki do konta.",
                        "&7",
                        "&fCeny magicznych skrzynek zaczynają się od &66 zł&f!",
                        "",
                        "&eKliknij, aby przejść!"
                ))
                .asGuiItem(event -> {
                    ///Player player = (Player) event.getWhoClicked();
                    //                    magicCaseCategory.openInventory(player);
                });

        //Item do kategorii rangi wizualne.
        GuiItem visual = ItemBuilder.from(Material.WRITABLE_BOOK)
                .name(this.color("&aRangi wizulane"))
                .lore(this.color(
                        "&7Odblokuj dostęp do unikalnych rang...",
                        "&7...wizualnych.",
                        "&7",
                        "&fCeny rang wizualnych zaczynają się od &61 zł&f!",
                        "",
                        "&eKliknij, aby przejść!"
                ))
                .asGuiItem(event -> {
                });

        GuiItem booster = ItemBuilder.from(Material.ANVIL)
                .name(this.color("&aBoostery"))
                .lore(this.color(
                        "&7Zakup boostery, aby odblokować...",
                        "&7...dostęp do przywilejów.",
                        "&7",
                        "&fCeny boostera zaczynają się od &61 zł&f!",
                        "",
                        "&eKliknij, aby przejść!"
                ))
                .asGuiItem(event -> {
                    this.boosterInventory.openInventory(player);
                });

        GuiItem quit = ItemBuilder.from(Material.BARRIER)
                .name(this.color("&aWyjdz"))
                .lore(this.color("&eKliknij, aby wyjść!"))
                .asGuiItem(event -> {
                    player.closeInventory();
                });

        gui.setItem(2, head);
        gui.setItem(11, glass);
        gui.setItem(29, money);
        gui.setItem(31, rank);
        gui.setItem(33, chest);
        gui.setItem(38, visual);
        gui.setItem(40, booster);
        gui.setItem(53, quit);

        gui.open(player);
    }

    private Component color(String text) {
        return Legacy.RESET_ITALIC.append(this.miniMessage.deserialize(text));
    }

    private List<Component> color(String... lore) {
        return Arrays.stream(lore)
                .map(this::color)
                .toList();
    }


}