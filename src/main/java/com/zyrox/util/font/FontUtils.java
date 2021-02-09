package com.zyrox.util.font;


import java.util.ArrayList;
import java.util.List;

public class FontUtils {

    public enum FontSize {
        SMALL, REGULAR, BOLD, FANCY
    }

    private static final GameFont[] FONTS = new GameFont[4];

    public static void initialize() {
        FONTS[0] = new GameFont(false, "p11_full", "./data/fonts/");
        FONTS[1] = new GameFont(false, "p12_full", "./data/fonts/");
        FONTS[2] = new GameFont(false, "b12_full", "./data/fonts/");
        FONTS[3] = new GameFont(true, "q8_full", "./data/fonts/");
    }

    public static GameFont getFont(FontSize size) {
        return FONTS[size.ordinal()];
    }

    public static int getWidth(FontSize size, String string) {
        return getFont(size).getWidth(string);
    }

    public static String[] wrapText(FontSize size, String text, int maxWidth) {
        return wrapText(getFont(size), text, maxWidth);
    }

    public static String wrapLine(FontSize size, String text, int maxWidth) {
        return wrapLine(getFont(size), text, maxWidth);
    }

    public static String ellipsis(FontSize size, String text, int max) {
        String ellipsis = "...";

        char[] chars = text.toCharArray();

        StringBuilder result = new StringBuilder();

        for (int index = 0; index < chars.length; index++) {
            StringBuilder augmented = new StringBuilder(result).append(chars[index]).append(ellipsis);

            int width = getWidth(size, augmented.toString());

            if (width >= max) {
                break;
            }
            result.append(chars[index]);
        }
        return result.append(ellipsis).toString();
    }

    public static String[] wrapText(GameFont font, String text, int maxWidth) {
        if (text == null) {
            return new String[]{};
        }
        if (maxWidth <= 0) {
            return new String[]{
                    text
            };
        }

        if (font.getWidth(text) <= maxWidth && !text.contains("\\n")) {
            return new String[]{
                    text
            };
        }
        if (text.contains("\\n")) {
            final String[] splits = text.split("\\\\n");
            List<String> lines = new ArrayList<String>();
            for (int split = 0; split < splits.length; split++) {
                String[] lineArray = wrapText(font, splits[split], maxWidth);
                for (String line : lineArray) {
                    if (line == null) {
                        continue;
                    }
                    lines.add(line);
                }
            }
            return lines.toArray(new String[lines.size()]);
        }
        char[] chars = text.toCharArray();
        List<String> lines = new ArrayList<String>();
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);
            if (chars[i] == ' ') {
                if (font.getWidth(line.toString() + word.toString()) > maxWidth) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }
                line.append(word);
                word.delete(0, word.length());
            }
        }
        if (font.getWidth(word.toString()) > 0) {
            if (font.getWidth(line.toString() + word.toString()) > maxWidth) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }
        if (font.getWidth(line.toString()) > 0) {
            lines.add(line.toString());
        }
        return lines.toArray(new String[lines.size()]);
    }

    public static String wrapLine(GameFont font, String text, int maxWidth) {
        if (text == null) {
            return "";
        }
        if (maxWidth <= 0) {
            return text;
        }

        if (font.getWidth(text) <= maxWidth && !text.contains("\\n")) {
            return text;
        }

        char[] chars = text.toCharArray();

        StringBuilder lines = new StringBuilder();

        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);
            if (chars[i] == ' ') {
                if (font.getWidth(line.toString() + word.toString()) > maxWidth) {
                    lines.append(line.toString()+"\\n");
                    line.delete(0, line.length());
                }
                line.append(word);
                word.delete(0, word.length());
            }
        }
        if (font.getWidth(word.toString()) > 0) {
            if (font.getWidth(line.toString() + word.toString()) > maxWidth) {
                lines.append(line.toString()+"\\n");
                line.delete(0, line.length());
            }
            line.append(word);
        }
        if (font.getWidth(line.toString()) > 0) {
            lines.append(line.toString()+"\\n");
        }
        return lines.toString();
    }


}