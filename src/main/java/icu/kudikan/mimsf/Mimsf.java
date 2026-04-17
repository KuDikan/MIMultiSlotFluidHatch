package icu.kudikan.mimsf;

import aztech.modern_industrialization.config.MIStartupConfig;
import aztech.modern_industrialization.misc.runtime_datagen.RuntimeDataGen;
import icu.kudikan.mimsf.datagen.MimsfDatagenServer;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(Mimsf.MODID)
public class Mimsf {

    public static final String MODID = "mimsf";

    public Mimsf(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
        modContainer.registerConfig(ModConfig.Type.STARTUP, MimsfConfig.SPEC);
        MultiSlotsFluidHatches.init();

        modEventBus.addListener(GatherDataEvent.class, event -> {
            MimsfDatagenServer.configure(
                    event.getGenerator(),
                    event.getExistingFileHelper(),
                    event.getLookupProvider(),
                    event.includeServer(),
                    false);
        });

        modEventBus.addListener(AddPackFindersEvent.class, event -> {
            if (dist == Dist.DEDICATED_SERVER && event.getPackType() == PackType.SERVER_DATA
                    && MIStartupConfig.INSTANCE.datagenOnStartup.getAsBoolean()) {
                RuntimeDataGen.run(MimsfDatagenServer::configure);
            }
        });
    }
}
