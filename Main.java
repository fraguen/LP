import java.io.*;
import java.util.regex.*;
import java.text.Normalizer; 
import java.text.Normalizer.Form; 
import java.util.ArrayList;
import java.util.Scanner; 

/*
	Classe principale
*/

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in); // Déclaration d'un scanner pour lire les entrées clavier
		System.setProperty( "file.encoding", "UTF-8" ); // On défini l'encodage system de la JVM en UTF-8
		GestionFichier ges = new GestionFichier(); // Instantie un nouveau GestionFichier
		int nbMotsDictionnaire = ges.getNbMotsDictionnaire();	// On récupère le nombre de mots dans le dictionnaire
		int nbMotsImportes = ges.getNbMotsImportes(); // On récupère le nombre de mots exportés
		System.out.println("Nombres de mots dans le dictionnaire : " + nbMotsDictionnaire);	// On affiche le nombre de mots dans le dictionnaire
		System.out.println("Nombres de mots dans le fichier des mots importés : " + nbMotsImportes);	// On affiche le nombre de mot dans le fichier de mots exportés

		if(nbMotsImportes != nbMotsDictionnaire){	// Si le nombre de mot dans les 2 fichiers est diférents
			ges.MaJDictionnaire(); // On met à jour le dictionnaire et les fichiers annexes
			ArrayList<String> listMots = ges.Tri(nbMotsImportes, "croissant");	// On fait un tri croissant des mots exportés
			ges.EcritureFichier("TriCroissant.txt", listMots);	// On écrit les mots triés dans le fichier TriCroissant.txt
			ArrayList<String> listMots2 = ges.Tri(nbMotsImportes, "decroissant");	// On fait un tri décroissant des mots exportés
			ges.EcritureFichier("TriDecroissant.txt", listMots2);	// On écrit les mots triés dans le fichier TriDecroissant.txt

		}
		System.out.print("Veuillez saisir la taille de la grille (max : 15; min : 5) "); // On demande à l'utilisateur de saisir la taille de la grille
		int taille = Integer.parseInt(sc.nextLine());	// On lit le nombre entré
		if(taille > 15) // S'il est plus grand que 15
			taille = 15;	// On le force à 15
		else if(taille < 5) // S'il est plus petit que 5
			taille = 5;	// On le force à 5
		System.out.println("Taille de la grille : " + taille +"x" + taille); // On affiche la taille de la grille
		
		CrossWord cw = new CrossWord(taille);	// On cré un nouveau mot croisé de la taille définie précédement
		
		//listMotByTaille = ges.TriBulleDecroissant(listMotByTaille);
		/*for(int i = 0; i < listMotByTaille.size(); i++){
			System.out.println(listMotByTaille.get(i));
		}*/
	}

}
