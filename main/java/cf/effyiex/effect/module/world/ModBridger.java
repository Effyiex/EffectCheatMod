package cf.effyiex.effect.module.world;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModBridger extends Module {

	public ModBridger() {
		super("Bridger", ModuleType.WORLD);
		this.listenTo(PlayerTickEvent.class);
	}

	@Override
	public void toggle() {
		if(this.isActive())
			KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindSneak.getKeyCode(), Keyboard.isKeyDown(CLIENT.gameSettings.keyBindSneak.getKeyCode()));
		super.toggle();
	}
	
	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			BlockPos bottomBlockPos = new BlockPos(CLIENT.thePlayer.posX, CLIENT.thePlayer.posY - 0.1F, CLIENT.thePlayer.posZ);
			Block bottomBlock = CLIENT.theWorld.getBlockState(CLIENT.thePlayer.getPosition().down()).getBlock();
			KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindSneak.getKeyCode(), (!(CLIENT.thePlayer.isCollidedVertically && bottomBlock != null && bottomBlock.isBlockSolid(CLIENT.theWorld, bottomBlockPos, EnumFacing.UP))));
		}
	}

}
