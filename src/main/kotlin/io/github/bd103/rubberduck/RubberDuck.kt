package io.github.bd103.rubberduck

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemGroups
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

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ItemGroupEvents.ModifyEntries {
            it.add(RubberDuckItem)
        })
    }
}
