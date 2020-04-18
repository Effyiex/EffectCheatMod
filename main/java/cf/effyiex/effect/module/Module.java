package cf.effyiex.effect.module;

import java.util.ArrayList;

import cf.effyiex.effect.Reference;

public abstract class Module implements Reference {
	
	public boolean holdingBind = false;
	
	private boolean state = false;
	
	protected boolean hideOnArray = false;

	private int bind = 0;
	
	private String name;
	private ModuleType type;
	
	private ArrayList<Class<?>> listeners;
	private ArrayList<Setting> settings;
	
	public Module(String name, ModuleType type) {
		this.name = name;
		this.type = type;
		this.listeners = new ArrayList<Class<?>>();
		this.settings = new ArrayList<Setting>();
	}
	
	public String getName() { return name; }
	public int getBind() { return bind; }
	public ModuleType getType() { return type; }
	public boolean isActive() { return state; }
	
	public void toggle() {
		this.state = !state;
	}
	
	public void setBind(int bind) {
		this.bind = bind;
	}
	
	protected void listenTo(Class<?> event) {
		this.listeners.add(event);
	}
	
	public boolean isListeningTo(Class<?> event) {
		return (listeners.contains(event));
	}
	
	public boolean renderOnArray() {
		return (!hideOnArray);
	}
	
	public ArrayList<Setting> getSettings() {
		return settings;
	}
	
	public Setting getSetting(String id) {
		for(Setting check : settings) {
			if(check.getID().equalsIgnoreCase(id))
				return check;
		}
		return null;
	}
	
	protected void add(Setting setting) {
		settings.add(setting);
	}
	
	public abstract void onEvent(Object event);

}
