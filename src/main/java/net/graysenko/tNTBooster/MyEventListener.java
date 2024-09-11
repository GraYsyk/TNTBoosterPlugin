package net.graysenko.tNTBooster;

import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;
import java.util.stream.Collectors;

public class MyEventListener implements Listener{

    private TNTBooster instance;

    public MyEventListener(TNTBooster tntBooster) {
        this.instance = tntBooster;
    }

    // TNT explosion configuration
    @EventHandler
    public void tntExplosion(ExplosionPrimeEvent event) {
        if(event.getEntity() instanceof TNTPrimed) {
            int power = instance.getInstance().getConfig().getInt("properties.tntexplosionpower");
            if (power > 600) {
                power = 600;
            }
            event.setRadius(power);
        }
    }

    // TNT block config
    @EventHandler
    public void tntBreak(EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            int choice = instance.getConfig().getInt("properties.tntblockbreak");
            if (choice == 1) {
                e.blockList().clear();
            } else if (choice == 2) {
                e.setCancelled(true);
            }
        }
    }

    //TNT Blacklist
    @EventHandler
    public void tntBlockList(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            String tntBlockListConfig = instance.getConfig().getString("properties.tntblocklist");

            if (tntBlockListConfig != null && !tntBlockListConfig.isEmpty()) {
                //Config string to materials
                List<Material> protectedBlocks = parseBlockList(tntBlockListConfig);

                // If Blacklisted - delete from List
                event.blockList().removeIf(block -> protectedBlocks.contains(block.getType()));
            }
        }
    }

    private static List<Material> parseBlockList(String tntBlockListConfig) {
        return List.of(tntBlockListConfig.split(",")).stream()
                .map(String::trim)
                .map(Material::matchMaterial)
                .filter(material -> material != null)
                .collect(Collectors.toList());
    }
}
