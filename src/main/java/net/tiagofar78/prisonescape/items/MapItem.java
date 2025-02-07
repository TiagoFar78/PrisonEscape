package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MapManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursor.Type;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapItem extends Item {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.FILLED_MAP;
    }

    @Override
    public ItemStack toItemStack(PEGame game, PEPlayer player) {
        ItemStack map = super.toItemStack(game, player);

        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView view = Bukkit.getServer().createMap(PEResources.getWorld());

        for (MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }

        view.addRenderer(new PrisonRenderer(game));
        mapMeta.setMapView(view);
        map.setItemMeta(mapMeta);

        return map;
    }

    private class PrisonRenderer extends MapRenderer {

        private static final int LOWER_MAP_COORDINATE = -128;
        private static final int MAP_SIDE = 256;

        private PEGame _game;
        private MapManager _mapConfig;

        private PrisonRenderer(PEGame game) {
            _game = game;
            _mapConfig = MapManager.getInstance(_game.getMapName());
        }

        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            canvas.drawImage(0, 0, _mapConfig.getImage());

            MapCursorCollection cursors = new MapCursorCollection();
            Location playerLoc = player.getLocation().subtract(_game.getPrison().getReference());
            Location mapTopLeftCornerLoc = _mapConfig.getMapTopLeftCornerLocation();
            Location mapBottomRightCornerLoc = _mapConfig.getMapBottomRightCornerLocation();
            double xAjust =
                    (double) MAP_SIDE / (double) (mapTopLeftCornerLoc.getBlockX() - mapBottomRightCornerLoc.getBlockX());
            double yAjust =
                    (double) MAP_SIDE / (double) (mapTopLeftCornerLoc.getBlockZ() - mapBottomRightCornerLoc.getBlockZ());
            int x = (int) ((mapTopLeftCornerLoc.getBlockX() - playerLoc.getBlockX()) * xAjust + LOWER_MAP_COORDINATE);
            int z = (int) ((mapTopLeftCornerLoc.getBlockZ() - playerLoc.getBlockZ()) * yAjust + LOWER_MAP_COORDINATE);
            MapCursor cursor = cursors.addCursor(z, x, (byte) 0);
            cursor.setType(Type.WHITE_CIRCLE);
            canvas.setCursors(cursors);
        }

    }

}
