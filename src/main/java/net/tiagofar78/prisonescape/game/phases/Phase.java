package net.tiagofar78.prisonescape.game.phases;

public abstract class Phase {
	
	public abstract Phase next();
	
	public abstract boolean isClockStoped();

}
