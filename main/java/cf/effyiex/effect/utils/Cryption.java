package cf.effyiex.effect.utilities;

import java.util.ArrayList;

public class Cryption {
	
	private static final String ALPHABET =
		("abcdefghijklmnopqrstuvwxyz")
		+ ("ABCDEFGHIJKLMNOPQRSTUVWXYZ") 
		+ ("1234567890")
		+ ("_.,;:-#\\\'+*~}][{��!\"�$%&/()=?<>|� ");
	
	public static char getByIndex(int index) {
		if(index < 0) index = ALPHABET.length() - Math.abs(index);
		return ALPHABET.charAt(index);
	}
	
	private String str;
	
	public Cryption(String str) {
		this.str = ("");
		for(char chr : str.toCharArray()) 
			if(chr != '\n')
				this.str += getByIndex(ALPHABET.indexOf(chr) - ALPHABET.length() / 2);
			else this.str += '\n';
	}
	
	@Override
	public String toString() { return str; }
	
	public static String[] splitTextByChar(String str, char ch) {
		ArrayList<String> args = new ArrayList<String>();
		String current = ("");
		for(char c : str.toCharArray()) {
			if(c != ch) current += c;
			else {
				args.add(current);
				current = ("");
			}
		}
		args.add(current);
		String[] out = new String[args.size()];
		for(short s = 0; s < args.size(); s++) out[s] = args.get(s);
		return out;
	}

}
