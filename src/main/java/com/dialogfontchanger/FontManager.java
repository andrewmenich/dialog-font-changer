/*
 * Copyright (c) 2017, Tyler <https://github.com/tylerthardy>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.dialogfontchanger;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.swing.text.StyleContext;

public class FontManager
{
    @Inject
    private DialogFontChangerConfig config;

    private final Map<String, FontData> fontDataMap = new HashMap<>();


    public FontManager(DialogFontChangerConfig config)
    {
        this.config = config;

        fontDataMap.put("Roboto", new FontData("roboto", 0));
        fontDataMap.put("Fondamento", new FontData("fondamento", 2));
        fontDataMap.put("Glegoo", new FontData("glegoo", -2));
        fontDataMap.put("Lato", new FontData("lato", 0));
        fontDataMap.put("Morris Roman", new FontData("morris_roman", 2));
        fontDataMap.put("PT Serif", new FontData("pt_serif", 0));
        fontDataMap.put("Runescape", new FontData("runescape", 1));
        fontDataMap.put("Runescape Bold", new FontData("runescape_bold", 1));
        fontDataMap.put("Runescape Chat", new FontData("runescape_chat", 2));
        fontDataMap.put("Runescape Smooth Chat", new FontData("runescape_smooth_chat", 3));
        fontDataMap.put("Runescape Smooth Large", new FontData("runescape_smooth_large", 4));
        fontDataMap.put("Sanchez", new FontData("sanchez", 0));
        fontDataMap.put("Ubuntu Light", new FontData("ubuntu_light", 0));
    }
    public Font findFont(String fontName)
    {
        FontData fontData = fontDataMap.get(fontName);

        if (fontData == null)
        {
            return null;
        }

        return fontData.getFont();
    }

    public int getAdjustedFontSize(int fontSizeAdjustment)
    {
        int configuredFontSize = config.fontSize();
        return configuredFontSize + fontSizeAdjustment;
    }

    private static Font registerFont(String fontName, int fontSize)
    {
        String fontPath = "/" + fontName + ".ttf";

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try (InputStream localFont = DialogFontChangerPlugin.class.getResourceAsStream(fontPath))
        {
            // custom font
            Font font = Font.createFont(Font.TRUETYPE_FONT, localFont)
                    .deriveFont(Font.PLAIN, fontSize);
            ge.registerFont(font);

            StyleContext styleContext = StyleContext.getDefaultStyleContext();
            Font customFont = styleContext.getFont(font.getName(), Font.PLAIN, fontSize);
            ge.registerFont(customFont);

            return customFont;

        }
        catch (FontFormatException ex)
        {
            throw new RuntimeException("Font loaded, but format incorrect.", ex);
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Font file not found.", ex);
        }
    }

    class FontData {
        private String fontName;
        private final int fontModifier;
        private final int fontSize;
        private final Font font;

        public FontData(String fontName, int fontModifier) {
            this.fontName = fontName;
            this.fontModifier = fontModifier;

            this.fontSize = getAdjustedFontSize(fontModifier);
            this.font = registerFont(fontName, this.fontSize);
        }

        public Font getFont() {
            return font;
        }

        public int getFontModifier() {
            return fontModifier;
        }

        public int getFontSize() {
            return fontSize;
        }
    }
}
