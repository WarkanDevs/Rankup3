package sh.okx.rankup.requirements.requirement;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class PlayerKillsRequirement extends ProgressiveRequirement {
  public PlayerKillsRequirement(RankupPlugin plugin) {
    super(plugin, "player-kills");
  }

  protected PlayerKillsRequirement(Requirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return player.getStatistic(Statistic.PLAYER_KILLS);
  }

  @Override
  public Requirement clone() {
    return new PlayerKillsRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Asesinar " + getTotalDisplay(player) + " jugadores:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Asesinar <#ffb000>" + getTotalDisplay(player) + "</#ffb000> <#ffec00>Jugadores</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
