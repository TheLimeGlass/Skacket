package me.limeglass.skacket;

import java.io.File;
import java.io.IOException;

import me.limeglass.skacket.managers.GlowingAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.limeglass.skacket.managers.SignManager;

public final class Skacket extends JavaPlugin {

	private ProtocolManager protocolManager;
	private static GlowingAPI glowing;
	private static Skacket instance;
	private SignManager signManager;
	private SkriptAddon addon;
	private Metrics metrics;

	@Override
	public void onEnable() {
		instance = this;
		glowing = new GlowingAPI(this);
		File configFile = new File(getDataFolder(), "config.yml");
		//If newer version was found, update configuration.
		int version = 1;
		if (version != getConfig().getInt("configuration-version", version)) {
			if (configFile.exists())
				configFile.delete();
		}
		saveDefaultConfig();
		metrics = new Metrics(this, 6699);
		metrics.addCustomChart(new SimplePie("skript_version", () ->
			Skript.getInstance().getDescription().getVersion()
		));
		protocolManager = ProtocolLibrary.getProtocolManager();
		signManager = new SignManager(this);
		try {
			addon = Skript.registerAddon(this)
					.loadClasses("me.limeglass.skacket", "elements", "listeners")
					.setLanguageFileDirectory("lang");
		} catch (IOException e) {
			e.printStackTrace();
		}
		getLogger().info("[Skacket] Skacket has been enabled!");
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

	public GlowingAPI getGlowingAPI() {
		return glowing;
	}

	public Metrics getMetrics() {
		return metrics;
	}

}
