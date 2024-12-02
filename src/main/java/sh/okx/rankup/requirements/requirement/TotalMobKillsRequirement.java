package sh.okx.rankup.requirements.requirement;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class TotalMobKillsRequirement extends ProgressiveRequirement {
  public TotalMobKillsRequirement(RankupPlugin plugin) {
    super(plugin, "total-mob-kills");
  }

  private TotalMobKillsRequirement(Requirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return player.getStatistic(Statistic.MOB_KILLS);
  }

  @Override
  public Requirement clone() {
    return new TotalMobKillsRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Asesinar x" + ((int) getTotal(player)) + " Entidades:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Asesinar <#ffb000>x" + ((int) getTotal(player)) + "</#ffb000> <#ffec00>Entidades</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
