package sh.okx.rankup.ranks;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import sh.okx.rankup.RankupPlugin;

import java.util.*;

public abstract class RankList<T extends Rank> {

  protected RankupPlugin plugin;
  @Getter
  private RankTree<T> tree;

  public RankList(RankupPlugin plugin, Collection<? extends T> ranks) {
    this.plugin = plugin;
    List<RankElement<T>> rankElements = new ArrayList<>();
    for (T rank : ranks) {
      if (rank != null && validateSection(rank)) {
        // find next
        rankElements.add(findNext(rank, rankElements));
      } else {
        plugin.getLogger().warning("Ignoring rank: " + rank);
      }
    }

    for (RankElement<T> rankElement : rankElements) {
      if (rankElement.isRootNode()) {
        if (tree == null) {
          tree = new RankTree<>(rankElement);
          addLastRank(plugin);
        } else {
          plugin.getLogger().severe("Multiple root rankup nodes detected (a root rankup nodes is a rankup that does not have anything that ranks up to it). This may lead to inconsistent behaviour.");
          plugin.getLogger().severe("Conflicting root node: " + rankElement.getRank() + ". Using root node: " + tree.getFirst().getRank());
        }
      }
    }
  }

  protected abstract void addLastRank(RankupPlugin plugin);

  private RankElement<T> findNext(T rank, List<RankElement<T>> rankElements) {
    Objects.requireNonNull(rank);

    RankElement<T> currentElement = new RankElement<>(rank, null);

    for (RankElement<T> rankElement : rankElements) {
      T rank1 = rankElement.getRank();
      if (rank1.getRank() != null
          && rank1.getRank().equalsIgnoreCase(rank.getNext())) {
        // current rank element is the next rank
        currentElement.setNext(rankElement);
      } else if (rank1.getNext() != null
          && rank1.getNext().equalsIgnoreCase(rank.getRank())) {
        rankElement.setNext(currentElement);
      }
    }
    return currentElement;
  }

  protected boolean validateSection(T rank) {
    String name = rank.getRank() == null ? "rank" : rank.getRank();
    String nextField = rank.getNext();
    if (nextField == null || nextField.isEmpty()) {
      plugin.getLogger().warning("Rankup section " + name + " does not have a 'next' field.");
      plugin.getLogger().warning("Having a final rank (for example: \"Z: rank: 'Z'\") from 3.4.2 or earlier should no longer be used.");
      plugin.getLogger().warning("If this is intended as a final rank, you should delete " + name);
      return false;
    } else if (rank.getRequirements() == null) {
      plugin.getLogger().warning("Rank " + name + " does not have any requirements.");
      return false;
    }
    return true;
  }

  public T getFirst() {
    return tree.getFirst().getRank();
  }

  public T getRankByName(String name) {
    if (name == null) {
      return null;
    }
    for (T rank : tree) {
      if (name.equalsIgnoreCase(rank.getRank())) {
        return rank;
      }
    }
    return null;
  }

  public RankElement<T> getByName(String name) {
    if (name == null) {
      return null;
    }
    List<RankElement<T>> rankElements = tree.asList();
    for (RankElement<T> rank : rankElements) {
      if (name.equalsIgnoreCase(rank.getRank().getRank())) {
        return rank;
      }
    }
    return null;
  }

  public RankElement<T> getByPlayer(Player player) {
    List<RankElement<T>> list = tree.asList();
    Collections.reverse(list);

    // Warkan Feat: Usar LuckPerms
    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    if (provider != null) {
      return resolveHighestRankUsingLuckPerms(provider.getProvider(), player, list);
    } else {
      // En caso de que LuckPerms no esté instalado - Lógica original
      for (RankElement<T> t : list) {
        if (t.getRank().isIn(player)) {
          return t;
        }
      }
    }

    return null;
  }

  public T getRankByPlayer(Player player) {
    List<RankElement<T>> list = tree.asList();
    Collections.reverse(list);

    // Warkan Feat: Usar LuckPerms
    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    if (provider != null) {
      return resolveHighestRankUsingLuckPerms(provider.getProvider(), player, list).getRank();
    } else {
      // Lógica original
      for (RankElement<T> t : list) {
        if (t.getRank().isIn(player)) {
          return t.getRank();
        }
      }
      return null;
    }
  }

  public RankElement<T> resolveHighestRankUsingLuckPerms(LuckPerms luckPerms, Player player, List<RankElement<T>> rankList) {
    // Determinar el grupo mas grande
    User user = luckPerms.getUserManager().getUser(player.getUniqueId());

    List<RankElement<T>> matchedRanks = new ArrayList<>();
    for (RankElement<T> t : rankList) {
      String rankName = t.getRank().getRank();
      if (user != null && user.getPrimaryGroup().equalsIgnoreCase(rankName)) {
        matchedRanks.add(t);
      } else if(user != null) {
        boolean isInGroup = user.getNodes(NodeType.INHERITANCE).stream()
                .map(InheritanceNode::getGroupName)
                .anyMatch(rankName::equalsIgnoreCase);
        if (isInGroup) {
          matchedRanks.add(t);
        }
      }
    }

    // De los rangos que han hecho match obtener el rango mas alto (weight mas alto)
    RankElement<T> highestRank = null;
    Group highestGroup = null;
    for (RankElement<T> t : matchedRanks) {
      Group group = luckPerms.getGroupManager().getGroup(t.getRank().getRank());
      if (group != null) {
        if (group.getWeight().isPresent()) {
          if (highestGroup == null || group.getWeight().getAsInt() > highestGroup.getWeight().getAsInt()) {
            highestGroup = group;
            highestRank = t;
          }
        }
      }
    }

    if (highestRank == null) {
      // Devolver default
      return rankList.stream().filter(rank -> rank.getRank().getRank().equalsIgnoreCase("default")).findFirst().orElse(null);
    }

    return highestRank;
  }
}
