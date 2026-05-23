package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AutoTotem extends Module {
    private int cooldown = 0;

    public AutoTotem() {
        super("AutoTotem", "Automatically equips a totem to your offhand when missing.", Category.MISC);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Check if offhand already has a totem
        if (mc.player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }

        // Find a totem in hotbar (0-8)
        int slot = -1;
        Inventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (inv.getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                slot = i;
                break;
            }
        }
        // If not found, search rest of inventory (9-35)
        if (slot == -1) {
            for (int i = 9; i < 36; i++) {
                if (inv.getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    slot = i;
                    break;
                }
            }
        }

        if (slot != -1) {
            // Move to offhand using vanilla click window (undetectable as normal player action)
            moveToOffhand(slot);
            cooldown = 2; // small delay to avoid spam
        }
    }

    private void moveToOffhand(int slot) {
        if (mc.player == null) return;
        AbstractContainerMenu container = mc.player.containerMenu;
        // Convert inventory slot index to container slot index
        // Player inventory slots in container: 9-44 (hotbar 36-44)
        int containerSlot;
        if (slot < 9) {
            // Hotbar: slot 36-44 in container
            containerSlot = 36 + slot;
        } else {
            // Main inventory: slot 9-35 map directly to container slots 9-35
            containerSlot = slot;
        }

        // Perform swap: pick up item then place into offhand (container slot 45)
        mc.gameMode.handleInventoryMouseClick(container.containerId, containerSlot, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(container.containerId, 45, 0, ClickType.PICKUP, mc.player);
        // If we had an item in offhand previously, it goes back to the original slot – fine.
    }
}
