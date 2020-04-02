package me.limeglass.skacket;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.limeglass.skacket.managers.SignManager;

public final class Skacket extends JavaPlugin {

	private ProtocolManager protocolManager;
	private static Skacket instance;
	private SignManager signManager;
	private SkriptAddon addon;
	private Metrics metrics;

	@Override
	public void onEnable() {
		instance = this;
		File configFile = new File(getDataFolder(), "config.yml");
		//If newer version was found, update configuration.
		int version = 2;
		if (version != getConfig().getInt("configuration-version", version)) {
			if (configFile.exists())
				configFile.delete();
		}
		saveDefaultConfig();
		metrics = new Metrics(this);
		protocolManager = ProtocolLibrary.getProtocolManager();
		signManager = new SignManager(this);
		try {
			addon = Skript.registerAddon(this)
					.loadClasses("me.limeglass.skacket", "elements", "listeners")
					.setLanguageFileDirectory("lang");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.getConsoleSender().sendMessage("[Skacket] Skacket has been enabled!");
	}

	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	public SkriptAddon getAddonInstance() {
		return addon;
	}

	public SignManager getSignManager() {
		return signManager;
	}

	public static Skacket getInstance() {
		return instance;
	}

	public Metrics getMetrics() {
		return metrics;
	}

}
