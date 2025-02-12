package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.Region;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.kits.Kit;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public abstract class PEPlayer {

    private static final int REGION_LINE_INDEX = 0;
    private static final int INTERACT_EVENT_COOLDOWN_TICKS = 5;
    private static final int TICKS_PER_SECOND = 20;
    private static final int FADE = (int) (1 * TICKS_PER_SECOND);
    private static final int STAY = (int) (3.5 * TICKS_PER_SECOND);
    private static final double CENTER_OF_BLOCK = 0.5;
    private static final String WANTED_TEAM_NAME = "Wanted";

    private static final int INVENTORY_SIZE = 4;
    private static final int[] INVENTORY_INDEXES = {0, 1, 2, 3};

    private PEGame _game;
    private String _name;
    private boolean _isOnline;
    private boolean _canMove;
    private List<Item> _inventory;
    private Kit _currentKit;
    private long _lastDrankPotionTime;
    private int _secondsLeft;
    private Hashtable<String, Long> _eventCooldown;
    private Region _currentRegion;
    private BossBar _bossBar;

    private ScoreboardData _scoreboardData;
    private Clickable _openedMenu;

    public PEPlayer(PEGame game, String name) {
        _game = game;
        _name = name;
        _isOnline = true;
        _inventory = createInventory();
        _lastDrankPotionTime = -1;
        _secondsLeft = 0;
        _eventCooldown = new Hashtable<>();
        _canMove = true;
        _currentRegion = null;
        _bossBar = Bukkit.createBossBar(null, BarColor.YELLOW, BarStyle.SOLID);

        _scoreboardData = createScoreboardData();
        setScoreboard(_scoreboardData.getScoreboard());
    }

    public PEGame getGame() {
        return _game;
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

    public BossBar getBossBar() {
        return _bossBar;
    }

//  ########################################
//  #                  Kit                 #
//  ########################################

    public Kit getKit() {
        return _currentKit;
    }

    public void setKit(Kit kit) {
        _currentKit = kit;
        kit.give(getGame(), this);
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

    public void updateViewTitle(String title) {
        if (_openedMenu != null) {
            getBukkitPlayer().getOpenInventory().setTitle(title);
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
        openInventoryView(menu.toInventory(getGame(), this));
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
//  #                Region                #
//  ########################################

    public Region getRegion() {
        return _currentRegion;
    }

    public void setRegion(Region region) {
        _currentRegion = region;
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

//  #########################################
//  #               Cooldowns               #
//  #########################################

    public void executedEvent(String eventName) {
        _eventCooldown.put(eventName, System.currentTimeMillis());
    }

    public boolean isOnCooldown(String eventName) {
        Long lastExecutionTime = _eventCooldown.get(eventName);
        if (lastExecutionTime == null) {
            return false;
        }

        long timeSinceLastExecution = System.currentTimeMillis() - lastExecutionTime;
        return milisecondsToTicks(timeSinceLastExecution) < INTERACT_EVENT_COOLDOWN_TICKS;
    }

    private int milisecondsToTicks(long miliseconds) {
        return (int) (miliseconds / (1000 / TICKS_PER_SECOND));
    }

//  ########################################
//  #              Scoreboard              #
//  ########################################

    public ScoreboardData getScoreboardData() {
        return _scoreboardData;
    }

    public ScoreboardData createScoreboardData() {
        ScoreboardData sbData = new ScoreboardData();

        registerTeam(sbData, PEGame.WAITING_TEAM_NAME, ChatColor.GRAY);
        registerTeam(sbData, PEGame.GUARDS_TEAM_NAME, ChatColor.BLUE);
        registerTeam(sbData, PEGame.PRISONERS_TEAM_NAME, ChatColor.GOLD);
        registerTeam(sbData, WANTED_TEAM_NAME, ChatColor.RED);

        return sbData;
    }

    protected List<String> buildBaseSideBar(int emptyLines, List<Integer> linesIndexes, List<String> linesContents) {
        List<String> baseSideBar = new ArrayList<>();

        for (int i = 0; i < emptyLines; i++) {
            baseSideBar.add("§" + i);
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());

        String regionLine = messages.getSideBarRegionLine(messages.getSideBarWaitingRegionName());
        baseSideBar.add(REGION_LINE_INDEX, regionLine);

        for (int i = 0; i < linesIndexes.size(); i++) {
            baseSideBar.add(linesIndexes.get(i), linesContents.get(i));
        }

        baseSideBar.add(messages.getSideBarLastLine());

        return baseSideBar;
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
        PETeam<Guard> guardsTeam = _game.getGuardsTeam();
        addScoreboardTeamMembers(guardsTeam);

        PETeam<Prisoner> prisonersTeam = _game.getPrisonerTeam();
        for (Prisoner prisoner : prisonersTeam.getMembers()) {
            if (prisoner.isWanted()) {
                addScoreboardTeamMember(prisoner.getName(), WANTED_TEAM_NAME);
            } else {
                addScoreboardTeamMember(prisoner.getName(), prisonersTeam.getName());
            }
        }
    }

    private void addScoreboardTeamMembers(PETeam<? extends PEPlayer> team) {
        Team sbTeam = getScoreboardData().getScoreboard().getTeam(team.getName());
        for (PEPlayer player : team.getMembers()) {
            sbTeam.addEntry(player.getName());
        }
    }

    public void addScoreboardTeamMember(String playerName, String currentTeamName) {
        Scoreboard sb = getScoreboardData().getScoreboard();
        Team previousTeam = sb.getEntryTeam(playerName);
        if (previousTeam != null) {
            previousTeam.removeEntry(playerName);
        }

        sb.getTeam(currentTeamName).addEntry(playerName);
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

    public void updateRegionLine(PrisonBuilding prison, DayPeriod dayPeriod) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());

        String regionName = _currentRegion == null ? null : _currentRegion.getName();
        if (regionName == null) {
            regionName = messages.getSideBarDefaultRegionName();
        }

        String line = prison.isRestrictedArea(_currentRegion, dayPeriod)
                ? messages.getSideBarRestrictedRegionLine(regionName)
                : messages.getSideBarRegionLine(regionName);
        getScoreboardData().updateLine(REGION_LINE_INDEX, line);
    }

//  ########################################
//  #                Bukkit                #
//  ########################################

    public Player getBukkitPlayer() {
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

    public void updateBossBar() {
        Player player = getBukkitPlayer();
        if (player != null) {
            _bossBar.addPlayer(player);
        }
    }

    public void removeBossBar() {
        Player player = getBukkitPlayer();
        if (player != null) {
            _bossBar.removePlayer(player);
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

        ItemStack bukkitItem = item.toItemStack(getGame(), this);
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

        player.playSound(player, sound, volume, 10f);
    }

    public boolean isSneaking() {
        Player player = getBukkitPlayer();
        if (player == null) {
            return false;
        }

        return player.isSneaking();
    }

    public void sendChatMessage(String message) {
        Player player = getBukkitPlayer();
        if (player == null) {
            return;
        }

        player.sendMessage(message);
    }

    public void sendChatMessage(String[] message) {
        Player bukkitPlayer = getBukkitPlayer();
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.sendMessage(message);
    }

    public void sendChatMessage(List<String> message) {
        sendChatMessage(message.toArray(new String[0]));
    }

    public void sendTitleMessage(String titleMessage, String subtitleMessage) {
        Player bukkitPlayer = getBukkitPlayer();
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.sendTitle(titleMessage, subtitleMessage, FADE, STAY, FADE);
    }

    public void teleport(Location loc) {
        Player bukkitPlayer = getBukkitPlayer();
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.teleport(loc.clone().add(CENTER_OF_BLOCK, 0, CENTER_OF_BLOCK));
    }


//  ########################################
//  #                 Util                 #
//  ########################################

    @Override
    public boolean equals(Object o) {
        return o instanceof PEPlayer && ((PEPlayer) o).getName().equals(this.getName());
    }

}
