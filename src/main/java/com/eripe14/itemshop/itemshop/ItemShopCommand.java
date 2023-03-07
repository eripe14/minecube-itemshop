package com.eripe14.itemshop.itemshop;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;

@Route(name = "itemshop", aliases = {"is", "shop", "sklep"})
@Permission("itemshop.command")
public class ItemShopCommand {

    private final ItemShopInventory itemShopInventory;

    public ItemShopCommand(ItemShopInventory itemShopInventory) {
        this.itemShopInventory = itemShopInventory;
    }

    @Execute
    void execute(Player player) {
        this.itemShopInventory.openInventory(player);
    }

}