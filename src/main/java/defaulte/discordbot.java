//MzkxMzc4NDk1MTE4NTA4MDMy.DRX2NA.G6cVAOTl9SGfRLp9d3H8orAyrmI
package defaulte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.util.concurrent.FutureCallback;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.message.TypingStartListener;

public class discordbot {
	public discordbot(String token) {
		// See "How to get the token" below
		DiscordAPI api = Javacord.getApi(token, true);
		// connect
		api.connect(new FutureCallback<DiscordAPI>() {
			public void onSuccess(final DiscordAPI api) {
				// do what you want now
				api.registerListener(new pdListener());
			}

			public void onFailure(Throwable t) {
				// login failed
				t.printStackTrace();
			}
		});
	}

	public static void main(String args[]) {
		discordbot bob = new discordbot();
	}
}

class pdListener implements MessageCreateListener {
	static Scanner sc = new Scanner(System.in);
	final static String path = "src/shattered-pixel-dungeon-master/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/items/";

	public void onMessageCreate(DiscordAPI arg0, Message arg1) {
		System.out.println(arg1);
		String msgStr = arg1.getContent();
		if (msgStr.matches("!item \\w+ \\w+")) {
			pdListener.outputfile(arg1, pdListener.testFolder(msgStr));
		} else
			if (msgStr.matches("!help")) {
			String s = "```\n!linkme query\n\tSearches the wiki for stuff. Query can only consist of letters and the `+` sign\n";

		} else if (msgStr.matches("!linkme [a-zA-Z\\+]+")) {
			try {
				linkme(arg1, msgStr.substring(msgStr.indexOf(" ") + 1, msgStr.length()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String linkme(Message arg0, String arg1) throws IOException {
		try {
			// http://pixeldungeon.wikia.com/api/v1/Search/List?query=water&namespaces=0%2C14
			// Starts a query to the wikia api
			URL wikiaSocket = new URL(
					"http://pixeldungeon.wikia.com/api/v1/Search/List?query=" + arg1 + "&limit=3&namespaces=0%2C14");
			URLConnection myURLConnection = wikiaSocket.openConnection();
			myURLConnection.connect();

			BufferedReader sc = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			String strarr[] = sc.readLine().replace("\\", "").split("[\\[\\]]")[1].split("\\{|\\},");
			Pattern p = Pattern.compile("(\\w+?)\": ?\"(.+?)\",");
			System.out.println(p);
			for (String g : strarr) {
				Matcher m = p.matcher(g);
				while (m.find()) {
					if (m.group(1).equals("url")) {
						arg0.reply(m.group(2) + "\n");
					}

				}
			}
		} catch (Exception e) {
			arg0.reply(arg1 + " not found");
		}
		return null;

	}

	// Returns the path of the object if it exists, else return null
	public static String testFolder(String s) {
		if (s.matches("!item \\w+ \\w+")) {
			String[] input = s.split(" ");
			String path2 = path;
			switch (input[1]) {
			case "ring":
				;//
			case "wand":
				;//
			case "potion":
				;//
			case "scroll":
				;//
				path2 += input[1] + "s";
				input[2] = input[1] + "of" + input[2];
				break;
			case "weapon":
				path2 += "weapon/melee";
				break;
			default:
				path2 += input[1];
			}
			path2 += "/" + input[2] + ".java";
			return new File(path2).exists() ? path2 : null;
		}
		return null;
	}

	public static void outputfile(Message arg1, String path) {
		if (path == null)
			return;
		try {
			Scanner sc = new Scanner(new FileInputStream(path));
			String reply = "```java\n";
			for (int i = 0; i < 20; i++)
				sc.nextLine();
			while (sc.hasNext())
				reply += sc.nextLine() + "\n";
			reply += "```";
			arg1.reply(reply);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
