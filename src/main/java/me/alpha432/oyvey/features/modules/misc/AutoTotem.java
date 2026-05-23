// Find in hotbar
int slot = -1;
for (int i = 0; i < 9; i++) {
    if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
        slot = i;
        break;
    }
}
// If not found, check whole inventory
if (slot == -1) {
    for (int i = 9; i < 36; i++) {
        if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
            slot = i;
            break;
        }
    }
}
// Move to offhand
if (slot != -1) {
    InvUtils.move().from(slot).toOffhand();  // or use vanilla: mc.player.getInventory().selected = slot; then pick and swap
}
