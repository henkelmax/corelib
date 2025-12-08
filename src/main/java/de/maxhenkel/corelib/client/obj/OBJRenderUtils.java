package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

import java.util.function.Function;

class OBJRenderUtils {

    public static final RenderPipeline.Snippet ENTITY_SNIPPET_TRIANGLES = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_LIGHT_DIR_SNIPPET)
            .withVertexShader("core/entity")
            .withFragmentShader("core/entity")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES)
            .buildSnippet();

    public static final RenderPipeline ENTITY_CUTOUT_TRIANGLES_PIPELINE = RenderPipeline.builder(ENTITY_SNIPPET_TRIANGLES).withLocation("pipeline/entity_cutout_triangles").withShaderDefine("ALPHA_CUTOUT", 0.1F).withSampler("Sampler1").build();

    public static void onRegisterPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(ENTITY_CUTOUT_TRIANGLES_PIPELINE);
    }

    public static final Function<Identifier, net.minecraft.client.renderer.rendertype.RenderType> ENTITY_CUTOUT_TRIANGLES = Util.memoize(
            p_465686_ -> {
                RenderSetup rendersetup = RenderSetup.builder(ENTITY_CUTOUT_TRIANGLES_PIPELINE)
                        .withTexture("Sampler0", p_465686_)
                        .useLightmap()
                        .useOverlay()
                        .affectsCrumbling()
                        .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                        .createRenderSetup();
                return net.minecraft.client.renderer.rendertype.RenderType.create("entity_cutout_triangles", rendersetup);
            }
    );

}
