package sh.okx.rankup.requirements.requirement;

import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class XpLevelRequirement extends ProgressiveRequirement {
  public XpLevelRequirement(RankupPlugin plugin, String name) {
    super(plugin, name);
  }

  protected XpLevelRequirement(XpLevelRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return player.getLevel();
  }

  @Override
  public Requirement clone() {
    return new XpLevelRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>" + ((int) getTotal(player)) + " Niveles de XP:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad><#ffb000>" + ((int) getTotal(player)) + "</#ffb000> <#ffec00>Niveles de XP</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" niveles restantes</#ff4444>";
    }
  }
}