package icu.kudikan.mimsf.mixin;

import aztech.modern_industrialization.compat.kubejs.machine.RegisterHatchesEventJS;
import aztech.modern_industrialization.machines.models.MachineCasings;
import icu.kudikan.mimsf.hatches.MultiSlotsFluidHatches;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RegisterHatchesEventJS.class)
public class RegisterHatchesEventJSMixin {
    @Shadow
    public void fluid(String englishPrefix, String prefix, String casing, int bucketCapacity) {
    }

    public void fluid(String englishPrefix, String prefix, String casing, int bucketCapacity, int slotsCount) {
        if (slotsCount <= 1) {
            fluid(englishPrefix, prefix, casing, bucketCapacity);
            return;
        }
        MultiSlotsFluidHatches.registerMultiSlotsFluidHatches(englishPrefix, prefix, MachineCasings.get(casing), bucketCapacity, slotsCount);
    }

    public void fluid(String englishPrefix, String prefix, String casing, int bucketCapacity, int rowSlotsCount, int columSlotsCount) {
        if (rowSlotsCount < 1 || columSlotsCount < 1) {
            fluid(englishPrefix, prefix, casing, bucketCapacity);
            return;
        }
        MultiSlotsFluidHatches.registerMultiSlotsFluidHatches(englishPrefix, prefix, MachineCasings.get(casing), bucketCapacity, rowSlotsCount, columSlotsCount);
    }
}
