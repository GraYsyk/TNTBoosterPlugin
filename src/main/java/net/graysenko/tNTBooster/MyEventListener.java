package net.graysenko.tNTBooster;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.stream.Collectors;

public class MyEventListener implements Listener{

    private TNTBooster instance;
    private static final NamespacedKey TNT_LEVEL_KEY = new NamespacedKey(TNTBooster.getInstance(), "tnt_level");

    public MyEventListener(TNTBooster tntBooster) {
        this.instance = tntBooster;
    }

    // TNT explosion configuration
    @EventHandler
    public void tntExplosion(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) e.getEntity();

            if(tnt.getFuseTicks() > 60 || tnt.isIncendiary() || tnt.getCustomName() != null) return;

            int choice = instance.getInstance().getConfig().getInt("properties.tntblockbreak");
            int defaultPower = instance.getInstance().getConfig().getInt("properties.tntexplosionpower");

            if (choice == 2) {
                e.setCancelled(true);
                return;
            }

            int power = Math.min(defaultPower, 600); // Максимум 600
            e.setRadius(power);
        }
    }


    @EventHandler
    public void onTNTplace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.TNT){

            ItemStack itemInHand = e.getItemInHand();

            if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().getPersistentDataContainer().has(TNT_LEVEL_KEY, PersistentDataType.INTEGER)) {                Block block = e.getBlockPlaced();
                int level = itemInHand.getItemMeta().getPersistentDataContainer().get(TNT_LEVEL_KEY, PersistentDataType.INTEGER);

                e.getBlock().setType(Material.AIR);
                spawnTNT(e.getBlock().getLocation(), level);
            } else {
                e.getBlock().setType(Material.AIR);
                spawnTNT(e.getBlock().getLocation(), 0);
            }
        }
    }

    // TNT block config
    @EventHandler
    public void tntBreak(EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) e.getEntity();
            int choice = instance.getConfig().getInt("properties.tntblockbreak");
            if (choice == 1) {
                e.blockList().clear();
            }
            if(tnt.hasMetadata("tntlvl")){
                int level = tnt.getMetadata("tntlvl").get(0).asInt();
                switch (level) {
                    case 0:
                        // Логика для TNT уровня 0
                        break;
                    case 5:
                        // Логика для TNT уровня 5
                        break;
                    case 10:
                        // Логика для TNT уровня 10
                        break;
                    case 15:
                        instance.getLogger().info("Case 15");
                        checkBlocksBelowTNT(tnt.getLocation());
                        break;
                    default:
                        // Логика для других случаев
                        break;
                }
                instance.getLogger().info("none");
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

    private void spawnTNT(Location location, int level){
        TNTPrimed tnt = (TNTPrimed) location.getWorld().spawn(location, TNTPrimed.class);

        tnt.setYield(4f);

        switch (level){
            case 0:
                tnt.setYield(4f);
                tnt.setFuseTicks(60);
                break;
            case 5:
                tnt.setFuseTicks(80);
                tnt.setYield(15f);
                tnt.setCustomName("Tnt 5");
                break;
            case 10:
                tnt.setFuseTicks(100);
                tnt.setYield(25f);
                tnt.setIsIncendiary(true);
                tnt.setCustomName("Tnt 10");
                break;
            case 15:
                tnt.setFuseTicks(160);
                tnt.setYield(40f);
                tnt.setCustomName("Tnt 15");
                break;
            default:
                tnt.setYield(4f);
                break;
        }
        tnt.setMetadata("tntlvl", new FixedMetadataValue(instance, level));
    }
    private void checkBlocksBelowTNT(Location tntLocation) {

        int baseX = tntLocation.getBlockX();
        int baseY = tntLocation.getBlockY() - 1;
        int baseZ = tntLocation.getBlockZ();

        World world = tntLocation.getWorld();

        for (int x = baseX - 1; x <= baseX + 1; x++) {
            for (int z = baseZ - 1; z <= baseZ + 1; z++) {
                Block block = world.getBlockAt(x, baseY, z);

                if (block.getType() == Material.OBSIDIAN) {
                    block.breakNaturally();
                }
            }
        }
    }
}
