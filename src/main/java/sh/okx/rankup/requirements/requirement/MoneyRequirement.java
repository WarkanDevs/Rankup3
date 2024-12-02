package sh.okx.rankup.requirements.requirement;

import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class MoneyRequirement extends ProgressiveRequirement {
  public MoneyRequirement(RankupPlugin plugin, String name) {
    super(plugin, name);
  }

  protected MoneyRequirement(MoneyRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return plugin.getEconomy().getBalance(player);
  }

  @Override
  public Requirement clone() {
    return new MoneyRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>$" + ((int) getTotal(player)) + " de Dinero:</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad><#ffb000>$" + ((int) getTotal(player)) + "</#ffb000> de <#ffec00>Dinero</#ffec00>:</#adadad> <#ff4444>$" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}