package com.dialogfontchanger;

import net.runelite.api.Client;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class PlayerDialogOverlay extends Overlay {

    private final Client client;
    private final DialogFontChangerConfig config;
    private final DialogFontChangerPlugin plugin;

    @Inject
    private PlayerDialogOverlay(Client client, DialogFontChangerPlugin plugin, DialogFontChangerConfig config)
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Widget npcDialog = client.getWidget(ComponentID.DIALOG_PLAYER_TEXT);
        String text = npcDialog.getText();

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

        // Starting y-coordinate
        int y = textLocation.y - lineHeight;

        // pad the top of the text on shorter lines
        if (lines.length < 3){
            y = y + lineHeight + textLocation.height / 2;
        } else {
            y = y  + textLocation.height / 2;
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
