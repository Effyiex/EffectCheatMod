package cf.effyiex.effect.module.movement;

import java.util.ArrayList;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.utils.AccessHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModStep extends Module {

	private Setting mode = new Setting("Mode", 0, new String[] {
		"Vanilla", "NCP", "AAC"
	});
	
	public ModStep() {
		super("Step", ModuleType.MOVEMENT);
		this.listenTo(PlayerTickEvent.class);
		this.add(mode);
	}

	private float vanillaStepHeight = 0.5F;
	
	@Override
	public void toggle() {
		if(this.isActive())
			CLIENT.thePlayer.stepHeight = vanillaStepHeight;
		else if(CLIENT.thePlayer != null)
			vanillaStepHeight = CLIENT.thePlayer.stepHeight;
		super.toggle();
	}
	
	private float timerReset = 0;
	
	@Override
	public void onEvent(Object event) {
		if(timerReset > 0) {
			((Timer)AccessHelper.hackGet(AccessHelper.timer)).timerSpeed = timerReset;
			timerReset = 0;
		}
		if(this.isActive()) {
			CLIENT.thePlayer.stepHeight = vanillaStepHeight;
			if(mode.toString().equalsIgnoreCase("Vanilla"))
				CLIENT.thePlayer.stepHeight = 1.0F;
			else {
				ArrayList<BlockPos> collisionBlocks = new ArrayList<BlockPos>();
				EntityPlayerSP player = CLIENT.thePlayer;
				BlockPos pos1 = new BlockPos(player.getEntityBoundingBox().minX - 0.001D, player.getEntityBoundingBox().minY - 0.001D, player.getEntityBoundingBox().minZ - 0.001D);
				BlockPos pos2 = new BlockPos(player.getEntityBoundingBox().maxX + 0.001D, player.getEntityBoundingBox().maxY + 0.001D, player.getEntityBoundingBox().maxZ + 0.001D);
				if(player.worldObj.isAreaLoaded(pos1, pos2)) {
					for(int x = pos1.getX(); x <= pos2.getX(); x++)
					for(int y = pos1.getY(); y <= pos2.getY(); y++)
					for(int z = pos1.getZ(); z <= pos2.getZ(); z++)
						if(y > player.posY - 1.0D && y <= player.posY)
							collisionBlocks.add(new BlockPos(x, y, z));
				}
				BlockPos belowPlayerPos = new BlockPos(player.posX, player.posY - 1.0F, player.posZ);
				for(BlockPos collisionBlock : collisionBlocks) {
					if(!(player.worldObj.getBlockState(collisionBlock.add(0, 1, 0)).getBlock() instanceof BlockFenceGate)) {
						try {
							if(player.worldObj.getBlockState(collisionBlock.add(0, 1, 0)).getBlock().getCollisionBoundingBox(CLIENT.theWorld, belowPlayerPos, CLIENT.theWorld.getBlockState(collisionBlock)) != null) return;
						} catch (Exception e) { e.printStackTrace(); }
					}
				}
				BlockPos bottomBlockPos = new BlockPos(CLIENT.thePlayer.posX, CLIENT.thePlayer.posY - 0.1F, CLIENT.thePlayer.posZ);
				Block bottomBlock = CLIENT.theWorld.getBlockState(CLIENT.thePlayer.getPosition().down()).getBlock();
				if(CLIENT.thePlayer.isCollidedHorizontally && CLIENT.theWorld.getBlockState(new BlockPos(CLIENT.thePlayer.getPosition().getX(), CLIENT.thePlayer.getPosition().getY() + 2.0F, CLIENT.thePlayer.getPosition().getZ())).getBlock() instanceof BlockAir) {
					if(CLIENT.thePlayer.isCollidedVertically && bottomBlock != null && bottomBlock.isBlockSolid(CLIENT.theWorld, bottomBlockPos, EnumFacing.UP)) {
						if(mode.toString().equalsIgnoreCase("NCP")) {
							CLIENT.thePlayer.setPosition(CLIENT.thePlayer.posX, CLIENT.thePlayer.posY + 1.0F, CLIENT.thePlayer.posZ);
						}else if(mode.toString().equalsIgnoreCase("AAC")) {
							CLIENT.thePlayer.jump();
							timerReset = ((Timer)AccessHelper.hackGet(AccessHelper.timer)).timerSpeed;
							((Timer)AccessHelper.hackGet(AccessHelper.timer)).timerSpeed = 16;
						}
					}
				}
			}
		}
	}

}
