package trxsh.ontop.theUltimateSMPLib.util;

import com.destroystokyo.paper.loottable.LootableBlockInventory;
import com.destroystokyo.paper.loottable.LootableEntityInventory;
import com.destroystokyo.paper.loottable.LootableInventory;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ChestHelper implements Listener {
    private static List<Pair<Structure, Pair<ItemStack, Double>>> items = new ArrayList<>();

    /**
     * adds stack to have a specific chance of spawning in a chest with a loot table (randomly generated).
     * You must also specify the structure that a chest can generate in!
     * probability of spawning is chance (an integer) lower than 10.
     * chance cannot be higher than 10!
     * @param stack item stack to be added to a chest
     * @param chance chance the item will be added to a chest
     * @param structure structure that a chest will generate in
     */
    public static void addItemToRandomTables(@NotNull ItemStack stack, double chance, @NotNull Structure structure) {
        if(chance > 10) throw new IllegalArgumentException("chance cannot be higher than 10");
        if(stack.getType() == Material.AIR) throw new IllegalArgumentException("Stack cannot be of type AIR.");
        items.add(Pair.of(structure, Pair.of(stack, chance)));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpenInventory(LootGenerateEvent event) {
        Inventory inventory = Objects.requireNonNull(event.getInventoryHolder()).getInventory();
        Entity e = event.getEntity();

        items.forEach(pair -> {
            Structure structure = pair.getLeft();
            ItemStack item = pair.getRight().getLeft();
            double chance = pair.getRight().getRight();

            assert e != null;
            StructureSearchResult result = e.getWorld().locateNearestStructure(e.getLocation(), structure, 3, false);

            if(result != null) {
                if (result.getStructure().equals(structure)) {
                    Bukkit.getLogger().info("structure " + structure + "found for loot table! " + e.getLocation());

                    if(new Random().nextDouble(0, chance) <= chance) {
                        int slot = randomFreeSlot(inventory, null);
                        inventory.setItem(slot, item);
                    }
                }
            }
        });
    }

    /**
     * finds a free slot randomly in an inventory
     * @param inventory
     * @param random
     * @return a random free slot in the inventory;
     */
    public static int randomFreeSlot(@NotNull Inventory inventory, @Nullable Random random) {
        if(inventory.firstEmpty() == -1) throw new IllegalArgumentException("inventory is full");

        Random rand = null;

        if(random != null) rand = random;
        else rand = new Random();

        int num = rand.nextInt(0, inventory.getSize());

        if(inventory.getItem(num) == null) return num;
        else return randomFreeSlot(inventory, rand);
    }
}
