package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainContainer {

	public static void main(String[] args) {
		try {
			//demarage d'un main container 
			Runtime runtime=Runtime.instance();
			Properties properties= new ExtendedProperties();
			//pour affiche l'interface de jade
			properties.setProperty(Profile.GUI, "true");
			Profile  profile= new ProfileImpl(properties);
			//creation du container
			AgentContainer 	mainContainer = runtime.createMainContainer(profile);
			mainContainer.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
