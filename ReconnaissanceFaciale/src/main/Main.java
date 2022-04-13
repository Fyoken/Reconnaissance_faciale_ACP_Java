package main;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import acp.Matrice;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
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
		
		return images;
	}
	
	@Override 
    public void start(Stage primaryStage) { 
        
    	// On récupère les distances
    	List<String> dist = getParameters().getUnnamed();
    	int taille = dist.size();
    	
    	// Création des K catégories
        final List<BarChart.Series> seriesList = new LinkedList<>(); 
        final String[] categoriesNames = new String[taille-1];
        for (int i = 0; i < taille-1; i++) {
        	int j = i+1;
        	categoriesNames[i] = ""+j;
        }
        final String[] seriesNames = {"Erreur"}; 
        final double[][] allValues = new double[1][taille];
        for (int  i = 0; i < taille-1; i++) {
            allValues[0][i]=Double.parseDouble(dist.get(i+1)); 
    	}
        final double minY = 0; 
        double maxY = -Double.MAX_VALUE; 
        for (int seriesIndex = 0; seriesIndex < seriesNames.length; seriesIndex++) { 
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
        
        // Création du graphique
        final CategoryAxis xAxis = new CategoryAxis(); 
        xAxis.getCategories().setAll(categoriesNames); 
        xAxis.setLabel("K"); 
        final NumberAxis yAxis = new NumberAxis(minY, maxY, 50); 
        yAxis.setLabel("Erreur"); 
        final BarChart chart = new BarChart(xAxis, yAxis); 
        chart.setTitle("Évolution de l'erreur en fonction de K"); 
        chart.getData().setAll(seriesList); 
        
        // Montage de l'interface 
        final StackPane root = new StackPane(); 
        root.getChildren().add(chart); 
        final Scene scene = new Scene(root, 500, 450); 
        primaryStage.setTitle("Histogramme"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    } 

	public static void main(String[] args) {
		initialisationBDD();
		Matrice images=initialisationMatriceImages();
		images.centralisation();
		images.matriceCovariance();
		images.valeursPropres();
		images.vecteursPropres();
		images.matriceProjection();
		Vecteur test5=images.reconstructionImage(0,15);
		test5.transfoMat().affichage();
		double[] vp = images.valeursPropres();
		
		
		// Méthode qui donne la variance cumulée
		images.normaliserEtAfficherVariation(vp);
		
		// Image test pour le calcul de l'erreur
		Image image = new Image("../BDD/Test/8.jpg");
		
		// On récupère les valeurs des erreurs en fonction de K et on les affiche graphiquement
		double[] d = new double[images.getVecteursPropres().getColumnDimension()];
		try {
			d = images.affichageGraphique(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String[] s = new String[d.length];
    	for (int i = 0; i < d.length; i++) {
    		s[i]=""+d[i];
    	}
    	launch(s); 
		
		
	}
}

