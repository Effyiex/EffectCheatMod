package cf.effyiex.effect.utils;

import java.util.ConcurrentModificationException;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import cf.effyiex.effect.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Targetting implements Reference {
	
	private static Entity pointedEntity;
	
	public static Entity getNearestTarget(float inRange) {
		Entity target = null;
		float inYawDiff = -1;
		try {
			for(Entity entity : CLIENT.theWorld.loadedEntityList) {
				if(entity instanceof EntityPlayer && entity != CLIENT.thePlayer) {
					float dist = CLIENT.thePlayer.getDistanceToEntity(entity);
					float yawDiff = cf.effyiex.effect.utils.MathHelper.difference(
						CLIENT.thePlayer.rotationYaw, getRotations(entity)[0]
					);
					if(dist < inRange && (inYawDiff <= 0 || yawDiff < inYawDiff) && CLIENT.thePlayer.canEntityBeSeen(entity) && !entity.isInvisible()) {
						target = entity;
						inYawDiff = yawDiff;
					}
				}
			}
		} catch(ConcurrentModificationException e) {}
		return target;
	}
	
	public static float[] getRotations(Entity entity) {
		if(entity == null) return null;
		double diffX = entity.posX - CLIENT.thePlayer.posX;
		double diffY;
		if(entity instanceof EntityLivingBase) {
			EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (CLIENT.thePlayer.posY + CLIENT.thePlayer.getEyeHeight());
		}else diffY = entity.posY + entity.height / 2.0D - (CLIENT.thePlayer.posY + CLIENT.thePlayer.getEyeHeight());
		double diffZ = entity.posZ - CLIENT.thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] { CLIENT.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - CLIENT.thePlayer.rotationYaw),
			CLIENT.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - CLIENT.thePlayer.rotationPitch) };
	}
	
	public static Entity getTargetOver(float dist) {
		Entity entity = CLIENT.getRenderViewEntity();
        if (entity != null) {
            if (CLIENT.theWorld != null) {
                double d0 = (double) dist;
                MovingObjectPosition objectMouseOver = entity.rayTrace(d0, 1.0F);
                double d1 = d0;
                Vec3 vec3 = entity.getPositionEyes(1.0F);
                @SuppressWarnings("unused") int i = 3;
                if (objectMouseOver != null) {
                    d1 = objectMouseOver.hitVec.distanceTo(vec3);
	                Vec3 vec31 = entity.getLook(1.0F);
	                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
	                pointedEntity = null;
	                @SuppressWarnings("unused") Vec3 vec33 = null;
	                float f = 1.0F;
	                List<Entity> list = CLIENT.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0)
	                		.expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			                    public boolean apply(Entity entity) { return entity.canBeCollidedWith(); }
	                }));
	                double d2 = d1;
	                for (int j = 0; j < list.size(); ++j) {
	                    Entity entity1 = (Entity)list.get(j);
	                    float f1 = entity1.getCollisionBorderSize();
	                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
	                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
	                    if (axisalignedbb.isVecInside(vec3)) {
	                        if (d2 >= 0.0D) {
	                            pointedEntity = entity1;
	                            vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
	                            d2 = 0.0D;
	                        }
	                    }else if (movingobjectposition != null) {
	                        double d3 = vec3.distanceTo(movingobjectposition.hitVec);
	                        if (d3 < d2 || d2 == 0.0D) {
	                            if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {
	                                if (d2 == 0.0D) {
	                                    pointedEntity = entity1;
	                                    vec33 = movingobjectposition.hitVec;
	                                }
	                            }else{
	                                pointedEntity = entity1;
	                                vec33 = movingobjectposition.hitVec;
	                                d2 = d3;
	                            }
	                        }
	                    }
	                }
	
	            }
            }
        }
        CLIENT.pointedEntity = pointedEntity;
        return pointedEntity;
	}
	
}
