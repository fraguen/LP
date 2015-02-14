import java.io.*;
import java.util.regex.*;
import java.text.Normalizer; 
import java.text.Normalizer.Form; 
import java.util.ArrayList;

public class GestionFichier {

	public GestionFichier(){
     
	}
	
    public static String translate(String src) {
		src = src.replace("œ", "oe");
		String tmp = src;
		String normalized = Normalizer.normalize(tmp, Normalizer.Form.NFD);
		//System.out.println(normalized);
		src = normalized.replaceAll("[^\\p{ASCII}]", "");
		//System.out.println(src);
		return src;
    }

    public static int getNbMotsDictionnaire(){
    	Matcher matcher; 
    	Pattern pattern = Pattern.compile("(.+\\|\\d)");
		int nbMots = 0;
		long startTime = System.currentTimeMillis();
		try {
			FileInputStream fis = new FileInputStream("thes_fr.dat");
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            for (String line; (line = fichier.readLine()) != null; ) {
				matcher = pattern.matcher(line);
				if(matcher.find()){
					String[] split = matcher.group().split("\\|");
					if(split[0].length() <= 15){
						nbMots++;
					}
					String nextLine = fichier.readLine();
				}
			}			
            fichier.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return nbMots;
    }

    public static int getNbMotsImportes(){
    	Matcher matcher; 
    	Pattern pattern = Pattern.compile("(.+#.+)");
		int nbMots = 0;
		long startTime = System.currentTimeMillis();
		try {
			FileInputStream fis = new FileInputStream("word.txt");
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            for (String line; (line = fichier.readLine()) != null; ) {
				matcher = pattern.matcher(line);
				if(matcher.find()){
					String[] split = matcher.group().split("#");
					nbMots = Integer.parseInt(split[1].trim());
					String nextLine = fichier.readLine();
				}
			}			
            fichier.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return nbMots;
    }

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
		    fis = new FileInputStream("thes_fr.dat");
		    fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			writerWorld = new BufferedWriter( new FileWriter("word.txt"));
			writerAdj = new BufferedWriter( new FileWriter("adj.txt"));
			pattern = Pattern.compile("(.+\\|\\d)");
			byte[] buf = new byte[1];
			long startTime = System.currentTimeMillis();
            for (String line; (line = fichier.readLine()) != null; ) {
				matcher = pattern.matcher(line);
					if(matcher.find()){
						String[] split = matcher.group().split("\\|");
						split[0] = translate(split[0]);
						//System.out.println(split[0]);
						if(split[0].length() <= 15){
							writerWorld.write(split[0] + "\n");
							nbMots++;
						}
						if(Integer.parseInt(split[1]) > 1){
							String ligneSyn = "";
							System.out.println(split[1]);
							int nbCallback = Integer.parseInt(split[1]);
							for(int callback = 0; callback < nbCallback; callback++){
								String nextLine = fichier.readLine();
								String[] adj = nextLine.split("\\)\\|");
								adj[1] = translate(adj[1]);
								if(split[0].length() <= 15){
									if(callback == nbCallback - 1){
										ligneSyn += adj[1];
									}
									else{
										ligneSyn += adj[1] + "|";
									}
									
								}
							}
							if(split[0].length() <= 15){
								ligneSyn += "\n";
								writerAdj.write(ligneSyn);
							}
						}
						else if (Integer.parseInt(split[1]) == 1){
							String nextLine = fichier.readLine();
							String[] adj = nextLine.split("\\)\\|");
							adj[1] = translate(adj[1]);
							if(split[0].length() <= 15){
								writerAdj.write(adj[1] + "\n");
							}
						}
					}
			}			
			writerWorld.write("Nombres de mot : # " + nbMots);
            fichier.close();
			writerWorld.close();
			writerAdj.close();
			System.out.println("MaJ dictionnaire réussie!");
				System.out.println("Temps lecture du fichier : " + (System.currentTimeMillis() - startTime) + "ms");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

    }

    public static ArrayList<String> Tri(int nblignes, String sensTri){
		ArrayList<String> listMots = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream("word.txt");
    		BufferedReader fichier = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
    		int ligneLues;
            for (ligneLues = 0; ligneLues < nblignes; ligneLues++) {
            	String ligne = fichier.readLine();
				listMots.add(ligne);
			}			
			if(sensTri.equals("croissant"))
				listMots = TriBulleCroissant(listMots);
			else if(sensTri.equals("decroissant"))
				listMots = TriBulleDecroissant(listMots);
            fichier.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return listMots;
    }

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