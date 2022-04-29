package main;

import java.io.File;
import java.io.IOException;
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
		String[] noms = new String[bdd.size() * Personne.AuzollesM.getImages().size()];
		int i = 0;
		for (Personne personne : bdd) {
			for (personne.Image image : personne.getImages()) {
				images.ajouterImage(image.getPhoto().transfoVect());
				noms[i] = image.getNomImage();
				i++;
			}
		}
		images.matriceVisage();
		images.noms(noms);
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
		final int reconstruit = 0;
		final int K = 10;
		final int seuil = 6;

		Vecteur moy = images.getMoy();
		moy.transfoMat().affichage("moyenne.jpg");
		
		Vecteur vecteurImage = images.reconstructionImage(reconstruit, images.getVecteursPropres().getColumnDimension());
		vecteurImage.transfoMat().affichage("Image.jpg");

		
		images.affichageEigenfaces();

		
		File fichier = new File("image_base.png");
		Image image = new Image(fichier.toURI().toString());
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(150);
		imageView.setPreserveRatio(true);

		Label texte = new Label("Projet sur la reconnaissance faciale");

		Button imageReconstruite = new Button("Afficher l'image reconstruite");
		Button eigenfaces = new Button("Afficher les 6 premières eigenfaces");
		Button moyenne =  new Button("Afficher le visage moyenne");
		Button grapheErreurs = new Button("Afficher le graphique de l'évolution de l'erreur ");
		Button testerUneImage = new Button("Choisir une image à tester");

		File img_f = new File(images.getNoms()[reconstruit]);
		String localUrl = img_f.toURI().toString();

		Image img = new Image(localUrl);
		ImageView image_r = new ImageView(img);
		image_r.setFitHeight(150);
		image_r.setPreserveRatio(true);

		VBox boutons = new VBox();
		boutons.getChildren().addAll(imageReconstruite, moyenne,eigenfaces, grapheErreurs, testerUneImage);

		VBox informations = new VBox();
		informations.getChildren().addAll(texte, imageView);

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

				
				Image image = new Image(new File("Image.jpg").toURI().toString());
				imageView.setImage(image);
				HBox images = new HBox();
				images.getChildren().addAll(imageView, image_r);
				texte.setText("Visage de la base de donnée reconstruit");
				informations.getChildren().add(images);
			}
		});

		eigenfaces.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				informations.getChildren().remove(1);

				Image image = new Image(new File("eigenfaces.jpg").toURI().toString());
				imageView.setImage(image);
				
				texte.setText("Les 6 premiers eigenfaces");
				
				informations.getChildren().add(imageView);

			}
		});

		/* Pour changer de scene et aller sur celle des graphes d'erreur */

		grapheErreurs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				informations.getChildren().remove(1);
				informations.getChildren().add(graphes);
			}
		});

		testerUneImage.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// on retire l'affichage precedent
				informations.getChildren().remove(1);

				// on ouvre une fenetre pour selectionner un fichier
				FileChooser choix = new FileChooser();
				choix.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));

				// on recupere le fichier choisi
				File imageChoisi = choix.showOpenDialog(stage);

				// on ajoute l'image choisi dans l'affichage
				imageView.setImage(new Image(imageChoisi.toURI().toString()));

				// on cree une nouvelle image a partir du fichier choisi
				personne.Image imageC = new personne.Image(imageChoisi.toPath().toString());

				int i = images.reconnaissance(imageC, K, seuil);
				
				texte.setText("Teste de reconnaissance facial");
				VBox resultats = new VBox();
				
				if (i == -1) {
					Label resultat = new Label("Personne n'a été trouvé");
					resultats.getChildren().addAll(imageView, resultat);
				} else {
					ImageView viewTrouve = new ImageView();
					viewTrouve.setImage(new Image(new File(images.getNoms()[i]).toURI().toString()));
					viewTrouve.setFitHeight(150);
					viewTrouve.setPreserveRatio(true);
					String nom = images.getNoms()[i];

					// On separe le nom de l'image selon '_' puis selon '/' pour recuperer que le
					// nom et le prenom
					String[] nomSansUnderscore = nom.split("_", 2);
					String[] chaineAvecNom = nomSansUnderscore[0].split("/");
					String[] chaineAvecPrenom = nomSansUnderscore[1].split("/");
					String personne = chaineAvecNom[chaineAvecNom.length - 1] + " " + chaineAvecPrenom[0];

					Label resultat = new Label(personne+" est reconnu depuis l'image de gauche.");
					HBox imagesAffichees = new HBox();
					imagesAffichees.getChildren().addAll(imageView,viewTrouve);
					resultats.getChildren().addAll(imagesAffichees,resultat);
					
				}


				informations.getChildren().add(resultats);

			}
		});
		
		moyenne.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				informations.getChildren().remove(1);
				
				

				Image image = new Image(new File("moyenne.jpg").toURI().toString());
				imageView.setImage(image);
				
				texte.setText("Visage moyenne de la base de donnée ");
				
				informations.getChildren().add(imageView);
			
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

		int K = 6;
		int seuil = 5;

		// Test pour reconstruire la première image

		System.out.println("Done");
		double[] vp = images.valeursPropres();

		// Méthode qui donne la variance cumulée en fonction de K
		double[] res = images.normaliserEtAfficherVariation(vp);

		// Première image de la base de référence pour le calcul de l'erreur
		personne.Image image = new personne.Image("../BDD/Train/LASGLEIZES_David/LASGLEIZES_David_3.jpg");

		// On teste la reconnaissance avec toutes les images d'une personne de la base
		// d'apprentissage
		// Les distances sont presques nulles
		for (int im = 1; im < 4; im++) {
			personne.Image test = new personne.Image("../BDD/Train/AUZOLLES_Melina/AUZOLLES_Melina_" + im + ".jpg");
			int i = images.reconnaissance(test, K, seuil);
			System.out.println("Apprentissage " + im);

			// Si i est different de -1, c'est qu'une correspondance a ete trouvee
			if (i != -1) {
				// On recupere le nom et le prenom via le nom image de la forme
				// ../BDD/NOM_Prenom∕NOM_Prenom_i.jpg
				String nom = images.getNoms()[i];

				// On separe le nom de l'image selon '_' puis selon '/' pour recuperer que le
				// nom et le prenom
				String[] nomSansUnderscore = nom.split("_", 2);
				String[] chaineAvecNom = nomSansUnderscore[0].split("/");
				String[] chaineAvecPrenom = nomSansUnderscore[1].split("/");
				String personne = chaineAvecNom[chaineAvecNom.length - 1] + " " + chaineAvecPrenom[0];

				// Affichage du nom et de la distance recalculee
				System.out.println("Ce visage correspond à celui de " + personne);
				double[] projection = images.projection(test, K);
				double distance = 0;
				for (int j = 0; j < projection.length; j++) {
					distance += Math.pow(images.getMatriceProjection().get(i, j) - projection[j], 2);
				}
				distance = Math.sqrt(distance);
				System.out.println("La distance est de : " + distance);
			} else {
				// Le seuil n'est pas respecte
				System.out.println("Ce visage n'appartient pas à la base");
			}
			System.out.println("\n");
		}

		// On teste la reconnaissance avec toutes les images de la base de test,
		// personne dans la base et personne pas dans la base,

		// le ratio de bonnes reponses est de 14/17 avec les 6 premieres eigenfaces et
		// un seuil de 5

		// Les distances sont entre 1 et 5
		for (int im = 1; im < 18; im++) {
			personne.Image test = new personne.Image("../BDD/Test/" + im + ".jpg");
			int i = images.reconnaissance(test, K, seuil);
			System.out.println("Test " + im);

			// Si i est different de -1, c'est qu'une correspondance a ete trouvee
			if (i != -1) {
				// On recupere le nom et le prenom via le nom image de la forme
				// ../BDD/NOM_Prenom∕NOM_Prenom_i.jpg
				String nom = images.getNoms()[i];

				// On separe le nom de l'image selon '_' puis selon '/' pour recuperer que le
				// nom et le prenom
				String[] nomSansUnderscore = nom.split("_", 2);
				String[] chaineAvecNom = nomSansUnderscore[0].split("/");
				String[] chaineAvecPrenom = nomSansUnderscore[1].split("/");
				String personne = chaineAvecNom[chaineAvecNom.length - 1] + " " + chaineAvecPrenom[0];

				// Affichage du nom et de la distance recalculee
				System.out.println("Ce visage correspond à celui de " + personne);
				double[] projection = images.projection(test, K);
				double distance = 0;
				for (int j = 0; j < projection.length; j++) {
					distance += Math.pow(images.getMatriceProjection().get(i, j) - projection[j], 2);
				}
				distance = Math.sqrt(distance);
				System.out.println("La distance est de : " + distance);
			} else {
				// Le seuil n'est pas respecte
				System.out.println("Ce visage n'appartient pas à la base");
			}
			System.out.println("\n");
		}

		// images.affichageEigenfaces();

		// Image de la bonne personne mais avec une image de test pour le calcul de
		// l'erreur
		// Image image = new Image("../BDD/Test/3.jpg");
		// On récupère les valeurs des erreurs en fonction de K

		// On compare image à la première image de la base 
		double[] d = images.affichageGraphique(image, 0);
		// On ajoute toutes les valeurs dedistances puis la variance cumulée en fonction
		// de K dans une chaîne
		String[] s = new String[d.length + res.length];
		for (int i = 0; i < d.length; i++) {
			s[i] = "" + d[i];
		}
		for (int i = d.length; i < s.length; i++) {
			s[i] = "" + res[i - d.length];
		}

		// On lance la méthode start avec notre paramètre et on affiche les deux
		// graphiques
		
		launch();
	}
}
