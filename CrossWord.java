import java.util.ArrayList;
import java.io.*;
import java.util.Random;

public class CrossWord{

	public CrossWord(int taille){

		ArrayList<String> listMotByTaille = getMotsByTailleMax(taille);
		/*for(int i = 0; i < listMotByTaille.size(); i++){
			System.out.println(listMotByTaille.get(i));
		}*/

		int nbCaractMaxi = taille * taille;
		int nbCasesVidesMax = nbCaractMaxi / 3;

		ArrayList<String> listMotsPick = this.getListPickMot(nbCaractMaxi, nbCasesVidesMax, listMotByTaille);
		GestionFichier ges = new GestionFichier();
		listMotsPick = ges.TriBulleDecroissant(listMotsPick);
		
		
		//voir toute la liste des mots sélécteionner aléatoirement
		for (int i = 0; i < listMotsPick.size(); i++ ) {
			System.out.println(listMotsPick.get(i));
		}
		
		/* Ajout récent */
		//Grille
		//char grille[][] = new matrice[taille][taille];
		char grille[][] = new char[taille][taille];
		//initialisation de la grille
		for (int hauteur=0;hauteur<taille;hauteur++)
		{
			for (int largeur=0;largeur<taille;largeur++)
			{
				grille[hauteur][largeur]='*';
			}
		}
		
		// récupération des deux plus grands mots
		String firstWord= listMotsPick.get(0);
		String secondWord= listMotsPick.get(1);
		
		char[] charCom;
		charCom = this.getCharCom(firstWord,secondWord);
		System.out.println(" ");
		char[] charCom2;
		charCom2 = this.getCharCom(secondWord,firstWord);
		
		char[] char1 =  firstWord.toCharArray();
		char[] char2 =  secondWord.toCharArray();
		// intersection de deux lettres identiques
		int nbCar=0;
		int nbCar2=0;
		boolean continuer1 = true; // sinon plusieurs premières lettres communes misent à diverses endroits
		boolean continuer2 = true; // pour continuer sur chaques lettres après ou avant la première lettre
		boolean continuer3 = true;
		while( nbCar<firstWord.length())
		{
			//System.out.println("mot1 ");
			nbCar2=0;
			//while( nbCar2<secondWord.length()&& (continuer1 == true || continuer2 == true || continuer3 == true))
			while( nbCar2<secondWord.length()&& continuer1 == true)
			{
				//System.out.println("mot2");
				// trouver le même caractère aux position nbCar et nbCar2 des mots
				// nbCar ligne de la matrice de la première chaine
				// nbCar2 ligne de la matrice de la seconde chaine
				// System.out.println(charCom[nbCar]+"=="+charCom2[nbCar2]+"&&"+charCom[nbCar]+"!='*'");
				if(charCom[nbCar]==charCom2[nbCar2]&& charCom[nbCar]!='*')
				{
					// placer la première lettre
					if( continuer1 == true){
						//System.out.println("grille["+nbCar2+"]["+nbCar+"] : "+charCom[nbCar]);
						grille[nbCar2][nbCar]=charCom[nbCar];
						continuer1=false;
					}	
					// Si on a des lettres du premier mot avant la lettre commune
					int numLettreAvant=0;
					while(numLettreAvant<nbCar)
					{
						grille[nbCar2][numLettreAvant]=char1[numLettreAvant];
						numLettreAvant++;
					}
					// Si on a des lettres du premier mot après la lettre commune
					int numLettreApres=nbCar+1;
					while(numLettreApres<taille)
					{
						grille[nbCar2][numLettreApres]=char1[numLettreApres];
						numLettreApres++;
					}
					// Si on a une lettre du second mot avant la lettre commune
					int numLettreAvantM2=0;
					while(numLettreAvantM2<nbCar2)
					{
						grille[numLettreAvantM2][nbCar]=char2[numLettreAvantM2];
						numLettreAvantM2++;
					}
					
					// Si on a une lettre du second mot après la lettre commune
					int numLettreApresM2=nbCar2+1;
					while(numLettreApresM2<taille)
					{
						grille[numLettreApresM2][nbCar]=char2[numLettreApresM2];
						numLettreApresM2++;
					}
				}
				
				nbCar2++;
			}	
			nbCar++;
		}
		
		// Affichage de la grille
		for (int hauteur=0;hauteur<taille;hauteur++)
		{
			for (int largeur=0;largeur<taille;largeur++)
			{
				System.out.print(" | ");
				System.out.print(grille[hauteur][largeur]);
			}
			System.out.println(" |");
		}
		
	}


	public static ArrayList<String> getMotsByTailleMax(int taille){

    	ArrayList<String> listMotByTailleMax = new ArrayList<String>();
    	try {
			FileInputStream fis = new FileInputStream("TriCroissant.txt");
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            for (String line; (line = fichier.readLine()) != null; ) {
				if(line.length() <= taille)
					listMotByTailleMax.add(line);
				else 
					return listMotByTailleMax;
			}
			fichier.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return listMotByTailleMax; 
	}

	public String pickMot(ArrayList<String> listMots){

		Random rand = new Random();

		int maxRandom = listMots.size();
		int random = rand.nextInt(maxRandom);

		//System.out.println(random);

		return listMots.get(random);
	}

	public ArrayList<String> getListPickMot(int nbCaractMaxi, int nbCasesVidesMax, ArrayList<String> listMots){

		ArrayList<String> listPickMot = new ArrayList<String>();
		System.out.println("Max : " + nbCaractMaxi + "; Cases Vides : " + nbCasesVidesMax);
		int nbCaractUtilises = 0;
		while(nbCaractMaxi - nbCaractUtilises >= nbCasesVidesMax){
			String motPick = pickMot(listMots);
			nbCaractUtilises += motPick.length();
			listPickMot.add(motPick);
		}
		System.out.println("Util : " + nbCaractUtilises+ "; Cases vides restantes : "+ (nbCaractMaxi - nbCaractUtilises));
		return listPickMot;
	}
	
	public char[] getCharCom(String firstWord, String secondWord)
	{
		char[] tab1 = firstWord.toCharArray();
		char[] tab2 = secondWord.toCharArray();
		// tableau de caractères communs
		char[] tab3= new char[tab1.length];
		
		for(int carTab1=0; carTab1<tab1.length; carTab1++)
		{
			tab3[carTab1]='*';
			for(int carTab2=0; carTab2<tab2.length; carTab2++)
			{
				if(tab1[carTab1]==tab2[carTab2])
				{
					tab3[carTab1]=tab1[carTab1];
				}
			}
		}
		
		// Affichage des lettres communes des deux mots
		/*for(int nbCar=0; nbCar<tab1.length; nbCar++)
		{
			if(tab3[nbCar]!='*')
			{
				System.out.println("Le caractère : "+tab3[nbCar]+" à la position : "+nbCar+" est commun aux deux mots");
			}
		}*/
		
		return tab3;
	}
}