package sh.okx.rankup.requirements.requirement;

import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.Requirement;

public class WorldRequirement extends Requirement {
  public WorldRequirement(RankupPlugin plugin) {
    super(plugin, "world");
  }

  protected WorldRequirement(Requirement clone) {
    super(clone);
  }

  @Override
  public boolean check(Player player) {
    String[] worlds = getValuesString();
    for (String world : worlds) {
      return player.getWorld().getName().equalsIgnoreCase(world);
    }
    return false;
  }

  @Override
  public double getTotal(Player player) {
    return 1;
  }

  @Override
  public Requirement clone() {
    return new WorldRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);
    String[] worlds = getValuesString();
    String worldsString = String.join(", ", worlds);

    if (remaining == 0) {
      return "<st><dark_gray>Estar en el mundo " + worldsString + ":</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Estar en el mundo " + worldsString + ":</#adadad> <#ff4444>Aún no estás en este mundo</#ff4444>";
    }
  }
}
