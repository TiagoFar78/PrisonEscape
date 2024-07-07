package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapItem extends Item {

    private static final String IMAGE_NAME = "prison_map.png";
    private static final BufferedImage IMAGE = createImage();

    private static BufferedImage createImage() {
        File imageFile = new File(PrisonEscape.getPrisonEscape().getDataFolder(), IMAGE_NAME);
        if (!imageFile.exists()) {
            throw new IllegalArgumentException(
                    "Could not find prison map image. Add a image named " + IMAGE_NAME + " to the plugin folder."
            );
        }

        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

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
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack map = super.toItemStack(messages);

        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView view = Bukkit.getServer().createMap(PEResources.getWorld());

        for (MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }

        view.addRenderer(new PrisonRenderer());
        mapMeta.setMapView(view);
        map.setItemMeta(mapMeta);

        return map;
    }

    private class PrisonRenderer extends MapRenderer {

        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            canvas.drawImage(0, 0, IMAGE);
        }

    }

}
