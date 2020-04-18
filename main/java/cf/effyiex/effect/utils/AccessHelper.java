package cf.effyiex.effect.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public class AccessHelper {
	
	public static Map<Object, Field> session;
	public static Map<Object, Field> theShaderGroup;
	public static Map<Object, Field> timer;
	public static Map<Object, Field> rightClickDelayTimer;
	
	public static void initialize() {
		session = hackPool(Minecraft.getMinecraft(), Session.class);
		theShaderGroup = hackPool(Minecraft.getMinecraft().entityRenderer, ShaderGroup.class);
		timer = hackPool(Minecraft.getMinecraft(), Timer.class);
		rightClickDelayTimer = hackPool(Minecraft.getMinecraft(), "rightClickDelayTimer");
	}
	
	public static Map<Object, Field> hackPool(Object object, Class<?> type) {
		for(Field field : object.getClass().getDeclaredFields()) {
			if(field.getType().equals(type))
				return new Map<Object, Field>(object, field);
		}
		return null;
	}
	
	public static Map<Object, Field> hackPool(Object object, String name) {
		for(Field field : object.getClass().getDeclaredFields()) {
			if(field.getName().equals(name))
				return new Map<Object, Field>(object, field);
		}
		return null;
	}
	
	public static Method hackStaticMethod(String name, Class<?> type, Class<?>[] params) {
		try {
			Method method = type.getDeclaredMethod(name, params);
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void hackSet(Map<Object, Field> field, Object value) {
		try {
			field.y.setAccessible(true);
			field.y.set(field.x, value);
			field.y.setAccessible(false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static Object hackGet(Map<Object, Field> field) {
		try {
			field.y.setAccessible(true);
			Object value = field.y.get(field.x);
			field.y.setAccessible(false);
			return value;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
