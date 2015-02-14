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
import java.io.*;
import java.util.regex.*;
import java.text.Normalizer; 
import java.text.Normalizer.Form; 
import java.util.ArrayList;
import java.util.Scanner; 

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.setProperty( "file.encoding", "UTF-8" );
		GestionFichier ges = new GestionFichier();
		int nbMotsDictionnaire = ges.getNbMotsDictionnaire();
		int nbMotsImportes = ges.getNbMotsImportes();
		System.out.println("Nombres de mots dans le dictionnaire : " + nbMotsDictionnaire);
		System.out.println("Nombres de mots dans le fichier des mots importés : " + nbMotsImportes);

		if(nbMotsImportes != nbMotsDictionnaire){
			ges.MaJDictionnaire();		
			ArrayList<String> listMots = ges.Tri(nbMotsImportes, "croissant");
			ges.EcritureFichier("TriCroissant.txt", listMots);
			ArrayList<String> listMots2 = ges.Tri(nbMotsImportes, "decroissant");
			ges.EcritureFichier("TriDecroissant.txt", listMots2);

		}

		System.out.print("Veuillez saisir la taille de la grille (max : 15; min : 5) ");
		int taille = Integer.parseInt(sc.nextLine());
		if(taille > 15)
			taille = 15;
		else if(taille < 5)
			taille = 5;
		System.out.println("Taille de la grille : " + taille +"x" + taille);
		
		CrossWord cw = new CrossWord(taille);
		//listMotByTaille = ges.TriBulleDecroissant(listMotByTaille);
		/*for(int i = 0; i < listMotByTaille.size(); i++){
			System.out.println(listMotByTaille.get(i));
		}*/
	}

}
