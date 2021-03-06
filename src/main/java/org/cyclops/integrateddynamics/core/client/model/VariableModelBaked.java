package org.cyclops.integrateddynamics.core.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;
import org.cyclops.cyclopscore.client.model.DelegatingChildDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.client.model.IVariableModelBaked;
import org.cyclops.integrateddynamics.api.client.model.IVariableModelProvider;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A baked variable model.
 * @author rubensworks
 */
public class VariableModelBaked extends DelegatingChildDynamicItemAndBlockModel implements IVariableModelBaked {

    private final Map<IVariableModelProvider, IVariableModelProvider.IBakedModelProvider> subModels = Maps.newHashMap();

    public VariableModelBaked(IBakedModel parent) {
        super(parent);
    }

    @Override
    public <B extends IVariableModelProvider.IBakedModelProvider> void setSubModels(IVariableModelProvider<B> provider, B subModels) {
        this.subModels.put(provider, subModels);
    }

    @Override
    public <B extends IVariableModelProvider.IBakedModelProvider> B getSubModels(IVariableModelProvider<B> provider) {
        return (B) this.subModels.get(provider);
    }

    @Override
    public IBakedModel handleBlockState(BlockState state, Direction side, Random rand, IModelData modelData) {
        return null;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        List<BakedQuad> quads = Lists.newLinkedList();
        // Add regular quads for variable
        quads.addAll(this.baseModel.getQuads(null, getRenderingSide(), this.rand, this.modelData));

        // Add variable type overlay
        IVariableFacade variableFacade = RegistryEntries.ITEM_VARIABLE.getVariableFacade(itemStack);
        variableFacade.addModelOverlay(this, quads, this.rand, this.modelData);

        return new SimpleBakedModel(quads, ModelHelpers.EMPTY_FACE_QUADS, this.isAmbientOcclusion(), this.isSideLit(), this.isGui3d(),
                this.getParticleTexture(), this.getItemCameraTransforms(), this.getOverrides());
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS_ITEM;
    }
}
