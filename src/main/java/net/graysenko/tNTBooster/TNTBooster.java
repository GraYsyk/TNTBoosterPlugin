package net.graysenko.tNTBooster;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TNTBooster extends JavaPlugin implements TabCompleter {

    TNTBooster instance;

    @Override
    public void onEnable() {
        getLogger().info("########################################");
        getLogger().info("#                                      #");
        getLogger().info("#              TNTBooster              #");
        getLogger().info("#                                      #");
        getLogger().info("#               Enabled!               #");
        getLogger().info("#                                      #");
        getLogger().info("#              BUILD - 1.0             #");
        getLogger().info("#              DEV: _grays             #");
        getLogger().info("########################################");

        instance = this;
        saveDefaultConfig();
        // Регистрируем EventListener
        Bukkit.getPluginManager().registerEvents(new MyEventListener(this), this);

        getCommand("tntbooster").setExecutor(new CommandExecutor(){
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.YELLOW + getConfig().getString("messages.usage"));
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload") && sender.hasPermission("tntbooster.*")) {
                    reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + getConfig().getString("messages.reload"));
                    return true;
                }else{
                    sender.sendMessage(ChatColor.RED + getConfig().getString("messages.noPermission"));
                    return true;
                }
            }
        });
        getCommand("tntbooster").setTabCompleter(this);
    }




    @Override
    public void onDisable() {
        // Логика отключения плагина
    }

    public TNTBooster getInstance() {
        return instance;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return filter(complete(sender, args), args);
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload", "help");
        return Lists.newArrayList();
    }

    private List<String> filter(List<String> list, String[] args) {
        if (list == null) return null;
        String last = args[args.length - 1].toLowerCase();
        List<String> result = new ArrayList<>();
        for (String arg : list) {
            if (arg.startsWith(last)) result.add(arg);
        }
        return result;
    }

}
