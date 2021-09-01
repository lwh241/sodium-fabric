package me.jellysquid.mods.sodium.client.gl.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gl.GlObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;

public class GlTexture extends GlObject {
    public GlTexture()  {
        this.setHandle(TextureUtil.generateTextureId());
    }

    public void setTextureData(TextureData data) {
        if (data.isDisposed()) throw new IllegalStateException("Texture data is invalid");

        // TODO: move to state tracker
        var prev = RenderSystem.getTextureId(GL11C.GL_TEXTURE_2D);

        RenderSystem.bindTexture(this.handle());

        // TODO: why is this necessary? what is changing this?
        GL11.glPixelStorei(GL11C.GL_UNPACK_SWAP_BYTES, 0);
        GL11.glPixelStorei(GL11C.GL_UNPACK_LSB_FIRST, 0);
        GL11.glPixelStorei(GL11C.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11C.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11C.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11C.GL_UNPACK_ALIGNMENT, 4);

        // TODO: make configurable
        GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL11C.GL_REPEAT);
        GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL11C.GL_REPEAT);
        GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_NEAREST);
        GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_NEAREST);

        // TODO: allow formats to be swapped
        GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGBA, data.width, data.height, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data.buffer);

        RenderSystem.bindTexture(prev);
    }

    public void delete() {
        GL11C.glDeleteTextures(this.handle());
        this.invalidateHandle();
    }
}
