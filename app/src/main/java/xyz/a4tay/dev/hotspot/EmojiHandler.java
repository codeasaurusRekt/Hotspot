package xyz.a4tay.dev.hotspot;

public class EmojiHandler
    {
    public String getEmojiByUnicode(int unicode)
        {
        return new String(Character.toChars(unicode));
        }
    }
