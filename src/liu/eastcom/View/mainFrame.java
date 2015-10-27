package liu.eastcom.View;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;

import liu.eastcom.Control.Work;

public class mainFrame extends JFrame implements ActionListener,KeyListener{

    private TextField input;
    private String updatePath="";
    private String outputPath="";
    private Date   upPoint=null;
    private String inputStr="请输入包含WEB-INF的路径";
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	public  mainFrame(){
		
		Properties pps = new Properties();
		String upTime = pps.getProperty("upPoint");
		try {
			pps.load(new FileInputStream("config.properties"));
			updatePath = pps.getProperty("updatePath");
			outputPath = pps.getProperty("outputPath");
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
		
		
		Panel pt= new Panel();
	
		input = new TextField(inputStr);
		input.setColumns(30);
		JButton ok= new JButton("YES");
		JButton cancle= new JButton("CANCLE");
		JButton def= new JButton("DEFAULT");
		
		pt.add(input);
		pt.add(ok);
		pt.add(cancle);
		pt.add(def);
		add(pt);
	
		this.setSize(300, 100);
		input.addKeyListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ok.addActionListener(this);
		cancle.addActionListener(this);
		def.addActionListener(this);
	}
	
	public static void main(String [] args){
		mainFrame mf = new mainFrame();
		mf.show();
	}


	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	   System.out.println("KeyPressed: "+ arg0.getKeyCode() );
	   if(arg0.getKeyCode()==10){
	     input.setText("");
	   }
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if(e.getActionCommand()=="YES"){
			if(!input.getText().equals("")){
			System.out.println("Ok is be pressed");
			updateFile();
			
			}else{
				input.setText("请输入包含WEB-INF的路径");
			}
		}else if(e.getActionCommand()=="DEFAULT"){
			input.setText(updatePath);
		}else{
		  input.setText("");	
		}
	}
    
	void updateFile(){
		
        String filePath=input.getText();
        System.out.println("=========>>"+filePath);
         	    
	           Work wk= new Work(outputPath,upPoint);
	          
	           if(wk.getProjectPath(filePath)!=null){//判断路径是否合法
	        	   if(SeachFile(wk, filePath)>0){ //存在更新的文件
	        		   //拷贝文件同时生成更新日志
	        		  
	        		  CopyFandWriteL(wk,filePath);
	        		  input.setText("更新成功");
	        	   }else{
	        		   input.setText("没有更新的文件");
	        	   }
	        	   
	           }else{
	        	       System.out.println("fdfdfd");
	        	       input.setText("路径中应该至少包含WEB-INF");
	           }            	
         	
           
           
    }
	 /**
	  * 搜寻更新文件
	  * 使用深度遍历
	  * */
	 private int SeachFile(Work wk, String filePath){
		 
		  int num=0;
		 String projectPath=wk.getProjectPath(filePath);    
         File projectF= new File(projectPath);
         File temp;
         Stack<File> fS= new Stack<File>();
         
         fS.push(projectF);
         while(!fS.empty()){
             //弹出栈顶
             temp=fS.pop();
             if(temp.isFile()){
            	 
                  if(wk.isUpdateFile(temp)){
                	  wk.update.add(temp);
                  }
             }else if(temp.isDirectory()){
                 
                File [] content= temp.listFiles();
                for(int i=0;i<content.length;i++){
                  //  System.out.println("push: " + content[i]);
    	            fS.push(content[i]);
                }
             }
         }
         num=wk.update.size();
         return num;
	 }
	 
	 /**
	  * 拷贝文件同时生成日志文件
	  * */
	 private void CopyFandWriteL(Work wk, String filePath){
			
		  Calendar now= Calendar.getInstance();  
		  int Am_Pm  = now.get(Calendar.AM_PM);
		  int year = now.get(Calendar.YEAR);
         int month  = now.get(Calendar.MONTH)+1;
         int day =   now.get(Calendar.DATE);
         int hour = now.get(Calendar.HOUR);
         int minute = now.get(Calendar.MINUTE);
         int second = now.get(Calendar.SECOND);
		  String deskPath=outputPath+"\\"+wk.getCurrentDir(filePath);
         File deskFile= new File(deskPath);
         File logFile= new File(deskPath+"\\update-"+year+"."+month+"."+day+".txt");
        
         //在桌面创建目录
         deskFile.mkdirs();
         System.out.println("//////////////////////////////");
         try {
			FileOutputStream fso= new FileOutputStream(logFile);
			//DataOutputStream dos= new DataOutputStream(fso);
			File toDesk, src;
			String p;
			for(int i=0;i<wk.update.size();i++){
				   //写入更新日志
				  p=wk.update.elementAt(i).getAbsolutePath();
				 
	     
				  fso.write(String.valueOf(i+1).getBytes());
				  fso.write(" >> ".getBytes());
				  fso.write(p.substring(p.indexOf(wk.getCurrentDir(filePath))).getBytes());
				  fso.write("\r\n\r\n".getBytes());
	               //拷贝更新文件到桌面
	              src= new File(p);
	              toDesk= new File(deskPath+"\\"+wk.getDeskPath(p));
	              
                 toDesk.getParentFile().mkdirs();
              
                 //拷贝文件
	              wk.copyFile(src, toDesk);
	        }
			String updateTime;
			     if(Am_Pm==1){
			    	 updateTime = " PM " + hour+":"+minute+":"+second;
			     }else{
			    	 updateTime = " AM " + hour+":"+minute+":"+second;
			     }
			      
			     fso.write("===================\r\nupdate num:  ".getBytes());
			     fso.write(String.valueOf(wk.update.size()).getBytes());
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
	
}
