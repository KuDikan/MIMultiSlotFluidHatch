package icu.kudikan.mimsf.datagen.loot;

import aztech.modern_industrialization.MIBlock;
import aztech.modern_industrialization.definition.BlockDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class MimsfBlockLootTableProvider extends BlockLootSubProvider {
    public MimsfBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.VANILLA_SET, registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return MIBlock.BLOCK_DEFINITIONS.values()
                .stream()
                .filter(x -> x.blockLoot != null)
                .<Block>map(BlockDefinition::asBlock)
                .filter(b -> b.getDescriptionId().contains("slots_fluid_"))
                .toList();
    }

    @Override
    public void generate() {
        for (BlockDefinition<?> blockDefinition : MIBlock.BLOCK_DEFINITIONS.values().stream().filter(b -> b.asBlock().getDescriptionId().contains("slots_fluid_")).toList()) {
            if (blockDefinition.blockLoot == null) {
                continue;
            }
            dropSelf(blockDefinition.asBlock());
        }
    }
}
