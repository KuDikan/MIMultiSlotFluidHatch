package icu.kudikan.mimsf.mixin;

import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(CrafterComponent.class)
public class CrafterComponentMixin {
    @Final
    @Shadow
    private CrafterComponent.Inventory inventory;

    @Final
    @Shadow
    private CrafterComponent.Behavior behavior;

    @Inject(method = "putFluidOutputs", at = @At("HEAD"), cancellable = true, require = 1)
    protected void putFluidOutputs(MachineRecipe recipe, boolean simulate, boolean toggleLock, CallbackInfoReturnable<Boolean> cir) {
        if (inventory.getFluidOutputs().size() > 1) {
            boolean result = mimsf$putMultiSlotFluidOutputs(recipe, simulate, toggleLock);
            cir.setReturnValue(result);
        }
    }

    @Unique
    private boolean mimsf$putMultiSlotFluidOutputs(MachineRecipe recipe, boolean simulate, boolean toggleLock) {
        // Multi-Slot Fluid Hatch Output Logic
        // Supports simultaneous one fluid output to multiple slots
        List<ConfigurableFluidStack> baseList = inventory.getFluidOutputs();
        List<ConfigurableFluidStack> stacks = simulate ? ConfigurableFluidStack.copyList(baseList) : baseList;

        List<Integer> locksToToggle = new ArrayList<>();
        List<Fluid> lockFluids = new ArrayList<>();

        boolean ok = true;
        for (int i = 0; i < Math.min(recipe.fluidOutputs.size(), behavior.getMaxFluidOutputs()); ++i) {
            MachineRecipe.FluidOutput output = recipe.fluidOutputs.get(i);
            if (output.probability() < 1) {
                if (simulate)
                    continue;
                float randFloat = ThreadLocalRandom.current().nextFloat();
                if (randFloat > output.probability())
                    continue;
            }

            long remaining = output.amount();
            FluidVariant outputKey = FluidVariant.of(output.fluid());

            for (int j = 0; j < stacks.size() && remaining > 0; j++) {
                ConfigurableFluidStack stack = stacks.get(j);
                if (stack.isResourceAllowedByLock(outputKey)
                        && !stack.isResourceBlank()
                        && stack.getResource().equals(outputKey)) {
                    remaining = mimsf$tryInsert(stack, outputKey, remaining, locksToToggle, lockFluids, simulate, j, output.fluid());
                }
            }

            for (int j = 0; j < stacks.size() && remaining > 0; j++) {
                ConfigurableFluidStack stack = stacks.get(j);
                if (stack.isResourceAllowedByLock(outputKey) && stack.isResourceBlank()) {
                    remaining = mimsf$tryInsert(stack, outputKey, remaining, locksToToggle, lockFluids, simulate, j, output.fluid());
                }
            }

            if (remaining != 0) {
                ok = false;
                break;
            }
        }

        if (toggleLock) {
            for (int i = 0; i < locksToToggle.size(); i++) {
                baseList.get(locksToToggle.get(i)).enableMachineLock(lockFluids.get(i));
            }
        }
        return ok;
    }

    @Unique
    private long mimsf$tryInsert(ConfigurableFluidStack stack, FluidVariant outputKey, long amount,
                                                                       List<Integer> locksToToggle, List<Fluid> lockFluids,
                                                                       boolean simulate, int slotIndex, Fluid fluid) {
        long inserted = Math.min(amount, stack.getRemainingSpace());

        if (inserted > 0) {
            stack.setKey(outputKey);
            stack.increment(inserted);

            locksToToggle.add(slotIndex);
            lockFluids.add(fluid);

            if (!simulate) {
                behavior.getStatsOrDummy().addProducedFluids(fluid, inserted);
            }
            return amount - inserted;
        }
        return amount;
    }
}
