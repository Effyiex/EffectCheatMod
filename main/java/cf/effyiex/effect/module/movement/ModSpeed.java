package cf.effyiex.effect.module.movement;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import cf.effyiex.effect.utils.AccessHelper;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModSpeed extends Module {

	private Setting mode = new Setting("Mode", 0, new String[] {
		"BHop", "YPort"
	});
	
	private Setting xzMultiplier = new Setting("XZ-Multiplier", 1.0F, SettingType.FLOAT, 0.0F, 10.0F);
	private Setting yMultiplier = new Setting("Y-Multiplier", 1.0F, SettingType.FLOAT, 0.0F, 10.0F);
	private Setting timerSpeed = new Setting("Timer", false, SettingType.BOOLEAN);
	
	public ModSpeed() { 
		super("Speed", ModuleType.MOVEMENT);
		this.listenTo(PlayerTickEvent.class);
		this.add(mode);
		this.add(xzMultiplier);
		this.add(yMultiplier);
		this.add(timerSpeed);
	}

	private boolean resetTimer = false;

	@Override
	public void toggle() {
		if(this.isActive()) {
			KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(CLIENT.gameSettings.keyBindJump.getKeyCode()));
			if(resetTimer) {
				((Timer)AccessHelper.hackGet(AccessHelper.timer)).timerSpeed = 1.0F;
				this.resetTimer = false;
			}
		}
		super.toggle();
	}
	
	@Override
	public void onEvent(Object event) {
		if(!this.isActive()) return;
		if(timerSpeed.toBoolean()) {
			((Timer)AccessHelper.hackGet(AccessHelper.timer)).timerSpeed = xzMultiplier.toFloat();
			this.resetTimer = true;
		}
		BlockPos bottomBlockPos = new BlockPos(CLIENT.thePlayer.posX, CLIENT.thePlayer.posY - 0.1F, CLIENT.thePlayer.posZ);
		Block bottomBlock = CLIENT.theWorld.getBlockState(CLIENT.thePlayer.getPosition().down()).getBlock();
		boolean onGround = (CLIENT.thePlayer.isCollidedVertically && bottomBlock != null && bottomBlock.isBlockSolid(CLIENT.theWorld, bottomBlockPos, EnumFacing.UP));
		if(CLIENT.thePlayer.moveForward != 0.0F || CLIENT.thePlayer.moveStrafing != 0.0F) {
			if(mode.toString().equalsIgnoreCase("bhop")) {
				if(onGround) {
					KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindJump.getKeyCode(), true);
					CLIENT.thePlayer.motionY *= yMultiplier.toFloat();
					if(!timerSpeed.toBoolean()) {
						CLIENT.thePlayer.motionX *= xzMultiplier.toFloat();
						CLIENT.thePlayer.motionZ *= xzMultiplier.toFloat();
					}
				}
			}else if(mode.toString().equalsIgnoreCase("yport")) {
				if(onGround) CLIENT.thePlayer.setPosition(
					CLIENT.thePlayer.posX,
					CLIENT.thePlayer.posY + yMultiplier.toFloat(),
					CLIENT.thePlayer.posZ
				); else CLIENT.thePlayer.motionY *= -yMultiplier.toFloat();
				if(onGround) {
					CLIENT.thePlayer.motionX *= xzMultiplier.toFloat();
					CLIENT.thePlayer.motionZ *= xzMultiplier.toFloat();
				}
			}
		}
	}
	
}
