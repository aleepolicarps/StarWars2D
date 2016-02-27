package upm.cmsc.starwars;

import java.net.URISyntaxException;

public class CustomFileUtil {
	
	public static String getFilePath(String resource){
		try {
			String path = CustomFileUtil.class.getResource(resource).toURI().getPath();
			if(System.getProperty("os.name").equals("Windows")){
				return path.substring(1);
			}
			return path;
		} catch (URISyntaxException e) {
			// TODO Do something here
		}
		return null;

	}

}
