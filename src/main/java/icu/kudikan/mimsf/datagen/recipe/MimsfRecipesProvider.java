package icu.kudikan.mimsf.datagen.recipe;

import aztech.modern_industrialization.datagen.recipe.MIRecipesProvider;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeBuilder;
import aztech.modern_industrialization.recipe.json.ShapedRecipeJson;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;

public class MimsfRecipesProvider extends MIRecipesProvider {
    private static final String pathPrefix = "hatches/";

    public MimsfRecipesProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        String[] casings = {"bronze", "steel", "advanced", "turbo", "highly_advanced"};
        String[] prefixes = {"fluid_input", "fluid_output"};

        for (int i = 0; i < casings.length; i++) {
            for (int j = 1; j < 32; j *= 2) {
                String baseFluidInput = String.format("modern_industrialization:%s_%dslots_fluid_input_hatch", casings[i], j);
                String baseFluidOutput = String.format("modern_industrialization:%s_%dslots_fluid_output_hatch", casings[i], j);
                if (j == 1) {
                    baseFluidInput = String.format("modern_industrialization:%s_fluid_input_hatch", casings[i]);
                    baseFluidOutput = String.format("modern_industrialization:%s_fluid_output_hatch", casings[i]);
                }
                int fluidSlots = j * 2;

                String fluidInput = String.format("modern_industrialization:%s_%dslots_fluid_input_hatch", casings[i], fluidSlots);
                String fluidOutput = String.format("modern_industrialization:%s_%dslots_fluid_output_hatch", casings[i], fluidSlots);
                String[][] AB = {{baseFluidInput, baseFluidOutput}, {fluidInput, fluidOutput}};

                for (int k = 0; k <= 1; k++) {
                    ShapedRecipeJson craft = new ShapedRecipeJson(AB[1][k], 1, "UU").addInput('U', AB[0][k]);
                    MachineRecipeBuilder craftAsbl = craft.exportToMachine(MIMachineRecipeTypes.ASSEMBLER, 8, 200, 1);
                    MachineRecipeBuilder unpacker = new MachineRecipeBuilder(MIMachineRecipeTypes.UNPACKER, 2, 200).addItemOutput(AB[0][k], 2).addItemInput(AB[1][k], 1);
                    ShapedRecipeJson craftFromOther = new ShapedRecipeJson(AB[1][k], 1, "U").addInput('U', AB[1][k ^ 1]);

                    craft.offerTo(consumer, pathPrefix + casings[i] + "/" + fluidSlots + "slots_" + prefixes[k] + "_hatch");
                    craftFromOther.offerTo(consumer, pathPrefix + casings[i] + "/" + fluidSlots + "slots_" + prefixes[k] + "_from_" + (k == 0 ? "output" : "input"));
                    craftAsbl.offerTo(consumer, pathPrefix + casings[i] + "/assembler/" + fluidSlots + "slots_" + prefixes[k] + "_hatch");
                    unpacker.offerTo(consumer, pathPrefix + casings[i] + "/unpacker/" + fluidSlots + "slots_" + prefixes[k] + "_hatch");
                }
            }
        }
    }
}
