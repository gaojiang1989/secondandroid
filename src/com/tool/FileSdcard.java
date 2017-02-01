package com.tool; 

import android.annotation.SuppressLint;
import java.io.File;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class FileSdcard {
	@SuppressLint("SdCardPath")
	private static String filename = "/mnt/sdcard/gaojiang/test.txt";
	
	public static void log(String msg){
		msg = msg+"\n";
		File file = new File(filename);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		
		PrintStream out = null;
		
		try{
//			out = new PrintStream(new FileOutputStream(file));
//			out.println(msg);
			
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(raf.length());
			raf.write(msg.getBytes());
			raf.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			
		}
	}
}
