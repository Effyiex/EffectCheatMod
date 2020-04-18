package cf.effyiex.effect.module.render;

import static cf.effyiex.effect.rendering.RenderHelper.renderESPBox;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cf.effyiex.effect.event.CustomEvent.LivingRenderEvent;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ModESP extends Module {

	private Setting player = new Setting("Player", true, SettingType.BOOLEAN);
	private Setting mobs = new Setting("Mobs", true, SettingType.BOOLEAN);
	private Setting animals = new Setting("Animals", true, SettingType.BOOLEAN);
	private Setting items = new Setting("Items", true, SettingType.BOOLEAN);
	private Setting playerColor = new Setting("PlayerColor", "255:55:255:55", SettingType.COLOR);
	private Setting mobColor = new Setting("MobColor", "255:55:255:55", SettingType.COLOR);
	private Setting animalColor = new Setting("AnimalColor", "255:55:255:55", SettingType.COLOR);
	private Setting itemColor = new Setting("ItemColor", "255:55:255:55", SettingType.COLOR);
	
	public ModESP() {
		super("ESP", ModuleType.RENDER);
		this.listenTo(LivingRenderEvent.class);
		this.add(player);
		this.add(mobs);
		this.add(animals);
		this.add(items);
		this.add(playerColor);
		this.add(mobColor);
		this.add(animalColor);
		this.add(itemColor);
	}

	@Override
	public void onEvent(Object event) {
		LivingRenderEvent e = ((LivingRenderEvent)event);
		if(this.isActive() && CLIENT.theWorld != null) {
			if(shouldRenderESP(e.entity)) {
				GL11.glPushMatrix();
				renderESPBox(e.x, e.y, e.z, e.entity.width,
						e.entity.height, getEntityColor(e.entity), true);
				GL11.glPopMatrix();
			}
		}
	}
	
	private boolean shouldRenderESP(Entity entity) {
		return (entity != CLIENT.thePlayer) && (
			(entity instanceof EntityPlayer && player.toBoolean()) ||
			(entity instanceof EntityMob && mobs.toBoolean()) ||
			(entity instanceof EntityAnimal && animals.toBoolean()) ||
			(entity instanceof EntityItem && items.toBoolean())
		);
	}
	
	private Color getEntityColor(Entity entity) {
		if (entity instanceof EntityPlayer) return playerColor.toColor();
		else if (entity instanceof EntityMob) return mobColor.toColor();
		else if (entity instanceof EntityAnimal) return animalColor.toColor();
		else if (entity instanceof EntityItem) return itemColor.toColor();
		return Color.white;
	}

}
