package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.kits.Kit;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class PEPlayer {

    private static final int TICKS_PER_SECOND = 20;
    private static final String WANTED_TEAM_NAME = "Wanted";

    private static final int INVENTORY_SIZE = 4;
    private static final int[] INVENTORY_INDEXES = {0, 1, 2, 3};

    private String _name;
    private boolean _isOnline;
    private boolean _canMove;
    private List<Item> _inventory;
    private Kit _currentKit;
    private long _lastDrankPotionTime;
    private int _secondsLeft;

    private ScoreboardData _scoreboardData;
    private Clickable _openedMenu;

    public PEPlayer(String name) {
        _name = name;
        _isOnline = true;
        _inventory = createInventory();
        _lastDrankPotionTime = -1;
        _secondsLeft = 0;
        _canMove = true;

        _scoreboardData = createScoreboardData();
        setScoreboard(_scoreboardData.getScoreboard());
    }

    public boolean isPrisoner() {
        return false;
    }

    public boolean isGuard() {
        return false;
    }

    public boolean isWaiting() {
        return false;
    }

    private List<Item> createInventory() {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public String getName() {
        return _name;
    }

//  ########################################
//  #                  Kit                 #
//  ########################################

    public Kit getKit() {
        return _currentKit;
    }

    public void setKit(Kit kit) {
        _currentKit = kit;
        kit.give(getName());
    }

//	########################################
//	#                Online                #
//	########################################

    public boolean isOnline() {
        return _isOnline;
    }

    public void playerLeft() {
        _isOnline = false;
    }

    public void playerRejoined() {
        _isOnline = true;
    }

//	########################################
//	#               Movement               #
//	########################################

    public boolean canMove() {
        return _canMove;
    }

    public void restrictMovement() {
        _canMove = false;
    }

    public void allowMovement() {
        _canMove = true;
    }

//	#########################################
//	#               Inventory               #
//	#########################################

    public List<Item> getItemsInInventory() {
        return _inventory;
    }

    public Item getItemAt(int slot) {
        Item item = _currentKit.getItemAt(slot);
        if (item != null) {
            return item;
        }

        int index = convertToInventoryIndex(slot);
        if (index < 0 || index >= INVENTORY_SIZE) {
            return new NullItem();
        }

        return _inventory.get(index);
    }

    public int inventoryEmptySlots() {
        int count = 0;

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (_inventory.get(i) instanceof NullItem) {
                count++;
            }
        }

        return count;
    }

    /**
     * @return 0 if success<br>
     *         -1 if full inventory
     */
    public int giveItem(Item item) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (_inventory.get(i) instanceof NullItem) {
                setItem(i, item);
                return 0;
            }
        }

        return -1;
    }

    public void setItem(int index, Item item) {
        _inventory.set(index, item);

        setItemBukkit(index, item);
    }

    /**
     * @return 0 if success<br>
     *         -1 if cannot remove item
     */
    public int removeItem(int slot) {
        int index = convertToInventoryIndex(slot);
        if (index == -1) {
            return -1;
        }

        return removeItemIndex(index);
    }

    public int removeItemIndex(int index) {
        _inventory.set(index, new NullItem());
        setItemBukkit(index, new NullItem());
        return 0;
    }

    public void clearInventory() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            _inventory.set(i, new NullItem());
        }

        updateInventory();
    }

    public int convertToInventoryIndex(int slot) {
        for (int i = 0; i < INVENTORY_INDEXES.length; i++) {
            if (slot == INVENTORY_INDEXES[i]) {
                return i;
            }
        }

        return -1;
    }

    public boolean hasIllegalItems() {
        for (Item item : _inventory) {
            if (item.isIllegal()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasMetalItems() {
        for (Item item : _inventory) {
            if (item.isMetalic()) {
                return true;
            }
        }

        return false;
    }

    public void updateInventory() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            Item item = _inventory.get(i);
            if (item.isTool() && ((ToolItem) item).isBroken()) {
                setItem(i, new NullItem());
            }

            setItemBukkit(i, _inventory.get(i));
        }
    }

    public void updateView() {
        if (_openedMenu != null) {
            _openedMenu.updateInventory(getBukkitPlayer().getOpenInventory().getTopInventory(), this);
        }
    }

//  ########################################
//  #                 Menu                 #
//  ########################################

    public Clickable getOpenedMenu() {
        return _openedMenu;
    }

    public void openMenu(Clickable menu) {
        _openedMenu = menu;
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());
        openInventoryView(menu.toInventory(messages));
    }

    public void closeMenu() {
        closeMenu(false);
    }

    public void closeMenu(boolean closeView) {
        if (_openedMenu != null) {
            _openedMenu.close(this);
        }

        _openedMenu = null;

        if (closeView) {
            closeInventoryView();
        }
    }

//  ########################################
//  #             Energy Drink             #
//  ########################################

    public void giveEnergyDrinkEffect(int seconds, int amplifier) {
        long currentTime = System.currentTimeMillis();
        _secondsLeft = Math.max(0, _secondsLeft + seconds - convertToSeconds(currentTime - _lastDrankPotionTime));

        if (_lastDrankPotionTime == -1) {
            setEffect(PotionEffectType.SPEED, seconds, amplifier);
        } else {
            setEffect(PotionEffectType.SPEED, _secondsLeft + seconds, amplifier);
        }

        _lastDrankPotionTime = currentTime;
    }

    private int convertToSeconds(long miliseconds) {
        return (int) (miliseconds / 1000);
    }

