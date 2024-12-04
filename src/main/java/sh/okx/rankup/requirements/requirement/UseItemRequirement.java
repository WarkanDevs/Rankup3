package sh.okx.rankup.requirements.requirement;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class UseItemRequirement extends ProgressiveRequirement {
  public UseItemRequirement(RankupPlugin plugin) {
    super(plugin, "use-item", true);
  }

  protected UseItemRequirement(UseItemRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    Material material = Material.matchMaterial(getSub());
    if (material == null) {
      throw new IllegalArgumentException("'" + getSub() + "' is not a valid item");
    }
    return player.getStatistic(Statistic.USE_ITEM, material);
  }

  @Override
  public Requirement clone() {
    return new UseItemRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    Material material = Material.matchMaterial(getSub());

    if (material == null) {
      throw new IllegalArgumentException("'" + getSub() + "' is not a valid item");
    }

    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Usar x" + getTotalDisplay(player) + " <lang:" + material.translationKey() + ">:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Usar <#ffb000>x" + getTotalDisplay(player) + "</#ffb000> <#ffec00><lang:" + material.translationKey() + "></#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
