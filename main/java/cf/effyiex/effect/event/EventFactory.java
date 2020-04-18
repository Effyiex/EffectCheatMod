package cf.effyiex.effect.event;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.Reference;
import cf.effyiex.effect.module.Module;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventFactory implements Reference {
	
	public static void triggerEvent(Event event) {
		MinecraftForge.EVENT_BUS.post(event);
	}
	
	public static void triggerModules(Class<?> eventClass, Object eventInstance) {
		for(Module module : Effect.getModules()) {
			if(module.isListeningTo(eventClass)) module.onEvent(eventInstance);
		}
	}

}
