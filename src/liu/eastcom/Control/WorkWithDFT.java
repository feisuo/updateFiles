package liu.eastcom.Control;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;
import java.util.StringTokenizer;

import liu.eastcom.util.IOUtil;

public class WorkWithDFT extends Work{
	
    //资源 上下文信息
	private ResourceManager rs;
	//更新时间点
	private Calendar now;
	//更新文件存放容器
	private ArrayList<File> updated;
	
	public WorkWithDFT(ResourceManager rs) {
		this.rs =rs;
		updated= new ArrayList<File>();
		
	    if(rs.getUpPoint()!=null){
	    	  Calendar upPoint = Calendar.getInstance();
	    	  upPoint.setTime(rs.getUpPoint());
	    	  this.now= upPoint;
	      }else{
	    	  this.now= setTimePoint();
	    }
	}

	public int SearchFile() {
	  
	   int num=0;
	   
       File projectF= new File(rs.getUpdatePath());
       File temp;
       Stack<File> fS= new Stack<File>();
       
       fS.push(projectF);
       while(!fS.empty()){
           //弹出栈顶
           temp=fS.pop();
           if(temp.isFile()){
          	 
                if(isUpdateFile(temp)){
              	   updated.add(temp);
                }
           }else if(temp.isDirectory()){
               
              File [] content= temp.listFiles();
              for(int i=0;i<content.length;i++){
                //  System.out.println("push: " + content[i]);
  	            fS.push(content[i]);
              }
           }
       }
       num= updated==null?0:updated.size();
       
       return num;
    }

	public void CopyFile() {
		IOUtil.getInstance().WriteAndCopyFile(this.updated, rs.getUpdatePath(), rs.getOutputPath());
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

}
