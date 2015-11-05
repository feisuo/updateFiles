package liu.eastcom.Control;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import liu.eastcom.util.IOUtil;

public class WorkWithPath extends Work{
		
	private Path filesPath = Paths.get(System.getProperty("user.dir") + System.getProperty("file.separator") + "filePath.txt");
    
	//资源 上下文信息
   	private ResourceManager rs;
    private ArrayList<String> dictonary;
    private ArrayList<File> findedFiles;
    private ArrayList<String> unfinded;
    private String rootPath="";
    

	public WorkWithPath(ResourceManager rs) {
        System.out.println("[PD] Loading path dictonary...");
        this.rs=rs;
        this.rootPath=rs.getUpdatePath();
        dictonary= new ArrayList<String>();
        findedFiles= new ArrayList<File>();
        unfinded = new ArrayList<String>();
        
        // Load passwords from file in array
        try (BufferedReader reader = Files.newBufferedReader(filesPath, Charset.forName("UTF-8"))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                	String temp =line.substring(line.indexOf("WEB-INF")+7);
                    dictonary.add(temp.replace("/", "\\"));
                    i++;
                }
        } catch (IOException ex) {
            System.err.println("Unable to open " + filesPath.toAbsolutePath() + "!");
        }       
        
        System.out.println("[PD] Loaded " + (dictonary!=null?dictonary.size():0));
	}

	public int SearchFile() {
		if(dictonary!=null){
	    	for(int i=0;i<this.dictonary.size();i++){
	    		String key =this.rootPath+this.dictonary.get(i);
	    		System.out.println("searchFiles >> "+ key);
	    		File temp = new File(key);
	    		if(temp.exists()){
	    			this.findedFiles.add(temp);
	    		}else{
	    			unfinded.add(key);
	    		}
	    	}
	    	if (findedFiles!=null && findedFiles.size()>0){
				return findedFiles.size();
	    	}else{
				return 0;
	    	}
		}else{
			return 0;
		}
	}

	public void CopyFile() {
		IOUtil.getInstance().WriteAndCopyFile(this.findedFiles, rs.getUpdatePath(), rs.getOutputPath());
	}

}
