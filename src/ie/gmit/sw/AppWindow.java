package ie.gmit.sw;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class AppWindow {
	private JFrame frame;
	private File file; 
	private String name = ""; 
	private int ce = 0; 
	private int ca = 0;
	
	@SuppressWarnings("rawtypes")
	ArrayList<Class> clsList = new ArrayList<Class>();
	
	HashMap<String, Integer> ceMap = new HashMap<String, Integer>(); //keys are class names. values are ce totals
	HashMap<String, Integer> caMap = new HashMap<String, Integer>(); //keys are class names. values are ca totals
	HashMap<String, Field[]> fieldMap = new HashMap<String, Field[]>(); //keys are class names. values are class fields. 
	
	
	public AppWindow(){
		//Create a window for the application
		frame = new JFrame();
		frame.setTitle("B.Sc. in Software Development - GMIT");
		frame.setSize(550, 500);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		
        //The file panel will contain the file chooser
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING));
        top.setBorder(new javax.swing.border.TitledBorder("Select File to Encode"));
        top.setPreferredSize(new java.awt.Dimension(500, 100));
        top.setMaximumSize(new java.awt.Dimension(500, 100));
        top.setMinimumSize(new java.awt.Dimension(500, 100));
        
        final JTextField txtFileName =  new JTextField(20);
		txtFileName.setPreferredSize(new java.awt.Dimension(100, 30));
		txtFileName.setMaximumSize(new java.awt.Dimension(100, 30));
		txtFileName.setMargin(new java.awt.Insets(2, 2, 2, 2));
		txtFileName.setMinimumSize(new java.awt.Dimension(100, 30));
		
		JButton btnChooseFile = new JButton("Browse");
		btnChooseFile.setToolTipText("Select JAR to Encode");
        btnChooseFile.setPreferredSize(new java.awt.Dimension(90, 30));
        btnChooseFile.setMaximumSize(new java.awt.Dimension(90, 30));
        btnChooseFile.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnChooseFile.setMinimumSize(new java.awt.Dimension(90, 30));
		btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
        		JFileChooser fc = new JFileChooser("./");
        		int returnVal = fc.showOpenDialog(frame);
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
                	//File file = fc.getSelectedFile().getAbsoluteFile();
                	file = fc.getSelectedFile().getAbsoluteFile();////////////
                	////////String name = file.getAbsolutePath(); 
                	name = file.getAbsolutePath(); ////////
                	txtFileName.setText(name);
                	System.out.println("You selected the following file: " + name);
            	}
			}
        });
		
		JButton btnOther = new JButton("Inspect");
		btnOther.setToolTipText("Inspect");
		btnOther.setPreferredSize(new java.awt.Dimension(150, 30));
		btnOther.setMaximumSize(new java.awt.Dimension(150, 30));
		btnOther.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnOther.setMinimumSize(new java.awt.Dimension(150, 30));
		btnOther.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	
            	//======================================
            	//Adapted from: http://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
            		
            	JarFile jarFile;
				try {
					System.out.println("\nClasses in JAR:");
					jarFile = new JarFile(file);
					Enumeration<JarEntry> e = jarFile.entries();
					URL[] urls = { new URL("jar:file:" + name+"!/") };
	            	URLClassLoader cl = URLClassLoader.newInstance(urls);
	            	
	            	while (e.hasMoreElements()) {
	            	    JarEntry je = e.nextElement();
	            	    if(je.isDirectory() || !je.getName().endsWith(".class")){
	            	        continue;
	            	    }
	            	    // -6 because of .class
	            	    String className = je.getName().substring(0,je.getName().length()-6);
	            	    className = className.replace('/', '.');
	            	    try {
							@SuppressWarnings("rawtypes")
							Class c = cl.loadClass(className); //load class
							clsList.add(c);//add class to list 
							
							
							System.out.println(c.getName());
							
							
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

	            	}
	            	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
				
				makeFieldMap();
				printCeMap();
				makeCaMap();
            	printCaMap();
            	calcStability();
            	
			}
        });
		
	
		
		
        top.add(txtFileName);
        top.add(btnChooseFile);
        top.add(btnOther);
        frame.getContentPane().add(top); //Add the panel to the window
        
        
        //A separate panel for the programme output
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEADING));
        mid.setBorder(new BevelBorder(BevelBorder.RAISED));
        mid.setPreferredSize(new java.awt.Dimension(500, 300));
        mid.setMaximumSize(new java.awt.Dimension(500, 300));
        mid.setMinimumSize(new java.awt.Dimension(500, 300));
        
        CustomControl cc = new CustomControl(new java.awt.Dimension(500, 300));
        cc.setBackground(Color.WHITE);
        cc.setPreferredSize(new java.awt.Dimension(300, 300));
        cc.setMaximumSize(new java.awt.Dimension(300, 300));
        cc.setMinimumSize(new java.awt.Dimension(300, 300));
        mid.add(cc);
		frame.getContentPane().add(mid);
		
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setPreferredSize(new java.awt.Dimension(500, 50));
        bottom.setMaximumSize(new java.awt.Dimension(500, 50));
        bottom.setMinimumSize(new java.awt.Dimension(500, 50));
        
        JButton btnDialog = new JButton("Show Dialog"); //Create Quit button
        btnDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	AppSummary as =  new AppSummary(frame, true);
            	as.show();
			}
        });
        
        JButton btnQuit = new JButton("Quit"); //Create Quit button
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.exit(0);
			}
        });
        bottom.add(btnDialog);
        bottom.add(btnQuit);

        frame.getContentPane().add(bottom);       
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new AppWindow();
	}
	
	
	public void makeFieldMap(){
		
		
		//loop through classList
		for(int i=0; i<clsList.size();i++)
		{
			
			//grab class
			@SuppressWarnings("rawtypes")
			Class cls = clsList.get(i);
			
			//grab class fields
			Field[] fields = cls.getFields(); 
			
			//add class name and fields to hashmap
			fieldMap.put(cls.getName(), fields);
			
		}
		
		
		//loop through hashmap
		@SuppressWarnings("rawtypes")
		Iterator iterator = fieldMap.keySet().iterator();
		
		System.out.println("\nfieldMap values: ");
		
		while (iterator.hasNext()) {
		   String key = iterator.next().toString();
		   Field[] value = fieldMap.get(key);
		   
		   String fieldsStr = "";
		   ce = 0;//reset ce
		   
		   for(int i=0; i<value.length; i++)
		   {
			   fieldsStr = value[i].toString() + " "; 
			   ce++; //add to ce counter
		   }

		   //print keys and fields
		   System.out.println("Key: " + key + ". Value(s): " + fieldsStr);
		   
		   //update Ce map
		   ceMap.put(key, ce);	
 
		}
		
			
	}
	
	
	public void initCeMap() 
	{
		
		
		//loop through classList
		for(int i=0; i<clsList.size();i++)
		{	
			ceMap.put(clsList.get(i).toString(), 0);
	
		}
		

		System.out.println("\ninitial ceMap values: ");
		//loop through hashmap
		@SuppressWarnings("rawtypes")
		Iterator iterator = ceMap.keySet().iterator();
				
		while (iterator.hasNext()) {
	      String key = iterator.next().toString();
	      int value = ceMap.get(key);
	 
	      //print keys and fields
	      System.out.println("Key: " + key + ". int: " + value);
		}
			   
		
	} 
	
	
	public void printCeMap()
	{
		
		System.out.println("\nceMap values: ");
		//loop through hashmap
		@SuppressWarnings("rawtypes")
		Iterator iterator = ceMap.keySet().iterator();
				
		while (iterator.hasNext()) {
	      String key = iterator.next().toString();
	      int value = ceMap.get(key);
	 
	      //print keys and fields
	      System.out.println("Key: " + key + ". Value: " + value);
		}
					   
		
	}
	
	
	public void addtoCeMap(int ce)
	{
		System.out.println("\nceMap values: ");
		//loop through hashmap
		@SuppressWarnings("rawtypes")
		Iterator iterator = ceMap.keySet().iterator();
				
		while (iterator.hasNext()) {
	      String key = iterator.next().toString();
	      ce += ceMap.get(key);
	      
	     ceMap.put(key, ce);
	 
	      //print keys and fields
	      System.out.println("Key: " + key + ". Value: " + ce);
		}
	}
	
	
	
	public void makeCaMap()
	{
		
		//loop through hashmap
		@SuppressWarnings("rawtypes")
		Iterator iterator = fieldMap.keySet().iterator();
		
		String target = "";
		String targetCheck = "";
		ca = 0;
		
		while (iterator.hasNext()) {
		   String key = iterator.next().toString();
		   Field[] value = fieldMap.get(key);
		   
		   target = key;
		  
		   for(int i=0; i<value.length; i++)
		   {
			   targetCheck = value[i].toString();
			
			   if (targetCheck.contains(target))
			   {
				   ca++;
			   }
  
		   }

		 //update Ca map
		 caMap.put(key, ca);
 
		}
					   
	}
	
	
	public void printCaMap()
	{
		
		System.out.println("\ncaMap values: ");
		//loop through hashmap
		@SuppressWarnings("rawtypes")
		Iterator iterator = caMap.keySet().iterator();
				
		while (iterator.hasNext()) {
	      String key = iterator.next().toString();
	      int value = caMap.get(key);
	 
	      //print keys and fields
	      System.out.println("Key: " + key + ". Value: " + value);
		}
					   
	}
	
	
	public void calcStability()
	{
		//adapted from: http://stackoverflow.com/questions/23562308/java-find-matching-keys-of-two-hashmaps
		
		 System.out.println("\nPositional Stability: ");
		for (String key : ceMap.keySet())
        {
            if (caMap.get(key).equals(caMap.get(key))) {
                float ca= caMap.get(key);
                float ce = ceMap.get(key);
                float answer = ca + ce/ce;
                
              //print answer
			  System.out.println("Class: " + key + ". Stability: " + answer);
            }
         
            
        }   
            
		
	}
	
	
}