package me.cominixo.nimipona;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NimiPona implements ModInitializer {

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
            new SimpleSynchronousResourceReloadListener() {
                @Override
                public Identifier getFabricId() {
                    return new Identifier("nimipona", "tok_assets");
                }

                @Override
                public void reload(ResourceManager manager) {
                    try {
                        Resource resource = manager.getResource(new Identifier("nimipona", "toki/tok.json"));

                        ContextualizedLanguage.initializeTranslation(resource);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }

}
