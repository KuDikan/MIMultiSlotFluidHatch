package icu.kudikan.mimsf.datagen.translation;

import aztech.modern_industrialization.definition.Definition;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import icu.kudikan.mimsf.MimsfConfigBuilder;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public final class MimsfTranslationProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String OUTPUT_PATH = "assets/mimsf/lang/en_us.json";

    private final PackOutput packOutput;
    private final Map<String, String> translationPairs = new TreeMap<>();

    public MimsfTranslationProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    public void addTranslation(String key, String englishValue) {
        if (!translationPairs.containsKey(key)) {
            translationPairs.put(key, englishValue);
        } else {
            throw new IllegalArgumentException(
                    String.format("Error adding translation key %s for translation %s : already registered for translation %s", key, englishValue,
                            translationPairs.get(key)));
        }
    }

    private void addManualEntries() {
        addTranslation("mod.mimsf", "ModernIndustrialization Multi-Slot Fluid Hatch");
        addTranslation("fml.menu.mods.info.description.mimsf", "Add Multi-Slot Fluid Input/Output Hatch to ModernIndustrialization Mod");
    }

    private void collectTranslationEntries() {
        addManualEntries();

        for (var entry : MimsfConfigBuilder.configTranslations.entrySet()) {
            addTranslation(entry.getKey(), entry.getValue());
        }

        for (Definition definition : Definition.TRANSLATABLE_DEFINITION) {
            if (definition.getTranslationKey().contains("slots_fluid_")) {
                addTranslation(definition.getTranslationKey(), definition.getEnglishName());
            }
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.runAsync(() -> {
            try {
                innerRun(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Util.backgroundExecutor());
    }

    public void innerRun(CachedOutput cache) throws IOException {
        collectTranslationEntries();

        customJsonSave(cache, GSON.toJsonTree(translationPairs), packOutput.getOutputFolder().resolve(OUTPUT_PATH));
    }

    private void customJsonSave(CachedOutput cache, JsonElement jsonElement, Path path) throws IOException {
        String sortedJson = GSON.toJson(jsonElement);
        String prettyPrinted = sortedJson.replace("\\u0027", "'");
        cache.writeIfNeeded(path, prettyPrinted.getBytes(StandardCharsets.UTF_8), Hashing.sha1().hashString(prettyPrinted, StandardCharsets.UTF_8));
    }

    @Override
    public String getName() {
        return "Translations";
    }
}
