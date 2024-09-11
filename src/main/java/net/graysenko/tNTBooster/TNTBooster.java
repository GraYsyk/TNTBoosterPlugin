package net.graysenko.tNTBooster;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TNTBooster extends JavaPlugin {

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
                if(args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + getConfig().getString("messages.reload"));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.YELLOW + getConfig().getString("messages.usage"));
                    return true;
                }
            }
        });
    }




    @Override
    public void onDisable() {
        // Логика отключения плагина
    }

    public TNTBooster getInstance() {
        return instance;
    }
}
