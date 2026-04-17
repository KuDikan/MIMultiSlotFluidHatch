package icu.kudikan.mimsf;


import aztech.modern_industrialization.config.MIStartupConfig;
import aztech.modern_industrialization.misc.runtime_datagen.RuntimeDataGen;
import icu.kudikan.mimsf.datagen.MimsfDatagenClient;
import icu.kudikan.mimsf.datagen.MimsfDatagenServer;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.Objects;

@Mod(value = Mimsf.MODID, dist = Dist.CLIENT)
public class MimsfClient {
    public MimsfClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    private static void init(FMLConstructModEvent ignored) {
        var modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        Objects.requireNonNull(modBus);

        modBus.addListener(GatherDataEvent.class, event -> {
            MimsfDatagenClient.configure(
                    event.getGenerator(),
                    event.getExistingFileHelper(),
                    event.getLookupProvider(),
                    event.includeServer(),
                    false);
        });

        if (MIStartupConfig.INSTANCE.datagenOnStartup.getAsBoolean()) {
            modBus.addListener(AddPackFindersEvent.class, event -> {
                if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                    RuntimeDataGen.run(MimsfDatagenClient::configure, MimsfDatagenServer::configure);
                }
            });
        }
    }
}