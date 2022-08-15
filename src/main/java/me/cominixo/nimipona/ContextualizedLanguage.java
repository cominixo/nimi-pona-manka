package me.cominixo.nimipona;

import com.google.gson.Gson;
import net.minecraft.resource.Resource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ContextualizedLanguage {

    public static Map<String, List<String>> possibleTranslations = new HashMap<>();


    public static String parseKeyWithInventory(String translationKey, Set<String> inventoryContext) {
        // ken suli la nasin ni li ike li kepeken tenpo suli
        int maxDepth = 1;
        if (possibleTranslations.containsKey(translationKey)) {
            for (String key : inventoryContext) {
                int depth = 1;
                if (!key.equals(translationKey)) {
                    if (possibleTranslations.containsKey(key)) {
                        for (int i = 0; i < possibleTranslations.get(translationKey).size(); i++) {
                            List<String> translation = possibleTranslations.get(key);
                            if (translation.size() > i) {
                                if (translation.get(i).equals(possibleTranslations.get(translationKey).get(i))) {
                                    depth++;
                                }
                            }
                        }
                    }
                    if (depth > maxDepth) {
                        maxDepth = depth;
                    }

                    if (possibleTranslations.get(translationKey).size() == maxDepth) {
                        break;
                    }
                }
            }
        } else {
            return null;
        }
        return String.join(" ", possibleTranslations.get(translationKey).subList(0,maxDepth));
    }

    public static void initializeTranslation(Resource resource) {

        Gson gson = new Gson();
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        possibleTranslations = gson.fromJson(reader, possibleTranslations.getClass());
    }

}
