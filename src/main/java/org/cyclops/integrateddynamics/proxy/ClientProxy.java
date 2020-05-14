package org.cyclops.integrateddynamics.proxy;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.core.client.model.ModelLoaderVariable;
import org.cyclops.integrateddynamics.core.ingredient.ItemMatchType;
import org.cyclops.integrateddynamics.core.inventory.container.slot.SlotVariable;
import org.cyclops.integrateddynamics.core.network.diagnostics.NetworkDiagnosticsPartOverlayRenderer;
import org.lwjgl.glfw.GLFW;

/**
 * Client Proxy
 * @author rubensworks
 */
public class ClientProxy extends ClientProxyComponent {

    private static final String KEYBINDING_CATEGORY_NAME = "key.categories." + Reference.MOD_ID;

    public static final KeyBinding FOCUS_LP_SEARCH = new KeyBinding(
            "key." + Reference.MOD_ID + ".logic_programmer_focus_search",
            KeyConflictContext.GUI, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F,
            KEYBINDING_CATEGORY_NAME);
    public static final KeyBinding FOCUS_LP_RENAME = new KeyBinding(
            "key." + Reference.MOD_ID + ".logic_programmer_open_rename",
            KeyConflictContext.GUI, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R,
            KEYBINDING_CATEGORY_NAME);

    public ClientProxy() {
        super(new CommonProxy());
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPreTextureStitch);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPostTextureStitch);
    }

    @Override
    public ModBase getMod() {
        return IntegratedDynamics._instance;
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "variable"), new ModelLoaderVariable());
        MinecraftForge.EVENT_BUS.register(NetworkDiagnosticsPartOverlayRenderer.getInstance());
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry) {
        super.registerKeyBindings(keyRegistry);
        ClientRegistry.registerKeyBinding(FOCUS_LP_SEARCH);
        ClientRegistry.registerKeyBinding(FOCUS_LP_RENAME);
    }

    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            event.addSprite(SlotVariable.VARIABLE_EMPTY);
            for (ItemMatchType itemMatchType : ItemMatchType.values()) {
                event.addSprite(itemMatchType.getSlotSpriteName());
            }
        }
    }

    public void onPostTextureStitch(TextureStitchEvent.Post event) {
        if (event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            event.getMap().getSprite(SlotVariable.VARIABLE_EMPTY);
            for (ItemMatchType itemMatchType : ItemMatchType.values()) {
                event.getMap().getSprite(itemMatchType.getSlotSpriteName());
            }
        }
    }
}
