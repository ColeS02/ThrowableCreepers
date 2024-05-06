package com.unclecole.throwablecreepers.listeners;

import com.unclecole.throwablecreepers.ThrowableCreepers;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void teleportEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(ThrowableCreepers.getInstance().getConfig().getString("Material")))) return;
        if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
        NBTItem item = new NBTItem(event.getPlayer().getInventory().getItemInMainHand());
        if(item.hasKey("explosion") && item.getString("explosion").equals("semtex")) {
            event.setCancelled(true);
            if(ThrowableCreepers.getInstance().getCooldown().containsKey(event.getPlayer().getUniqueId()) && ThrowableCreepers.getInstance().getCooldown().get(event.getPlayer().getUniqueId()) > System.currentTimeMillis()) return;
            if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            }
            /*ThrowableCreepers.getInstance().getCooldown().put(event.getPlayer().getUniqueId(),
                    (System.currentTimeMillis() + ThrowableCreepers.getInstance().getConfig().getLong("Cooldown") * 1000L));*/
            Item creeper = event.getPlayer().getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.CREEPER_SPAWN_EGG));
            creeper.setVelocity(player.getEyeLocation().getDirection());
            creeper.setPickupDelay(1000);
            ThrowableCreepers.getInstance().getThrowableCreepers().add(creeper);
        }
    }

}
