package sh.okx.rankup.requirements.requirement;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class FishCaughtRequirement extends ProgressiveRequirement {
  public FishCaughtRequirement(RankupPlugin plugin) {
    super(plugin, "fish-caught");
  }

  protected FishCaughtRequirement(FishCaughtRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return player.getStatistic(Statistic.FISH_CAUGHT);
  }

  @Override
  public Requirement clone() {
    return new FishCaughtRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Pescar x" + getTotalDisplay(player) + " Veces:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Pescar <#ffb000>x" + getTotalDisplay(player) + "</#ffb000> <#ffec00>Veces</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