//  ########################################
//  #              Scoreboard              #
//  ########################################

    public ScoreboardData getScoreboardData() {
        return _scoreboardData;
    }

    public ScoreboardData createScoreboardData() {
        ScoreboardData sbData = new ScoreboardData();

        String guardsTeamName = GameManager.getGame().getGuardsTeam().getName();
        registerTeam(sbData, guardsTeamName, ChatColor.BLUE);

        String prisonersTeamName = GameManager.getGame().getPrisonerTeam().getName();
        registerTeam(sbData, prisonersTeamName, ChatColor.GOLD);

        registerTeam(sbData, WANTED_TEAM_NAME, ChatColor.RED);

        return sbData;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        Player player = Bukkit.getPlayer(getName());
        if (player != null && player.isOnline()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void removeScoreboard() {
        setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    private void registerTeam(ScoreboardData sbData, String teamName, ChatColor color) {
        Team sbTeam = sbData.registerTeam(teamName);
        sbTeam.setColor(color);
    }

    public void updateScoreaboardTeams() {
        PETeam<Guard> guardsTeam = GameManager.getGame().getGuardsTeam();
        addScoreboardTeamMembers(guardsTeam);

        PETeam<Prisoner> prisonersTeam = GameManager.getGame().getPrisonerTeam();
        addScoreboardTeamMembers(prisonersTeam);
    }

    private void addScoreboardTeamMembers(PETeam<? extends PEPlayer> team) {
        Team sbTeam = getScoreboardData().getScoreboard().getTeam(team.getName());
        for (PEPlayer player : team.getMembers()) {
            sbTeam.addEntry(player.getName());
        }
    }

    public void addScoreboardWantedTeamMember(String prisonerTeamName, String playerName) {
        Scoreboard sb = getScoreboardData().getScoreboard();
        sb.getTeam(prisonerTeamName).removeEntry(playerName);
        sb.getTeam(WANTED_TEAM_NAME).addEntry(playerName);
    }

    public void removeScoreboardWantedTeamMember(String prisonerTeamName, String playerName) {
        Scoreboard sb = getScoreboardData().getScoreboard();
        sb.getTeam(WANTED_TEAM_NAME).removeEntry(playerName);
        sb.getTeam(prisonerTeamName).addEntry(playerName);
    }

//  ########################################
//  #                Bukkit                #
//  ########################################

    private Player getBukkitPlayer() {
        Player player = Bukkit.getPlayer(getName());

        if (player == null || !player.isOnline()) {
            return null;
        }

        return player;
    }

    public Location getLocation() {
        Player player = getBukkitPlayer();
        if (player == null) {
            return null;
        }

        return player.getLocation();
    }

    public void setGameMode(GameMode gameMode) {
        Player player = getBukkitPlayer();
        if (player != null) {
            player.setGameMode(gameMode);
        }
    }

    public void setBossBar(BossBar bossBar) {
        Player player = getBukkitPlayer();
        if (player != null) {
            bossBar.addPlayer(player);
        }
    }

    public void setEffect(PotionEffectType effect, int seconds, int level) {
        Player player = Bukkit.getPlayer(getName());
        if (player == null || !player.isOnline()) {
            return;
        }

        int ticksDuration = seconds * TICKS_PER_SECOND;

        player.addPotionEffect(new PotionEffect(effect, ticksDuration, level));
    }

    public void clearEffects() {
        Player player = Bukkit.getPlayer(getName());
        if (player == null || !player.isOnline()) {
            return;
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    private void setItemBukkit(int index, Item item) {
        Player bukkitPlayer = Bukkit.getPlayer(getName());
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());
        ItemStack bukkitItem = item.toItemStack(messages);
        bukkitPlayer.getInventory().setItem(INVENTORY_INDEXES[index], bukkitItem);
    }

    private void openInventoryView(Inventory inv) {
        Player player = getBukkitPlayer();
        if (player == null) {
            return;
        }

        player.openInventory(inv);
    }

    private void closeInventoryView() {
        Player player = getBukkitPlayer();
        if (player == null) {
            return;
        }

        player.closeInventory();
    }

    public void setCursorItem(ItemStack item) {
        Player player = getBukkitPlayer();
        if (player == null) {
            return;
        }

        player.setItemOnCursor(item);
    }

    public void playSound(Sound sound) {
        playSound(sound, 1);
    }

    public void playSound(Sound sound, int volume) {
        Player player = getBukkitPlayer();
        if (player == null) {
            return;
        }

        player.playSound(player, sound, volume, 0.5f);
    }

    public boolean isSneaking() {
        Player player = getBukkitPlayer();
        if (player == null) {
            return false;
        }

        return player.isSneaking();
    }

//  ########################################
//  #                 Util                 #
//  ########################################

    @Override
    public boolean equals(Object o) {
        return o instanceof PEPlayer && ((PEPlayer) o).getName().equals(this.getName());
    }

}
