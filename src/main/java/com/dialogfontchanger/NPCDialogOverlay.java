package com.dialogfontchanger;

import java.awt.*;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.runelite.api.Client;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class NPCDialogOverlay extends Overlay {

    private static final Logger log = LoggerFactory.getLogger(NPCDialogOverlay.class);
    private final Client client;
    private final DialogFontChangerConfig config;
    private DialogFontChangerPlugin plugin;

    private Widget npcDialogName;

    @Inject
    private NPCDialogOverlay(Client client, DialogFontChangerPlugin plugin, DialogFontChangerConfig config)
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        this.npcDialogName = client.getWidget(ComponentID.DIALOG_NPC_NAME);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Widget npcDialog = client.getWidget(ComponentID.DIALOG_NPC_TEXT);
        this.npcDialogName = client.getWidget(ComponentID.DIALOG_NPC_NAME);
        String text = npcDialog.getText();
//        log.info("hello you are here");
//        log.info(String.valueOf(npcDialog.getParentId()));
        Font font = plugin.getCustomFont();
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Rectangle textLocation = npcDialog.getBounds();
//        int textX = textLocation.x + textLocation.width / 2;
//        int textY = textLocation.y + textLocation.height / 2;
        String[] lines = text.split("<br>");

        // Get font metrics for measuring text width
        FontMetrics fontMetrics = graphics.getFontMetrics();

        // Draw each line
        int lineHeight = fontMetrics.getHeight();

//        log.info("lineHeight: " + lineHeight);

        int defaultY = textLocation.y;

        if(this.npcDialogName != null){
            int nameBoxHeight = this.npcDialogName.getBounds().height;
            int nameHeight = this.npcDialogName.getHeight();
            int nameLineHeight = this.npcDialogName.getLineHeight();
        }


        // Starting y-coordinate
        int y = textLocation.y + ((textLocation.height / 2) + (lineHeight / 2) - ((lines.length - 1) * lineHeight ));

        // ensure our calculation doesn't put the text off the top of the dialog box
        if(y < textLocation.y){
            y = defaultY;
        }

        // only hide NPC name if it will overlap with the custom dialog text
        if(this.config.hideNPCName() && (lines.length == 4 || y == defaultY || this.npcDialogName.getBounds().intersects(textLocation))){
            if(this.npcDialogName != null){
                this.npcDialogName.setHidden(true);
            }
        }

        for (String line : lines) {
            // Calculate x-coordinate to center the text
            int x = (textLocation.x + (textLocation.width / 2) - (fontMetrics.stringWidth(line) / 2));

            // Draw the line
            graphics.drawString(line, x, y);

            // Move to the next line
            y += lineHeight;
        }

        //        graphics.drawString(text, textX, textY);
        return null;
    }


}
