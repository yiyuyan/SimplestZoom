package cn.ksmcbrigade.stz;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Mixin(GameRenderer.class)
public abstract class SimplestZoom {

    @Unique
    public int simplestZoom$fov = 9;

    @Unique
    public File simplestZoom$config = new File("config/stz-config.json");

    @Inject(method = "getFov",at = @At("RETURN"), cancellable = true)
    public void fov(Camera p_109142_, float p_109143_, boolean p_109144_, CallbackInfoReturnable<Double> cir) throws IOException {
        if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_B)){
            if(!simplestZoom$config.exists()){
                JsonObject object = new JsonObject();
                object.addProperty("fov",this.simplestZoom$fov);
                Files.writeString(simplestZoom$config.toPath(),object.toString());
            }
            this.simplestZoom$fov = JsonParser.parseString(Files.readString(simplestZoom$config.toPath())).getAsJsonObject().get("fov").getAsInt();
            if(this.simplestZoom$fov==0) this.simplestZoom$fov = 9;
            cir.setReturnValue(cir.getReturnValue()/ simplestZoom$fov);
        }
    }
}
