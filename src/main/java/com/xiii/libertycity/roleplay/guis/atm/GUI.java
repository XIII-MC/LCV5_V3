package com.xiii.libertycity.roleplay.guis.atm;

import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.enums.MsgType;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.core.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI {

    private final ItemStack glass_empty = InventoryUtils.createItem(Material.STAINED_GLASS_PANE, 1, " ", " ");
    private final ItemStack close_item = InventoryUtils.createItem(Material.BARRIER, 1, "§cRetour");
    private final ItemStack b1 = new ItemStack(Material.IRON_NUGGET),
            b2 = new ItemStack(Material.GOLD_NUGGET),
            b5 = new ItemStack(Material.BRICK),
            b10 = new ItemStack(Material.COAL),
            b20 = new ItemStack(Material.IRON_INGOT),
            b50 = new ItemStack(Material.GOLD_INGOT),
            b100 = new ItemStack(Material.REDSTONE),
            b200 = new ItemStack(Material.LAPIS_BLOCK),
            b500 = new ItemStack(Material.DIAMOND),
            b1000 = new ItemStack(Material.EMERALD),
            b2000 = new ItemStack(Material.NETHER_STAR),
            b5000 = new ItemStack(Material.EMERALD_BLOCK);
    //TODO: Change Materials to real modded bill items IDs

    public int depositedMoney = 0, withdrewMoney = 0;

    public void openATM(final Player player, final boolean refresh) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final long bankBalance = profile.rpBankBalance;

        Inventory inv;

        if (!refresh) inv = Bukkit.createInventory(player, 36, MsgType.BANK.getMessage() + "§2§lATM");
        else inv = player.getOpenInventory().getTopInventory();

        reset(inv);

        inv.setItem(10, InventoryUtils.createItem(b1.getType(), 1, "§aDéposer des espèces", "§7Cet argent sera sera ajouté à votre compte banquaire."));
        inv.setItem(13, InventoryUtils.createItem(Material.PAPER, 1, MsgType.BANK_PREFIX.getMessage(), "§7Informations sur votre compte banquaire", "", "§7Type de compte » §f" + profile.rpBankType, "§7Votre compte » §6$" + bankBalance + "§7/§6" + profile.rpBankMaxBalance, "§7Découvert autorisé » §c$" + profile.rpBankMinBalance, "§7Dépôt restant » §6$" + (profile.rpBankMaxDep - profile.rpBankCurrDep) + "§7/§6" + profile.rpBankMaxDep, "§7Retrait restant » §6$" + (profile.rpBankMaxWit - profile.rpBankCurrWit) + "§7/§6" + profile.rpBankMaxWit));
        inv.setItem(16, InventoryUtils.createItem(b5000.getType(), 1, "§cRetirer des espèces", "§7Cet argent sera retiré de votre compte banquaire."));

        if (!refresh) {
            player.closeInventory();
            player.openInventory(inv);
        } else player.updateInventory();
    }

    public void openWit(final Player player, final boolean refresh) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final long bankBalance = profile.rpBankBalance;

        Inventory inv;

        if (!refresh) inv = Bukkit.createInventory(player, 36, MsgType.BANK.getMessage() + "§2§lATM §7» §c§LRetrait");
        else inv = player.getOpenInventory().getTopInventory();

        reset(inv);

        final int roundb1 = convertMax(round(bankBalance / 1)),
                roundb2 = convertMax(round(bankBalance / 2)),
                roundb5 = convertMax(round(bankBalance / 5)),
                roundb10 = convertMax(round(bankBalance / 10)),
                roundb20 = convertMax(round(bankBalance / 20)),
                roundb50 = convertMax(round(bankBalance / 50)),
                roundb100 = convertMax(round(bankBalance / 100)),
                roundb200 = convertMax(round(bankBalance / 200)),
                roundb500 = convertMax(round(bankBalance / 500)),
                roundb1000 = convertMax(round(bankBalance / 1000)),
                roundb2000 = convertMax(round(bankBalance / 2000)),
                roundb5000 = convertMax(round(bankBalance / 5000));

        if (roundb1 < 8) inv.setItem(10, InventoryUtils.createItem(b1.getType(), roundb1, "§cRetirer $1", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 1), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (1))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$1"));
        if (roundb2 < 8) inv.setItem(11, InventoryUtils.createItem(b2.getType(), roundb2, "§cRetirer $2", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 2), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (2))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$2"));
        if (roundb5 < 8) inv.setItem(12, InventoryUtils.createItem(b5.getType(), roundb5, "§cRetirer $5", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 5), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (5))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$5"));
        if (roundb10 < 8) inv.setItem(13, InventoryUtils.createItem(b10.getType(), roundb10, "§cRetirer $10", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 10), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (10))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$10"));
        if (roundb20 < 8) inv.setItem(14, InventoryUtils.createItem(b20.getType(), roundb20, "§cRetirer $20", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 20), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (20))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$20"));
        if (roundb50 < 8) inv.setItem(15, InventoryUtils.createItem(b50.getType(), roundb50, "§cRetirer $50", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 50), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (50))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$50"));
        if (roundb100 < 8) inv.setItem(16, InventoryUtils.createItem(b100.getType(), roundb100, "§cRetirer $100", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 100), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (100))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$100"));
        if (roundb200 < 8) inv.setItem(20, InventoryUtils.createItem(b200.getType(), roundb200, "§cRetirer $200", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 200), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (200))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$200"));
        if (roundb500 < 8) inv.setItem(21, InventoryUtils.createItem(b500.getType(), roundb500, "§cRetirer $500", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 500), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (500))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$500"));
        if (roundb1000 < 8) inv.setItem(22, InventoryUtils.createItem(b1000.getType(), roundb1000, "§cRetirer $1000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 1000), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (1000))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$1000"));
        if (roundb2000 < 8) inv.setItem(23, InventoryUtils.createItem(b2000.getType(), roundb2000, "§cRetirer $2000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 2000), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (2000))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$2000"));
        if (roundb5000 < 8) inv.setItem(24, InventoryUtils.createItem(b5000.getType(), roundb5000, "§cRetirer $5000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 5000), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (5000))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$5000"));

        if (roundb1 >= 8) inv.setItem(10, InventoryUtils.createItem(b1.getType(), roundb1, "§cRetirer $1", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 1), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (1))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$1", "", "§7Si vous souhaitez retirer 8 billets de $1:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 1)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (1 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 1)));
        if (roundb2 >= 8) inv.setItem(11, InventoryUtils.createItem(b2.getType(), roundb2, "§cRetirer $2", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 2), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (2))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$2", "", "§7Si vous souhaitez retirer 8 billets de $2:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 2)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (2 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 2)));
        if (roundb5 >= 8) inv.setItem(12, InventoryUtils.createItem(b5.getType(), roundb5, "§cRetirer $5", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 5), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (5))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$5", "", "§7Si vous souhaitez retirer 8 billets de $5:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 5)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (5 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 5)));
        if (roundb10 >= 8) inv.setItem(13, InventoryUtils.createItem(b10.getType(), roundb10, "§cRetirer $10", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 10), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (10))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$10", "", "§7Si vous souhaitez retirer 8 billets de $10:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 10)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (10 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 10)));
        if (roundb20 >= 8) inv.setItem(14, InventoryUtils.createItem(b20.getType(), roundb20, "§cRetirer $20", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 20), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (20))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$20", "", "§7Si vous souhaitez retirer 8 billets de $20:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 20)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (20 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 20)));
        if (roundb50 >= 8) inv.setItem(15, InventoryUtils.createItem(b50.getType(), roundb50, "§cRetirer $50", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 50), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (50))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$50", "", "§7Si vous souhaitez retirer 8 billets de $50:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 50)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (50 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 50)));
        if (roundb100 >= 8) inv.setItem(16, InventoryUtils.createItem(b100.getType(), roundb100, "§cRetirer $100", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 100), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (100))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$100", "", "§7Si vous souhaitez retirer 8 billets de $100:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 100)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (100 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 100)));
        if (roundb200 >= 8) inv.setItem(20, InventoryUtils.createItem(b200.getType(), roundb200, "§cRetirer $200", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 200), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (200))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$200", "", "§7Si vous souhaitez retirer 8 billets de $200:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 200)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (200 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 200)));
        if (roundb500 >= 8) inv.setItem(21, InventoryUtils.createItem(b500.getType(), roundb500, "§cRetirer $500", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 500), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (500))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$500", "", "§7Si vous souhaitez retirer 8 billets de $500:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 500)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (500 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 500)));
        if (roundb1000 >= 8) inv.setItem(22, InventoryUtils.createItem(b1000.getType(), roundb1000, "§cRetirer $1000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 1000), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (1000))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$1000", "", "§7Si vous souhaitez retirer 8 billets de $1000:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 1000)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (1000 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 1000)));
        if (roundb2000 >= 8) inv.setItem(23, InventoryUtils.createItem(b2000.getType(), roundb2000, "§cRetirer $2000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 2000), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (2000))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$2000", "", "§7Si vous souhaitez retirer 8 billets de $2000:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 2000)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (2000 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 2000)));
        if (roundb5000 >= 8) inv.setItem(24, InventoryUtils.createItem(b5000.getType(), roundb5000, "§cRetirer $5000", "§7Cet argent sera retiré de votre compte banquaire.", "", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - 5000), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (5000))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$5000", "", "§7Si vous souhaitez retirer 8 billets de $5000:", " ", "§7Avant retrait » §6$" + bankBalance, "§7Après retrait » §6$" + (bankBalance - (8 * 5000)), "§7Limite de retrait » §6$" + (profile.rpBankMaxWit - (profile.rpBankCurrWit + (5000 * 8))) + "§7/§6" + profile.rpBankMaxWit, "§7Transaction » §4-$" + (8 * 5000)));

        if (!refresh) {
            player.closeInventory();
            player.openInventory(inv);
        }
    }

    public void openDep(final Player player, final boolean refresh) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final long bankBalance = profile.rpBankBalance;

        Inventory inv;

        if (!refresh) inv = Bukkit.createInventory(player, 36, MsgType.BANK.getMessage() + "§2§lATM §7» §a§LDepôt");
        else inv = player.getOpenInventory().getTopInventory();

        reset(inv);

        final int amountb1 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b1, true, true)),
                amountb2 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b2, true, true)),
                amountb5 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b5, true, true)),
                amountb10 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b10, true, true)),
                amountb20 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b20, true, true)),
                amountb50 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b50, true, true)),
                amountb100 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b100, true, true)),
                amountb200 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b200, true, true)),
                amountb500 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b500, true, true)),
                amountb1000 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b1000, true, true)),
                amountb2000 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b2000, true, true)),
                amountb5000 = (InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), b5000, true, true));

        if (amountb1 > 1) inv.setItem(10, InventoryUtils.createItem(b1.getType(), convertMax(amountb1), "§aDéposer $1", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 1), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (1))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$1", "", "§7Si vous souhaitez déposer vos " + (amountb1) + " billets de $1:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb1 * 1)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb1 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb1 * 1)));
        if (amountb2 > 1) inv.setItem(11, InventoryUtils.createItem(b2.getType(), convertMax(amountb2), "§aDéposer $2", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 2), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (2))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$2", "", "§7Si vous souhaitez déposer vos " + (amountb2) + " billets de $2:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb2 * 2)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb2 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb2 * 2)));
        if (amountb5 > 1) inv.setItem(12, InventoryUtils.createItem(b5.getType(), convertMax(amountb5), "§aDéposer $5", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 5), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (5))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$5", "", "§7Si vous souhaitez déposer vos " + (amountb5) + " billets de $5:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb5 * 5)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb5 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb5 * 5)));
        if (amountb10 > 1) inv.setItem(13, InventoryUtils.createItem(b10.getType(), convertMax(amountb10), "§aDéposer $10", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 10), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (10))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$10", "", "§7Si vous souhaitez déposer vos " + (amountb10) + " billets de $10:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb10 * 10)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb10 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb10 * 10)));
        if (amountb20 > 1) inv.setItem(14, InventoryUtils.createItem(b20.getType(), convertMax(amountb20), "§aDéposer $20", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 20), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (20))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$20", "", "§7Si vous souhaitez déposer vos " + (amountb20) + " billets de $20:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb20 * 20)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb20 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb20 * 20)));
        if (amountb50 > 1) inv.setItem(15, InventoryUtils.createItem(b50.getType(), convertMax(amountb50), "§aDéposer $50", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 50), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (50))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$50", "", "§7Si vous souhaitez déposer vos " + (amountb50) + " billets de $50:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb50 * 50)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb50 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb50 * 50)));
        if (amountb100 > 1) inv.setItem(16, InventoryUtils.createItem(b100.getType(), convertMax(amountb100), "§aDéposer $100", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 100), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (100))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$100", "", "§7Si vous souhaitez déposer vos " + (amountb100) + " billets de $100:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb100 * 100)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb100 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb100 * 100)));
        if (amountb200 > 1) inv.setItem(20, InventoryUtils.createItem(b200.getType(), convertMax(amountb200), "§aDéposer $200", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 200), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (200))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$200", "", "§7Si vous souhaitez déposer vos " + (amountb200) + " billets de $200:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb200 * 200)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb200 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb200 * 200)));
        if (amountb500 > 1) inv.setItem(21, InventoryUtils.createItem(b500.getType(), convertMax(amountb500), "§aDéposer $500", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 500), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (500))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$500", "", "§7Si vous souhaitez déposer vos " + (amountb500) + " billets de $500:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb500 * 500)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb500 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb500 * 500)));
        if (amountb1000 > 1) inv.setItem(22, InventoryUtils.createItem(b1000.getType(), convertMax(amountb1000), "§aDéposer $1000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 1000), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (1000))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$1000", "", "§7Si vous souhaitez déposer vos " + (amountb1000) + " billets de $1000:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb1000 * 1000)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb1000 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb1000 * 1000)));
        if (amountb2000 > 1) inv.setItem(23, InventoryUtils.createItem(b2000.getType(), convertMax(amountb2000), "§aDéposer $2000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 2000), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (2000))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$2000", "", "§7Si vous souhaitez déposer vos " + (amountb2000) + " billets de $2000:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb2000 * 2000)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb2000 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb2000 * 2000)));
        if (amountb5000 > 1) inv.setItem(24, InventoryUtils.createItem(b5000.getType(), convertMax(amountb5000), "§aDéposer $5000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 5000), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (5000))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$5000", "", "§7Si vous souhaitez déposer vos " + (amountb5000) + " billets de $5000:", " ", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + (amountb5000 * 5000)), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + (amountb5000 * 8))) + "§7/§6" + profile.rpBankMaxDep, "§7Transaction » §2+$" + (amountb5000 * 5000)));

        if (amountb1 <= 1) inv.setItem(10, InventoryUtils.createItem(b1.getType(), convertMax(amountb1), "§aDéposer $1", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 1), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 1)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$1"));
        if (amountb2 <= 1) inv.setItem(11, InventoryUtils.createItem(b2.getType(), convertMax(amountb2), "§aDéposer $2", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 2), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 2)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$2"));
        if (amountb5 <= 1) inv.setItem(12, InventoryUtils.createItem(b5.getType(), convertMax(amountb5), "§aDéposer $5", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 5), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 5)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$5"));
        if (amountb10 <= 1) inv.setItem(13, InventoryUtils.createItem(b10.getType(), convertMax(amountb10), "§aDéposer $10", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 10), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 10)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$10"));
        if (amountb20 <= 1) inv.setItem(14, InventoryUtils.createItem(b20.getType(), convertMax(amountb20), "§aDéposer $20", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 20), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 20)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$20"));
        if (amountb50 <= 1) inv.setItem(15, InventoryUtils.createItem(b50.getType(), convertMax(amountb50), "§aDéposer $50", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 50), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 50)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$50"));
        if (amountb100 <= 1) inv.setItem(16, InventoryUtils.createItem(b100.getType(), convertMax(amountb100), "§aDéposer $100", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 100), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 100)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$100"));
        if (amountb200 <= 1) inv.setItem(20, InventoryUtils.createItem(b200.getType(), convertMax(amountb200), "§aDéposer $200", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 200), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 200)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$200"));
        if (amountb500 <= 1) inv.setItem(21, InventoryUtils.createItem(b500.getType(), convertMax(amountb500), "§aDéposer $500", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 500), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 500)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$500"));
        if (amountb1000 <= 1) inv.setItem(22, InventoryUtils.createItem(b1000.getType(), convertMax(amountb1000), "§aDéposer $1000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 1000), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 1000)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$1000"));
        if (amountb2000 <= 1) inv.setItem(23, InventoryUtils.createItem(b2000.getType(), convertMax(amountb2000), "§aDéposer $2000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 2000), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 2000)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$2000"));
        if (amountb5000 <= 1) inv.setItem(24, InventoryUtils.createItem(b5000.getType(), convertMax(amountb5000), "§aDéposer $5000", "§7Cet argent sera ajouté a votre compte banquaire.", "", "§7Avant dépôt » §6$" + bankBalance, "§7Après dépôt » §6$" + (bankBalance + 5000), "§7Limite de dépôt » §6$" + (profile.rpBankMaxDep - (profile.rpBankCurrDep + 5000)) + "§7/§6" + (profile.rpBankMaxDep), "§7Transaction » §2+$5000"));

        if (!refresh) {
            player.closeInventory();
            player.openInventory(inv);
        }
    }

    private void reset(final Inventory inv) {
        for(int i = 0; i < 35; i++) {
            inv.setItem(i, glass_empty);
        }
        inv.setItem(35, close_item);
    }

    private int round(final double number) {
        return number == 0 ? 1 : Integer.parseInt(String.valueOf(Math.round(number)));
    }

    public boolean processDep(final Player player, final ItemStack itemStack, final int button) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        //Get the amount we need to deposit
        long clickedMoney;
        try {
            clickedMoney = Integer.parseInt(itemStack.getItemMeta().getDisplayName().replace("§aDéposer $", ""));
        } catch (NullPointerException ignored) {
            return false;
        }

        //Remove the money from the player
        if (InventoryUtils.hasItem(player, itemStack, true)) {

            final long billAmount = InventoryUtils.getAmount(player.getOpenInventory().getBottomInventory(), itemStack, false, true);

            if (button == 1) { //Right click aka dep all

                if (profile.rpBankMaxDep - (profile.rpBankCurrDep + (billAmount * clickedMoney)) >= 0 && (profile.rpBankBalance + (billAmount * clickedMoney)) <= profile.rpBankMaxBalance) {

                    profile.rpBankBalance += (billAmount * clickedMoney);
                    profile.rpBankCurrDep += (billAmount * clickedMoney);
                    depositedMoney += (billAmount * clickedMoney);
                    player.getInventory().remove(itemStack.getType());
                    return true;
                } else return false;
            }
            if (button == 0) { //Left click aka dep 1 by 1

                if (profile.rpBankMaxDep - (profile.rpBankCurrDep + clickedMoney) >= 0 && (profile.rpBankBalance + (billAmount)) <= profile.rpBankMaxBalance) {

                    profile.rpBankBalance += clickedMoney;
                    profile.rpBankCurrDep += clickedMoney;
                    depositedMoney += clickedMoney;
                    InventoryUtils.removeOne(player.getInventory(), itemStack, true);
                    return true;
                } else return false;
            }

        } else return false;

        return false;
    }

    public boolean processWit(final Player player, final ItemStack itemStack, final int button) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        //Get the amount we need to deposit
        long clickedMoney;
        try {
            clickedMoney = Integer.parseInt(itemStack.getItemMeta().getDisplayName().replace("§cRetirer $", ""));
        } catch (NullPointerException ignored) {
            return false;
        }

        //Remove the money from the player
        if (InventoryUtils.canStore(player, itemStack)) {

            final ItemStack bill = new ItemStack(itemStack.getType(), 1);

            if (button == 1) { //Right click aka withdraw 8 bills

                bill.setAmount(8);

                if ((profile.rpBankBalance - (clickedMoney * 8)) > (profile.rpBankMinBalance) && (profile.rpBankMaxWit - (profile.rpBankCurrWit + (clickedMoney * 8))) >= 0) {

                    profile.rpBankBalance -= (clickedMoney * 8);
                    profile.rpBankCurrWit += (clickedMoney * 8);
                    withdrewMoney += (clickedMoney * 8);
                    player.getInventory().addItem(bill);
                    return true;
                } else return false;
            } else if (button == 0) { //Left click aka wit 1 by 1

                if ((profile.rpBankBalance - clickedMoney) > profile.rpBankMinBalance && profile.rpBankMaxWit - (profile.rpBankCurrWit + clickedMoney) >= 0) {

                    profile.rpBankBalance -= clickedMoney;
                    profile.rpBankCurrWit += clickedMoney;
                    withdrewMoney += clickedMoney;
                    player.getInventory().addItem(bill);
                    return true;
                } else return false;
            }

        } else return false;

        return false;
    }

    public void processATMClose(final Player player) {

        if (depositedMoney > 0) ChatUtils.multicast(MsgType.BANK.getMessage() + "§fVous avez déposé §6$" + depositedMoney + "§f! Bonne journée.", player);
        if (withdrewMoney > 0) ChatUtils.multicast(MsgType.BANK.getMessage() + "§fVous avez retiré §6$" + withdrewMoney + "§f! Bonne journée.", player);

        depositedMoney = 0;
        withdrewMoney = 0;
    }

    public boolean isMoneySlot(final int slot) {
        return (slot >= 10 && slot <= 16) || (slot >= 20 && slot <= 24);
    }

    private int convertMax(final int number) {
        return number > 64 ? 64 : number <= 0 ? 1 : number;
    }
}
