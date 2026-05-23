package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Category;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class Flight extends Module {
    private boolean wasFlying = false;
    private int groundTicks = 0;

    public Flight() {
        super("Flight", "Allows creative-like flight in survival.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        wasFlying = mc.player.getAbilities().flying;
        mc.player.getAbilities().mayfly = true;
        mc.player.getAbilities().flying = true;
        groundTicks = 0;
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        if (!mc.player.isCreative()) {
            mc.player.getAbilities().mayfly = false;
            mc.player.getAbilities().flying = wasFlying;
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        // Keep flight enabled
        if (!mc.player.getAbilities().flying) {
            mc.player.getAbilities().mayfly = true;
            mc.player.getAbilities().flying = true;
        }
        // Speed control (adjustable)
        float speed = 0.1f;
        mc.player.getAbilities().setFlySpeed(speed);

        // Anti-kick: send ground packet every 5 ticks to avoid "flying" kick
        groundTicks++;
        if (groundTicks >= 5) {
            mc.player.setOnGround(true);
            groundTicks = 0;
        }
    }
}
