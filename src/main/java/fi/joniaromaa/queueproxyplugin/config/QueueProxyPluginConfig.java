package fi.joniaromaa.queueproxyplugin.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class QueueProxyPluginConfig
{
	@Getter private String databaseHost;
	@Getter private int databasePort;
	@Getter private String databaseUser;
	@Getter private String databasePass;
	@Getter private String databaseName;
	
	public QueueProxyPluginConfig(String name) throws IOException
	{
		if (!QueueProxyPlugin.getPlugin().getDataFolder().exists())
		{
			QueueProxyPlugin.getPlugin().getDataFolder().mkdir();
		}

        File file = new File(QueueProxyPlugin.getPlugin().getDataFolder(), name);
     
        if (!file.exists())
        {
            try (InputStream in = QueueProxyPlugin.getPlugin().getResourceAsStream(name))
            {
                Files.copy(in, file.toPath());
            }
        }
        
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		
		this.databaseHost = configuration.getString("database.host");
		this.databasePort = configuration.getInt("database.port");
		this.databaseUser = configuration.getString("database.user");
		this.databasePass = configuration.getString("database.pass");
		this.databaseName = configuration.getString("database.name");
	}
}
