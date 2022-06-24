package dev.bozho.mixins;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ThaliaScriptsMixinPlugin implements IMixinConfigPlugin {

    boolean deobfEnvironment = false;

    @Override
    public void onLoad(String mixinPackage) {
        deobfEnvironment = (boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false);
        if (deobfEnvironment) {
            System.out.println("We are in a deobfuscated environment, loading compatibility mixins.");
        }
        MixinBootstrap.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith("$mixinPackage.deobfenv") && !deobfEnvironment) {
            System.out.println("Mixin $mixinClassName is for a deobfuscated environment, disabling.");
            return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
