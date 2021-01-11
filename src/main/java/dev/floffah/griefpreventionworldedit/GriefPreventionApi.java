package dev.floffah.griefpreventionworldedit;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GriefPreventionApi {
    public static boolean usePlugin;
    public static GriefPrevention ess;

    static {
        GriefPreventionApi.usePlugin = false;
    }

    public static void init() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        final Plugin essPlugin = pluginManager.getPlugin("GriefPrevention");
        if(essPlugin != null && essPlugin.isEnabled()) {
            GriefPreventionApi.ess = (GriefPrevention) essPlugin;
            GriefPreventionApi.usePlugin = true;
        }
    }

    public static boolean canWorldEdit(final Player p) {
        LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(p.getName());
        if(session != null) {
            try {
                World w = Bukkit.getWorld(session.getSelectionWorld().getName());
                Region r = session.getSelection(session.getSelectionWorld());

                return canWorldEdit(p, convert(w, r.getMinimumPoint())) && canWorldEdit(p, convert(w, r.getMaximumPoint()));
            } catch(IncompleteRegionException ignored) {
            }
        }

        return true;
    }

    private static Location convert(World w, BlockVector3 vec) {
        return new Location(w, vec.getX(), vec.getY(), vec.getZ());
    }

    private static boolean canWorldEdit(Player p, Location l) {
        final Claim claim = GriefPreventionApi.ess.dataStore.getClaimAt(l, true, null);
        return claim != null && claim.getOwnerName().toLowerCase().equals(p.getName().toLowerCase());
    }
}