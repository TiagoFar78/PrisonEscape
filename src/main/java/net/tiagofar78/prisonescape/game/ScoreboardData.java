package net.tiagofar78.prisonescape.game;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class ScoreboardData {

    private static final String SIDE_BAR_NAME = "SideBar";

    private Scoreboard _sb;
    private List<String> _sideBarLines;

    public ScoreboardData() {
        _sb = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public ScoreboardData(Scoreboard scoreboard) {
        _sb = scoreboard;
    }

    public Scoreboard getScoreboard() {
        return _sb;
    }

    public void createSideBar(String displayName, List<String> baseSideBar) {
        _sideBarLines = baseSideBar;

        Objective obj = _sb.registerNewObjective(SIDE_BAR_NAME, Criteria.DUMMY, displayName);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int size = _sideBarLines.size();
        for (int i = 0; i < size; i++) {
            obj.getScore(_sideBarLines.get(i)).setScore(size - i);
            setLine(obj, i, size);
        }
    }

    public void addLine(int index, String line) {
        _sideBarLines.add(index, line);

        int size = _sideBarLines.size();

        Objective obj = _sb.getObjective(SIDE_BAR_NAME);
        setLine(obj, index, size);

        for (int i = index + 1; i < size; i++) {
            _sb.resetScores(_sideBarLines.get(i));
            setLine(obj, i, size);
        }

    }

    public void updateLine(int index, String newLine) {
        String previousLine = _sideBarLines.get(index);

        _sb.resetScores(previousLine);

        _sideBarLines.set(index, newLine);
        setLine(_sb.getObjective(SIDE_BAR_NAME), index, _sideBarLines.size());
    }

    private void setLine(Objective obj, int i, int size) {
        obj.getScore(_sideBarLines.get(i)).setScore(size - i);
    }

}
