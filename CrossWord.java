import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.util.Random;


/*
	Classse permettant la gestion du mot croisé
*/
public class CrossWord{

	ArrayList<String> wordsOnGrid = new ArrayList<String>(); // Création d'un liste pour les mots présents sur la grille
	HashMap<String, String> wordsOnGridAndOrientation = new HashMap<String, String>(); // Création d'un HashMap pour mémorisation de la position d'un mot placé sur la grille
	private int tailleGrille;

	public CrossWord(int taille){

		this.tailleGrille = taille;

		ArrayList<String> listMotByTaille = getMotsByTailleMax(taille); // Liste des mot triés par taille

		int nbCaractMaxi = taille * taille; // Nb de caractère maxi plaçables dans la grille
		int nbCasesVidesMax = nbCaractMaxi / 3; // Case vide maxi = 1/3 du max

		ArrayList<String> listMotsPick = this.getListPickMot(nbCaractMaxi, nbCasesVidesMax, listMotByTaille); // Liste des mots a placés
		GestionFichier ges = new GestionFichier(); // Appel au gestionnaire de fichier
		listMotsPick = ges.TriBulleDecroissant(listMotsPick); // trié cette liste dans l'ordre décroissant
		int indexMotPlaces = 0; // Initialisation du nb de mot placés
		
		
		// Debug Voir toute la liste des mots séléctionner aléatoirement 
		for (int i = 0; i < listMotsPick.size(); i++ ) {
			System.out.println(listMotsPick.get(i));
		}
		

		// Création de la grille
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

		// Grille tampon
		
		char grilleModif[][] = new char[taille][taille];

		//Placer les 2 premiers mots

		grilleModif = addWord(firstWord,secondWord,grille);
		grille = copieGrille(grilleModif, taille);
		
		/*System.out.print("  ");
		for(int index = 0; index < taille; index++){
			System.out.print(" | ");
			System.out.print(indexHorizontaux[index]);
		}
		System.out.print(" | ");
		System.out.println();

		for (int hauteur=0;hauteur<taille;hauteur++)
		{	
			System.out.print(indexVerticaux[hauteur]);
			for (int largeur=0;largeur<taille;largeur++)
			{	
				System.out.print(" | ");
				System.out.print(grilleModif[hauteur][largeur]);
			}
			System.out.println(" |");
		}*/

		affichageGrille(grilleModif);

		// Essaye de placés tous les mots dans la grille 
		// Début à l'index 2 car les 2 premiers mot sont placés avant
		for(int indexMotPick = 2; indexMotPick < listMotsPick.size(); indexMotPick++){
			indexMotPlaces = 0;
			System.out.println("CrossWord(87) : Mot suivant a placé! : " + listMotsPick.get(indexMotPick));

			// Test sur tous les mots placés sur la grille
			while(indexMotPlaces < wordsOnGrid.size()){
				char[] charCom;

				//Recherche des caractères communs
				charCom = this.getCharCom(wordsOnGrid.get(indexMotPlaces), listMotsPick.get(indexMotPick));
				if(charCom != null){
					String wordPlaced = wordsOnGrid.get(indexMotPlaces); // Récup du mot placé sur la grille
					String wordWantToPlace = listMotsPick.get(indexMotPick); // Récup du mot à placé

					//Debug
					System.out.println("CrossWord(100) : wordsOnGrid.get(" + indexMotPlaces + ") = " + wordPlaced + "; listMotsPick.get(" + indexMotPick + ") = " + wordWantToPlace);
					grilleModif = tryToAdd(wordWantToPlace, wordPlaced, grille); //Test si le mot pick peut être ajouté au mot déjà placé

					System.out.println("CrossWord(103) : GrilleModif : "); //Debug
					affichageGrille(grilleModif); //Debug Affichage de la grille tampon 

					/*for (int hauteur=0;hauteur<taille;hauteur++)
					{
						for (int largeur=0;largeur<taille;largeur++)
						{
							System.out.print(" | ");
							System.out.print(grilleModif[hauteur][largeur]);
						}
						System.out.println(" |");
					}*/


					System.out.println("CrossWord(117) Grille : "); //Debug
					affichageGrille(grille); // Debug Affichage de la grille

					/*
					for (int hauteur=0;hauteur<taille;hauteur++)
					{
						for (int largeur=0;largeur<taille;largeur++)
						{
							System.out.print(" | ");
							System.out.print(grille[hauteur][largeur]);
						}
						System.out.println(" |");
					}*/

					if(grilleModif == grille){ // Si la grille n'as pas changée => aucun nouveau mot placé
						indexMotPlaces++; // Prends le prochain mot sur la grille
						System.out.println("CrossWord(133) : Mot suivant sur la grille !"); //Debug 
					}
					else{ //Si la grille a changée
						grille = copieGrille(grilleModif, taille); // Copie la grille tampon dans la grille principale
						indexMotPlaces = wordsOnGrid.size(); // Force l'arrêt de la recherche de mot suivant sur la grille car mot placé
					}
					

				}
			}

		}


		System.out.println("CrossWord(147) : Grille finale : "); // Debug Affichage de la grille avec tous les mots placés
		/*for (int hauteur=0;hauteur<taille;hauteur++)
		{
			for (int largeur=0;largeur<taille;largeur++)
			{
				System.out.print(" | ");
				System.out.print(grille[hauteur][largeur]);
			}
			System.out.println(" |");
		}*/

		affichageGrille(grille); //Affichage de la grille finale

		/* Debug
		String orientationWordPlaced = wordsOnGridAndOrientation.get(secondWord);
		//System.out.println("orientationWordPlaced : " + orientationWordPlaced);
		int[] posXYWordPlaced = findXYWordPlaced(secondWord, grille, orientationWordPlaced);
		System.out.println("PosX mot : " + posXYWordPlaced[0] + " posY mot : " + posXYWordPlaced[1]);
		*/
	}

