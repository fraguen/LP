import java.util.ArrayList;
import java.io.*;
import java.util.Random;

public class CrossWord{

	ArrayList<String,> wordsOnGrid = new ArrayList<String>();
	HashMap<String, String> wordsOnGridAndOrientation = new HashMap<String, String>();

	public CrossWord(int taille){

		private int taille = taille;

		ArrayList<String> listMotByTaille = getMotsByTailleMax(taille);
		/*for(int i = 0; i < listMotByTaille.size(); i++){
			System.out.println(listMotByTaille.get(i));
		}*/

		int nbCaractMaxi = taille * taille;
		int nbCasesVidesMax = nbCaractMaxi / 3;

		ArrayList<String> listMotsPick = this.getListPickMot(nbCaractMaxi, nbCasesVidesMax, listMotByTaille);
		GestionFichier ges = new GestionFichier();
		listMotsPick = ges.TriBulleDecroissant(listMotsPick);
		
		
		//voir toute la liste des mots séléctionner aléatoirement
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
		String firstWord = listMotsPick.get(0);
		String secondWord = listMotsPick.get(1);
		
		char grilleModif[][] = new char[taille][taille];

		//Placer les 2 premiers mots

		grilleModif = addWord(firstWord,secondWord,grille,taille);

		for(int indexMotPick = 2; indexMotPick < listMotsPick.length; indexMotPick++){
			for (int indexMotPlaces = 0; indexMotPlaces < wordsOnGrid.length; indexMotPlaces++){
				char[] charCom;
				charCom = this.getCharCom(wordsOnGrid.get(indexMotPlaces), listMotsPick.get(indexMotPick));
				if(charCom != null){
					String wordWantToPlace = wordsOnGrid.get(indexMotPlaces);
					String wordTested = listMotsPick.get(indexMotPick);
					grilleModif = tryToAdd(wordWantToPlace, wordTested, charCom, grille);
				}
			}

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


	public char[][] tryToAdd(String wordWantToPlace, String wordTested, char[] charCom, char[][] grille){

		String orientationWordPlaced = wordsOnGridAndOrientation.get(wordTested);
		String orientationWordWantToPlace = null;
		if(orientationWordPlaced.equals("vertical")){
			orientationWordWantToPlace = "horizontal";
		}
		else if(orientationWordPlaced.equals("horizontal")){
			orientationWordWantToPlace = "vertical";
		}

		char[][] posXYWordPlaced = findXYWordPlaced(wordTested, grille, orientationWordPlaced);


	}

	public char[][] findXYWordPlaced(String wordPlaced, char[][] grille, String orientation){

		int posX = 0;
		int posY = 0;
		int indexArray = 0;
		boolean wordFind = false;
		char[][] posXY;

		char[] wordPlacedArray = wordPlaced.toCharArray();

		while(posX < taille){

			while(posY < taille){

				if(grille[posX][posY] == wordPlacedArray[indexArray]){
					indexArray++;
					wordFind = true;
					if(orientation.equals("vertical")){
						posY++;
					}
					else{
						posX++;
					}
				}
				if(wordFind == true && indexArray == wordPlaced.length){
					if(orientation.equals("vertical")){
						posXY[0] = posX;
						posXY[1] = posY - wordPlaced.length;
					}
					else{
						posXY[0] = posX - wordPlaced.length;
						posXY[1] = posY;
					}
				}
				posY++;
			}
			posY = 0;
			posX++;

		}
		return posXY;
	}

	public char[][] addWordToGrid(String word, int posX, int posY, String orientation, char[][] grille){

		return grille;
	}
	/*
	//////////////////////////////////////////////////////////////////////////////////////
	// fait : première occurence                                                        //
	// a faire seconde occurence ou plus ( un seul mot a placer)                        //
	// A rajouter : si la lettre commune est déjà utilisée passer a la lettre comune +1 //
	//////////////////////////////////////////////////////////////////////////////////////
	*/

	public char[][] addWord(String firstWord, String secondWord, char grille[][], int taille)
	{	
		char[] charCom;
		charCom = this.getCharCom(firstWord,secondWord);
		System.out.println(" ");
		char[] charCom2;
		charCom2 = this.getCharCom(secondWord,firstWord);
		
		char[] char1 =  firstWord.toCharArray();
		char[] char2 =  secondWord.toCharArray();
		// intersection de deux lettres identiques
		int nbCar = 0;
		int nbCar2 = 0;
		
		boolean continuer1 = true; // sinon plusieurs premières lettres communes misent à diverses endroits
		
		while(nbCar < firstWord.length())
		{
			nbCar2 = 0;
			while( nbCar2 < secondWord.length() && continuer1 == true)
			{
				// trouver le même caractère aux positions nbCar et nbCar2 des mots
				// nbCar ligne de la matrice de la première chaine
				// nbCar2 ligne de la matrice de la seconde chaine

				if(charCom[nbCar] == charCom2[nbCar2] && charCom[nbCar]!='*')
				{
					if(isEmpty(grille, taille))
					{
						// placer la première lettre
						if( continuer1 == true){
							grille[nbCar2][nbCar] = charCom[nbCar];
							continuer1 = false;
						}
					}
					else
					{
						// rechercher où le mot est placé
						int posVert = 0;
						int posHor = 0;
						int posWord = 0;
						int[] posLastChar;
						posLastChar=searchWordGrille(grille, char1, posVert, posHor, posWord, taille);
						System.out.println("x : " + posLastChar[0] + " y : " + posLastChar[1] + grille[posLastChar[0]][posLastChar[1]]);
					}
					// Si on a des lettres du premier mot avant la lettre commune
					int numLettreAvant = 0;
					while(numLettreAvant < char1.length)
					{
						grille[nbCar2][numLettreAvant] = char1[numLettreAvant];
						numLettreAvant++;
					}
					// Si on a des lettres du premier mot après la lettre commune
					int numLettreApres = nbCar + 1;
					while(numLettreApres < char1.length)
					{
						grille[nbCar2][numLettreApres] = char1[numLettreApres];
						numLettreApres++;
					}
					// Si on a une lettre du second mot avant la lettre commune
					int numLettreAvantM2 = 0;
					while(numLettreAvantM2 < nbCar2)
					{
						grille[numLettreAvantM2][nbCar] = char2[numLettreAvantM2];
						numLettreAvantM2++;
					}
					
					// Si on a une lettre du second mot après la lettre commune
					int numLettreApresM2 = nbCar2 + 1;
					while(numLettreApresM2 < char2.length)
					{
						grille[numLettreApresM2][nbCar] = char2[numLettreApresM2];
						numLettreApresM2++;
					}
				}
				nbCar2++;
			}	
			nbCar++;
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		int posVert= 0;
		int posHor = 0;
		int posWord = 0;
		int[] posLastChar;
		posLastChar = searchWordGrille(grille, char1, posVert, posHor, posWord, taille);
		if(posLastChar[0] == 20)
		{
			System.out.println("x : " + posLastChar[0] + " y : " + posLastChar[1]);
		}
		else
		{
			System.out.println("x : " + posLastChar[0] + " y : " + posLastChar[1] + grille[posLastChar[0]][posLastChar[1]]);
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		wordsOnGrid.add(firstWord);
		wordsOnGridAndOrientation.add(firstWord, "horizontal");
		wordsOnGrid.add(secondWord);
		wordsOnGridAndOrientation(secondWord, "vertical");
		return grille;
	}
	
	public boolean isEmpty(char grille[][], int taille)
	{
		boolean empty = true;
		for(int i = 0; i < taille; i++)
		{
			for(int j = 0; i < taille; i++)
			{
				if(grille[i][j] != '*')
				{
					empty = false;
				}
			}
		}
		return empty;
	}
	
	// La fonction nous renverra la position du dernier caractère du mot ainsi que son orientation 
	public int[] searchWordGrille(char grille[][], char[] word, int posVert, int posHor, int posWord, int taille)
	{
		int[] tab = new int[3];
		// on utilise la valeur 20 car les mots peuvent être au maximum de 15 caractère et donc impossible qu'ils soient jusqu'à 20			
		tab[0] = 20;
		tab[1] = 20;
		tab[2] = 20;
		boolean continuer = true;
		System.out.println("posVert : " + posVert);
		System.out.println("posHor : " + posHor);
		System.out.println("taille : " + taille);
		System.out.print("word : ");
		for(int i = 0; i < word.length; i++){
			System.out.print(word[i]);
		}
		System.out.println();
		System.out.println("posWord :" + posWord);

		while (posVert < taille - 1 && continuer == true)
		{	
			System.out.println("posVert < taille - 1 && continuer == true");
			System.out.println("posVert : " + posVert);
			int posHor2 = posHor;
			while (posHor2 < taille - 1 && continuer == true)
			{
				System.out.println("posHor2 < taille - 1 && continuer == true");
				System.out.println("posHor : " + posHor2);
				
				if (grille[posVert][posHor2] == word[posWord])
				{
					System.out.println("grille[posVert][posHor2] == word[posWord]");
					System.out.println("x : " + posVert + " y : " + posHor2 + " lettre : " + grille[posVert][posHor2]);
					if(grille[posVert + 1][posHor2] == word[posWord + 1])
					{
						if(posWord + 1 == word.length)
						{ 
							System.out.println("posWord + 1 == word.length");
							// tab[2] sert a dire si le mot est vertical ou horizontal
							tab[0] = posVert + 1;
							tab[1] = posHor2;
							tab[2] = 0;
							System.out.println("x : " + tab[0] + " y : " + tab[1] + grille[tab[0]][tab[1]]);
							continuer = false;
							return tab;
						}
						else
						{	System.out.println("else [posWord + 1 == word.length]");
							return searchWordGrille(grille, word, posVert + 1, posHor2, posWord + 1, taille);
						}
					}
					else if(grille[posVert][posHor2 + 1] == word[posWord + 1])
					{
						System.out.println("grille[posVert][posHor2 + 1] == word[posWord + 1]");
						if(posWord + 1 == word.length)
						{
							System.out.println("posWord + 1 == word.length");
							// tab[2] sert a dire si le mot est vertical ou horizontal
							tab[0] = posVert;
							tab[1] = posHor2 + 1;
							tab[2] = 1;
							System.out.println("x : " + tab[0] + " y : " + tab[1] + grille[tab[0]][tab[1]]);
							continuer = false;
							return tab;
						}
						else
						{
							System.out.println("else [posWord + 1 == word.length]");
							return searchWordGrille(grille, word, posVert, posHor2 + 1, posWord + 1, taille);
						}
					}
					else
					{
						System.out.println("Le mot n'existe pas dans cette grille");
					}
				}
				posHor2++;
			}
			posVert++;
		}
		return tab;
	}
}