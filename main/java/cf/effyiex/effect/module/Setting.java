package cf.effyiex.effect.module;

import java.awt.Color;

import cf.effyiex.effect.utils.Map;
import cf.effyiex.effect.utils.Vector2D;

public class Setting extends Map<Object, Setting.SettingType> {

	public static final char SPLITTER = ':';
	
	public static enum SettingType {
		
		FLOAT(Float.class),
		INTEGER(Integer.class),
		STRING(String.class),
		BOOLEAN(Boolean.class),
		MODE(String.class),
		COLOR(Color.class);
		
		private Class<?> parent;
		
		SettingType(Class<?> parent) {
			this.parent = parent;
		}
		
		public Class<?> getParent() {
			return parent;
		}
		
	}
	
	private String[] modes;
	private String id;
	
	private Vector2D<Float> boundings;
	
	public Setting(String id, Object x, SettingType y) { 
		super(x, y);
		this.id = id;
		if(y.equals(SettingType.INTEGER))
			this.boundings = new Vector2D<Float>(0.0F, 20.0F);
		else if(y.equals(SettingType.FLOAT))
			this.boundings = new Vector2D<Float>(0.0F, 1.0F);
	}
	
	public Setting(String id, Integer index, String[] modes) {
		super(index, SettingType.MODE);
		this.modes = modes;
		this.id = id;
	}
	
	public Setting(String id, Object x, SettingType y, float min, float max) {
		this(id, x, y);
		this.boundings = new Vector2D<Float>(min, max);
	}
	
	public Vector2D<Float> getBoundings() { return this.boundings; }
	
	public Object getValue() { return (modes != null ? modes[Integer.parseInt(x.toString())] : x); }
	
	public String toString() { return this.getValue().toString(); }
	public Boolean toBoolean() { return Boolean.parseBoolean(toString()); }
	public Float toFloat() { return Float.parseFloat(toString()); }
	public Integer toInteger() { return Integer.parseInt(toString()); }
	public String[] toArguments() { return toString().split(String.valueOf(SPLITTER)); }
	
	public Color toColor() {
		String[] args = toArguments();
		return new Color(
			Integer.parseInt(args[0]),
			Integer.parseInt(args[1]),
			Integer.parseInt(args[2]),
			Integer.parseInt(args[3])
		);
	}
	
	public String getID() { return id; }
	public String[] getModes() { return modes; };
	
	public void setValue(Object value) {
		if(this.modes != null) {
			int index = 0;
			for(String mode : this.modes) {
				if(mode.equalsIgnoreCase(value.toString())) {
					this.x = index;
					break;
				} else index++;
			}
		} else this.x = value; 
	}
	
	public void setAWTColor(Color color) { this.setValue(color.getRed() + ":" + color.getGreen() + ":" + color.getBlue() + ":" + color.getAlpha()); }
	
}
