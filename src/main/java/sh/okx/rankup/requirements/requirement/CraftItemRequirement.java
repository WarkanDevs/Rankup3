package sh.okx.rankup.requirements.requirement;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class CraftItemRequirement extends ProgressiveRequirement {
  public CraftItemRequirement(RankupPlugin plugin) {
    super(plugin, "craft-item", true);
  }

  protected CraftItemRequirement(CraftItemRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    Material material = Material.matchMaterial(getSub());
    if (material == null) {
      throw new IllegalArgumentException("'" + getSub() + "' is not a valid item");
    }
    return player.getStatistic(Statistic.CRAFT_ITEM, material);
  }

  @Override
  public Requirement clone() {
    return new CraftItemRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    Material material = Material.matchMaterial(getSub());

    if (material == null) {
      throw new IllegalArgumentException("'" + getSub() + "' is not a valid item");
    }

    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Fabricar x" + getTotalDisplay(player) + " <lang:" + material.translationKey() + ">:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Fabricar <#ffb000>x" + getTotalDisplay(player) + "</#ffb000> <#ffec00><lang:" + material.translationKey() + "></#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
