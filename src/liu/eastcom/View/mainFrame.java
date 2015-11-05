package liu.eastcom.View;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import liu.eastcom.Control.ResourceManager;
import liu.eastcom.Control.Work;
import liu.eastcom.Control.WorkWithDFT;
import liu.eastcom.Control.WorkWithPath;

public class mainFrame extends JFrame implements ActionListener,KeyListener{

    private TextField input;
    private String inputStr="请输入包含WEB-INF的路径";
    ResourceManager rm ;
    
    
	public  mainFrame(){
		//获取公共资源
		rm = ResourceManager.getResource();
		
		//添加布局文件
		Panel pt= new Panel();
	
		input = new TextField(inputStr);
		input.setColumns(25);
		JButton ok= new JButton("BYTIME");
		JButton def= new JButton("DEFAULT");
		JButton path= new JButton("BYPATH");
		
		pt.add(input);
		pt.add(def);
		pt.add(ok);
		pt.add(path);
		add(pt);
	
		this.setSize(300, 100);
		input.addKeyListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ok.addActionListener(this);
		def.addActionListener(this);
		path.addActionListener(this);
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
	
			if(e.getActionCommand()=="BYTIME"){
				if(!input.getText().equals("") && input.getText().contains("\\WEB-INF")){
//					rm.setUpdatePath(input.getText());
					updateFile();
				}else{
					input.setText("请输入包含WEB-INF的路径");
				}
			}else if(e.getActionCommand()=="DEFAULT"){
				input.setText(rm.getUpdatePath());
			}else if(e.getActionCommand()=="BYPATH"){
				if(!input.getText().equals("")&& input.getText().contains("\\WEB-INF")){
					rm.setUpdatePath(input.getText());
					updateFilePath();
				}else{
					input.setText("请输入包含WEB-INF的路径");
				}
			}
	}
    
	void updateFile(){
		
	      Work wk= new WorkWithDFT(rm);
		  if(wk.SearchFile()>0){ //存在更新的文件
			   //拷贝文件同时生成更新日志
			  wk.CopyFile(); 
			  input.setText("更新成功");
		  }else{
			   input.setText("没有更新的文件");
		  }
	        	   
    }
	void updateFilePath(){
		
         Work wk= new WorkWithPath(rm);
		 if(wk.SearchFile()>0){ //存在更新的文件
			   //拷贝文件同时生成更新日志
			  wk.CopyFile();
			  input.setText("更新成功");
		 }else{
			  input.setText("没有更新的文件");
		 }
           
    }
	
}
