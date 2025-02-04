package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MapManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

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

        private BufferedImage _image;

        private PrisonRenderer(PEGame game) {
            _image = MapManager.getInstance(game.getMapName()).getImage();
        }

        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            canvas.drawImage(0, 0, _image);
        }

    }

}
