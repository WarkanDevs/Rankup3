package sh.okx.rankup.requirements.requirement;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class BreedAnimalsRequirement extends ProgressiveRequirement {
  public BreedAnimalsRequirement(RankupPlugin plugin) {
    super(plugin, "breed-animals");
  }

  protected BreedAnimalsRequirement(BreedAnimalsRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return player.getStatistic(Statistic.ANIMALS_BRED);
  }

  @Override
  public Requirement clone() {
    return new BreedAnimalsRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Reproducir x" + getTotalDisplay(player) + " Animales:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Reproducir <#ffb000>x" + getTotalDisplay(player) + "</#ffb000> <#ffec00>Animales</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
