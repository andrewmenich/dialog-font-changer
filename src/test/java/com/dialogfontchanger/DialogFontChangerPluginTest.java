package com.dialogfontchanger;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DialogFontChangerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DialogFontChangerPlugin.class);
		RuneLite.main(args);
	}
}