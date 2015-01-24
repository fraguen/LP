import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.util.regex.*;

public class Main {
	public static void main(String[] args) {
		//Nous déclarons nos objets en dehors du bloc try/catch
		System.setProperty( "file.encoding", "UTF-8" );
		System.out.println(System.getProperty("file.encoding"));
	    FileInputStream fis;
	    FileOutputStream fos;
		BufferedInputStream bis;   
		BufferedOutputStream bos;  
		BufferedWriter writer = null;
		Pattern pattern;
		Matcher matcher;   
		int bytesRead=0;
	    	try {
		    bis = new BufferedInputStream(new FileInputStream(new File("thes_fr.dat")));
			fos = new FileOutputStream(new File("out.dat"));
			writer = new BufferedWriter( new FileWriter("out.dat"));
			//pattern = Pattern.compile("^([.]+[\\|]{1}[1]{1})$");
			pattern = Pattern.compile("(.+\\|1)");
			System.out.println(pattern);
			byte[] buf = new byte[32];
			long startTime = System.currentTimeMillis();
			String str = null;
			while((bytesRead = bis.read(buf)) != -1){
				str += new String(buf, 0, bytesRead);
				matcher = pattern.matcher(str);
				if(matcher.find()){
					System.out.println(matcher.group());
					String[] split = matcher.group().split("\\|");
					System.out.println(split[0]);
					writer.write(split[0] + "\n");
					str = new String();
					matcher.reset();
			}
					
					//str += (char)bit;		

            }
			bis.close();
			fos.close();
			writer.close();
			System.out.println("Temps de lecture avec BufferedInputStream : " + (System.currentTimeMillis() - startTime) + "ms");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}

}
