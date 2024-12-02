package sh.okx.rankup.requirements.requirement;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class PlaytimeMinutesRequirement extends ProgressiveRequirement {
  private static final int TICKS_PER_MINUTE = 20 * 60;
  private Statistic playOneTick;

  public PlaytimeMinutesRequirement(RankupPlugin plugin) {
    super(plugin, "playtime-minutes");
    try {
      playOneTick = Statistic.valueOf("PLAY_ONE_MINUTE");
    } catch (IllegalArgumentException e) {
      // statistic was changed in 1.13.
      playOneTick = Statistic.valueOf("PLAY_ONE_TICK");
    }
  }

  protected PlaytimeMinutesRequirement(PlaytimeMinutesRequirement clone) {
    super(clone);
    this.playOneTick = clone.playOneTick;
  }

  @Override
  public double getProgress(Player player) {
    return player.getStatistic(playOneTick) / TICKS_PER_MINUTE;
  }

  @Override
  public Requirement clone() {
    return new PlaytimeMinutesRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Jugar " + ((int) getTotal(player)) + " minutos:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Jugar <#ffb000>" + ((int) getTotal(player)) + "</#ffb000> <#ffec00>Minutos</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" minutos restantes</#ff4444>";
    }
  }
}
