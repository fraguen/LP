import java.io.*;
import java.util.regex.*;
import java.text.Normalizer; 
import java.text.Normalizer.Form; 
import java.util.ArrayList;

/*
	Classe permettant la gestion des fichiers dictionnaire et autre.
*/

public class GestionFichier {

	public GestionFichier(){
     
	}

	/*
		Fonction permettant de convertir tous les caractères spéciaux (accentués, etc) en caractères normaux
	*/
	
    public static String translate(String src) {
		src = src.replace("œ", "oe");
		String tmp = src;
		String normalized = Normalizer.normalize(tmp, Normalizer.Form.NFD);
		src = normalized.replaceAll("[^\\p{ASCII}]", "");
		return src;
    }

    /*
		Fonction retournant le nombre de mots dans le dictionnaire
    */

    public static int getNbMotsDictionnaire(){
    	Matcher matcher;
    	Pattern pattern = Pattern.compile("(.+\\|\\d)"); // Expression régulière recherchant un ou plusieurs caractères suivis d'une pipe ("|"") et d'un nombre décimal
		int nbMots = 0; // Initialisation du compteur
		try {
			FileInputStream fis = new FileInputStream("thes_fr.dat"); // Ouverture du fichier dictionnaire
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8")); // Buffer d'écriture
            for (String line; (line = fichier.readLine()) != null; ) { // Tant qu'il y a des lignes à lire
				matcher = pattern.matcher(line); // Application de l'expression régulière à la ligne
				if(matcher.find()){	// Si l'expréssion match
					String[] split = matcher.group().split("\\|"); // Coupe la chaîne avant et après la pipe
					if(split[0].length() <= 15){ // Si le mot (avant la pipe) au maximun 15 de long (taille max de la grille)
						nbMots++;	//Incrémente le nombre de mots de 15 caractères maxi
					}
					String nextLine = fichier.readLine(); // Lexture de la ligne suivante car une ligne sur 2 est un mot
				}
			}			
            fichier.close(); // Fermeture du fichier
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return nbMots;
    }

    /*
		Fonction retournant le nombre de mot extraits du dictionnaire
    */

    public static int getNbMotsImportes(){
    	Matcher matcher; 
    	Pattern pattern = Pattern.compile("(.+#.+)"); // Patter recherchant un ou pls caractères suivis d'un # et d'un ou pls caractères
    												// Car écriture à la fin du fichier de mot exporté du nombre de mot
    	int nbMots;
		try {
			FileInputStream fis = new FileInputStream("word.txt"); // Ouverture du fichier de mot exportés (word.txt)
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            for (String line; (line = fichier.readLine()) != null; ) { // Tant qu'il y a des lignes à lire
				matcher = pattern.matcher(line); // Application du pattern
				if(matcher.find()){ // Si le patern match
					String[] split = matcher.group().split("#"); // Découpe la chaine avant et après le #
					// Le nombre de mot se situe après le # donc split[1] mais il y a un espace avant pour cause de lisibilité
					// donc on utilise trim() pour supprimer les espaces et on converti la chaine de caractère en Integer
					nbMots = Integer.parseInt(split[1].trim());  // Récup le nombre de mots exportés
				}
			}			
            fichier.close(); // Fermeture du fichier
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return nbMots;
    }

    /*
		Fonction permettant de mettre à jour les fichiers si le dictionnaire à changé
    */

    public static void MaJDictionnaire(){
		
		FileInputStream fis;
		BufferedReader fichier;
		BufferedWriter writerWorld = null;
		BufferedWriter writerAdj = null;
		Pattern pattern;
		Matcher matcher; 
		int nbMots = 0;  

    	try {
    		System.out.println("Début de la mise à jour du dictionnaire");	
		    fis = new FileInputStream("thes_fr.dat"); // Ouverture du fichier dictionnaire
		    fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8")); // Buffer de lecture
			writerWorld = new BufferedWriter( new FileWriter("word.txt")); // Ouverture/Création du fichier word.txt
			writerAdj = new BufferedWriter( new FileWriter("adj.txt")); // Ouverture/Création du fichier adj.txt
			pattern = Pattern.compile("(.+\\|\\d)"); // Pattern recherchant un/pls caractères suivis d'une pipe ("|") et d'un xhiffre décimal
			long startTime = System.currentTimeMillis(); // Récup de l'heure en ms
            for (String line; (line = fichier.readLine()) != null; ) { // Tant qu'il y a des lignes à lire
				matcher = pattern.matcher(line); // Application du pattern
					if(matcher.find()){ // Si le pattern match
						String[] split = matcher.group().split("\\|"); // Découpe la chaîne avant et après la pipe
						split[0] = translate(split[0]); // On convertis le mot pour qu'il ne contient aucun caractère spéciaux
						//System.out.println(split[0]);
						if(split[0].length() <= 15){ // Si le mot est au maxi de 15 caractères
							writerWorld.write(split[0] + "\n"); // On écrit le mot dans le fichier word.txt
							nbMots++; // Increment du nombre de mot
						}
						/*
						 Le fichier dictionnaire est de la forme suivante :
						 	mon_mot|nombre_sens
						 	synonymes
						 	etc..
						 	Donc on regarde s'il y a plus d'un sens pour un mot
						*/
						if(Integer.parseInt(split[1]) > 1){ 
							String ligneSyn = ""; // Variable de concaténation pour les synonymes 
							int nbCallback = Integer.parseInt(split[1]); // Nombre de sens
							for(int callback = 0; callback < nbCallback; callback++){ // Callback pour extraires tous les synonymes s'il y a pls sens
								String nextLine = fichier.readLine(); // Lecture de la ligne suivante
								/*
									Les synonymes sont de la forme suivante :

									(Nature)synonyme1 | synonyme2 | ...
								*/
								String[] adj = nextLine.split("\\)\\|"); // On recherche le premier synonyme tout en enlevant la première pipe
								adj[1] = translate(adj[1]); // On convertis tous les caractères spéciaux en nomalisé
								if(split[0].length() <= 15){ // Si le mot fait au plus 15 caractère
									if(callback == nbCallback - 1){ // S'il reste encore un sens
										ligneSyn += adj[1];  // On concatène les synomymes
									}
									else{
										ligneSyn += adj[1] + "|"; // Sinon on concatène et on ajooute un pipe final
									}
									
								}
							}
							// Quand nous avons extrait tous les synonymes 
							if(split[0].length() <= 15){ // Si le mot fait au plus 15 caractères
								ligneSyn += "\n"; // On ajoute un retour à la ligne au synonymes car il faut une ligne pour un mot et dans un autre fichier cette même ligne correspond à ces synonymes
								writerAdj.write(ligneSyn); // On écrit les synonymes das le fichier adj.txt
							}
						}
						else if (Integer.parseInt(split[1]) == 1){ // S'il n'y a qu'un sens pour un mot
							String nextLine = fichier.readLine(); // On lit la ligne suivante
							String[] adj = nextLine.split("\\)\\|"); // On coupe la chaîne
							adj[1] = translate(adj[1]); // On convertis les adjectifs en caractères normaliséq
							if(split[0].length() <= 15){ // Si le mot fait au plus 15 caractères
								writerAdj.write(adj[1] + "\n"); // On écrit dans le fichier adj.txt avec un saut de ligne
							}
						}
					}
			}			
			writerWorld.write("Nombres de mot : # " + nbMots); // On écrit à la fin du fichier de mots exportés (word.txt) le nombre de mot
            fichier.close(); // On ferme le fichier de dictionnaire
			writerWorld.close(); // On ferme le fichier de mots exportés
			writerAdj.close(); // On ferme le fichier de synonymes
			System.out.println("MaJ dictionnaire réussie!"); // On affiche la réussite de la MàJ
			System.out.println("Temps lecture du fichier : " + (System.currentTimeMillis() - startTime) + "ms"); // On affiche le temps mis pour faire la MàJ
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

    }

    /*
		Fonction permettant de faire un tri croissant ou décroissant dans le fichier de mots
    */

    public static ArrayList<String> Tri(int nblignes, String sensTri){
		ArrayList<String> listMots = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream("word.txt");  // Ouverture du fichier de mot
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8")); // Buffer de lecture
    		int ligneLues; // Variable permettant de borné la lecture du fichier

    		/*
				On ajoue chaque mots dans une liste de mot suivant un nombre maximal de mot
    		*/

            for (ligneLues = 0; ligneLues < nblignes; ligneLues++) {
            	String ligne = fichier.readLine();
				listMots.add(ligne);
			}			
			// Si le tri est croissant
			if(sensTri.equals("croissant"))
				listMots = TriBulleCroissant(listMots); // Appel à la fonction TriABulleCroissant
			//Sinon s'il est décroissant
			else if(sensTri.equals("decroissant")) // Appel à la fonction TriABulleDecroissant
				listMots = TriBulleDecroissant(listMots);
            fichier.close();//Fermeture du fichier de mot
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return listMots;
    }

    /*
		Fonction permettant d'écrire dans un fichier une liste de String
    */

    public static void EcritureFichier(String filename, ArrayList<String> tab){
    	try {
    		BufferedWriter writer = new BufferedWriter( new FileWriter(filename));
    		for (int ligneEcrite = 0; ligneEcrite < tab.size(); ligneEcrite++) {

				writer.write(tab.get(ligneEcrite) + "\n" );
						
			}
			writer.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  		
    }

    /*
		Fonction réalisant un tri à bulle croissant sur une liste de String
    */

    public static ArrayList<String>  TriBulleCroissant(ArrayList<String> list){
    	System.out.println("Début du tri croissant");
	    int n = list.size() - 1 ;
	   	for (int i = n; i >= 1; i--){
	    	for (int j = 2; j <= i; j ++){
		    	if (list.get(j - 1).length() > list.get(j).length() )
	     		{
					String  temp = list.get(j - 1) ;
					list.set(j - 1, list.get(j)) ;
					list.set(j, temp) ;
				}
			}
		}
		/*
		for (int i = 0; i < list.size(); i++ ) {
			System.out.println(list.get(i));
		}
		*/
		System.out.println("Fin du tri croissant");
		return list;
	}

	/*
		Fonction permettant de faire un tri à bulle décroissant sur une liste de String
	*/

	public static ArrayList<String>  TriBulleDecroissant(ArrayList<String> list){
	    int n = list.size() - 1 ;
	    System.out.println("Début du tri décroissant");
	   	for (int i = n; i >= 1; i--){
	    	for (int j = 1; j <= i; j ++){
		    	if (list.get(j - 1).length() < list.get(j).length() )
	     		{
					String  temp = list.get(j - 1) ;
					list.set(j - 1, list.get(j)) ;
					list.set(j, temp) ;
				}
			}
		}
		
		/*for (int i = 0; i < list.size(); i++ ) {
			System.out.println(list.get(i));
		}*/
		
		System.out.println("Fin du tri décroissant");
		return list;

	}
}