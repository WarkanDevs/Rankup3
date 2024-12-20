package sh.okx.rankup.requirements;

import lombok.Getter;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;

import javax.annotation.Nullable;

public abstract class Requirement implements Cloneable {
  protected final RankupPlugin plugin;
  @Getter
  protected final String name;
  private String value;
  private String toDisplay;
  @Getter
  private String sub;
  private boolean subRequirement;

  public Requirement(RankupPlugin plugin, String name) {
    this(plugin, name, false);
  }

  public Requirement(RankupPlugin plugin, String name, boolean subRequirement) {
    this.plugin = plugin;
    this.name = name;
    this.subRequirement = subRequirement;
  }

  protected Requirement(Requirement clone) {
    this.plugin = clone.plugin;
    this.name = clone.name;
    this.value = clone.value;
    this.toDisplay = clone.toDisplay;
    this.sub = clone.sub;
    this.subRequirement = clone.subRequirement;
  }

  public void setValue(String value) {
    if (hasSubRequirement()) {
      String[] parts = value.split(" ", 2);
      if (parts.length < 2) {
        throw new IllegalArgumentException("Amount and sub-requirement not present for requirement '" + getName() + "'. You must use the format '" + getName() + " <sub-requirement> <amount>'");
      }

      this.sub = parts[0];
      this.value = parts[1].split("\\|")[0];
      if (parts[1].split("\\|").length > 1) {
        this.toDisplay = parts[1].split("\\|")[1];
      }
    } else {
      this.value = value.split("\\|")[0];
      if (value.split("\\|").length > 1) {
        this.toDisplay = value.split("\\|")[1];
      }
    }
  }

  public String getValueString() {
    return value.split("\\|")[0];
  }

  public String[] getValuesString() {
    return value.split("\\|")[0].split(" ");
  }

  public double getValueDouble() {
    return Double.parseDouble(value.split("\\|")[0]);
  }

  public int getValueInt() {
    return Integer.parseInt(value.split("\\|")[0]);
  }

  public boolean getValueBoolean() {
    return Boolean.parseBoolean(value.split("\\|")[0]);
  }

  public String getFullName() {
    if (hasSubRequirement()) {
      return name + "#" + sub;
    } else {
      return name;
    }
  }

  @Nullable
  public String getTotalAmountToDisplay() {
    if (toDisplay != null) {
      return toDisplay;
    }

    return null;
  }

  /**
   * Check if a player meets this requirement
   *
   * @param player the player to check
   * @return true if they meet the requirement, false otherwise
   */
  public abstract boolean check(Player player);

  /**
   * Get the remaining amount needed for <code>Requirement#check(Player)</code> to yield true.
   * This is not required and is only used in placeholders.
   *
   * @param player the player to find the remaining amount of
   * @return the remaining amount needed. Should be non-negative.
   */
  public double getRemaining(Player player) {
    return check(player) ? 0 : 1;
  }

  public final boolean hasSubRequirement() {
    return subRequirement;
  }

  public abstract Requirement clone();

  public double getTotal(Player player) {
    return 1;
  }

  public String getTotalDisplay(Player player) {
    var display = getTotalAmountToDisplay();
    if (display != null) {
      return display;
    } else {
      return String.valueOf((int) getTotal(player));
    }
  }

  public String buildRemainingString(Player player) {
    return getName() + " faltan " + getRemaining(player) + " de " + getTotalDisplay(player);
  }
}
