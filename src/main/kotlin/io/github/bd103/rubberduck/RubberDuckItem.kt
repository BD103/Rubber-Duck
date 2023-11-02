package io.github.bd103.rubberduck

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.BlockItem
import net.minecraft.util.Rarity

private val settings = FabricItemSettings().rarity(Rarity.UNCOMMON)

object RubberDuckItem : BlockItem(RubberDuckBlock, settings)
