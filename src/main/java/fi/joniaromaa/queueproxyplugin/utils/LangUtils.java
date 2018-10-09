package fi.joniaromaa.queueproxyplugin.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class LangUtils
{
	private static final HashMap<Locale, TreeMap<String, String>> textsByLang = new HashMap<>();
	private static TreeMap<String, String> defaultTexts = null;
	
	public static void init(Locale defaultLang)
	{
		/*TreeMap<String, String> english = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		english.put("command.queue.missing-gametype", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "Please enter gamemode where you would like to queue");
		english.put("command.queue.unknown-gametype", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "No gamemode was found for query {0}");
		english.put("command.queue.queued", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "Looking for game {0}");
		english.put("command.queue.server-found", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "Server was found, connecting");
		english.put("command.queue.server-found-unknown", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "Server was found but we are unable to connect you. Please try again");
		english.put("command.queue.in-queue", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "You are already in queue");
		english.put("command.queue.no-servers", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "No servers ware awaible at the moment. Please try again");
		english.put("command.queue.request-expired", ChatColor.AQUA + "QUEUE > " + ChatColor.RESET + "Game queue request expired, no servers ware found. Please try again");
		LangUtils.textsByLang.put(Locale.forLanguageTag("en-US"), english);*/
		
		TreeMap<String, String> finnish = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		finnish.put("command.queue.missing-gametype", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Ole hyv� ja anna pelimuodon nimi, mihin haluat liitty�");
		finnish.put("command.queue.unknown-gametype", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Ei peli muotoja l�ydetty hakusanalla {0}");
		finnish.put("command.queue.queued", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Etsit��n palvelinta pelimuodolle {0}");
		finnish.put("command.queue.server-found", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Palvelin l�ytyi, yhdistet��n");
		finnish.put("command.queue.server-found-unknown", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Palvelin l�ytyi, mutta emme onnistuneet yhdist�m��n siihen. Ole hyv� ja yrit� uudelleen");
		finnish.put("command.queue.in-queue", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Olet jo jonossa, malttia");
		finnish.put("command.queue.no-servers", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Vapaita palvelimia ei l�ytynyt t�ll� hetkell�. Ole hyv� ja yrit� uudelleen");
		finnish.put("command.queue.request-expired", ChatColor.AQUA + "JONO > " + ChatColor.RESET + "Pelimuoto jono pyynt� ep�onnistui. Ole hyv� ja yrit� uudelleen");
		finnish.put("command.duel.help", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "HELP HELP HELP!");
		finnish.put("command.duel.modes", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Saatavilla olevia pelimuotoja: classic, soup, skywars");
		finnish.put("command.duel.challange.invalid-usage", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "K�yt� muodossa /duel challange <nimi> <pelimuoto>");
		finnish.put("command.duel.challange.target-not-found", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "K�ytt�j�� {0} ei l�ydetty!");
		finnish.put("command.duel.challange.unknown-gametype", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Pelimuotoa {0} ei l�ytynyt!");
		finnish.put("command.duel.challange.done", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Olet haastanut k�ytt�j�n {0} kaksintaisteluun pelimuodossa {1}!");
		finnish.put("command.duel.challange.receive", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "K�ytt�j� {0} on haastanut sinut kaksintaisteluun pelimuodossa {1}!");
		finnish.put("command.duel.challange.waiting-response", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Voit haastaa yhen pelaajan kerralla!");
		finnish.put("command.duel.challange.request-expired-sender", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Kaksintaistelu haaste pelaajalle {0} pelimuodossa {1} on er��ntynyt");
		finnish.put("command.duel.challange.request-expired-target", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Kaksintaistelu haaste pelaajalta {0} pelimuodossa {1} on er��ntynyt");
		finnish.put("command.duel.challange.self", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Et voi haastaa itse�si!");
		finnish.put("command.duel.accept.invalid-usage", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "K�yt� muodossa /duel accept <nimi>");
		finnish.put("command.duel.accept.target-not-found", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "K�ytt�j�� {0} ei l�ydetty!");
		finnish.put("command.duel.accept.failed", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Kaksintaistelu haastetta pelaajalta {0} ei voitu hyv�ksy�");
		finnish.put("command.duel.accept.done", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "Hyv�ksyit k�ytt�j�n {0} kaksintaistelu haasteen, etsit��n vapaata palvelinta");
		finnish.put("command.duel.accept.accepted", ChatColor.AQUA + "DUEL > " + ChatColor.RESET + "K�ytt�j� {0} hyv�ksyit kaksintaistelu haasteesi, etsit��n vapaata palvelinta");
		LangUtils.textsByLang.put(Locale.forLanguageTag("fi-FI"), finnish);

		LangUtils.defaultTexts = LangUtils.textsByLang.get(defaultLang);
		if (LangUtils.defaultTexts == null && LangUtils.textsByLang.size() > 0)
		{
			for(TreeMap<String, String> texts : LangUtils.textsByLang.values())
			{
				LangUtils.defaultTexts = texts;
				break;
			}
		}
	}
	
	public static BaseComponent[]getText(Locale locale, String key)
	{
		TreeMap<String, String> texts = LangUtils.textsByLang.getOrDefault(locale, LangUtils.defaultTexts);
		if (texts != null)
		{
			String text = texts.get(key);
			if (text != null)
			{
				return TextComponent.fromLegacyText(text);
			}
		}
		
		return new TextComponent[] { new TextComponent(key) };
	}
	
	public static BaseComponent[] getText(Locale locale, String key, Object... params)
	{
		TreeMap<String, String> texts = LangUtils.textsByLang.getOrDefault(locale, LangUtils.defaultTexts);
		if (texts != null)
		{
			String text = texts.get(key);
			if (text != null)
			{
				return TextComponent.fromLegacyText(MessageFormat.format(text, params));
			}
		}

		return new TextComponent[] { new TextComponent(key) };
	}
}