	/*
		Fonction permettant de récupérer une liste de mot suivant la taille de la grille
	*/

	public static ArrayList<String> getMotsByTailleMax(int taille){

    	ArrayList<String> listMotByTailleMax = new ArrayList<String>();
    	try {
			FileInputStream fis = new FileInputStream("TriCroissant.txt"); //Ouverture du fichier TriCroissant.txt
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8")); //Buffer de lecture
            for (String line; (line = fichier.readLine()) != null; ) { // Pour chaque ligne lues
				if(line.length() <= taille) // Si le mot est plus petit que la taille de la grille
					listMotByTailleMax.add(line);	//Ajout à la liste 
				else 
					return listMotByTailleMax; // Sinon on retourne la liste
			}
			fichier.close(); // Fermeture du fichier
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return listMotByTailleMax;  // Retourne la liste de mot
	}

	/*
		Fonction permettant de choisir un mot aléatoirement dans une liste de mot
	*/

	public String pickMot(ArrayList<String> listMots){

		Random rand = new Random();
		int maxRandom = listMots.size();
		int random = rand.nextInt(maxRandom);

		//System.out.println(random);

		return listMots.get(random);
	}

	/*
		Fonction permettant d'obtenir une liste de mot par rapport à la taille de la grille et au contreinte de case vide (1/3)
	*/

	public ArrayList<String> getListPickMot(int nbCaractMaxi, int nbCasesVidesMax, ArrayList<String> listMots){

		ArrayList<String> listPickMot = new ArrayList<String>();
		System.out.println("getListPickMot(216) : Max : " + nbCaractMaxi + "; Cases Vides : " + nbCasesVidesMax); //Debug Affichage du nombre de caract maxi et du nb case vide maxi
		int nbCaractUtilises = 0;
		while(nbCaractMaxi - nbCaractUtilises >= nbCasesVidesMax){ //Boucle pour pick un mot suivant les contreintes
			String motPick = pickMot(listMots); // Recup d'un mot aléatoirement dans la liste de mot triée par taille
			nbCaractUtilises += motPick.length(); //Incrément du nb de caract utilisés
			listPickMot.add(motPick); // Ajout à la liste de mot pick
		}
		System.out.println("getListPickMot(223) : Util : " + nbCaractUtilises+ "; Cases vides restantes : "+ (nbCaractMaxi - nbCaractUtilises)); //Debug Affichage nb caract utilisé et nb cases vides restantes
		return listPickMot; // Retourne la liste de mot pick
	}
	
	/*
		Fonction permettant d'obtenir la liste des caractères commun
	*/

	public char[] getCharCom(String firstWord, String secondWord)
	{
		char[] firstWordArray = firstWord.toCharArray(); //Conversion des mots   (tab1)
		char[] secondWordArray = secondWord.toCharArray();// en Tableau   (tab2)
		// tableau de caractères communs
		char[] charCom = new char[firstWordArray.length]; //Tableau de caractère commun (tab3)
		
		for(int charAtFirstWord = 0; charAtFirstWord < firstWordArray.length; charAtFirstWord++) // Parcour le premier mot en entier
		{
			charCom[charAtFirstWord] = '*'; 
			for(int charAtSecondWord = 0; charAtSecondWord < secondWordArray.length; charAtSecondWord++) //Parcours le deuxième mot en entier
			{
				if(firstWordArray[charAtFirstWord] == secondWordArray[charAtSecondWord]) // Si le caractère du premier = caractère du second
				{
					charCom[charAtFirstWord] = firstWordArray[charAtFirstWord]; // Ajout de ce caractère au caractère commun
				}
			}
		}
		
		// Debug Affichage des lettres communes des deux mots
		/*for(int nbCar=0; nbCar<tab1.length; nbCar++)
		{
			if(tab3[nbCar]!='*')
			{
				System.out.println("Le caractère : "+tab3[nbCar]+" à la position : "+nbCar+" est commun aux deux mots");
			}
		}*/
		
		return charCom;
	}


	/*
		Fonction permettant de tester l'ajout d'un mot dans la grille par rapport à un mot déjà placé
	*/

	public char[][] tryToAdd(String wordWantToPlace, String wordTested, char[][] grille){

		String orientationWordPlaced = wordsOnGridAndOrientation.get(wordTested); // Récup de l'orientation du mot déjà placé
		System.out.println("tryToAdd(270) : " + wordTested); // Debug Affichage du mot a testé
		String orientationWordWantToPlace = null; //Orientation du mot a placé
		if(orientationWordPlaced.equals("vertical")){ // Si mot placé vertical 
			orientationWordWantToPlace = "horizontal";	// Mot a placé forcement horizontal
		}
		else if(orientationWordPlaced.equals("horizontal")){ // Et vice versa
			orientationWordWantToPlace = "vertical";
		}

		int[] posXYWordPlaced = findXYWordPlaced(wordTested, grille, orientationWordPlaced); // Récup de la position XY du mot déjà placé
		System.out.println("tryToAdd(280) : posX : " + posXYWordPlaced[0] + "; posY : " + posXYWordPlaced[1]); //Debug Affichage position XY

		char[][] grilleClone = testAddWord(wordWantToPlace, wordTested, posXYWordPlaced, orientationWordWantToPlace, grille); // Test si le mot peut être ajouté


		return grilleClone; // Retourne la nouvelle grille


	}

	/*
		Fonction permettant de recherche s'il y des collisions entre les mots
	*/

	public boolean testCollision(int[] posXY, String orientation, String sens, char[][] grille){
		int posX = posXY[0], posY = posXY[1];
		boolean collision = false;
		int taille = getTailleGrille();

		System.out.println("testCollision(225) : [" + posX + ", " + posY + "]");
		if(orientation.equals("horizontal")){
			if(sens.equals("increment")){ //Test vers la droit (y++) 
				if(posY + 1 < taille){
					if( grille[posX][posY + 1] == '*'){
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
						else if(posX + 1 > taille){
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
						else if(posX + 1 == taille){

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
				else if(posY + 1 == taille){

				}
				else{
					collision = true;
				}
			}
			else if(sens.equals("decrement")){ //Test vers la gauche (y--)
				if(posY - 1 >= 0){
					if( grille[posX][posY - 1] == '*'){
						if(posX - 1 >= -1){
							if(grille[posX + 1][posY - 1] == '*'){
								if(posY - 2 >= 0){	
									if(grille[posX][posY - 2] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < -1){
									collision = true;
								}
							}
							else collision  = true;
						}
						else if(posX + 1 >= taille){

							if(grille[posX - 1][posY - 1] == '*'){
								if(posY - 2 >= 0){	
									if(grille[posX][posY - 2] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < -1){
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
							else if(posY - 2 < -1){
								collision = true;
							}
							else collision = true;
						}
						else collision  = true;
					}
					else collision = true;
				}
				else if(posY - 1 == -1){


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
							if(grille[posX + 1][posY - 1] == '*'){
								if(posX + 2 < taille){	
									if(grille[posX + 2][posY + 1] == '*'){

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

						}
						else if( (grille[posX + 1][posY + 1] == '*')  && (grille[posX + 1][posY - 1] == '*')){
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
				else if(posX + 1 == taille){

				}
				else{ 
					collision = true;
				}
			}
			else if(sens.equals("decrement")){ //Test vers le haut (x--)
				if(posX - 1 >= 0){
					if( grille[posX - 1][posY] == '*'){
						if(posY - 1 == -1){
							if(grille[posX - 1][posY + 1] == '*'){
								if(posX - 2 >= 0){	
									if(grille[posX - 2][posY] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < -1){
									collision = true;
								}
							}
							else collision  = true;
						}
						else if(posY + 1 == taille){
							if(grille[posX - 1][posY - 1] == '*'){
								if(posX - 2 >= 0){	
									if(grille[posX - 2][posY] == '*'){

									}
									else collision = true;
								}
								else if(posY - 2 < -1){
									collision = true;
								}
							}
							else collision = true;
						}
						else if( (grille[posX - 1][posY + 1] == '*')  && (grille[posX - 1][posY - 1] == '*')){
							if(posX - 2 >= 0){	
								if(grille[posX - 2][posY] == '*'){

								}
								else collision = true;
							}
							else if(posY - 2 < -1){
								collision = true;
							}
						}
						else collision  = true;
					}
					else collision = true;
				}
				else if(posX - 1 ==  -1){

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

		
		int taille = getTailleGrille(); //Récup taille grille
		char[] charCom;
		charCom = this.getCharCom(wordsOnGrid, wordWantToPlace); //Récup caractères communs

		char [][] grilleClone = new char[taille][taille]; // Définition d'une nouvelle grille tampon

		grilleClone = copieGrille(grille, taille); // Copie de la grille courante dans la grille tampon

		String orientationWordPlaced = wordsOnGridAndOrientation.get(wordsOnGrid); // Récup de l'orientation du mot placé

		char[] wordWantToPlaceArray =  wordWantToPlace.toCharArray(); //Conversion des mots
		char[] wordsOnGridArray =  wordsOnGrid.toCharArray();		  // en tableau

		// Variables de position des caractères
		int charAtWordWantToPlace = 0;
		int chatAtWordOnGrid = 0;
		int charCommPlaced = 0;

		int[] posXYCourant = {0, 0}; // Position XY
		
		boolean collision = false; // Variable de collision
		boolean wordPlaced = false; // Variable permettant de savoir si le mot est placé ou non

		final int finalPosX = posXYWordPlaced[0], finalPosY = posXYWordPlaced[1]; // Position du mot placé

		int posX = posXYWordPlaced[0], posY = posXYWordPlaced[1]; // Variable pour le parcours de la grille

		//Contrôle

		if(posX < 0) 
			posX = 0;
		else if(posY < 0)
			posY = 0;

		for(int i = 0; i < charCom.length; i++){
			System.out.println(charCom[i]);
		}

		/*
			Evite les '*' dans les caractères commnuns	
		*/

		while(charCom[charCommPlaced] == '*' && charCommPlaced < charCom.length - 1){
			charCommPlaced++;
		}

		// Debug affiche le caractère commun courrant
		System.out.println("testAdd(584) : charCom[" + charCommPlaced + "] = " + charCom[charCommPlaced]);


		while(wordPlaced == false && charCommPlaced < charCom.length - 1){

			// Si le mot testé sur la grille est vertical et que le mot n'as pas été placé
			if(orientationWordPlaced.equals("vertical")){

				// Debug		
				System.out.println("testAdd(593) : " + posX + " ! " + posY + " charCommPlaced : " + charCommPlaced);

				posX = finalPosX + charCommPlaced;
				//Recherche de la position du caractère commun dans le mot placé
				while(grilleClone[posX][posY] != charCom[charCommPlaced] && posX < taille - 1){
					posX++;
				}

				// Si l'on rouve la caractère commun dans le mot (recherche de position)
				if(grilleClone[posX][posY] == charCom[charCommPlaced]){

					// Recherche du caractère commun dans le mot à placer
					if(charAtWordWantToPlace < wordWantToPlace.length()){
						while(wordWantToPlaceArray[charAtWordWantToPlace] != charCom[charCommPlaced] && charAtWordWantToPlace < wordWantToPlace.length() - 1){
							charAtWordWantToPlace++;
						}
					}

					if(wordWantToPlaceArray[charAtWordWantToPlace] == charCom[charCommPlaced]){

						//Debug 
						System.out.println("testAdd(614) : [" + posX + ", " + posY + "]; charCom[charCommPlaced] = " + charCom[charCommPlaced]);

						// Mémorisation des position X et Y 
						int tempPosX = posX;
						int tempPosY = posY;

						// Memorisation de l'endroit du caractère commun dans le mot à placer
						int tempCharAtWordWantToPlace = charAtWordWantToPlace;

						// Permet l'ajout d'un caractère du mot à placer tant que l'on est pas au début et qu'il n'y a pas eût de collision
						while(tempCharAtWordWantToPlace >= 0){


							//Détection de la sortie de la grille
							if(tempPosY < 0){
								tempPosY = 0;
								collision = true;
								tempCharAtWordWantToPlace = -1;
							}

							if(collision == false){

								// Création d'un tableau de position pour le test de collision
								posXYCourant[0] = tempPosX;
								posXYCourant[1] = tempPosY;

								//Debug 
								System.out.println(" testAdd(641) : testCollision");

								// Test de collision
								if(testCollision(posXYCourant, "horizontal", "decrement", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
									grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
								else{ //S'il y a collision remetre la grille d'origine
									collision = true;
									System.out.println("testAdd(648) : " + collision);
								}
								System.out.println("testAdd(650) : " + wordWantToPlaceArray[tempCharAtWordWantToPlace]);

								//Décrémenter la position en Y car le mot a placer est à l'horizontal
								tempPosY--;

								//Décrémenter la position du caractère dans le mot à placer
								tempCharAtWordWantToPlace--;
							}
							else
								tempPosY = -1;
						}

						// S'il n'y a plus de caractère dans le mot à placer
						if(tempCharAtWordWantToPlace < 0){

							// Réinitialiser toutes les variables
							tempCharAtWordWantToPlace = charAtWordWantToPlace;
							tempPosX = posX;
							tempPosY = posY;
						}

						// Permet l'ajout des caractères après le caractère commun s'il n'y a pas eût de collision
						while(tempCharAtWordWantToPlace < wordWantToPlace.length()){


							//Detection de la sortie de la grille
							if(tempPosY > taille - 1){
								collision = true;
								tempPosY = taille - 1;
								tempCharAtWordWantToPlace = wordWantToPlace.length();
							}

							if(collision == false){

								//Création du tableau de position pour le test de collision
								posXYCourant[0] = tempPosX;
								posXYCourant[1] = tempPosY;

								//Debug
								System.out.println("testAdd(689) : testCollision");
								if(testCollision(posXYCourant, "horizontal", "increment", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
									grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
								else{ // S'il y a collison remettre la grille d'origine
									collision = true;
									System.out.println("testAdd(694) : " + collision);
								}

								// Debug
								System.out.println("testAdd(698) : " + wordWantToPlaceArray[tempCharAtWordWantToPlace]);

								// Increment de la position en Y car mot à placer horizontal
								tempPosY++;
								//Increment la position du caractère dans le mot à placer
								tempCharAtWordWantToPlace++;
							}
							else
								tempPosY = taille;
						}
						// S'il y a eût collision
						if(collision == true){
							System.out.println("testAdd(710) :  collision ! ");
							collision = false;
							// Le mot n'as pas été placé
							wordPlaced = false;
							charCommPlaced++;
							grilleClone = copieGrille(grille, taille);
							// Incrément du caractére commun et raz de la position en Y et des collisions
							while(charCom[charCommPlaced] == '*' && charCommPlaced < charCom.length - 1){
								charCommPlaced++;
								collision = false;
							}
							posY = finalPosY;
						}
						// S'il n'y a pas eût de collison et que tous les caractères ont été placés
						else if(tempCharAtWordWantToPlace >= wordWantToPlaceArray.length){
							wordPlaced = true;
						}
					} //Devenu inutile
					/*else if(charAtWordWantToPlace < wordWantToPlaceArray.length - 1){
						charAtWordWantToPlace++;
						System.out.println("testAdd(631) : charAtWordWantToPlace++ : " + charAtWordWantToPlace);
					}*/
					else{
						wordPlaced = false;
						while(charCom[charCommPlaced] == '*' && charCommPlaced < charCom.length - 1){
								charCommPlaced++;
								collision = false;
						}
						posY = finalPosY;
						charAtWordWantToPlace = 0;
					}
				} //S'il ne trouve pas le caractère commun dans le mot placé à la position X on incrémente posX (innutile)
				else{
					grilleClone = copieGrille(grille, taille);
				/*
					if(posX < taille - 1){
						posX++; 
						System.out.println("X++ : " + posX) ;
					}
					else{
						System.out.println("-----------");
						if(charCommPlaced < charCom.length){
							System.out.println("++ : " + charCom[charCommPlaced]);
							charCommPlaced++;
							charAtWordWantToPlace = 0;
							posX = 0;
							collision = false;
						}
						else
							grilleClone = grille;
					}
					*/
				}
			}
			// Sinon si le mot placé est à la l'horizontal
			else if(orientationWordPlaced.equals("horizontal")){

				//Debug
				System.out.println("testAdd(768) : " + posX + " ! " + posY + " charCommPlaced : " + charCommPlaced);

				posY = finalPosY +  charCommPlaced;
				//Recherche de la position du caractère commun dans le mot placé
				while(grilleClone[posX][posY] != charCom[charCommPlaced] && posY < taille - 1){
					posY++;
				}

				// Si l'on trouve la position du caractère commun dans le mot placé 
				if(grilleClone[posX][posY] == charCom[charCommPlaced]){


					// Recherche du caractère commun dans le mot à placer
					if(charAtWordWantToPlace < wordWantToPlace.length() - 1){
						while(wordWantToPlaceArray[charAtWordWantToPlace] != charCom[charCommPlaced] && charAtWordWantToPlace < wordWantToPlace.length() -1){
							charAtWordWantToPlace++;
						}
					}

					if(wordWantToPlaceArray[charAtWordWantToPlace] == charCom[charCommPlaced]){

						//Debug
						System.out.println("testAdd(790)  : [" + posX + ", " + posY + "]; charCom[charCommPlaced] = " + charCom[charCommPlaced]);

						//Memorisation des positions XY
						int tempPosX = posX;
						int tempPosY = posY;

						//Mémorisation de la place du caracère commun dans le mot à placé
						int tempCharAtWordWantToPlace = charAtWordWantToPlace;
						
						// Permet l'ajout d'un caractère du mot à placer tant que l'on est pas au début et qu'il n'y a pas eût de collision
						while(tempCharAtWordWantToPlace >= 0){


							//Detection de la sortie de la grille
							if(tempPosX < 0){
								collision = true;
								tempCharAtWordWantToPlace = -1;
							}

							if(collision == false){

								// Création d'un tableau de position pour le test de collision
								posXYCourant[0] = tempPosX;
								posXYCourant[1] = tempPosY;

								//Debug
								System.out.println("testAdd(816) : testCollision");

								// Test de collision
								if(testCollision(posXYCourant, "vertical", "decrement", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
									grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
								else{ //S'il y a eût collison raz de la grille
									collision = true;
									System.out.println("testAdd(823) : " + collision);
								}

								//Debug 
								System.out.println("testAdd(827) : " +  wordWantToPlaceArray[tempCharAtWordWantToPlace]);

								//Décrément de la position car mot placé à la vertical donc mot à placé à l'horizontal
								tempPosX--;

								//Décrement de la position du caractère dans le mot à placé
								tempCharAtWordWantToPlace--;
							}
							else
								tempPosX  = -1;
						}

						// Si l'on n'y a plus de caractère avant le caractère commun du mot à placé
						if(tempCharAtWordWantToPlace < 0){

							//RAZ variable de position
							tempCharAtWordWantToPlace = charAtWordWantToPlace;
							System.out.println("testAdd(844) : " + posX + ";" + posY);
							tempPosX = posX;
							tempPosY = posY;
						}

						// Permet l'ajout des caractères après le caractère commun s'il n'y a pas eût de collision
						while(tempCharAtWordWantToPlace < wordWantToPlace.length()){


							if(tempPosX > taille - 1){
								collision = true;
								tempPosX = taille - 1;
								tempCharAtWordWantToPlace = wordWantToPlace.length();
							}

							if(collision == false){

								// Création d'un tableau de position pour le test de collision
								posXYCourant[0] = tempPosX;
								posXYCourant[1] = tempPosY;

								//Debug
								System.out.println("testAdd(866) : testCollision");

								//Test de collison
								if(testCollision(posXYCourant, "vertical", "increment", grilleClone) == false) //int[] posXY, String orientation, String sens, char[][] grille
									grilleClone[tempPosX][tempPosY] = wordWantToPlaceArray[tempCharAtWordWantToPlace];
								else{ //S'il y a eût collison RAZ grille
									collision = true;
									System.out.println("testAdd(873) : " + collision);
								}

								//Debug
								System.out.println("testAdd(877) : " + wordWantToPlaceArray[tempCharAtWordWantToPlace]);

								// Incrément de la position en X
								tempPosX++;

								//Incrément de la position du caractère du mot à placé
								tempCharAtWordWantToPlace++;
							}
							else
								tempPosX = taille;
						}
						// S'il y a eût collision
						if(collision == true){
							collision = false;
							System.out.println("testAdd(891) :  collision ! ");
							// Le mot n'as pas été placé
							wordPlaced = false;
							charCommPlaced++;
							grilleClone = copieGrille(grille, taille);

							// Incrément du caractére commun et raz de la position en X et des collision
							while(charCom[charCommPlaced] == '*' && charCommPlaced < charCom.length - 1){
								charCommPlaced++;
							}
							posX = finalPosX;
							tempCharAtWordWantToPlace = charAtWordWantToPlace;
						}
						// S'il n'y a pas eût de collison et que tous les caractères ont été placés
						else if(tempCharAtWordWantToPlace >= wordWantToPlaceArray.length){
							wordPlaced = true;
						}
					} //Inutile
					/*else if(charAtWordWantToPlace < wordWantToPlaceArray.length - 1){
						charAtWordWantToPlace++;
						System.out.println("testAdd(792) : charAtWordWantToPlace++ : " + charAtWordWantToPlace);
						
					}*/
					else{
						wordPlaced = false;
						while(charCom[charCommPlaced] == '*' && charCommPlaced < charCom.length - 1){
								charCommPlaced++;
						}
						collision = false;
						posX = finalPosX;
						charAtWordWantToPlace = 0;
					}

				}
				else{
					grilleClone = copieGrille(grille, taille);
				/*
					if(posY < taille - 1){
						posY++; 
						System.out.println("Y++ : " + posY);
					}
					else {
						System.out.println("-----------");
						if(charCommPlaced < charCom.length){
							System.out.println("++ : " + charCom[charCommPlaced]);
							charCommPlaced++;
							charAtWordWantToPlace = 0;
							posY = 0;
							}
						else
							grilleClone = grille;
					}*/
				}
			}
		}
		return grilleClone;
	}


	/*
		Fonction permettant de faire la copie de deux grille
	*/

	public char[][] copieGrille(char[][] grille, int taille){
		char[][] grilleCopie = new char[taille][taille];

		for(int x = 0; x < taille; x++){
			for (int y = 0; y < taille; y++) {
				grilleCopie[x][y] = grille[x][y];
			}
		}

		return grilleCopie;
	}

	/*
		Fonction permettant l'affichage d'un grille
	*/

	public void affichageGrille(char[][] grille){
		int taille = getTailleGrille();

		// Création des index horizontaux

		char[] indexHorizontaux = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'}; //Les 15ère lettres de l'alphabet

		//Création des index verticaux

		String[] indexVerticaux = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15"};


		System.out.print("  ");
		for(int index = 0; index < taille; index++){
			System.out.print(" | ");
			System.out.print(indexHorizontaux[index]);
		}
		System.out.print(" | ");
		System.out.println();

		for (int hauteur=0;hauteur<taille;hauteur++)
		{	
			System.out.print(indexVerticaux[hauteur]);
			for (int largeur=0;largeur<taille;largeur++)
			{	
				System.out.print(" | ");
				System.out.print(grille[hauteur][largeur]);
			}
			System.out.println(" |");
		}
		
	}

	/*
		Fonction permettant de rechercher la position initiale en X et Y d'un mot placé dans la grille
	*/

	public int[] findXYWordPlaced(String wordPlaced, char[][] grille, String orientation){

		int posX = 0; // Postion en X
		int posY = 0;	// Position en Y
		int indexArray = 0;
		boolean wordFind = false; //Variable permettant de savoir si le mot est trouvé où non
		int[] posXY = {0,0}; // Tableau de pos
		int taille = getTailleGrille(); //Récup taille de la grille

		char[] wordPlacedArray = wordPlaced.toCharArray(); // Conversion du mot en tableau

		while(posX < taille){ // Parcours de la grille en X

			while(posY < taille && posX < taille){ // Parcours de la grille en Y

				if(grille[posX][posY] == wordPlacedArray[indexArray] && indexArray < wordPlaced.length()){ // Si caractère trouvé en XY et que ce n'est pas le dernier du mot recherché
					indexArray++; //Incrément du caractère recherché
					wordFind = true; // Supossiotion que le mot est trouvé
					if(orientation.equals("vertical")){ // Si en vertical
						if(posX < taille) 
							posX++; //Increment de la pos X
					}
					else{
						if(posY < taille)
							posY++; //Si horizontal incrément en Y
					}
					if(wordFind == true && indexArray > wordPlaced.length() - 1){ // Si le mot est supposé trouvé et qu'il n'y a plus de caractère a recherché
						wordFind = false;	// RAZ variable pour ne pas rebouclé
						indexArray = 0; 
						if(orientation.equals("vertical")){ //Si vertical
							posXY[0] = posX - wordPlaced.length(); //Retour au début du mot
							posXY[1] = posY;
						}
						else{
							posXY[0] = posX;
							posXY[1] = posY - wordPlaced.length(); //Sinon c'est horizontal t retour au début du mot
						}
					}
				}
				else if(posY < taille)
					posY++; //Incrément de la pos Y
			}

			posY = 0; //Retour en haut de la grille
			if(posX < taille)
				posX++; //Incrément de posX
		}
		return posXY; //Retourne la position du début du mot
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

	/*
		Fonction regardant si la grille est vide
	*/
	
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
	
	// La fonction nous renverra la position du dernier caractère du mot ainsi que son orientation (Not used)
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