package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;

import java.util.Random;

public class NotePartItem extends Item {

    private static final int NOTES_AMOUNT = 4;

    private int _number;

    public NotePartItem() {
        _number = new Random().nextInt(NOTES_AMOUNT) + 1;
    }

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getDisplayName(MessageLanguageManager messages) {
        return super.getDisplayName(messages).replace("{NUMBER}", Integer.toString(_number));
    }

}
