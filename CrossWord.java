import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.util.Random;

public class CrossWord{

	ArrayList<String> wordsOnGrid = new ArrayList<String>();
	HashMap<String, String> wordsOnGridAndOrientation = new HashMap<String, String>();
	private int tailleGrille;

	public CrossWord(int taille){

		this.tailleGrille = taille;

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

		grilleModif = addWord(firstWord,secondWord,grille);

		for (int hauteur=0;hauteur<taille;hauteur++)
		{
			for (int largeur=0;largeur<taille;largeur++)
			{
				System.out.print(" | ");
				System.out.print(grilleModif[hauteur][largeur]);
			}
			System.out.println(" |");
		}
		

		for(int indexMotPick = 2; indexMotPick < listMotsPick.size(); indexMotPick++){
			for (int indexMotPlaces = 0; indexMotPlaces < wordsOnGrid.size(); indexMotPlaces++){
				char[] charCom;
				charCom = this.getCharCom(wordsOnGrid.get(indexMotPlaces), listMotsPick.get(indexMotPick));
				if(charCom != null){
					String wordPlaced = wordsOnGrid.get(indexMotPlaces);
					String wordWantToPlace = listMotsPick.get(indexMotPick);
					System.out.println("wordsOnGrid.get(" + indexMotPlaces + ") = " + wordPlaced + "; listMotsPick.get(" + indexMotPick + ") = " + wordWantToPlace);
					grilleModif = tryToAdd(wordWantToPlace, wordPlaced, grille);
					grille = grilleModif;

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

		/*
		String orientationWordPlaced = wordsOnGridAndOrientation.get(secondWord);
		//System.out.println("orientationWordPlaced : " + orientationWordPlaced);
		int[] posXYWordPlaced = findXYWordPlaced(secondWord, grille, orientationWordPlaced);
		System.out.println("PosX mot : " + posXYWordPlaced[0] + " posY mot : " + posXYWordPlaced[1]);
		*/
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


	public char[][] tryToAdd(String wordWantToPlace, String wordTested, char[][] grille){

		String orientationWordPlaced = wordsOnGridAndOrientation.get(wordTested);
		System.out.println(wordTested);
		String orientationWordWantToPlace = null;
		if(orientationWordPlaced.equals("vertical")){
			orientationWordWantToPlace = "horizontal";
		}
		else if(orientationWordPlaced.equals("horizontal")){
			orientationWordWantToPlace = "vertical";
		}

		int[] posXYWordPlaced = findXYWordPlaced(wordTested, grille, orientationWordPlaced);
		System.out.println("posX : " + posXYWordPlaced[0] + "; posY : " + posXYWordPlaced[1]);

		char[][] grilleClone = testAddWord(wordWantToPlace, wordTested, posXYWordPlaced, orientationWordWantToPlace, grille); 



		return grille;


	}

	/*
		Fonction permettant de recherche s'il y des collisions entre les mots
	*/

	public boolean testCollision(int[] posXY, String orientation, String sens, char[][] grille){
		int posX = posXY[0], posY = posXY[1];
		boolean collision = false;
		int taille = getTailleGrille();

		System.out.println("[" + posX + ", " + posY + "]");
		if(orientation.equals("horizontal")){
			if(sens.equals("increment")){ //Test vers la droit (y++) 
				if(posY + 1 < taille){
					if( grille[posX][posY + 1] == '*' && (posY + 1) < taille ){
						if(posX - 1 == -1){
							if(grille[posX + 1][posY + 1] == '*'){
								if(posY + 2 < taille){	
									if(grille[posX][posY + 2] == '*'){

									}
									else collision = true;
								}
								else if(posY + 2 > taille){
									collision = true;
								}
							}
							else collision  = true;
						}
						else if(posX + 1 >= taille){
							collision = true;
							/*
							if(grille[posX - 1][posY + 1] == '*'){
								if(posY + 2 < taille){	
									if(grille[posX][posY + 2] == '*'){

									}
									else collision = true;
								}
								else if(posY + 2 > taille){
									collision = true;
								}			
							}
							else collision  = true;
							*/
						}
						else if( (grille[posX + 1][posY + 1] == '*')  && (grille[posX - 1][posY + 1] == '*')){
							if(posY + 2 < taille){	
								if(grille[posX][posY + 2] == '*'){

								}
								else collision = true;
							}
							else if(posY + 2 > taille){
								collision = true;
							}
						}
						else collision  = true;
					}
					else collision = true;
				}	
				else 
					collision = true;
			}
			else if(sens.equals("decrement")){ //Test vers la gauche (y--)
				if(posY - 1 > 0){
					if( grille[posX][posY - 1] == '*' && (posY - 1) > 0 ){
						if(posX - 1 == -1){
							if(grille[posX + 1][posY - 1] == '*'){
								if(posY - 2 >= 0){	
									if(grille[posX][posY - 2] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < 0){
									collision = true;
								}
							}
							else collision  = true;
						}
						else if(posX - 1 > -1){
							if(grille[posX - 1][posY - 1] == '*'){
								if(posY - 2 >= 0){	
									if(grille[posX][posY - 2] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < 0){
									collision = true;
								}
								else collision = true;
							}
							else collision  = true;
						}
						else if( (grille[posX + 1][posY - 1] == '*')  && (grille[posX - 1][posY - 1] == '*')){
							if(posY - 2 >= 0){	
								if(grille[posX][posY - 2] == '*'){

								}
								else collision = true;
							}
							else if(posY - 2 < 0){
								collision = true;
							}
							else collision = true;
						}
						else collision  = true;
					}
					else collision = true;
				}
				else collision = true;
			}
		}
		else if(orientation.equals("vertical")){ 
			if(sens.equals("increment")){ //Test vers le bas (x++) 
				if(posX + 1 < taille){
					if( grille[posX + 1][posY] == '*' && (posX + 1) < taille ){
						if(posY - 1 == -1){
							if(grille[posX + 1][posY + 1] == '*'){
								if(posX + 2 < taille){	
									if(grille[posX + 2][posY ] == '*'){

									}
									else collision = true;
								}
								else if(posX + 2 > taille){
									collision = true;
								}
							}
							else collision  = true;
						}
						else if(posY + 1 == taille){
							collision = true;
							/*
							if(grille[posX - 1][posY + 1] == '*'){
								if(posY + 2 < taille){	
									if(grille[posX][posY + 2] == '*'){

									}
									else collision = true;
								}
								else if(posY + 2 > taille){
									collision = true;
								}			
							}
							else collision  = true;
							*/
						}
						else if( (grille[posX + 1][posY + 1] == '*')  && (grille[posX - 1][posY + 1] == '*')){
							if(posX + 2 < taille){	
								if(grille[posX + 2][posY] == '*'){

								}
								else collision = true;
							}
							else if(posX + 2 > taille){
								collision = true;
							}
						}
						else collision  = true;
					}
					else collision = true;
				}	
				else 
					collision = true;
			}
			else if(sens.equals("decrement")){ //Test vers le haut (x--)
				if(posX - 1 > 0){
					if( grille[posX - 1][posY] == '*' && (posX - 1) > 0 ){
						if(posY - 1 == -1){
							if(grille[posX - 1][posY + 1] == '*'){
								if(posX - 2 >= 0){	
									if(grille[posX - 2][posY] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < 0){
									collision = true;
								}
							}
							else collision  = true;
						}
						else if(posY - 1 > -1){
							if(grille[posX - 1][posY - 1] == '*'){
								if(posX - 2 >= 0){	
									if(grille[posX - 2][posY] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < 0){
									collision = true;
								}
							}
							else collision = true;
						}
						else if( (grille[posX + 1][posY - 1] == '*')  && (grille[posX - 1][posY - 1] == '*')){
							if(posX - 2 >= 0){	
								if(grille[posX - 2][posY] == '*'){

								}
								else collision = true;
							}
							else if(posY - 2 < 0){
								collision = true;
							}
						}
						else collision  = true;
					}
					else collision = true;
				}
				else collision = true;
			}
		}
		return collision;
	}

	/*
		Fonction testant si le mot que l'on veux placer peux être placé sur la grille que l'on clone si tout va bien
		la grille clonée est fusionnée avec la grille original et est supprimer
	*/

	public char[][] testAddWord(String wordWantToPlace, String wordsOnGrid, int[] posXYWordPlaced, String orientationWordWantToPlace, char[][] grille){

		
		char[] charCom;
		charCom = this.getCharCom(wordsOnGrid, wordWantToPlace);
		char[] charCom2;
		charCom2 = this.getCharCom(wordWantToPlace, wordsOnGrid);
		char[][] grilleClone = grille;
		String orientationWordPlaced = wordsOnGridAndOrientation.get(wordsOnGrid);

		char[] wordWantToPlaceArray =  wordWantToPlace.toCharArray();
		char[] wordsOnGridArray =  wordsOnGrid.toCharArray();
		// intersection de deux lettres identiques
		int charAtWordWantToPlace = 0;
		int chatAtWordOnGrid = 0;
		int charCommPlaced = 0;

		int[] posXYCourant = {0, 0};

		int taille = getTailleGrille();
		
		boolean collision = false; // sinon plusieurs premières lettres communes misent à diverses endroits

		int posX = posXYWordPlaced[0], posY = posXYWordPlaced[1];
		for(int i = 0; i < charCom.length; i++){
			System.out.println(charCom[i]);
		}

		while(charCommPlaced < charCom.length - 1){
			System.out.println("charCom[" + charCommPlaced + "] = " + charCom[charCommPlaced]);
			if(orientationWordPlaced.equals("vertical")){
				if(grilleClone[posX][posY] == charCom[charCommPlaced]){
					if(wordWantToPlaceArray[charAtWordWantToPlace] == charCom[charCommPlaced]){
						System.out.println("[" + posX + ", " + posY + "]; charCom[charCommPlaced] = " + charCom[charCommPlaced]);
						int tempPosX = posX;
						int tempPosY = posY;
						int tempCharAtWordWantToPlace = charAtWordWantToPlace;

						while(tempCharAtWordWantToPlace > 0 && collision == false){
							posXYCourant[0] = tempPosX;
							posXYCourant[1] = tempPosY;
							System.out.println("testCollision");
							if(testCollision(posXYCourant, "horizontal", "decrement", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
								grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
							else{
								grilleClone = grille;
								collision = true;
								System.out.println(collision);
							}
							System.out.println(wordWantToPlaceArray[tempCharAtWordWantToPlace]);
							tempPosY--;
							tempCharAtWordWantToPlace--;
						}
						if(tempCharAtWordWantToPlace <= 0){
							tempCharAtWordWantToPlace = charAtWordWantToPlace;
							tempPosX = posX;
							tempPosY = posY;
						}
						while(tempCharAtWordWantToPlace < wordWantToPlace.length() && collision == false){
							posXYCourant[0] = tempPosX;
							posXYCourant[1] = tempPosY;
							System.out.println("testCollision");
							if(testCollision(posXYCourant, "horizontal", "increment", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
								grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
							else{
								grilleClone = grille;
								collision = true;
								System.out.println(collision);
							}

							System.out.println(wordWantToPlaceArray[tempCharAtWordWantToPlace]);
							tempPosY++;
							tempCharAtWordWantToPlace++;
						}
						if(collision){
							if(posX < taille - 1){
								posX++;
							}
							else if(charCommPlaced < charCom.length){
								charCommPlaced++;
							}

						}
						else if(tempCharAtWordWantToPlace >= wordWantToPlaceArray.length){
							if(posX < taille - 1){
								posX++;
							}

						}
					}
					else if(charAtWordWantToPlace < wordWantToPlaceArray.length - 1){
						charAtWordWantToPlace++;
						System.out.println("charAtWordWantToPlace++ : " + charAtWordWantToPlace);
					}
					else
						break;
				}
				else{
					if(posX < taille - 1){
						posX++; 
						System.out.println("X++ : " + posX) ;
					}
					else if(charCommPlaced < charCom.length - 1){
						charCommPlaced++;
						charAtWordWantToPlace = 0;
						System.out.println("++ : " + charCom[charCommPlaced]);
						posX = 0;
						collision = false;
					}
					else
						grilleClone = grille;
				}
			}
			else if(orientationWordPlaced.equals("horizontal")){
				System.out.println(posX + " ! " + posY);
				if(grilleClone[posX][posY] == charCom[charCommPlaced]){
					if(wordWantToPlaceArray[charAtWordWantToPlace] == charCom[charCommPlaced]){
						System.out.println("[" + posX + ", " + posY + "]; charCom[charCommPlaced] = " + charCom[charCommPlaced]);
						int tempPosX = posX;
						int tempPosY = posY;
						int tempCharAtWordWantToPlace = charAtWordWantToPlace;

						while(tempCharAtWordWantToPlace > 0 && collision == false){
							posXYCourant[0] = tempPosX;
							posXYCourant[1] = tempPosY;
							System.out.println("testCollision");
							if(testCollision(posXYCourant, "vertical", "decrement", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
								grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
							else{
								grilleClone = grille;
								collision = true;
								System.out.println(collision);
							}
							System.out.println(wordWantToPlaceArray[tempCharAtWordWantToPlace]);
							posX--;
							tempCharAtWordWantToPlace--;
						}
						if(tempCharAtWordWantToPlace <= 0){
							tempCharAtWordWantToPlace = charAtWordWantToPlace;
							tempPosX = posX;
							tempPosY = posY;
						}
						while(tempCharAtWordWantToPlace < wordWantToPlace.length() && collision == false){
							posXYCourant[0] = tempPosX;
							posXYCourant[1] = tempPosY;
							System.out.println("testCollision");
							if(testCollision(posXYCourant, "vertical", "increment", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
								grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
							else{
								grilleClone = grille;
								collision = true;
								System.out.println(collision);
							}
							System.out.println(wordWantToPlaceArray[tempCharAtWordWantToPlace]);
							tempPosX++;
							tempCharAtWordWantToPlace++;
						}
						if(collision){
							if(posY < taille - 1){
								posY++;
							}
							else if(charCommPlaced < charCom.length){
								charCommPlaced++;
							}
						}
						else if(tempCharAtWordWantToPlace >= wordWantToPlaceArray.length){
							if(posX < taille - 1){
								posX++;
							}

						}
					}
					else if(charAtWordWantToPlace < wordWantToPlaceArray.length - 1){
						charAtWordWantToPlace++;
						System.out.println("charAtWordWantToPlace++ : " + charAtWordWantToPlace);
					}
					else
						break;
				}
				else{
					if(posY < taille - 1){
						posY++; 
						System.out.println("Y++ : " + posY);
					}
					else if(charCommPlaced < charCom.length - 1){
						charCommPlaced++;
						charAtWordWantToPlace = 0;
						System.out.println("++ : " + charCom[charCommPlaced]);
						posY = 0;
					}
					else
						grilleClone = grille;
				}
			}
		}
		return grilleClone;
	}

	/*
		Fonction permettant de rechercher la position initiale en X et Y d'un mot placé dans la grille
	*/

	public int[] findXYWordPlaced(String wordPlaced, char[][] grille, String orientation){

		int posX = 0;
		int posY = 0;
		int indexArray = 0;
		boolean wordFind = false;
		int[] posXY = {0,0};
		int taille = getTailleGrille();

		char[] wordPlacedArray = wordPlaced.toCharArray();

		while(posX < taille){

			while(posY < taille && posX < taille){
				//System.out.println("Coord : [" + posX + "," + posY + "]");

				//System.out.println(indexArray);

				//System.out.println(grille[posX][posY] + "; " +  wordPlacedArray[indexArray]);
				if(grille[posX][posY] == wordPlacedArray[indexArray] && indexArray < wordPlaced.length()){
					indexArray++;
					wordFind = true;
					if(orientation.equals("vertical")){
						if(posX < taille)
							posX++;
					}
					else{
						if(posY < taille)
							posY++;
					}
					if(wordFind == true && indexArray > wordPlaced.length() - 1){
						wordFind = false;
						indexArray = 0;
						//System.out.println("OK");
						//System.out.println("Final : [" + posX + "," + posY + "]");
						if(orientation.equals("vertical")){
							posXY[0] = posX - wordPlaced.length();
							posXY[1] = posY;
						}
						else{
							posXY[0] = posX;
							posXY[1] = posY - wordPlaced.length(); 
						}
					}
				}
				else if(posY < taille)
					posY++;
			}

			posY = 0;
			if(posX < taille)
				posX++;
			//else System.out.println("Supp");

		}
		//System.out.println("[" + posXY[0] + "," + posXY[1] + "]");
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

	public char[][] addWord(String firstWord, String secondWord, char grille[][])
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
					if(isEmpty(grille, getTailleGrille()))
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
						posLastChar=searchWordGrille(grille, char1, posVert, posHor, posWord, getTailleGrille());
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
		/*posLastChar = searchWordGrille(grille, char1, posVert, posHor, posWord, getTailleGrille());
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
		*/
		wordsOnGrid.add(firstWord);
		wordsOnGridAndOrientation.put(firstWord, "horizontal");
		wordsOnGrid.add(secondWord);
		wordsOnGridAndOrientation.put(secondWord, "vertical");
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

	public int getTailleGrille(){
		return tailleGrille;
	}
}