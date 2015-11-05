package liu.eastcom.Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ResourceManager {
	
	//配置文件名
	private static final String conf="config.properties";
	private static final String UPDATE_PATH="updatePath";
	private static final String OUTPUT_PATH="outputPath";
	private static final String UP_POINT="upPoint";
	
	private Properties pps= new Properties();
	
    private String updatePath="";
    private String outputPath="";
    private Date   upPoint=null;
    private Map<String,String> context = new HashMap<String,String>();
    
	private static ResourceManager rm=null;
	
	private  ResourceManager(){
		//获取配置文件
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			pps.load(new FileInputStream(conf));
			updatePath = pps.getProperty(UPDATE_PATH);
			outputPath = pps.getProperty(OUTPUT_PATH);
			String upTime = pps.getProperty(UP_POINT);
			if(upTime!=null && !"".equals(upTime)){
				upPoint=sdf.parse(upTime);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized ResourceManager getResource(){
        if (ResourceManager.rm == null) {
        	ResourceManager.rm = new ResourceManager();
        }
        return ResourceManager.rm;
	}

	public void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}

	public String getUpdatePath() {
		return updatePath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public Date getUpPoint() {
		return upPoint;
	}
	
	

}
