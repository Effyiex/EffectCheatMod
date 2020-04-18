package cf.effyiex.effect.rendering;

public enum ColorCode {
	
	WHITE('f', 0xFFFFFF),
	BLACK('0', 0x000000),
	DARK_RED('4', 0xAA0000),
	RED('c', 0xFF5555),
	GOLD('6', 0xFFAA00),
	YELLOW('e', 0xFFFF55),
	DARK_GREEN('2', 0x00AA00),
	GREEN('a', 0x55FF55),
	AQUA('b', 0x55FFFF),
	DARK_AQUA('3', 0x00AAAA),
	DARK_BLUE('1', 0x0000AA),
	BLUE('9', 0x5555FF),
	LIGHT_PURPLE('d', 0xFF55FF),
	DARK_PURPLE('5', 0xAA00AA),
	GRAY('7', 0xAAAAAA),
	DARK_GRAY('8', 0x555555);
	
	private char index;
	private int hexCode;
	
	ColorCode(char index, int hexCode) {
		this.index = index;
		this.hexCode = hexCode;
	}
	
	public int getHexCode() { return hexCode; }
	public char getIndex() { return index; }
	
	@Override
	public String toString() { return ("§" + index); }

	public static ColorCode get(char index) {
		for(ColorCode code : values()) {
			if(code.getIndex() == index)
				return code;
		}
		return WHITE;
	}
	
}
