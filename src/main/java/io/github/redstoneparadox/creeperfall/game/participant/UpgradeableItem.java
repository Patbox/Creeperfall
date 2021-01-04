package io.github.redstoneparadox.creeperfall.game.participant;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Objects;

public class UpgradeableItem implements Upgradeable {
	private final List<ItemStack> tiers;

	private int currentTier = -1;

	public UpgradeableItem(List<ItemStack> tiers) {
		this.tiers = tiers;
	}

	@Override
	public boolean upgrade(CreeperfallParticipant participant) {
		ServerWorld world = participant.getGameSpace().getWorld();
		ServerPlayerEntity player = participant.getPlayer().getEntity(world);
		PlayerInventory inventory = Objects.requireNonNull(player).inventory;

		if (currentTier + 1 >= tiers.size()) return false;

		currentTier += 1;

		if (currentTier == 0) {
			player.giveItemStack(tiers.get(0).copy());
		}

		for (int slot = 0; slot < inventory.size(); slot++) {
			if (ItemStack.areEqual(inventory.getStack(slot), tiers.get(currentTier - 1))) {
				inventory.setStack(slot, tiers.get(currentTier));
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack getIcon() {
		return tiers.get(currentTier);
	}
}