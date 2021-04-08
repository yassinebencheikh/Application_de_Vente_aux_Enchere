package sma.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.ControllerException;
import sma.CnosommateurContainer;

//guiagent sa fait dir un agent possede un interface graphique
public class ConsommateurAgent extends GuiAgent{
	//objet de type container pour l'agent
	private CnosommateurContainer gui;
	
	//initialisation de l'agent
	@Override
	protected void setup() {
		//pour recupere les asgument de l'gent c'est un tableau
		//l'interface liee a l'agent 
		gui=(CnosommateurContainer) getArguments()[0];
		//l'agent liee a l'interface graphique 
		gui.setConsommateurAgent(this);
		System.out.println("Initialisation de l'agent :"+this.getAID().getName());
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				MessageTemplate messageTemplate =  MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
						MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage message=  receive(messageTemplate);
			if(message!=null) {
				System.out.println("Reception d'un message"+ message.getContent());
				GuiEvent guiEvent= new GuiEvent(this,1);
				guiEvent.addParameter(message.getContent());
				
				gui.viewMessage(guiEvent);
			}
				
			}
		});
	}
 
  //definir les evenement dans l'interface graphique
@Override
public void onGuiEvent(GuiEvent guiEvent) {
	// si type==1 je vais envoyer un message
	if(guiEvent.getType()==1) {
	ACLMessage  aclMessage= new ACLMessage(ACLMessage.REQUEST);
	//recupere le msg avant l'envoye
	String produit = guiEvent.getParameter(0).toString();
	aclMessage.setContent(produit);
	aclMessage.addReceiver(new AID("acheteur", AID.ISLOCALNAME));
	send(aclMessage);
	}
}
}
