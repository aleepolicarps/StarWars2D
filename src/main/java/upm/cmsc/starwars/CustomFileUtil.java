package upm.cmsc.starwars;

import java.net.URISyntaxException;

public class CustomFileUtil {
	
	public static String getFilePath(String resource){
		try {
			String path = CustomFileUtil.class.getResource(resource).toURI().getPath();
			String os = System.getProperty("os.name").toLowerCase();
			if(os.contains("windows")){
				return path.substring(1);
			}
			return path;
		} catch (URISyntaxException e) {
			// TODO Do something here
		}
		return null;

	}

}
