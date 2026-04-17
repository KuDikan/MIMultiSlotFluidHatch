package icu.kudikan.mimsf.hatches;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MITooltips;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.datagen.model.MachineModelProperties;
import aztech.modern_industrialization.datagen.model.MachineModelsToGenerate;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.hatches.FluidHatch;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.init.MachineRegistrationHelper;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.util.FluidHelper;
import icu.kudikan.mimsf.MimsfConfig;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiSlotsFluidHatches {
    private static final int FLUID_HATCH_SLOT_X = 80;
    private static final int FLUID_HATCH_SLOT_Y = 40;

    public static void init() {
        int j = 1, i = 1;
        for (int t = 1; t <= MimsfConfig.INSTANCE.maxMultiSlotFluidTier.getAsInt(); t += 1) {
            i *= 2;

            if (i > 8) {
                j = i / 8;
            } else if (i > 4) {
                j = i / 4;
            } else if (i > 2) {
                j = i / 2;
            }
            if (MimsfConfig.INSTANCE.enableBronzeMultiSlotFluidHatch.getAsBoolean()) {
                registerMultiSlotsFluidHatches("Bronze", "bronze", MachineCasings.BRONZE, 4, i / j, j);
            }
            if (MimsfConfig.INSTANCE.enableSteelMultiSlotFluidHatch.getAsBoolean()) {
                registerMultiSlotsFluidHatches("Steel", "steel", MachineCasings.STEEL, 16, i / j, j);
            }
            if (MimsfConfig.INSTANCE.enableAdvancedMultiSlotFluidHatch.getAsBoolean()) {
                registerMultiSlotsFluidHatches("Advanced", "advanced", CableTier.MV.casing, 64, i / j, j);
            }
            if (MimsfConfig.INSTANCE.enableTurboMultiSlotFluidHatch.getAsBoolean()) {
                registerMultiSlotsFluidHatches("Turbo", "turbo", CableTier.HV.casing, 256, i / j, j);
            }
            if (MimsfConfig.INSTANCE.enableHighlyAdvancedMultiSlotFluidHatch.getAsBoolean()) {
                registerMultiSlotsFluidHatches("Highly Advanced", "highly_advanced", CableTier.EV.casing, 1024, i / j, j);
            }
        }
    }

    public static void registerMultiSlotsFluidHatches(String englishPrefix, String prefix, MachineCasing casing, int bucketCapacity, int rowSlotsCount, int columSlotsCount) {
        if (rowSlotsCount < 1 || columSlotsCount < 1) {
            rowSlotsCount = columSlotsCount = 1;
        }
        int totalSlotsCount = rowSlotsCount * columSlotsCount;
        int finalRowSlotsCount = rowSlotsCount;
        int finalColumSlotsCount = columSlotsCount;

        for (int iter = 0; iter < 2; ++iter) {
            boolean input = iter == 0;
            String machine = prefix + "_" + totalSlotsCount + "slots_fluid_" + (input ? "input" : "output") + "_hatch";
            String englishName = englishPrefix + " " + totalSlotsCount + "-Slot Fluid" + (input ? " Input" : " Output") + " Hatch";
            MachineRegistrationHelper.registerMachine(englishName, machine, bet -> {
                List<ConfigurableFluidStack> fluidStacks = IntStream.range(0, totalSlotsCount).mapToObj(i -> input ? ConfigurableFluidStack.standardInputSlot(bucketCapacity * 1000L)
                        : ConfigurableFluidStack.standardOutputSlot(bucketCapacity * 1000L)).collect(Collectors.toList());

                MIInventory inventory = new MIInventory(Collections.emptyList(), fluidStacks, SlotPositions.empty(),
                        new SlotPositions.Builder().addSlots(FLUID_HATCH_SLOT_X - 9 * finalRowSlotsCount + 9, FLUID_HATCH_SLOT_Y - 9 * finalColumSlotsCount + 7, finalRowSlotsCount, finalColumSlotsCount).build());
                return new FluidHatch(bet, new MachineGuiParameters.Builder(machine, true).build(), input, !prefix.equals("bronze"), inventory) {
                    @Override
                    public @NotNull List<Component> getTooltips() {
                        int slotCounts = inventory.getFluidStacks().toArray().length;
                        long capacity = inventory.getFluidStacks().getFirst().getCapacity();
                        return List.of(Component.translatable("text.mimsf.HatchCapacityFluid",
                                        FluidHelper.getFluidAmountLarge(slotCounts * capacity)
                                                .setStyle(MITooltips.NUMBER_TEXT),
                                        Component.literal(String.valueOf(slotCounts)).setStyle(MITooltips.NUMBER_TEXT),
                                        FluidHelper.getFluidAmountLarge(capacity)
                                                .setStyle(MITooltips.NUMBER_TEXT)
                                ).withStyle(MITooltips.DEFAULT_STYLE)
                        );
                    }
                };
            }, MachineBlockEntity::registerFluidApi);


            var model = new MachineModelProperties.Builder(casing);
            model.addOverlay("side", MI.id("block/machines/hatch_fluid/overlay_side"));
            model.addOverlay("output", MI.id("block/overlays/output_fluid"));
            model.addOverlay("fluid_auto", MI.id("block/overlays/fluid_auto"));
            MachineModelsToGenerate.register(machine, model.build());
        }
    }

    public static void registerMultiSlotsFluidHatches(String englishPrefix, String prefix, MachineCasing casing, int bucketCapacity, int slotCounts) {
        registerMultiSlotsFluidHatches(englishPrefix, prefix, casing, bucketCapacity, slotCounts, 1);
    }
}
