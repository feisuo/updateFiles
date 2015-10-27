package liu.eastcom.Control;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;


public class Work{

	//存储路径
	private static String outputPath; 
	private static int BufferLength=512;
	
    public Vector<File> update=null;
    Calendar now;
    
    public Work(String outputPath,Date timePoint){
      this.update= new Vector<File>();
      this.outputPath =outputPath;
      if(timePoint!=null){
    	  Calendar upPoint = Calendar.getInstance();
    	  upPoint.setTime(timePoint);
    	  this.now= upPoint;
      }else{
    	  this.now= setTimePoint();
      }
    }

    /**
     *判断是否是要更新的文件
     **/
    public boolean isUpdateFile(File f){
    	  // 默认从上次更新时间开始 
          long time=f.lastModified();
          Calendar cal= Calendar.getInstance();                   
          cal.setTimeInMillis(time);
          if(cal.after(now)){
        	   System.out.println("file: "+f.getName()+" |\t"+cal.getTimeInMillis()+" |\t"+ cal.getTime());
               return true;
          }
           return false;
    }
    /**
     *设置时间点
     ***/
    @SuppressWarnings("deprecation")
	Calendar setTimePoint(){
           //获取当前系统时间
           Calendar now= Calendar.getInstance();      
           //起始时间为当天00:00:00  
           int year = now.get(Calendar.YEAR);
           int month  = now.get(Calendar.MONTH);
           int day =   now.get(Calendar.DATE);
           now.set(year, month, day-1);           
           return now;
     }

    /**
     * 获取class文件目录
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
                  System.out.println("wook.getStringPath():"+currentDir);
                  
              }
         }
         return null;
    }
  
    
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
     * 复制文件
     * */
    
    public void copyFile(File f1, File f2){
    	
    	try {
    	
			FileInputStream in=new FileInputStream(f1);
			FileOutputStream out= new FileOutputStream(f2);
		
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

    
}