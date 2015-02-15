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
