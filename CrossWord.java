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
		
		
		//voir toute la liste des mots s√©l√©cteionner al√©atoirement
		for (int i = 0; i < listMotsPick.size(); i++ ) {
			System.out.println(listMotsPick.get(i));
		}
		
		/* Ajout r√©cent */
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
		
		// r√©cup√©ration des deux plus grands mots
		String firstWord= listMotsPick.get(0);
		String secondWord= listMotsPick.get(1);
		
<<<<<<< HEAD
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
		boolean continuer1 = true; // sinon plusieurs premi√®res lettres communes misent √† diverses endroits
		boolean continuer2 = true; // pour continuer sur chaques lettres apr√®s ou avant la premi√®re lettre
		boolean continuer3 = true;
		while( nbCar<firstWord.length())
		{
			//System.out.println("mot1 ");
			nbCar2=0;
			//while( nbCar2<secondWord.length()&& (continuer1 == true || continuer2 == true || continuer3 == true))
			while( nbCar2<secondWord.length()&& continuer1 == true)
			{
				//System.out.println("mot2");
				// trouver le m√™me caract√®re aux position nbCar et nbCar2 des mots
				// nbCar ligne de la matrice de la premi√®re chaine
				// nbCar2 ligne de la matrice de la seconde chaine
				// System.out.println(charCom[nbCar]+"=="+charCom2[nbCar2]+"&&"+charCom[nbCar]+"!='*'");
				if(charCom[nbCar]==charCom2[nbCar2]&& charCom[nbCar]!='*')
				{
					// placer la premi√®re lettre
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
					// Si on a des lettres du premier mot apr√®s la lettre commune
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
					
					// Si on a une lettre du second mot apr√®s la lettre commune
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
		
		char grilleModif[][] = new char[taille][taille];
		grilleModif=addWord(firstWord,secondWord,grille,taille);
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
		// tableau de caract√®res communs
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
				System.out.println("Le caract√®re : "+tab3[nbCar]+" √† la position : "+nbCar+" est commun aux deux mots");
			}
		}*/
		
		return tab3;
	}
	/*
	//////////////////////////////////////////////////////////////////////////////////////
	// fait : premiËre occurence                                                        //
	// a faire seconde occurence ou plus ( un seul mot a placer)                        //
	// A rajouter : si la lettre commune est dÈj‡ utilisÈe passer a la lettre comune +1 //
	//////////////////////////////////////////////////////////////////////////////////////
	*/
	public char[][] addWord(String firstWord,String secondWord,char grille[][], int taille)
	{	
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
		
		boolean continuer1 = true; // sinon plusieurs premiËres lettres communes misent ‡ diverses endroits
		
		while( nbCar<firstWord.length())
		{
			nbCar2=0;
			while( nbCar2<secondWord.length()&& continuer1 == true)
			{
				// trouver le mÍme caractËre aux position nbCar et nbCar2 des mots
				// nbCar ligne de la matrice de la premiËre chaine
				// nbCar2 ligne de la matrice de la seconde chaine
				if(charCom[nbCar]==charCom2[nbCar2]&& charCom[nbCar]!='*')
				{
					if(isEmpty(grille, taille))
					{
						// placer la premiËre lettre
						if( continuer1 == true){
							grille[nbCar2][nbCar]=charCom[nbCar];
							continuer1=false;
						}
					}
					else
					{
						// rechercher o˘ le mot est placÈ
						int posVert=0;
						int posHor=0;
						int posWord=0;
						int[] posLastChar;
						//posLastChar=searchWordGrille(grille,char1,posVert,posHor,posWord,taille);
						//System.out.println("x : "+posLastChar[0]+"y: "+posLastChar[1]+grille[posLastChar[0]][posLastChar[1]]);
					}
					// Si on a des lettres du premier mot avant la lettre commune
					int numLettreAvant=0;
					while(numLettreAvant<char1.length)
					{
						grille[nbCar2][numLettreAvant]=char1[numLettreAvant];
						numLettreAvant++;
					}
					// Si on a des lettres du premier mot aprËs la lettre commune
					int numLettreApres=nbCar+1;
					while(numLettreApres<char1.length)
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
					
					// Si on a une lettre du second mot aprËs la lettre commune
					int numLettreApresM2=nbCar2+1;
					while(numLettreApresM2<char2.length)
					{
						grille[numLettreApresM2][nbCar]=char2[numLettreApresM2];
						numLettreApresM2++;
					}
				}
				nbCar2++;
			}	
			nbCar++;
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		/*int posVert=0;
		int posHor=0;
		int posWord=0;
		int[] posLastChar;
		//posLastChar=searchWordGrille(grille,char1,posVert,posHor,posWord,taille);
		if(posLastChar[0]==20)
		{
			System.out.println("x : "+posLastChar[0]+"y: "+posLastChar[1]);
		}
		else
		{
			System.out.println("x : "+posLastChar[0]+"y: "+posLastChar[1]+grille[posLastChar[0]][posLastChar[1]]);
		}*/
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		return grille;
	}
	
	public boolean isEmpty(char grille[][],int taille)
	{
		boolean empty=true;
		for(int i = 0; i<taille;i++)
		{
			for(int j = 0; i<taille;i++)
			{
				if(grille[i][j]!='*')
				{
					empty = false;
				}
			}
		}
		
		return empty;
	}
	
	// la fonction nous renverra la position du dernier caractËre du mot ainsi que son orientation 
	/*public int[] searchWordGrille(char grille[][],char[] word, int posVert, int posHor, int posWord, int taille)
	{
		int[] tab = new int[3];
		// on utilise la valeur 20 car les mots peuvent Ítre au maximum de 15 caractËre et donc impossible qu'ils soient jusqu'‡ 20			
		tab[0]=20;
		tab[1]=20;
		tab[2]=20;
		boolean continuer=true;
		System.out.println("posVert : "+posVert);
		System.out.println("posHor : "+posHor);
		System.out.println("taille : "+taille);
		while (posVert<taille-1|| continuer==true)
		{	
			System.out.println("posVert : "+posVert);
			int posHor2=posHor;
			while (posHor2<taille-1 || continuer==true)
			{
				System.out.println("posHor : "+posHor2);
				
				if (grille[posVert][posHor2]==word[posWord])
				{
					System.out.println("x : "+posVert+"y : "+posHor2+"lettre : "+grille[posVert][posHor2]);
					if(grille[posVert+1][posHor2]==word[posWord+1])
					{
						if(posWord+1==word.length)
						{
							// tab[2] sert a dire si le mot est vertical ou horizontal
							tab[0]=posVert+1;
							tab[1]=posHor2;
							tab[2]=0;
							System.out.println("x : "+tab[0]+"y: "+tab[1]+grille[tab[0]][tab[1]]);
							continuer=false;
							return tab;
						}
						else
						{
							return searchWordGrille(grille,word,posVert+1,posHor2,posWord+1,taille);
						}
					}
					else if(grille[posVert][posHor2+1]==word[posWord+1])
					{
						if(posWord+1==word.length)
						{
							// tab[2] sert a dire si le mot est vertical ou horizontal
							tab[0]=posVert;
							tab[1]=posHor2+1;
							tab[2]=1;
							System.out.println("x : "+tab[0]+"y: "+tab[1]+grille[tab[0]][tab[1]]);
							continuer=false;
							return tab;
						}
						else
						{
							return searchWordGrille(grille,word,posVert,posHor2+1,posWord+1,taille);
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
	}*/
}