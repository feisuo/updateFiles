package liu.eastcom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public final class IOUtil {

	private static IOUtil instance;
	private static int BufferLength=512;
	
    private Calendar now= Calendar.getInstance();  
	private int Am_Pm;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	
	private IOUtil(){
		  Am_Pm  = now.get(Calendar.AM_PM);
		  year = now.get(Calendar.YEAR);
		  month  = now.get(Calendar.MONTH)+1;
		  day =   now.get(Calendar.DATE);
		  hour = now.get(Calendar.HOUR);
		  minute = now.get(Calendar.MINUTE);
		  second = now.get(Calendar.SECOND);
	}
	
	public static synchronized IOUtil getInstance(){
		if(IOUtil.instance==null){
			IOUtil.instance = new IOUtil();
		}
		return IOUtil.instance;
	}
	
	public void WriteAndCopyFile(ArrayList<File> files,String inPath,String outputPath){
		
	     //在桌面创建目录
		 String projectRoot=getCurrentDir(inPath);
	     String deskPath=outputPath+"\\"+projectRoot;
         File deskFile= new File(deskPath);
         File logFile= new File(deskPath+"\\update-"+year+"."+month+"."+day+".txt");
         deskFile.mkdirs();
         
         
         System.out.println("//////////////////////////////");
         try {
			FileOutputStream fso= new FileOutputStream(logFile);
			System.out.println(logFile.getAbsolutePath());
			File toDesk, src;
			String p;
			for(int i=0;i<files.size();i++){
				   //写入更新日志
				  p=files.get(i).getAbsolutePath();
				 
				  fso.write(String.valueOf(i+1).getBytes());
				  fso.write(" >> ".getBytes());
				  fso.write(p.substring(p.indexOf(projectRoot)).getBytes());
				  fso.write("\r\n\r\n".getBytes());
	               //拷贝更新文件到桌面
	              src= new File(p);
	              toDesk= new File(deskPath+"\\"+getDeskPath(p));
                  toDesk.getParentFile().mkdirs();
              
                 //拷贝文件
	              CopyFile(src, toDesk);
	        }
			String updateTime;
		     if(Am_Pm==1){
		    	 updateTime = " PM " + hour+":"+minute+":"+second;
		     }else{
		    	 updateTime = " AM " + hour+":"+minute+":"+second;
		     }
		      
		     fso.write("===================\r\nupdate num:  ".getBytes());
		     fso.write(String.valueOf(files.size()).getBytes());
		     fso.write("\r\nupdate time: ".getBytes());
		     fso.write(String.valueOf(updateTime).getBytes());
		     fso.write("\r\n===================\r\n".getBytes());
			 fso.close();
		  } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			  e.printStackTrace();
		  }catch(IOException e){
			  e.printStackTrace();
		  }finally{
			
		  }
	}
	
    /**
     * 复制文件
     * */
    
    private void CopyFile(File src, File target){
    	
    	try {
    	
			FileInputStream in=new FileInputStream(src);
			FileOutputStream out= new FileOutputStream(target);
		
			byte[] buffer= new byte[BufferLength];
			while(true){
				
				int ins=in.read(buffer);
				if(ins == -1){
					in.close();
					out.flush();
					out.close();
					break;
				}else{
					out.write(buffer,0,ins);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			System.out.println("copyFile\t\n"+e.getCause());
			System.out.println("=====================");
			e.printStackTrace();
		}
    	
    }
    
    /**
     * 返回到 WEB-INF的路径
     * **/
    public String getProjectPath(String filePath){

        String fpath="";
        String currentDir="";
        StringTokenizer t= new StringTokenizer(filePath, "\\");
        for( fpath=currentDir=t.nextToken(); t.hasMoreTokens();){
              
           currentDir= t.nextToken(); 
           if(currentDir.equals("WEB-INF")){
                  return fpath;
             }else{
                 fpath+="\\"+currentDir;
             }
        }
        return null;
   }
 
   /**
    * 返回tomcat容器中位置，例如 webapps/yrtz....  返回  yrtz....
    * **/  
   public String getDeskPath(String filePath){

   	 int s=filePath.indexOf("webapps");
   	 if(s>0){
		 String str= filePath.substring(s+8);
          return str;
        }else{
          return null;
        }
  }
   
   /**
    * 获取tomat容器中 项目根目录
    **/
   public String getCurrentDir(String filePath){
   
       String lastDir="";
       String currentDir="";
       for(StringTokenizer t = new StringTokenizer(filePath,"\\"); t.hasMoreTokens();){
              currentDir=t.nextToken();
             if(currentDir.equals("WEB-INF")){
                  return lastDir;
             }else{
                 lastDir=currentDir;
             }
       }
       return null;
   }

}
