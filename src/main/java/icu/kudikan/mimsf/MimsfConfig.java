package icu.kudikan.mimsf;

import net.neoforged.neoforge.common.ModConfigSpec;


public final class MimsfConfig {
    public static final MimsfConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        var builder = new MimsfConfigBuilder();
        INSTANCE = new MimsfConfig(builder);
        SPEC = builder.build();
    }

    public final ModConfigSpec.IntValue maxMultiSlotFluidTier;
    public final ModConfigSpec.BooleanValue enableBronzeMultiSlotFluidHatch;
    public final ModConfigSpec.BooleanValue enableSteelMultiSlotFluidHatch;
    public final ModConfigSpec.BooleanValue enableAdvancedMultiSlotFluidHatch;
    public final ModConfigSpec.BooleanValue enableTurboMultiSlotFluidHatch;
    public final ModConfigSpec.BooleanValue enableHighlyAdvancedMultiSlotFluidHatch;

    private MimsfConfig(MimsfConfigBuilder builder) {
        this.maxMultiSlotFluidTier = builder.start("maxMultiSlotFluidTier",
                        "Maximum Multi-Slot Fluid Tier",
                        "Maximum tier of added multi-slot fluid hatches.\\nEach tier has 2^tier slots.\\nSet to 0 to disable multi-slot fluid hatches.")
                .gameRestart()
                .defineInRange("maxMultiSlotFluidTier", 3, 0, 5);

        this.enableBronzeMultiSlotFluidHatch = builder.start("enableBronzeMultiSlotFluidHatch",
                        "Enable Bronze Multi-Slot Fluid Hatch",
                        "Whether to register Bronze multi-slot fluid hatches.")
                .gameRestart()
                .define("enableBronzeMultiSlotFluidHatch", true);
        this.enableSteelMultiSlotFluidHatch = builder.start("enableSteelMultiSlotFluidHatch",
                        "Enable Steel Multi-Slot Fluid Hatch",
                        "Whether to register Steel multi-slot fluid hatches.")
                .gameRestart()
                .define("enableSteelMultiSlotFluidHatch", true);
        this.enableAdvancedMultiSlotFluidHatch = builder.start("enableAdvancedMultiSlotFluidHatch",
                        "Enable Advanced Multi-Slot Fluid Hatch",
                        "Whether to register Advanced multi-slot fluid hatches.")
                .gameRestart()
                .define("enableAdvancedMultiSlotFluidHatch", true);
        this.enableTurboMultiSlotFluidHatch = builder.start("enableTurboMultiSlotFluidHatch",
                        "Enable Turbo Multi-Slot Fluid Hatch",
                        "Whether to register Turbo multi-slot fluid hatches.")
                .gameRestart()
                .define("enableTurboMultiSlotFluidHatch", true);
        this.enableHighlyAdvancedMultiSlotFluidHatch = builder.start("enableHighlyAdvancedMultiSlotFluidHatch",
                        "Enable Highly Advanced Multi-Slot Fluid Hatch",
                        "Whether to register Highly Advanced multi-slot fluid hatches.")
                .gameRestart()
                .define("enableHighlyAdvancedMultiSlotFluidHatch", true);
    }
}
