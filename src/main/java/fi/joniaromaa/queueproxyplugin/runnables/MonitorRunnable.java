package fi.joniaromaa.queueproxyplugin.runnables;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;

public class MonitorRunnable implements Runnable
{
	@Override
	public void run()
	{
		QueueProxyPlugin.getPlugin().getNetworkManager().onMonitor();
		QueueProxyPlugin.getPlugin().getDuelsManager().onMonitor();
	}
}
