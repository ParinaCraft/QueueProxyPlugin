package fi.joniaromaa.queueproxyplugin.datanase;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DatabaseManager
{
	private DataSource pool;
	
	public DatabaseManager(String host, int port, String user, String pass, String name)
	{
		String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC";
		
		PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName("com.mysql.jdbc.Driver");
        poolProperties.setUrl(connectionString);
        poolProperties.setUsername(user);
        poolProperties.setPassword(pass);
        poolProperties.setMinIdle(8);
        poolProperties.setMaxIdle(64);
        poolProperties.setMaxActive(64);
        poolProperties.setInitialSize(0);
        poolProperties.setMaxWait(-1);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setRemoveAbandonedTimeout(60);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setValidationInterval(30000);
        this.pool = new DataSource(poolProperties);
	}
	
	public Connection getConnection() throws SQLException
	{
		return this.pool.getConnection();
	}
}
