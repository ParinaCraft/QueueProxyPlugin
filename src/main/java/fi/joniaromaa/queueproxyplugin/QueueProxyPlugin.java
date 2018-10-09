package fi.joniaromaa.queueproxyplugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fi.joniaromaa.queueproxyplugin.commands.DuelCommand;
import fi.joniaromaa.queueproxyplugin.commands.QueueCommand;
import fi.joniaromaa.queueproxyplugin.commands.SpectateCommand;
import fi.joniaromaa.queueproxyplugin.config.QueueProxyPluginConfig;
import fi.joniaromaa.queueproxyplugin.datanase.DatabaseManager;
import fi.joniaromaa.queueproxyplugin.duels.DuelsManager;
import fi.joniaromaa.queueproxyplugin.game.GameTypeManager;
import fi.joniaromaa.queueproxyplugin.listeners.PlayerListener;
import fi.joniaromaa.queueproxyplugin.listeners.PluginMessageListener;
import fi.joniaromaa.queueproxyplugin.net.NetworkManager;
import fi.joniaromaa.queueproxyplugin.runnables.MonitorRunnable;
import fi.joniaromaa.queueproxyplugin.server.ServerManager;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class QueueProxyPlugin extends Plugin
{
	@Getter private static QueueProxyPlugin plugin;
	
	@Getter private QueueProxyPluginConfig pluginConfig;
	@Getter private DatabaseManager databaseManager;
	@Getter private GameTypeManager gameTypeManager;
	@Getter private ServerManager serverManager;
	@Getter private NetworkManager networkManager;
	@Getter private DuelsManager duelsManager;
	
	public QueueProxyPlugin()
	{
		QueueProxyPlugin.plugin = this;
	}
	
	@Override
	public void onEnable()
	{
		LangUtils.init(Locale.forLanguageTag("fi-FI"));
		
		try
		{
			this.pluginConfig = new QueueProxyPluginConfig("config.yml");
		}
		catch (IOException e)
		{
			e.printStackTrace();

			throw new RuntimeException("Failed to load config!");
		}
		
		this.databaseManager = new DatabaseManager(this.pluginConfig.getDatabaseHost(), this.pluginConfig.getDatabasePort(), this.pluginConfig.getDatabaseUser(), this.pluginConfig.getDatabasePass(), this.pluginConfig.getDatabaseName());
		this.gameTypeManager = new GameTypeManager();
		try
		{
			this.gameTypeManager.load();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			throw new RuntimeException("Failed to load gametypes!");
		}
		
		this.serverManager = new ServerManager();
		try
		{
			this.serverManager.load();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			throw new RuntimeException("Failed to load servers!");
		}
		
		this.networkManager = new NetworkManager();
		
		try
		{
			this.networkManager.start();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();

			throw new RuntimeException("Failed to start network manager!");
		}
		
		this.duelsManager = new DuelsManager();
		
		this.getProxy().getPluginManager().registerCommand(this, new QueueCommand());
		this.getProxy().getPluginManager().registerCommand(this, new DuelCommand());
		this.getProxy().getPluginManager().registerCommand(this, new SpectateCommand());
		
		this.getProxy().getPluginManager().registerListener(this, new PluginMessageListener());
		this.getProxy().getPluginManager().registerListener(this, new PlayerListener());
		
		this.getProxy().registerChannel("Queue");
		
		this.getProxy().getScheduler().schedule(this, new MonitorRunnable(), 250, 250, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void onDisable()
	{
		this.networkManager.stop();
	}
}
