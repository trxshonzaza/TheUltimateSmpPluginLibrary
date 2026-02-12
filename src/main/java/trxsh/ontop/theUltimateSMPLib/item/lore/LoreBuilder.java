package trxsh.ontop.theUltimateSMPLib.item.lore;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple lore builder for custom items. 
 * The methods are recursive, allowing single line lore building.
 * See: CustomItemStack.java
 */
public class LoreBuilder {
    public List<Component> components;
    public ItemMeta meta;

    /**
     * initalizer
     * @param meta the itemstack's metadata in order to add the lore once finalized
     */
    public LoreBuilder(ItemMeta meta) {
        this.components = new ArrayList<>();
        this.meta = meta;
    }

    /**
     * adds the component to the specified lore builder
     * @param component
     * @return the lore builder
     */
    public LoreBuilder add(Component component) {
        components.add(component);
        return this;
    }

    /**
     * adds a list of components to the specified lore builder
     * @param list
     * @return the lore builder
     */
    public LoreBuilder addRange(Component... list) {
        Collections.addAll(components, list);
        return this;
    }

    /**
     * adds a component then appends a line separator
     * @param component
     * @return the lore builder
     */
    public LoreBuilder addThenSpace(Component component) {
        return add(component).space(false);
    }

    /**
     * adds a component then prepends a line separator
     * @param component
     * @return the lore builder
     */
    public LoreBuilder spaceThenAdd(Component component) {
        return space(false).add(component);
    }

    /**
     * creates a section of lore (header, and information) the header and each element are separated by spaces.
     * @param header the header component
     * @param info the list of extra components
     * @return the lore builder
     */
    public LoreBuilder section(Component header, Component... info) {
        add(header);
        for(Component c : info) add(c);

        return this;
    }

    /**
     * creates a space (line separator)
     * @param doubleSpace true to add two spaces, false for only one space.
     * @return the lore builder
     */
    public LoreBuilder space(boolean doubleSpace) {
        if(doubleSpace) {
            add(Component.text(" "));
            add(Component.text(" "));
        } else add(Component.text(" "));

        return this;
    }

    /**
     * returns the item meta with the built lore (calls lore before returning)
     * @return the item meta containing the built lore
     */
    public ItemMeta finish() {
        meta.lore(this.components);
        return meta;
    }
}
