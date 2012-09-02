package code.husky;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EndRespawn extends JavaPlugin implements Listener {

	List<String> sel = new ArrayList<String>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		createConfig();
	}

	public void onDisable() {

	}

	public void createConfig() {
		File con = new File("plugins/EndRespawn");
		File conf = new File("plugins/EndRespawn/config.yml");
		if(!con.exists()) {
			con.mkdir();
			if(!conf.exists()) {
				try {
					conf.createNewFile();
					YamlConfiguration config = YamlConfiguration.loadConfiguration(conf);
					config.options().header("EndRespawn, Made by Husky.");
					config.save(conf);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(!conf.exists()) {
				try {
					conf.createNewFile();
					YamlConfiguration config = YamlConfiguration.loadConfiguration(conf);
					config.options().header("EndRespawn, Made by Husky.");
					config.save(conf);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void onDragonDeath(EntityCreatePortalEvent e) { // Logically, the dragon dies, then bukkit calls this event.
		if(e.getEntity() instanceof EnderDragon) {// enderdragon made the portal. :)
			EnderDragon ed = (EnderDragon) e.getEntity();
			World end = ed.getWorld();
			File conf = new File("plugins/EndRespawn/config.yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(conf);
			int tx = config.getInt("endrespawn.x");
			int ty = config.getInt("endrespawn.y");
			int tz = config.getInt("endrespawn.z");
			String wn = config.getString("endrespawn.worldname");
			World tp = getServer().getWorld(wn);
			Location l = new Location(tp,tx,ty,tz);
			for(Player kill : end.getPlayers()) {
				kill.teleport(l);
				kill.sendMessage(ChatColor.GREEN + "You have just slain the enderdragon!");
			}
			end.spawnEntity(ed.getLocation(), EntityType.ENDER_DRAGON);
		}
	}

	@EventHandler
	public void selectRespawn(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		File conf = new File("plugins/EndRespawn/config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(conf);
		World w = p.getWorld();
		String wn = w.getName();
		if(sel.contains(p.getName())) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block b = e.getClickedBlock();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				config.set("endrespawn.x",x);
				config.set("endrespawn.y",y);
				config.set("endrespawn.z",z);
				config.set("endrespawn.worldname",wn);
				try {
					config.save(conf);
					sel.remove(p.getName());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				p.sendMessage(ChatColor.GREEN + "Selected, and saved!");
			} else {

			}
		} else {
			e.setCancelled(false);
		}
	}

	public boolean onCommand(CommandSender sender, Command command, String c, String[] args) {
		Player p = (Player) sender;
		if(c.equalsIgnoreCase("er") || c.equalsIgnoreCase("endrespawn")) {
			if(p.hasPermission("endrespawn.admin")) {
				if(args.length != 0) {
					if(args[0].equalsIgnoreCase("select")) {
						sel.add(p.getName());
						p.sendMessage(ChatColor.GREEN + "Now right click a block where you want the players to respawn from the end :)");
						return true;
					}
				}
			}
		}
		return false;
	}

}
