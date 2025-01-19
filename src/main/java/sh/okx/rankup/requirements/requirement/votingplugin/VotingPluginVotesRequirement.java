package sh.okx.rankup.requirements.requirement.votingplugin;

import com.bencodez.votingplugin.VotingPluginMain;
import com.bencodez.votingplugin.topvoter.TopVoter;
import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.requirements.ProgressiveRequirement;
import sh.okx.rankup.requirements.Requirement;

public class VotingPluginVotesRequirement extends ProgressiveRequirement {
  public VotingPluginVotesRequirement(RankupPlugin plugin) {
    super(plugin, "votingplugin-votes");
  }

  protected VotingPluginVotesRequirement(Requirement clone) {
    super(clone);
  }

  @Override
  public double getProgress(Player player) {
    return VotingPluginMain.getPlugin().getVotingPluginUserManager().getVotingPluginUser(player).getTotal(TopVoter.AllTime);
  }

  @Override
  public Requirement clone() {
    return new VotingPluginVotesRequirement(this);
  }

  @Override
  public String buildRemainingString(Player player) {
    var remaining = getRemaining(player);

    if (remaining == 0) {
      return "<st><dark_gray>Votar " + getTotalDisplay(player) + " veces</dark_gray></st> <#80ff00>¡Completado!</#80ff00>";
    } else {
      return "<#adadad>Votar <#ffb000>" + getTotalDisplay(player) + " veces</#ffb000> <#ffec00>(comando /vote)</#ffec00>:</#adadad> <#ff4444>" + ((int) remaining) +" restantes</#ff4444>";
    }
  }
}
