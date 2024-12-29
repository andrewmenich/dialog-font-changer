package com.dialogfontchanger;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("dialogfontchanger")
public interface DialogFontChangerConfig extends Config
{
	@ConfigItem(
		keyName = "font",
		name = "Font",
		description = "Select the font you want to replace the default dialog Quill font with.",
		position = 1
	)
	default FontOptions fontOptions()
	{
		return FontOptions.ROBOTO;
	}

	@ConfigItem(
			keyName = "fontSize",
			name = "Font Size",
			description = "Specify between 12-20. Use with caution!",
			position = 2
	)
	@Range(
			min = 12,
			max = 20
	)
	default int fontSize()
	{
		return 16;
	}

	@ConfigItem(
		keyName = "playerDialog",
		name = "Modify Player Dialog",
		description = "Uses selected font for player dialog.",
		position = 3
	)
	default boolean playerDialog()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideNPCName",
			name = "Hide NPC Name",
			description = "Doing so provides more room for the custom font positioning.",
			position = 4
	)
	default boolean hideNPCName()
	{
		return false;
	}
}
