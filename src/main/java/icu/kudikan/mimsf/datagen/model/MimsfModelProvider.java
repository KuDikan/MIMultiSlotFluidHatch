package icu.kudikan.mimsf.datagen.model;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.datagen.model.BaseModelProvider;
import aztech.modern_industrialization.datagen.model.MachineModelsToGenerate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MimsfModelProvider extends BaseModelProvider {
    public MimsfModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (var entry : MachineModelsToGenerate.props.entrySet()) {
            if (entry.getKey().contains("slots_fluid_")) {
                simpleBlockWithItem(BuiltInRegistries.BLOCK.get(MI.id(entry.getKey())), models()
                        .getBuilder(entry.getKey())
                        .customLoader((bmb, exFile) -> new MachineModelBuilder<>(entry.getValue(), bmb, exFile))
                        .end());
            }
        }
    }
}
