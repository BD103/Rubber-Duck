package io.github.bd103.rubberduck

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object RubberDuck : ModInitializer {
    private const val MOD_ID: String = "rubber_duck"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Quack!")

        Registry.register(Registries.BLOCK, Identifier(MOD_ID, "rubber_duck"), RubberDuckBlock)
        Registry.register(Registries.ITEM, Identifier(MOD_ID, "rubber_duck"), RubberDuckItem)

        // Potentially also redstone category?
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ItemGroupEvents.ModifyEntries {
            it.add(RubberDuckItem)
        })
    }
}
