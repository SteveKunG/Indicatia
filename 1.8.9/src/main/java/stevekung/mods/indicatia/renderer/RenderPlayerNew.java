package stevekung.mods.indicatia.renderer;

import java.util.Iterator;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SideOnly(Side.CLIENT)
public class RenderPlayerNew extends RenderPlayer
{
    public RenderPlayerNew()
    {
        this(false);
    }

    public RenderPlayerNew(boolean smallArms)
    {
        super(IndicatiaMod.MC.getRenderManager(), smallArms);
        this.mainModel = new ModelPlayerNew(0.0F, smallArms);

        boolean removedVanilla = false;
        Iterator<LayerRenderer<AbstractClientPlayer>> iterator = this.layerRenderers.iterator();

        while (iterator.hasNext())
        {
            LayerRenderer<AbstractClientPlayer> renderer = iterator.next();

            if (renderer.getClass().equals(LayerCustomHead.class))
            {
                iterator.remove();
                removedVanilla = true;
            }
        }

        if (removedVanilla)
        {
            this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        }
    }

    @Override
    public void renderRightArm(AbstractClientPlayer player)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        super.renderRightArm(player);
        GlStateManager.disableBlend();
    }

    @Override
    public void renderLeftArm(AbstractClientPlayer player)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        super.renderLeftArm(player);
        GlStateManager.disableBlend();
    }
}