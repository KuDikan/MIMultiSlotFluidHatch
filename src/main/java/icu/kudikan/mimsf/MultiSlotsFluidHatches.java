package icu.kudikan.mimsf;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.datagen.model.MachineModelProperties;
import aztech.modern_industrialization.datagen.model.MachineModelsToGenerate;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.hatches.FluidHatch;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.init.MachineDefinition;
import aztech.modern_industrialization.machines.init.MachineRegistrationHelper;
import aztech.modern_industrialization.machines.init.MultiblockHatches;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiSlotsFluidHatches {

    private static final int FLUID_HATCH_SLOT_X = 80;
    private static final int FLUID_HATCH_SLOT_Y = 40;

    public static void init() {
        int j = 1, i = 1;
        for (int t = 1; t <= MimsfConfig.INSTANCE.maxSlotsCount.getAsInt(); t += 1) {
            i *= 2;

            if (i > 8) {
                j = i / 8;
            } else if (i > 4) {
                j = i / 4;
            } else if (i > 2) {
                j = i / 2;
            }

            registerMultiSlotsFluidHatches("Bronze", "bronze", MachineCasings.BRONZE, 4, i / j, j);
            registerMultiSlotsFluidHatches("Steel", "steel", MachineCasings.STEEL, 16, i / j, j);
            registerMultiSlotsFluidHatches("Advanced", "advanced", CableTier.MV.casing, 64, i / j, j);
            registerMultiSlotsFluidHatches("Turbo", "turbo", CableTier.HV.casing, 256, i / j, j);
            registerMultiSlotsFluidHatches("Highly Advanced", "highly_advanced", CableTier.EV.casing, 1024, i / j, j);
        }

    }

    public static MultiblockHatches.HatchPair<FluidHatch> registerMultiSlotsFluidHatches(String englishPrefix, String prefix, MachineCasing casing, int bucketCapacity, int rowSlotsCount, int columSlotsCount) {
        List<MachineDefinition<FluidHatch>> definitions = new ArrayList<>(2);
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
            var def = MachineRegistrationHelper.registerMachine(englishName, machine, bet -> {
                List<ConfigurableFluidStack> fluidStacks = IntStream.range(0, totalSlotsCount).mapToObj(i -> input ? ConfigurableFluidStack.standardInputSlot(bucketCapacity * 1000L)
                        : ConfigurableFluidStack.standardOutputSlot(bucketCapacity * 1000L)).collect(Collectors.toList());

                MIInventory inventory = new MIInventory(Collections.emptyList(), fluidStacks, SlotPositions.empty(),
                        new SlotPositions.Builder().addSlots(FLUID_HATCH_SLOT_X - 9 * finalRowSlotsCount + 9, FLUID_HATCH_SLOT_Y - 9 * finalColumSlotsCount + 7, finalRowSlotsCount, finalColumSlotsCount).build());
                return new FluidHatch(bet, new MachineGuiParameters.Builder(machine, true).build(), input, !prefix.equals("bronze"), inventory);
            }, MachineBlockEntity::registerFluidApi);
            definitions.add(def);

            var model = new MachineModelProperties.Builder(casing);
            model.addOverlay("side", MI.id("block/machines/hatch_fluid/overlay_side"));
            model.addOverlay("output", MI.id("block/overlays/output_fluid"));
            model.addOverlay("fluid_auto", MI.id("block/overlays/fluid_auto"));
            MachineModelsToGenerate.register(machine, model.build());
        }

        return new MultiblockHatches.HatchPair<>(definitions.get(0), definitions.get(1));
    }

    public static MultiblockHatches.HatchPair<FluidHatch> registerMultiSlotsFluidHatches(String englishPrefix, String prefix, MachineCasing casing, int bucketCapacity, int slotCounts) {
        return registerMultiSlotsFluidHatches(englishPrefix, prefix, casing, bucketCapacity, slotCounts, 1);
    }
}
