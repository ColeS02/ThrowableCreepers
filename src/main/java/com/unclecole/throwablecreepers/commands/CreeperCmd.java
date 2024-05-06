package com.unclecole.throwablecreepers.commands;
import com.unclecole.throwablecreepers.ThrowableCreepers;
import com.unclecole.throwablecreepers.utils.PlaceHolder;
import com.unclecole.throwablecreepers.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreeperCmd implements CommandExecutor {

    private ThrowableCreepers plugin;

    public CreeperCmd() {
        plugin = ThrowableCreepers.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {

        if(args.length < 3) {
            TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/creeper give <name> <amount>"));
            return false;
        }
        if(args[0].equals("give")) {

            if(!s.hasPermission("creeper.give")) {
                TL.NO_PERMISSION.send(s);
                return false;
            }

            if(Bukkit.getPlayer(args[1]) == null) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/creeper give <name> <amount>"));
                return false;
            }

            if(!isParsable(args[2])) {
                TL.INVALID_ARGUMENTS.send(s, new PlaceHolder("<command>", "/creeper give <name> <amount>"));
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);
            int amount = Integer.parseInt(args[2]);

            for(int i = 0; i < amount; i++) {

                ItemStack item = ThrowableCreepers.getInstance().getCreeperItem();
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setString("explosion", "semtex");
                nbtItem.applyNBT(item);

                player.getInventory().addItem(item);
            }

            if(!s.equals(player)) {
                TL.GAVE_CREEPERS.send(s, new PlaceHolder("%player%", player.getName()), new PlaceHolder("%amount%", amount));
            }

            TL.RECEIVED_CREEPERS.send(player, new PlaceHolder("%amount%", amount));
        }

        return false;
    }



    public boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
