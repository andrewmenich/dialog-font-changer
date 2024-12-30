package com.dialogfontchanger;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.Font;
import java.util.List;
import java.util.Objects;

@Slf4j
@PluginDescriptor(
	name = "Dialog Font Changer"
)
public class DialogFontChangerPlugin extends Plugin
{
	private static final Logger log = LoggerFactory.getLogger(DialogFontChangerPlugin.class);

	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private DialogFontChangerConfig config;

	@Inject
	private NPCDialogOverlay npcOverlay;

	@Inject
	private PlayerDialogOverlay playerOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClientThread clientThread;

	private Font customFont;

	public boolean enabled = false;

	public int npcDialogId = ComponentID.DIALOG_NPC_TEXT;

	public int playerDialogId = ComponentID.DIALOG_PLAYER_TEXT;

	public Widget activeWidget;
	public Overlay activeOverlay;

	public Widget npcDialogWidget;
	public Widget playerDialogWidget;

	public String dialogBoxBounds;

    @Override
	protected void startUp() throws Exception
	{
		this.enabled = true;
		log.info("Dialog Font Changer plugin v1.0.0 started.");
	}

	@Override
	protected void shutDown() throws Exception
	{
		this.enabled = false;

		if(this.activeWidget != null){
			this.activeWidget.setHidden(false);
		}

		if(this.activeOverlay != null){
			this.overlayManager.remove(this.activeOverlay);
		}

		log.info("Dialog Font Changer plugin v1.0.0 stopped.");
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetID.DIALOG_NPC_GROUP_ID){
			clientThread.invokeLater(() -> {
				if (this.enabled && this.client.getWidget(ComponentID.DIALOG_NPC_TEXT) != null) {
					this.npcDialogWidget = this.client.getWidget(ComponentID.DIALOG_NPC_TEXT);
					assert this.npcDialogWidget != null;
					this.dialogBoxBounds = this.npcDialogWidget.getBounds().toString();
					log.info("Processing NPC Dialog Overlay");
					processWidgetOverlay(this.npcDialogId, this.npcOverlay);
				}
			});
		}

		if (event.getGroupId() == WidgetID.DIALOG_PLAYER_GROUP_ID && config.playerDialog()){
			clientThread.invokeLater(() -> {
				if (this.enabled && this.client.getWidget(ComponentID.DIALOG_PLAYER_TEXT) != null) {
					this.playerDialogWidget = this.client.getWidget(ComponentID.DIALOG_PLAYER_TEXT);

					if (config.playerDialog()) {
						processWidgetOverlay(playerDialogId, playerOverlay);
					}
				}
			});
		}
	}

	@Subscribe
	public void onWidgetClosed(WidgetClosed event)
	{
		if(event.getGroupId() == WidgetID.DIALOG_NPC_GROUP_ID){
			this.hideWidgetOverlay(this.npcDialogId, this.npcOverlay);
		}

		if(event.getGroupId() == WidgetID.DIALOG_PLAYER_GROUP_ID){
			this.hideWidgetOverlay(this.playerDialogId, this.playerOverlay);
		}
	}

	public Font getCustomFont()
	{
		FontManager fontManager = new FontManager(config);
		String configFont = config.fontOptions().toString();
		return fontManager.findFont(configFont);
	}

	private void processWidgetOverlay(int dialogId, Overlay overlay)
	{
		Widget dialog = this.client.getWidget(dialogId);

		if(dialog != null){
			this.activeWidget = dialog;
			this.activeOverlay = overlay;
			String text = dialog.getText();

			if(!text.isEmpty()){
				dialog.setHidden(true);
				overlayManager.add(overlay);
			} else {
				overlayManager.remove(overlay);
			}
		}
	}

	private void showWidgetOverlay(int dialogId, Overlay overlay)
	{
		Widget dialog = this.client.getWidget(dialogId);

		if(dialog != null){
			String text = dialog.getText();

			if(!text.isEmpty()){
				dialog.setHidden(true);
				overlayManager.add(overlay);
			}
		}
	}

	private void hideWidgetOverlay(int dialogId, Overlay overlay)
	{
		Widget dialog = this.client.getWidget(dialogId);

		if(dialog != null){
			dialog.setHidden(false);
			overlayManager.remove(overlay);
		}
	}


	@Provides
	DialogFontChangerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DialogFontChangerConfig.class);
	}
}
