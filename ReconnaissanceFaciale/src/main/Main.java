package main;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application { 
	  
    @Override 
    public void start(Stage primaryStage) { 
        
    	// On récupère les distances
    	List<String> dist = getParameters().getUnnamed();
    	int taille = dist.size();
    	
    	// Création des K catégories
        final List<BarChart.Series> seriesList = new LinkedList<>(); 
        final String[] categoriesNames = new String[taille];
        for (int i = 0; i < taille; i++) {
        	int j = i+1;
        	categoriesNames[i] = ""+j;
        }
        final String[] seriesNames = {"Erreur"}; 
        final double[][] allValues = new double[1][taille];
        for (int  i = 0; i < taille; i++) {
            allValues[0][i]=Double.parseDouble(dist.get(i)); 
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
    	
        // Mettre dans d la valeur renvoyée par affichageGraphique()
    	double[] d = {50,25,12,5,3};
    	String[] s = new String[d.length];
    	for (int i = 0; i < d.length; i++) {
    		s[i]=""+d[i];
    	}
    	launch(s); 
    }

}