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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import personne.Personne;
import vectorisation.Vecteur;

public class Main extends Application {
	public static Set<Personne> bdd = new HashSet<Personne>();

	// methode permetant d'initialiser la base de donne
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

	// methode permettant d'initialiser la matrice creee a partir de la base de
	// donnee
	public static Matrice initialisationMatriceImages() {
		// creation d'une matrice
		Matrice images = new Matrice(50 * 50, bdd.size() * Personne.AuzollesM.getImages().size());
		// initialisation de la variable pour la liste des noms des fichiers
		String[] noms = new String[bdd.size() * Personne.AuzollesM.getImages().size()];
		int i = 0;
		for (Personne personne : bdd) {
			for (personne.Image image : personne.getImages()) {
				// ajout dans la matrice chaque images de chaque personne
				images.ajouterImage(image.getPhoto().transfoVect());
				// ajout du nom de l'image
				noms[i] = image.getNomImage();
				i++;
			}
		}
		images.matriceVisage();
		// ajout la liste des noms dans la matrice
		images.noms(noms);
		// initialisation du visage moyen de la matrice
		images.moyenne();
		// centralisation des valeurs dans la matrice
		images.centralisation();
		// initialisation de la matrice de covariance
		images.matriceCovariance();
		// initialisation des valeurs propres
		images.valeursPropres();
		// initialisation des vecteurs propres
		images.vecteursPropres();
		// initialisation de la matrice de projection
		images.matriceProjection();

		return images;
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Logiciel de reconnaissance faciale | Groupe 5");

		initialisationBDD();
		final Matrice images = initialisationMatriceImages();

		// constante pour le numero de l'image a reconstruire
		final int reconstruit = 0;
		// constante pour le numero de l'image a tester pour avoir le graphe d'erreur
		final int valeurTest = 1;
		// constante du nombre d'eigenface utilise
		final int K = 17;
		// seuil acceptable pour la reconnaissance d'un visage
		final int seuil = 7;

		// creation de l'image representant le visage moyen
		Vecteur moy = images.getMoy();
		moy.transfoMat().affichage("moyenne.jpg");

		// creation de l'image du visage reconstruit
		Vecteur vecteurImage = images.reconstructionImage(reconstruit, K);
		vecteurImage.transfoMat().affichage("Image.jpg");

		// creation de l'image avec les 6 premieres eigenfaces
		images.affichageEigenfaces();

		// on recupere les valeurs propres
		double[] vp = images.valeursPropres();
		// Méthode qui donne la variance cumulée en fonction de K
		double[] varianceCumule = images.normaliserEtAfficherVariation(vp);

		// Première image de la base de référence pour le calcul de l'erreur
		personne.Image imageTest = new personne.Image(images.getNoms()[valeurTest]);
		// On compare image à la première image de la base
		double[] d = images.affichageGraphique(imageTest, valeurTest);

		// fichier pour l'image afficher lors de l'ouverture de la fenetre
		File fichier = new File("image_base.png");
		Image image = new Image(fichier.toURI().toString());
		// creation d'un image view pour l'image
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(150);
		imageView.setPreserveRatio(true);

		// texte afficher dans la fenetre
		Label texte = new Label("Projet sur la reconnaissance faciale");

		Button imageReconstruite = new Button("Afficher l'image reconstruite");
		Button eigenfaces = new Button("Afficher les 6 premiers eigenfaces");
		Button moyenne = new Button("Afficher le visage moyen");
		Button grapheErreurs = new Button("Afficher le graphique de l'évolution de l'erreur ");
		Button valeurPropre = new Button("Afficher l'évolution des valeurs propres ");
		Button testerUneImage = new Button("Choisir une image à tester");

		// image de reference de l'image reconstruite
		File img_f = new File(images.getNoms()[reconstruit]);
		String localUrl = img_f.toURI().toString();
		Image img = new Image(localUrl);
		// creation d'un image view pour l'image de reference
		ImageView image_r = new ImageView(img);
		image_r.setFitHeight(150);
		image_r.setPreserveRatio(true);

		// vbox contenant tous les boutons
		VBox boutons = new VBox();
		boutons.getChildren().addAll(imageReconstruite, moyenne, eigenfaces, grapheErreurs,valeurPropre, testerUneImage);

		// vbox pour afficher les informations en fonction du boutons clique
		VBox informations = new VBox();
		informations.getChildren().addAll(texte, imageView);

		// panneau general de l'ihm
		HBox general = new HBox();
		general.getChildren().addAll(informations, boutons);
		general.setPrefSize(1200, 500);

		// Taille pour les distances
		int tailleDist = d.length;

		// Création des K catégories pour les distances
		final List<BarChart.Series> seriesList = new LinkedList<>();
		final String[] categoriesNames = new String[tailleDist];
		for (int i = 0; i < tailleDist; i++) {
			categoriesNames[i] = "" + i;
		}

		// Ajout de la série Erreur et ajout des valeurs
		final String[] seriesNames = { "Erreur" };
		final double[][] allValues = new double[1][tailleDist];
		for (int i = 0; i < tailleDist; i++) {
			allValues[0][i] = d[i];

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
		final NumberAxis xAxis2 = new NumberAxis(1, varianceCumule.length, 1);
		final NumberAxis yAxis2 = new NumberAxis();
		
		// Déclaration d'une courbe
		final AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis2, yAxis2);
		areaChart.setTitle("Variance cumulée des K premières valeurs propres");
		areaChart.setLegendSide(Side.LEFT);
		
		// Valeurs des x et y
		XYChart.Series<Number, Number> seriesVar = new XYChart.Series<Number, Number>();
		
		seriesVar.setName("Pourcentage de variance cumulée");
		
		// On ajoute toutes les valeurs
		for (int i = 0; i < varianceCumule.length; i++) {
			seriesVar.getData().add(new XYChart.Data<Number, Number>(i + 1, varianceCumule[i]));
		}
		// Création de la courbe pour les valeurs propres
		final NumberAxis xAxis3 = new NumberAxis(1, varianceCumule.length, 1);
		final NumberAxis yAxis3 = new NumberAxis();

		// Déclaration d'une courbe
		final AreaChart<Number, Number> valeurPropreGraphe = new AreaChart<Number, Number>(xAxis3, yAxis3);
		valeurPropreGraphe.setTitle("Valeurs propres par ordre décroissant");

		// Valeurs des x et y
		XYChart.Series<Number, Number> valeurs = new XYChart.Series<Number, Number>();
		valeurs.setName("Valeurs propres");
		
		// On ajoute toutes les valeurs
		for (int i = 0; i < vp.length; i++) {
			valeurs.getData().add(new XYChart.Data<Number, Number>(i + 1, vp[i]));
		}

		/* Pour la courbe des variances cumulées*/
		BorderPane courbe = new BorderPane();
		courbe.setCenter(areaChart);
		areaChart.getData().addAll(seriesVar);
		courbe.setPrefSize(400, 300);
		
		/* Pour la courbe des variances cumulées*/
		BorderPane courbeVP = new BorderPane();
		courbeVP.setCenter(valeurPropreGraphe);
		valeurPropreGraphe.getData().addAll(valeurs);
		courbeVP.setPrefSize(400, 300);

		/* Pour l'histogramme */
		StackPane histo = new StackPane();
		histo.getChildren().add(chart);
		histo.setPrefSize(500, 450);

		/* HBox avec les 2 graphes qu'on ajoute */
		HBox graphes = new HBox();
		graphes.getChildren().addAll(courbe,courbeVP);

		/* Pour changer de scene et aller sur celle de l'image reconstruite */

		imageReconstruite.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// suppression de l'affichage precedent
				informations.getChildren().remove(1);

				// creation et insertion dans l'imageView d'une image contenant la
				// reconstruction d'un visage de la base de donnee
				Image image = new Image(new File("Image.jpg").toURI().toString());
				imageView.setImage(image);

				// hbox pour pouvoir comparer l'image de reference et l'image reconstruite
				HBox images = new HBox();
				images.getChildren().addAll(imageView, image_r);

				// modification du texte
				texte.setText("Visage de la base de données reconstruit");

				// ajout des images dans l'affichage
				informations.getChildren().add(images);
			}
		});

		// evenement pour afficher les eigenfaces
		eigenfaces.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// suppression de l'affichage precedent
				informations.getChildren().remove(1);

				// creation d'une image a partir du fichier image des eigenfaces
				Image image = new Image(new File("eigenfaces.jpg").toURI().toString());
				// ajout de l'image dans l'image view
				imageView.setImage(image);

				// modification du texte
				texte.setText("Les 6 premiers eigenfaces");

				// ajout de l'imageView dans l'affichage
				informations.getChildren().add(imageView);

			}
		});

		
		//evenement pour afficher les graphiques liés aux valeurs propres	
		valeurPropre.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// suppression de l'affichage precedent
				informations.getChildren().remove(1);
				// modification du texte
				texte.setText("Graphiques liées aux valeurs propres");
				// ajout des graphes dans l'affichage
				informations.getChildren().add(graphes);
				
			}
		});
		
		//evenement pour afficher le graphique de l'évolution de l'erreur	
		grapheErreurs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// suppression de l'affichage precedent
				informations.getChildren().remove(1);
				// modification du texte
				texte.setText("Evolution de l'erreur en fonction de K");
				// ajout du graphes dans l'affichage
				informations.getChildren().add(histo);

			}
		});

		// evenement pour faire la reconnaissance faciale sur une image
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
				personne.Image imageC = new personne.Image(imageChoisi.getPath());

				// on fait la reconnaissance sur l'image choisi
				int i = images.reconnaissance(imageC, K, seuil);

				// modification du texte a afficher
				texte.setText("Test de reconnaissance faciale");

				// creation d'une nouvelle vbox pour afficher les resultats
				VBox resultats = new VBox();

				// si la reconnaissance n'a pas fonctionne on affiche seulement un texte
				if (i == -1) {
					Label resultat = new Label("Personne n'a été trouvé");
					resultats.getChildren().addAll(imageView, resultat);
				} else {
					// si la reconnaissance a fonctionne

					// creation d'un nouvel imageView
					ImageView viewTrouve = new ImageView();
					// creation et insertion dans l'imageView de l'image trouve par la
					// reconnaissance
					viewTrouve.setImage(new Image(new File(images.getNoms()[i]).toURI().toString()));
					viewTrouve.setFitHeight(150);
					viewTrouve.setPreserveRatio(true);
					// on recupere le chemin de l'image trouvee
					String nom = images.getNoms()[i];

					// On separe le nom de l'image selon '_' puis selon '/' pour recuperer que le
					// nom et le prenom
					String[] nomSansUnderscore = nom.split("_", 2);
					String[] chaineAvecNom = nomSansUnderscore[0].split("/");
					String[] chaineAvecPrenom = nomSansUnderscore[1].split("/");
					String personne = chaineAvecNom[chaineAvecNom.length - 1] + " " + chaineAvecPrenom[0];

					// creation d'un texte contenant le nom de la personne retrouve
					Label resultat = new Label(personne + " est reconnu depuis l'image de gauche.");
					// hbox pour pouvoir comparer l'image choisie et l'image trouvee
					HBox imagesAffichees = new HBox();
					imagesAffichees.getChildren().addAll(imageView, viewTrouve);
					// ajout des resultats dans le vbox
					resultats.getChildren().addAll(imagesAffichees, resultat);

				}
				// ajout des resultats dans l'affichage
				informations.getChildren().add(resultats);

			}
		});

		// evenement pour afficher le visage moyen
		moyenne.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// suppression de l'affichage precedent
				informations.getChildren().remove(1);

				// creation et insertion dans l'imageView d'une image avec le fichier image du
				// visage moyen
				Image image = new Image(new File("moyenne.jpg").toURI().toString());
				imageView.setImage(image);

				// modification du texte
				texte.setText("Visage moyen de la base de donnée ");

				// ajout dans l'affichage
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
		launch();
	}
}
