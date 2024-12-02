package sh.okx.rankup.requirements.requirement;

import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.Requirement;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PermissionRequirement extends Requirement {
  public PermissionRequirement(RankupPlugin plugin) {
    super(plugin, "permission");
  }

  protected PermissionRequirement(Requirement clone) {
    super(clone);
  }

  @Override
  public boolean check(Player player) {
    for (String permission : getValuesString()) {
      if (player.hasPermission(permission)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Requirement clone() {
    return new PermissionRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

      String fullStr;
      if (remaining == 0) {
          fullStr = Arrays.stream(getValuesString()).map(permission -> "<st><dark_gray>Tener el permiso " + permission + ":</dark_gray></st> <#80ff00>¡Completado!</#80ff00>\n").collect(Collectors.joining());
      } else {
          fullStr = Arrays.stream(getValuesString()).map(permission -> "<st><#adadad>Tener el permiso <#ffec00>" + permission + "</#ffec00>:</#adadad></st> <#ff4444>Aun no tienes el permiso</#ff4444>\n").collect(Collectors.joining());
      }
      return fullStr.substring(0, fullStr.length() - 1);
  }
}
