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

    public final ModConfigSpec.IntValue maxSlotsCount;

    private MimsfConfig(MimsfConfigBuilder builder) {
        this.maxSlotsCount = builder.start("maxSlotsTier",
                        "Maximum Fluid Slots Tier",
                        "The maximum slots tier of multi-slots fluid hatch to add,each tier has pow(2, tier) slots, set 0 to disable.")
                .gameRestart()
                .defineInRange("maxSlotsTier", 3, 0, 5);
    }
}
