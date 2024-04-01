package net.tiagofar78.prisonescape.game.prisonbuilding;

public class PrisonEscapeLocation {

    private int x;
    private int y;
    private int z;

    public PrisonEscapeLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public String createKey() {
        return "X" + this.x + "Y" + this.y + "Z" + this.z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PrisonEscapeLocation)) {
            return false;
        }

        PrisonEscapeLocation loc = (PrisonEscapeLocation) o;

        return this.x == loc.getX() && this.y == loc.getY() && this.z == loc.getZ();
    }

}
