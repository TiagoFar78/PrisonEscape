package net.tiagofar78.prisonescape.items;

import org.bukkit.event.Event;

public abstract class FunctionalItem extends Item {

    @Override
    public boolean isFunctional() {
        return true;
    }

    public abstract void use(Event event);
}
