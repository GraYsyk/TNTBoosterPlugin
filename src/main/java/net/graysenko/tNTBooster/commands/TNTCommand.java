package net.graysenko.tNTBooster.commands;

import net.graysenko.tNTBooster.TNTBooster;
import net.graysenko.tNTBooster.Util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TNTCommand extends AbstractCommand {

    public TNTCommand() {
        super("tntbooster");
    }

    ColorUtil colorUtil = new ColorUtil();

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.YELLOW + TNTBooster.getInstance().getConfig().getString("messages.usage"));
            return;
        }
        if(args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("tntbooster.*")) {
                sender.sendMessage(ChatColor.RED + TNTBooster.getInstance().getConfig().getString("messages.noPermission"));
            }
            TNTBooster.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + TNTBooster.getInstance().getConfig().getString("messages.reload"));
            return;
        }


        //GET TNT Command
        if(args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("tntbooster.*")) {
                sender.sendMessage(ChatColor.RED + TNTBooster.getInstance().getConfig().getString("messages.noPermission"));
                return;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.YELLOW + TNTBooster.getInstance().getConfig().getString("messages.usage"));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + TNTBooster.getInstance().getConfig().getString("messages.noplayerfound"));
                return;
            }
            String level = args[2];
            ItemStack item = new ItemStack(Material.TNT);
            ItemMeta meta = item.getItemMeta();

            String tntl1 = ColorUtil.parseAllColors(TNTBooster.getInstance().getConfig().getString("tnt.level1.name"));
            List<String> loreLevel1 = TNTBooster.getInstance().getConfig().getStringList("tnt.level1.lore");

            String tntl2 = ColorUtil.parseAllColors(TNTBooster.getInstance().getConfig().getString("tnt.level2.name"));
            List<String> loreLevel2 = TNTBooster.getInstance().getConfig().getStringList("tnt.level2.lore");

            String tntl3 = ColorUtil.parseAllColors(TNTBooster.getInstance().getConfig().getString("tnt.level3.name"));
            List<String> loreLevel3 = TNTBooster.getInstance().getConfig().getStringList("tnt.level3.lore");

            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(TNTBooster.getInstance(), "tnt_level");

            switch (level) {
                case "1":
                    meta.setDisplayName(tntl1);
                    meta.setLore(ColorUtil.parseLoreColors(loreLevel1));
                    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 5);
                    break;
                case "2":
                    meta.setDisplayName(tntl2);
                    meta.setLore(ColorUtil.parseLoreColors(loreLevel2));
                    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 10);
                    break;
                case "3":
                    meta.setDisplayName(tntl3);
                    meta.setLore(ColorUtil.parseLoreColors(loreLevel3));
                    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 15);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Invalid level");
                    return;
            }

            meta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            target.getInventory().addItem(item);
            sender.sendMessage(ChatColor.GREEN + "TNTBooster: You have given " + target.getName() + " TNT LEVEL " + level + "!");
        }
    }

    private List<String> convertLoreColors(List<String> lore) {
        if (lore == null) return null;
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            if (line == null) break;
            coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return coloredLore;
    }
}
