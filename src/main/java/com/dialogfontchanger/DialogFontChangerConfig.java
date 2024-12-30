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
			keyName = "lineHeightMultiplier",
			name = "Line Height Multiplier",
			description = "Adjust the line height for dialog text (default: 1.0)",
			position = 3
	)
	@Range(
			min = 0,
			max = 200
	)
	default int lineHeightMultiplier() {
		return 100;
	}

	@ConfigItem(
		keyName = "playerDialog",
		name = "Modify Player Dialog",
		description = "Uses selected font for player dialog.",
		position = 4
	)
	default boolean playerDialog()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideNPCName",
			name = "Hide NPC Name",
			description = "Will only hide the NPC Name if there are 4 lines of text.",
			position = 5
	)
	default boolean hideNPCName()
	{
		return true;
	}
}
