package main;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import acp.Matrice;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import personne.Personne;
import vectorisation.Vecteur;

public class Main extends Application {
	public static Set<Personne> bdd = new HashSet<Personne>();

	public static void initialisationBDD() {
		bdd.add(Personne.AuzollesM);
		bdd.add(Personne.BarbosaM);
		bdd.add(Personne.ChambasM);
		bdd.add(Personne.RibayneM);
		bdd.add(Personne.SallM);
		bdd.add(Personne.BertailsC);
		bdd.add(Personne.BlondeyB);
		bdd.add(Personne.DonneyV);
		bdd.add(Personne.JousselinH);
		bdd.add(Personne.LamyM);
		bdd.add(Personne.LasgleizesD);
		bdd.add(Personne.MongourM);
		bdd.add(Personne.RibasJ);
		bdd.add(Personne.RodriguesS);
	}

	public static Matrice initialisationMatriceImages() {
		Matrice images = new Matrice(50 * 50, bdd.size() * Personne.AuzollesM.getImages().size());
		for (Personne personneBDD : bdd) {
			for (personne.Image image : personneBDD.getImages()) {
				images.ajouterImage(image.getPhoto().transfoVect());
			}
		}
		images.moyenne();
		images.centralisation();
		images.matriceCovariance();
		images.valeursPropres();
		images.vecteursPropres();
		images.matriceProjection();
		return images;
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Logiciel de reconnaissance faciale | Groupe 5");

		initialisationBDD();
		final Matrice images = initialisationMatriceImages();
		final int K=6;
		final int seuil=5;

		
		File fichier = new File("image_base.png");
		Image image = new Image(fichier.toURI().toString());
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(150);
		imageView.setPreserveRatio(true);

		Label texte = new Label("Projet sur la reconnaissance faciale");

		Button imageReconstruite = new Button("Afficher l'image reconstruite");
		Button eigenfaces = new Button("Afficher les 6 premières eigenfaces");
		Button grapheErreurs = new Button("Afficher le graphique de l'évolution de l'erreur ");
		Button testerUneImage = new Button("Choisir une image à tester");

		File img_f = new File("../BDD/Train/LASGLEIZES_David/LASGLEIZES_David_3.jpg");
		String localUrl = img_f.toURI().toString();

		Image img = new Image(localUrl);
		ImageView image_r = new ImageView(img);
		image_r.setFitHeight(150);
		image_r.setPreserveRatio(true);


		VBox boutons = new VBox();
		boutons.getChildren().addAll(imageReconstruite, eigenfaces, grapheErreurs, testerUneImage);

		VBox informations = new VBox();
		informations.getChildren().addAll(texte,imageView);

		HBox general = new HBox();
		general.getChildren().addAll(informations, boutons);
		general.setPrefSize(1200, 500);

		// On récupère les distances et les points de la courbe
		List<String> distPoint = getParameters().getUnnamed();

		// Taille pour les distances
		int tailleDist = distPoint.size() / 2 + 1;

		// Création des K catégories pour les distances
		final List<BarChart.Series> seriesList = new LinkedList<>();
		final String[] categoriesNames = new String[tailleDist - 1];
		for (int i = 0; i < tailleDist - 1; i++) {
			int j = i + 1;
			categoriesNames[i] = "" + j;
		}

		// Ajout de la série Erreur et ajout des valeurs
		final String[] seriesNames = { "Erreur" };
		final double[][] allValues = new double[1][tailleDist];
		for (int i = 0; i < tailleDist - 1; i++) {
			allValues[0][i] = Double.parseDouble(distPoint.get(i + 1));
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
		final NumberAxis xAxis2 = new NumberAxis(1, tailleDist - 1, 1);
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
			seriesVar.getData()
					.add(new XYChart.Data<Number, Number>(i + 1 - tailleDist, Double.parseDouble(distPoint.get(i))));
		}

		/* Pour la courbe */
		BorderPane courbe = new BorderPane();
		courbe.setCenter(areaChart);
		areaChart.getData().addAll(seriesVar);
		courbe.setPrefSize(400, 300);

		/* Pour l'histogramme */
		StackPane histo = new StackPane();
		histo.getChildren().add(chart);
		histo.setPrefSize(500, 450);

		/* HBox avec les 2 graphes qu'on ajoute */
		HBox graphes = new HBox();
		graphes.getChildren().addAll(courbe, histo);

		/* Pour changer de scene et aller sur celle de l'image reconstruite */

		imageReconstruite.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				informations.getChildren().remove(1);

				Vecteur test = images.reconstructionImage(0, images.getVecteursPropres().getColumnDimension());
				test.transfoMat().affichage();
				
				Image image = new Image(new File("Image.jpg").toURI().toString());
				imageView.setImage(image);
				HBox images = new HBox();
				images.getChildren().addAll(imageView,image_r);
				texte.setText("Visage de la base de donnée reconstruit");
				informations.getChildren().addAll(texte, images);
			}
		});

		eigenfaces.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				informations.getChildren().remove(1);
				
				images.affichageEigenfaces();

				Image image = new Image(new File("eigenfaces.jpg").toURI().toString());
				imageView.setImage(image);
				texte.setText("Les 6 premiers eigenfaces");
				informations.getChildren().addAll(texte, imageView);


			}
		});

		/* Pour changer de scene et aller sur celle des graphes d'erreur */

		grapheErreurs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				informations.getChildren().remove(0);
				informations.getChildren().remove(0);
				informations.getChildren().addAll(texte, graphes);
			}
		});

		testerUneImage.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				//on retire l'affichage precedent
				informations.getChildren().remove(1);
				
				//on ouvre une fenetre pour selectionner un fichier
				FileChooser choix = new FileChooser();
				choix.getExtensionFilters().add(new ExtensionFilter("Image Files","*.jpg","*.png","*.jpeg"));
				
				//on recupere le fichier choisi
				File imageChoisi = choix.showOpenDialog(stage);
				
				//on ajoute l'image choisi dans l'affichage
				imageView.setImage(new Image(imageChoisi.toURI().toString()));
				
				//on cree une nouvelle image a partir du fichier choisi
				personne.Image imageC = new personne.Image(imageChoisi.toPath().toString());
				
				int i =images.reconnaissance(imageC, K, seuil);
				Label resultat = new Label("C'est l'image : "+String.valueOf(i));
				if(i==-1) {
					resultat.setText("Personne n'a été trouvé");
				}
				
				texte.setText("Teste de reconnaissance facial");
				HBox resultats = new HBox();
				resultats.getChildren().addAll(imageView,resultat);
				
				informations.getChildren().add(resultats);
				
				

				
			}
		});
		
		/*
		 * La scène principale, la première sur laquelle on est et celle sur laquelle on
		 * peut revenir
		 */
		Scene scene = new Scene(general);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	public static void main(String[] args) {
		initialisationBDD();
		Matrice images = initialisationMatriceImages();


		// Test pour reconstruire la première image
	
		System.out.println("Done");
		double[] vp = images.valeursPropres();

		// Méthode qui donne la variance cumulée en fonction de K
		double[] res = images.normaliserEtAfficherVariation(vp);

		// Première image de la base de référence pour le calcul de l'erreur
		personne.Image image = new personne.Image("../BDD/Train/LASGLEIZES_David/LASGLEIZES_David_3.jpg");

		// Image de la bonne personne mais avec une image de test pour le calcul de
		// l'erreur
		// Image image = new Image("../BDD/Test/3.jpg");

		// On récupère les valeurs des erreurs en fonction de K

		// On compare image à la première image de la base
		double[] d = images.affichageGraphique(image, 0);
		// On ajoute toutes les valeurs de distances puis la variance cumulée en
		// fonction de K dans une chaîne
		String[] s = new String[d.length + res.length];
		for (int i = 0; i < d.length; i++) {
			s[i] = "" + d[i];
		}
		for (int i = d.length; i < s.length; i++) {
			s[i] = "" + res[i - d.length];
		}

		// On lance la méthode start avec notre paramètre et on affiche les deux
		// graphiques
		launch(s);

	}
}
