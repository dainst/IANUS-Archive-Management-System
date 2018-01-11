/**
 * 
 */
package de.ianus.ingest.core.processEngine.ms.fits;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hendrik
 *
 */
public class BoolString {
	
	
	public static Map<String, Boolean> VALUE_MAP = new HashMap<String, Boolean>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3134584979677788056L;
	{
		put("1", true);
		put("0", false);
		put("yes", true);
		put("no", false);
		put("ja", true);
		put("nein", false);
		put("y", true);
		put("n", false);
		put("j", true);
		put("true", true);
		put("false", false);
		put("ok", true);
		put("", false);
	}};
	
	public static Boolean eval(String str) {
		str = str.toLowerCase().trim();
		return VALUE_MAP.get(str);
	}
}
