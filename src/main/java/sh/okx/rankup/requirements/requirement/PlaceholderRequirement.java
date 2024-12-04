package sh.okx.rankup.requirements.requirement;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

import java.util.Objects;

public class PlaceholderRequirement extends ProgressiveRequirement {

  public static final double DELTA = 0.00001D;

  public PlaceholderRequirement(RankupPlugin plugin) {
    super(plugin, "placeholder");
  }

  public PlaceholderRequirement(PlaceholderRequirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    String[] parts = getParts(player);
    String parsed = parts[0];
    String value = parts[2];

    // string operations
    switch (parts[1]) {
      case "=":
        return parsed.equals(value) ? 1 : 0;
      case "!=":
        return parsed.equals(value) ? 0 : 1;
    }

    // numeric operations
    double p = Double.parseDouble(parsed.replace(",", ""));
    double v = Double.parseDouble(value.replace(",", ""));
    switch (parts[1]) {
      case ">":
        return p > v ? 1 : 0;
      case ">=":
        return Math.min(p, v);
      case "<":
        return p < v ? 1 : 0;
      case "<=":
        return p <= v ? 1 : 0;
      case "==":
        return p == v ? 1 : 0;
    }
    throw new IllegalArgumentException("Invalid operation: " + parts[1]);
  }

  @Override
  public double getTotal(Player player) {
    String[] parts = getParts(player);

    switch (parts[1]) {
      case ">=":
        return Double.parseDouble(parts[2]);
      default:
        return 1;
    }
  }

  private String[] getParts(Player player) {
    String[] parts = getValueString().split(" ");
    if (parts.length < 3) {
      throw new IllegalArgumentException(
          "Placeholder requirements must be in the form %placeholder% <operation> string");
    }
    String parsed = PlaceholderAPI.setPlaceholders(player, parts[0]);
    if (!PlaceholderAPI.containsPlaceholders(parts[0]) || parsed.equals(parts[0])) {
      throw new IllegalArgumentException(parts[0] + " is not a PlaceholderAPI placeholder!");
    }
    parts[0] = parsed;
    return parts;
  }

  @Override
  public String getFullName() {
    String[] parts = getValueString().split(" ");
    return name + "#" + parts[0].replace("%", "");
  }

  @Override
  public boolean check(Player player) {
    return getRemaining(player) <= 0;
  }

  @Override
  public Requirement clone() {
    return new PlaceholderRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    String[] parts = getValueString().split(" ");
    String placeholder = parts[0];
    var remaining = getRemaining(player);

    var configuration = plugin.getConfig();
    String customFormat = placeholder;
    if (configuration.contains("placeholders-formats")) {
        var formats = configuration.getConfigurationSection("placeholders-formats");
        if (Objects.requireNonNull(formats).contains(placeholder.replace("%", ""))) {
          customFormat = Objects.requireNonNull(formats.getString(placeholder.replace("%", "")));
        }
    }

    if (customFormat.contains("{{total}}")) {
      customFormat = customFormat.replace("{{total}}", String.valueOf(getTotalDisplay(player)));
    }
    if (customFormat.contains("{{remaining}}")) {
      customFormat = customFormat.replace("{{remaining}}", String.valueOf((int) remaining));
    }
    if (customFormat.contains("{{progress}}")) {
      customFormat = customFormat.replace("{{progress}}", String.valueOf((int) getProgress(player)));
    }

    if (remaining == 0) {
      var formatWithoutColor = customFormat.replaceAll("<[^>]*>", "");
      return "<st><dark_gray>" + formatWithoutColor + ":</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>" + customFormat + ":</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
