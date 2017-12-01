package com.rokejits.android.tool.utils;

import java.util.Formatter;
import java.util.Hashtable;
import java.util.Vector;

import android.util.Patterns;

public class StringUtils {

	private static Hashtable<String, String> htmlEntities;
	static {
		htmlEntities = new Hashtable<String, String>();
		htmlEntities.put("&lt;", "<");
		htmlEntities.put("&gt;", ">");
		htmlEntities.put("&amp;", "&");
		htmlEntities.put("&quot;", "\"");
		htmlEntities.put("&agrave;", "ÃƒÂ ");
		htmlEntities.put("&Agrave;", "Ãƒâ‚¬");
		htmlEntities.put("&acirc;", "ÃƒÂ¢");
		htmlEntities.put("&auml;", "ÃƒÂ¤");
		htmlEntities.put("&Auml;", "Ãƒâ€ž");
		htmlEntities.put("&Acirc;", "Ãƒâ€š");
		htmlEntities.put("&aring;", "ÃƒÂ¥");
		htmlEntities.put("&Aring;", "Ãƒâ€¦");
		htmlEntities.put("&aelig;", "ÃƒÂ¦");
		htmlEntities.put("&AElig;", "Ãƒâ€ ");
		htmlEntities.put("&ccedil;", "ÃƒÂ§");
		htmlEntities.put("&Ccedil;", "Ãƒâ€¡");
		htmlEntities.put("&eacute;", "ÃƒÂ©");
		htmlEntities.put("&Eacute;", "Ãƒâ€°");
		htmlEntities.put("&egrave;", "ÃƒÂ¨");
		htmlEntities.put("&Egrave;", "ÃƒË†");
		htmlEntities.put("&ecirc;", "ÃƒÂª");
		htmlEntities.put("&Ecirc;", "ÃƒÅ ");
		htmlEntities.put("&euml;", "ÃƒÂ«");
		htmlEntities.put("&Euml;", "Ãƒâ€¹");
		htmlEntities.put("&iuml;", "ÃƒÂ¯");
		htmlEntities.put("&Iuml;", "Ãƒï¿½");
		htmlEntities.put("&ocirc;", "ÃƒÂ´");
		htmlEntities.put("&Ocirc;", "Ãƒâ€�");
		htmlEntities.put("&ouml;", "ÃƒÂ¶");
		htmlEntities.put("&Ouml;", "Ãƒâ€“");
		htmlEntities.put("&oslash;", "ÃƒÂ¸");
		htmlEntities.put("&Oslash;", "ÃƒËœ");
		htmlEntities.put("&szlig;", "ÃƒÅ¸");
		htmlEntities.put("&ugrave;", "ÃƒÂ¹");
		htmlEntities.put("&Ugrave;", "Ãƒâ„¢");
		htmlEntities.put("&ucirc;", "ÃƒÂ»");
		htmlEntities.put("&Ucirc;", "Ãƒâ€º");
		htmlEntities.put("&uuml;", "ÃƒÂ¼");
		htmlEntities.put("&Uuml;", "ÃƒÅ“");
		htmlEntities.put("&nbsp;", " ");
		htmlEntities.put("&copy;", "\u00a9");
		htmlEntities.put("&reg;", "\u00ae");
		htmlEntities.put("&euro;", "\u20a0");
	}

	public static int getStringAsInt(String source, int defaultValue){  
	  if(source != null && source.length() > 0){
	    try{
	      return Integer.parseInt(source);	
	    }catch(Exception e){
	      e.printStackTrace();	
	    }	  
	  }
	  return defaultValue;
	}	
	
	public static long getStringAsLong(String source, long defaultValue){  
	  if(source != null && source.length() > 0){
	    try{
	      return Long.parseLong(source);	
	    }catch(Exception e){
	      e.printStackTrace();	
	    }	  
	  }
	  return defaultValue;
	}
	
	public static float getStringAsLong(String source, float defaultValue){  
	  if(source != null && source.length() > 0){
	    try{
	      return Float.parseFloat(source);	
	    }catch(Exception e){
	      e.printStackTrace();	
	    }	  
	  }
	  return defaultValue;
	}
	
	public static double getStringAsLong(String source, double defaultValue){  
	  if(source != null && source.length() > 0){
	    try{
	      return Double.parseDouble(source);	
	    }catch(Exception e){
	      e.printStackTrace();	
	    }	  
	  }
	  return defaultValue;
	}
	
	public static boolean getStringAsInt(String source, boolean defaultValue){  
	  if(source != null && source.length() > 0){
	    try{
	      return Boolean.parseBoolean(source);	
	    }catch(Exception e){
          e.printStackTrace();	
	    }	  
	  }
	  return defaultValue;
	}
	
	public static boolean checkPhoneNumberFormat(String phoneNumber){
	  if(phoneNumber == null)
	    return false;
	  if(phoneNumber.length() < 9 || phoneNumber.length() > 10)
	    return false;
	 // Pattern pattern = Pattern.compile("[0-9]");
	  return Patterns.PHONE.matcher(phoneNumber).matches();
	}
	
	public static boolean checkEmailFormat( String checkValue ){
	  if(checkValue == null)
	    return false;
	  //Pattern pattern = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+.([a-zA-Z])+([a-zA-Z])+" );	  
	  return Patterns.EMAIL_ADDRESS.matcher(checkValue).matches();
	}
	
	public static String toHexString(byte[] bytes) {  
      StringBuilder sb = new StringBuilder(bytes.length * 2);  

      Formatter formatter = new Formatter(sb);  
      for (byte b : bytes) {  
        formatter.format("%02x", b);  
      } 

	  return sb.toString();  
	} 
	
	public static String unescapeHTML(String source, int start) {
		
		int i, j;
		i = source.indexOf("&", start);
		if (i > -1) {
			
			j = source.indexOf(";", i);
			if (j > i) {
				
				String entityToLookFor = source.substring(i, j + 1);
				String value = (String) htmlEntities.get(entityToLookFor);
				
				if (value != null) {
					
					source = new StringBuffer().append(source.substring(0, i)).append(value).append(source.substring(j + 1)).toString();
					return unescapeHTML(source, i + 1); // recursive call
				}
			}
		}
		return source;
	}
	
	
	public static String[] split(String inString, String delimeter) {
		String[] retAr = new String[0];

		try {
			Vector vec = new Vector();
			int indexA = 0;
			int indexB = inString.indexOf(delimeter);

			while (indexB != -1) {
				if (indexB > indexA)
					vec.addElement(new String(inString.substring(indexA, indexB)));
				indexA = indexB + delimeter.length();
				indexB = inString.indexOf(delimeter, indexA);
			}
			vec.addElement(new String(inString.substring(indexA, inString.length())));
			retAr = new String[vec.size()];
			for (int i = 0; i < vec.size(); i++) {
				retAr[i] = vec.elementAt(i).toString();
			}
		} catch (Exception e) {

		}
		return retAr;
	}
	
	public static String replaceAll(String source, String pattern, String replacement) {
        if (source == null) {
            return "";
        }
       
        StringBuffer sb = new StringBuffer();
        int idx = -1;
        int patIdx = 0;

        while ((idx = source.indexOf(pattern, patIdx)) != -1) {
            sb.append(source.substring(patIdx, idx));
            sb.append(replacement);
            patIdx = idx + pattern.length();
        }
        sb.append(source.substring(patIdx));
        return sb.toString();

    }
	
	public static int getStingIndexInArray(String findString, String[] stringArray){
	  for(int i = 0;i <stringArray.length;i++){
	    if(findString.equals(stringArray[i]))
	      return i;
	  }
	  return -1;
	}
	
}
