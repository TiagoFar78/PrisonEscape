package net.tiagofar78.prisonescape.game;

import java.util.ArrayList;
import java.util.List;

public class PETeam<T extends PEPlayer> {

    private String _name;
    private List<T> _players;

    public PETeam(String name) {
        this._name = name;
        this._players = new ArrayList<>();
    }

    public String getName() {
        return _name;
    }

    public void addMember(T player) {
        _players.add(player);
    }

    public int getPlayerIndex(PEPlayer player) {
        for (int i = 0; i < _players.size(); i++) {
            if (_players.get(i).equals(player)) {
                return i;
            }
        }

        return -1;
    }

    public List<T> getMembers() {
        return _players;
    }

    public boolean isOnTeam(PEPlayer player) {
        return _players.contains(player);
    }

//    public int countArrestedPlayers() {
//        return (int) _players.stream().filter(player -> ).count();
//    }

    public int getSize() {
        return _players.size();
    }

    public T getMember(int index) {
        return _players.get(index);
    }

    public T getMember(String playerName) {
        for (T player : _players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

}
