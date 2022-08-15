package me.cominixo.nimipona.mixin;

import com.google.common.collect.ImmutableList;
import me.cominixo.nimipona.ContextualizedLanguage;
import me.cominixo.nimipona.ContextualizedTranslatableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
import net.minecraft.text.TranslationException;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(TranslatableText.class)
public abstract class TranslatableTextMixin {

    @Shadow @Final private String key;

    @Shadow private List<StringVisitable> translations;

    @Shadow protected abstract void forEachPart(String translation, Consumer<StringVisitable> partsConsumer);

    @Shadow private @Nullable Language languageCache;

    @Inject(method = "updateTranslations", at = @At("HEAD"), cancellable = true)
    private void updateTranslations(CallbackInfo ci) {

        // TODO only do this when necessary

        if ((Object)this instanceof ContextualizedTranslatableText && ContextualizedTranslatableText.getInventoryContext() != null) {

            String value = ContextualizedLanguage.parseKeyWithInventory(key, ContextualizedTranslatableText.getInventoryContext());
            // Fallback if translation was not found
            if (value == null) {
                Language language = Language.getInstance();
                if (language == this.languageCache) {
                    return;
                }
                this.languageCache = language;
                value = language.get(this.key);
            }

            try {
                ImmutableList.Builder<StringVisitable> builder = ImmutableList.builder();
                this.forEachPart(value, builder::add);
                this.translations = builder.build();
            } catch (TranslationException translationException) {
                this.translations = ImmutableList.of(StringVisitable.plain(value));
            }

            ci.cancel();
        }

    }

}
