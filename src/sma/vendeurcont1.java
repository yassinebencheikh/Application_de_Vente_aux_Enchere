package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sma.agents.ConsommateurAgent;
import sma.agents.VendeurAgent;


public class vendeurcont1 extends Application{
 
	//liste des string  element de viewliste
	private ObservableList<String> observableList;
	//objet de type agent  pour le container
	private VendeurAgent vendeur1;
	
	
	public static void main(String[] args) {
	
		//demmare l'interface graphique du consommateur
		launch(vendeurcont1.class);

	}
	public void StartContainer() {
		
		
		try {
			Runtime runtime = Runtime.instance();
			//false car se pas un main container c un simple container
			Profile profile = new ProfileImpl(false);
			//ce container vas conecte aux main container qui se trouve sur la machine local
			profile.setParameter(profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer=runtime.createAgentContainer(profile);
			AgentController agentController=agentContainer.createNewAgent
					("vendeur1", "sma.agents.VendeurAgent", new Object[] {this});
			agentController.start();
			
		} catch (ControllerException e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//pour demmarage de la methode
		StartContainer();
		primaryStage.setTitle("Vendeur1");
		//un paneau qui divise la scene un 5 
		BorderPane borderPane = new BorderPane();
		//pour organise des elements horizentalement
		HBox hBox = new HBox();
		//espacement de 10 pixel
		hBox.setPadding(new Insets(10));
		//espace entre les element 
		hBox.setSpacing(10);
		Label label = new Label("Prix:");
		TextField textField = new TextField();
		Button buttonacheter = new Button("Envoyer");
		
		//ajouter les element
		hBox.getChildren().add(label);
		hBox.getChildren().add(textField);
		hBox.getChildren().add(buttonacheter);
		//metre hbox au nord
		borderPane.setTop(hBox);
		
		//element vertical
		VBox vBox = new VBox();
		//divise un paneau sous fore de gris avec des lignes et colones
		GridPane gridPane= new GridPane();
		 observableList=FXCollections.observableArrayList();
		 // afiche des donnes stocke dans opservabliste
		ListView<String> listViewmessage = new ListView<String>(observableList);
		gridPane.add(listViewmessage, 0, 0);
		vBox.getChildren().add(gridPane);
		//place la liste au centre
		borderPane.setCenter(vBox); 
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		Scene scene= new Scene(borderPane,400,500);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonacheter.setOnAction(new EventHandler<ActionEvent>() {
			
			//handle defini  l'action 
			@Override
			public void handle(ActionEvent event) {
				
				String livre = textField.getText();
				//  guievent contient 2 parameter ressouce de l'evenement=this ce l'agent , 1 ce le type 
				// car on peux definie plus type 2 et 3 etc 
				GuiEvent guiEvent =new GuiEvent(this,1);
				guiEvent.addParameter(livre); 
				vendeur1.onGuiEvent(guiEvent);
				
			}
		});
		
	}

	

	 public VendeurAgent getVendeur1() {
		return vendeur1;
	}
	public void setVendeur1(VendeurAgent vendeur1) {
		this.vendeur1 = vendeur1;
	}
	public void viewMessage(GuiEvent guiEvent) {
		 String message = guiEvent.getParameter(0).toString();
		 observableList.add(message);
		 
	 }

}
