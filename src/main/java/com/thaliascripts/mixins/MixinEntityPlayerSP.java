package com.thaliascripts.mixins;

import com.thaliascripts.hypixel.RawLocationParser;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "setClientBrand", at = @At(value = "HEAD"))
    public void setClientBrand(String brand, CallbackInfo ci) {
        // this called when brand changed
        RawLocationParser.INSTANCE.onClientBrandChanged(brand);
    }
}
