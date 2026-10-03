package com.lordminato.factionrpg.item;

import com.lordminato.factionrpg.FactionRPG;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private final FactionRPG plugin;
    private final Map<String, CustomItem> customItems;

    public ItemManager(FactionRPG plugin) {
        this.plugin = plugin;
        this.customItems = new HashMap<>();
        loadCustomItems();
    }

    private void loadCustomItems() {
        customItems.clear();

        if (plugin.getConfigManager().getItems().contains("items")) {
            for (String itemId : plugin.getConfigManager().getItems().getConfigurationSection("items").getKeys(false)) {
                CustomItem item = CustomItem.fromConfig(itemId,
                    plugin.getConfigManager().getItems().getConfigurationSection("items." + itemId).getValues(false));
                customItems.put(itemId, item);
            }
        }
    }

    public CustomItem getCustomItem(String id) {
        return customItems.get(id);
    }

    public boolean isCustomItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        NamespacedKey key = new NamespacedKey(plugin, "factionrpg_item_id");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    public String getCustomItemId(ItemStack item) {
        if (!isCustomItem(item)) {
            return null;
        }

        NamespacedKey key = new NamespacedKey(plugin, "factionrpg_item_id");
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public void reloadItems() {
        loadCustomItems();
    }
}
