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
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget npcDialog = client.getWidget(ComponentID.DIALOG_NPC_TEXT);
        Widget npcDialogName = client.getWidget(ComponentID.DIALOG_NPC_NAME);

        if (npcDialog == null) {
            return null;
        }

        // Configure graphics settings
        configureGraphics(graphics);

        // Get dialog content and bounds
        String[] lines = npcDialog.getText().split("<br>");
        Rectangle dialogBounds = npcDialog.getBounds();

        // Handle NPC name visibility and get visibility state
        boolean npcNameHidden = handleNpcNameVisibility(npcDialogName, calculateTextMetrics(graphics, lines, dialogBounds, false), lines.length);

        // Recalculate text positioning with NPC name visibility adjustment
        TextMetrics textMetrics = calculateTextMetrics(graphics, lines, dialogBounds, npcNameHidden);

        // Render the text
        renderDialogText(graphics, lines, textMetrics);

        return null;
    }

    private void configureGraphics(Graphics2D graphics) {
        graphics.setFont(plugin.getCustomFont());
        graphics.setColor(Color.BLACK);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private static class TextMetrics {
        final int startY;
        final int lineHeight;
        final FontMetrics fontMetrics;
        final Rectangle bounds;
        final int totalTextHeight;

        TextMetrics(int startY, int lineHeight, FontMetrics fontMetrics, Rectangle bounds, int totalTextHeight) {
            this.startY = startY;
            this.lineHeight = lineHeight;
            this.fontMetrics = fontMetrics;
            this.bounds = bounds;
            this.totalTextHeight = totalTextHeight;
        }
    }

    private TextMetrics calculateTextMetrics(Graphics2D graphics, String[] lines, Rectangle dialogBounds, boolean npcNameHidden) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int defaultLineHeight = fontMetrics.getHeight();

        // Apply a custom line height multiplier (e.g., 0.8 for tighter lines, 1.2 for looser lines)
        float lineHeightMultiplier = plugin.getLineHeightMultiplier(); // Add this as a config option or constant
        int lineHeight = Math.round(defaultLineHeight * lineHeightMultiplier);

        // Calculate total height of all text lines
        int totalTextHeight = lineHeight * lines.length;

        // Adjust starting position if NPC name is hidden
        int verticalAdjustment = npcNameHidden ? -(lineHeight/2) : 0;

        // Calculate starting Y position to center all lines vertically in the bounds
        int startY = dialogBounds.y + (dialogBounds.height - totalTextHeight) / 2 + fontMetrics.getAscent() + verticalAdjustment;

        return new TextMetrics(startY, lineHeight, fontMetrics, dialogBounds, totalTextHeight);
    }

    private boolean handleNpcNameVisibility(Widget npcDialogName, TextMetrics metrics, int lineCount) {
        if (npcDialogName == null) {
            return false;
        }

        // Get the NPC name widget bounds
        Rectangle nameBounds = npcDialogName.getBounds();

        // Calculate if there would be an overlap
        boolean wouldOverlap = metrics.startY <= (nameBounds.y + nameBounds.height);

        // Hide name if config option is enabled and either:
        // 1. There are 4 lines of text, or
        // 2. The text would overlap with the name
        boolean shouldHide = config.hideNPCName() && (lineCount == 4 || wouldOverlap);

        npcDialogName.setHidden(shouldHide);
        return shouldHide;
    }

    private void renderDialogText(Graphics2D graphics, String[] lines, TextMetrics metrics) {
        int currentY = metrics.startY;

        for (String line : lines) {
            int centeredX = calculateCenteredX(line, metrics);
            graphics.drawString(line, centeredX, currentY);
            currentY += metrics.lineHeight;
        }
    }

    private int calculateCenteredX(String line, TextMetrics metrics) {
        int textWidth = metrics.fontMetrics.stringWidth(line);
        return metrics.bounds.x + (metrics.bounds.width / 2) - (textWidth / 2);
    }
}
