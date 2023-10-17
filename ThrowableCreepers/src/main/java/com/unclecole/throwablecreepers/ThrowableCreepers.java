package com.unclecole.throwablecreepers;

import com.unclecole.throwablecreepers.commands.CreeperCmd;
import com.unclecole.throwablecreepers.listeners.PlayerListener;
import com.unclecole.throwablecreepers.utils.C;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public final class ThrowableCreepers extends JavaPlugin {

    public static ThrowableCreepers instance;
    private ArrayList<Item> throwableCreepers;
    private ItemStack creeperItem;
    private HashMap<UUID, Long> cooldown;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //PENIS
        instance = this;
        throwableCreepers = new ArrayList<>();
        cooldown = new HashMap<>();
        saveDefaultConfig();
        checkPlayers();
        loadConfig();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("creeper").setExecutor(new CreeperCmd());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        ItemBuilder item = new ItemBuilder(Material.getMaterial(this.getConfig().getString("Material")));
        item.setName(C.color(this.getConfig().getString("Title")));
        this.getConfig().getStringList("Lore").forEach(string -> {
            item.addLore(C.color(string));
        });
        creeperItem = item;
    }

    public ItemStack getCreeperItem() { return creeperItem; }

    public static ThrowableCreepers getInstance() { return instance; }
    public ArrayList<Item> getThrowableCreepers() { return throwableCreepers; }
    public HashMap<UUID, Long> getCooldown() { return cooldown; }

    public void checkPlayers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if(throwableCreepers.isEmpty()) return;

                Iterator iterator = throwableCreepers.iterator();
                while(iterator.hasNext()) {
                    Item entity = (Item) iterator.next();
                    if(entity.isDead()) iterator.remove();
                    if(entity.getVelocity().getX() == 0 || entity.getVelocity().getZ() == 0 || entity.getVelocity().getY() == 0) {
                        Location loc = entity.getLocation();
                        entity.remove();
                        LivingEntity mob = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
                        Creeper creeper = (Creeper) mob;
                        creeper.ignite();
                        creeper.setMaxFuseTicks(0);
                        creeper.setInvisible(true);
                    }
                };
            }
        }, 0L, 1L);
    }
}
