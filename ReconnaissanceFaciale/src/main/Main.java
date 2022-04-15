package main;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import acp.Matrice;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import personne.Image;
import personne.Personne;
import vectorisation.Vecteur;

public class Main extends Application{
	public static Set<Personne> bdd=new HashSet<Personne>();
	
	public static void initialisationBDD() {
		bdd.add(Personne.AuzollesM);
		bdd.add(Personne.BarbosaM);
		bdd.add(Personne.ChambasM);
		bdd.add(Personne.RibayneM);
		bdd.add(Personne.SallM);
	}
	
	public static Matrice initialisationMatriceImages() {
		Matrice images= new Matrice(100*100,bdd.size()*Personne.AuzollesM.getImages().size());
		for (Personne personne : bdd) {
			for (Image image : personne.getImages()) {
				images.ajouterImage(image.getPhoto().transfoVect());
			}	
		}
		images.centralisation();
		images.matriceCovariance();
		images.valeursPropres();
		images.vecteursPropres();
		images.matriceProjection();
		return images;
	}
	
	@Override 
    public void start(Stage primaryStage) { 
        
    	// On récupère les distances et les points de la courbe
    	List<String> distPoint = getParameters().getUnnamed();
    	
    	// Taille pour les distances
    	int tailleDist = distPoint.size()/2+1;
    	
    	// Création des K catégories pour les distances
        final List<BarChart.Series> seriesList = new LinkedList<>(); 
        final String[] categoriesNames = new String[tailleDist-1];
        for (int i = 0; i < tailleDist-1; i++) {
        	int j = i+1;
        	categoriesNames[i] = ""+j;
        }
        
        // Ajout de la série Erreur et ajout des valeurs
        final String[] seriesNames = {"Erreur"}; 
        final double[][] allValues = new double[1][tailleDist];
        for (int  i = 0; i < tailleDist-1; i++) {
            allValues[0][i]=Double.parseDouble(distPoint.get(i+1)); 
    	}
        
        // On fixe les axes
        final double minY = 0; 
        double maxY = -Double.MAX_VALUE;
        
        // Pour chaque série, on en a une seule ici
        for (int seriesIndex = 0; seriesIndex < seriesNames.length; seriesIndex++) { 
        	
        	// On crée un histogramme et on y ajoute toutes les valeurs des catégories
            final BarChart.Series series = new BarChart.Series<>(); 
            series.setName(seriesNames[seriesIndex]); 
            final double[] values = allValues[seriesIndex]; 
            for (int categoryIndex = 0; categoryIndex < categoriesNames.length; categoryIndex++) { 
                final double value = values[categoryIndex]; 
                final String category = categoriesNames[categoryIndex]; 
                maxY = Math.max(maxY, value); 
                final BarChart.Data data = new BarChart.Data(category, value); 
                series.getData().add(data); 
            } 
            seriesList.add(series); 
        } 
        
        // Création du graphique/histogramme pour les distances
        
        // Création et labellisation des axes
        final CategoryAxis xAxis = new CategoryAxis(); 
        xAxis.getCategories().setAll(categoriesNames); 
        xAxis.setLabel("K"); 
        final NumberAxis yAxis = new NumberAxis(minY, maxY, 10); 
        yAxis.setLabel("Distance euclidienne"); 
        final BarChart chart = new BarChart(xAxis, yAxis); 
        chart.setTitle("Évolution de l'erreur en fonction de K"); 
        chart.getData().setAll(seriesList); 
        
        // Création de la courbe pour les variances cumulées
        final NumberAxis xAxis2 = new NumberAxis(1, tailleDist-1, 1);
        final NumberAxis yAxis2 = new NumberAxis();
        
        // Déclaration d'une courbe
        final AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis2, yAxis2);
        areaChart.setTitle("Variance cumulée des K premières valeurs propres");
        areaChart.setLegendSide(Side.LEFT);
        
        // Valeurs des x et y
        XYChart.Series<Number, Number> seriesVar = new XYChart.Series<Number, Number>();

        
        seriesVar.setName("Pourcentage de variance cumulée");
        
        // On ajoute toutes les valeurs
        for (int i = tailleDist; i < distPoint.size(); i++) {
        	seriesVar.getData().add(new XYChart.Data<Number, Number>(i+1-tailleDist, Double.parseDouble(distPoint.get(i))));
        }
        
        // Montage de l'interface et affichage des deux graphiques
        
        // On crée un deuxième Stage pour la courbe, on ajoute tout et on l'affiche
        Stage stage = new Stage();
		stage.setTitle("Courbe de la variance cumulée des K premières valeurs propres");
        Scene sceneVar = new Scene(areaChart, 400, 300);
        areaChart.getData().addAll(seriesVar);
        stage.setScene(sceneVar);
        stage.show();
        
        // On ajoute toutes les valeurs de notre histogramme à notre Stage passé en paramètre
        final StackPane root = new StackPane(); 
        root.getChildren().add(chart); 
        final Scene scene = new Scene(root, 500, 450); 
        primaryStage.setTitle("Évolution des distances euclidiennes (l'erreur) en fonction de K"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    } 

	public static void main(String[] args) {
		initialisationBDD();
		Matrice images=initialisationMatriceImages();

		images.affichageEigenfaces();

		
		
		// Test pour reconstruire la première image
		Vecteur test=images.reconstructionImage(0,images.getVecteursPropres().getColumnDimension());
		test.transfoMat().affichage();
		System.out.println("Done");
		double[] vp = images.valeursPropres();
		
		
		// Méthode qui donne la variance cumulée en fonction de K
		double[] res = images.normaliserEtAfficherVariation(vp);
		
		// Première image de la base de référence pour le calcul de l'erreur
		Image image = new Image("../BDD/Train/CHAMBAS_Mathilde/CHAMBAS_Mathilde_3.jpg");
		

		// Image de la bonne personne mais avec une image de test pour le calcul de l'erreur
		//Image image = new Image("../BDD/Test/3.jpg");
		
		// On récupère les valeurs des erreurs en fonction de K
		try {
			// On compare image à la première image de la base
			double[] d = images.affichageGraphique(image,0);
			// On ajoute toutes les valeurs de distances puis la variance cumulée en fonction de K dans une chaîne
	    	String[] s = new String[d.length+res.length];
	    	for (int i = 0; i < d.length; i++) {
	    		s[i]=""+d[i];
	    	}
	    	for (int i = d.length; i < s.length; i++) {
	    		s[i]=""+res[i-d.length];
	    	}
	    	
	    	// On lance la méthode start avec notre paramètre et on affiche les deux graphiques
	    	launch(s); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		
	}
}

