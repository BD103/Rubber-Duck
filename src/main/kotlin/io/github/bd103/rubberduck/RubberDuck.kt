package io.github.bd103.rubberduck

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.minecraft.item.ItemGroups
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTables
import net.minecraft.loot.entry.EmptyEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object RubberDuck : ModInitializer {
    private const val MOD_ID: String = "rubber_duck"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    val squeakSound: SoundEvent = SoundEvent.of(Identifier(MOD_ID, "squeak"))

    override fun onInitialize() {
        logger.info("Quack!")

        Registry.register(Registries.BLOCK, Identifier(MOD_ID, "rubber_duck"), RubberDuckBlock)
        Registry.register(Registries.ITEM, Identifier(MOD_ID, "rubber_duck"), RubberDuckItem)
        Registry.register(Registries.SOUND_EVENT, Identifier(MOD_ID, "squeak"), squeakSound)

        // Add rubber duck to item group
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ItemGroupEvents.ModifyEntries {
            it.add(RubberDuckItem)
        })

        // Modify dungeon loot table
        LootTableEvents.MODIFY.register(LootTableEvents.Modify { _, _, id, tableBuilder, source ->
            if (source.isBuiltin && id.equals(LootTables.SIMPLE_DUNGEON_CHEST)) {
                tableBuilder.pool(
                    // 10% chance of rolling a duck
                    LootPool.builder()
                        .with(ItemEntry.builder(RubberDuckItem).weight(1))
                        .with(EmptyEntry.builder().weight(9))
                )
            }
        })
    }
}
