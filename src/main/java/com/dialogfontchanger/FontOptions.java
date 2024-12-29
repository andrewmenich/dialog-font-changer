package com.dialogfontchanger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FontOptions {
    FONDAMENTO("Fondamento"),
    GLEGOO("Glegoo"),
    LATO("Lato"),
    MORRISROMAN("Morris Roman"),
    PT_SERIF("PT Serif"),
    ROBOTO("Roboto"),
    RUNESCAPE("Runescape"),
    RUNESCAPE_BOLD("Runescape Bold"),
    RUNESCAPE_CHAT("Runescape Chat"),
    RUNESCAPE_SMOOTH_CHAT("Runescape Smooth Chat"),
    RUNESCAPE_SMOOTH_LARGE("Runescape Smooth Large"),
    SANCHEZ("Sanchez"),
    UBUNTULIGHT("Ubuntu Light");


    private final String type;

    @Override
    public String toString()
    {
        return type;
    }
}
