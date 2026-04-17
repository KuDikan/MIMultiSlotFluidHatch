package icu.kudikan.mimsf.datagen;

import icu.kudikan.mimsf.datagen.model.MimsfModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class MimsfDatagenClient {
    public static void configure(
            DataGenerator gen,
            ExistingFileHelper fileHelper,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            boolean run,
            boolean runtimeDatagen) {
        gen.addProvider(run, new MimsfModelProvider(gen.getPackOutput(), fileHelper));
    }
}
