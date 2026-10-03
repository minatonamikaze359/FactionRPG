package com.lordminato.factionrpg.item;

import com.lordminato.factionrpg.FactionRPG;
import com.lordminato.factionrpg.faction.Faction;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomItem {
    private final String id;
    private final String displayName;
    private final List<String> lore;
    private final Material material;
    private final Map<Enchantment, Integer> enchantments;
    private final Faction requiredFaction;
    private final double baseDamage;
    private final double damagePerLevel;
    private final int customModelData;

    public CustomItem(String id, String displayName, List<String> lore, Material material,
                     Map<Enchantment, Integer> enchantments, Faction requiredFaction,
                     double baseDamage, double damagePerLevel, int customModelData) {
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.enchantments = enchantments;
        this.requiredFaction = requiredFaction;
        this.baseDamage = baseDamage;
        this.damagePerLevel = damagePerLevel;
        this.customModelData = customModelData;
    }

    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);

            // Add enchantment glow if there are enchantments
            if (!enchantments.isEmpty()) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            // Add custom model data if configured
            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }

            // Add persistent data to identify the custom item
            NamespacedKey key = new NamespacedKey(FactionRPG.getInstance(), "factionrpg_item_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);

            item.setItemMeta(meta);

            // Apply enchantments
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }

        return item;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public Faction getRequiredFaction() {
        return requiredFaction;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getDamagePerLevel() {
        return damagePerLevel;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    // Factory method to create from config
    public static CustomItem fromConfig(String id, Map<String, Object> config) {
        String displayName = (String) config.get("display-name");
        List<String> lore = (List<String>) config.get("lore");
        Material material = Material.valueOf((String) config.get("material"));

        // Parse enchantments
        Map<Enchantment, Integer> enchantments = new java.util.HashMap<>();
        if (config.containsKey("enchantments")) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) config.get("enchantments")).entrySet()) {
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(entry.getKey().toLowerCase()));
                if (enchant != null) {
                    enchantments.put(enchant, (Integer) entry.getValue());
                }
            }
        }

        // Parse faction restriction
        Faction requiredFaction = null;
        if (config.containsKey("required-faction")) {
            requiredFaction = Faction.fromString((String) config.get("required-faction"));
        }

        double baseDamage = config.containsKey("base-damage") ? ((Number) config.get("base-damage")).doubleValue() : 0;
        double damagePerLevel = config.containsKey("damage-per-level") ? ((Number) config.get("damage-per-level")).doubleValue() : 0;
        int customModelData = config.containsKey("custom-model-data") ? (Integer) config.get("custom-model-data") : 0;

        return new CustomItem(id, displayName, lore, material, enchantments, requiredFaction,
                            baseDamage, damagePerLevel, customModelData);
    }
}
