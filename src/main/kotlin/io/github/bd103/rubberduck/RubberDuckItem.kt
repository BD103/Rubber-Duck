package io.github.bd103.rubberduck

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.BlockItem
import net.minecraft.item.Equipment
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Rarity

private val settings = FabricItemSettings().rarity(Rarity.UNCOMMON)

object RubberDuckItem : BlockItem(RubberDuckBlock, settings), Equipment {
    override fun getSlotType() = EquipmentSlot.HEAD
    override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER
}
