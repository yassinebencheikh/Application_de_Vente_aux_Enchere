package sma.agents;


import java.util.ArrayList;



import java.util.Collections;
import java.util.List;
import java.util.Vector;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.ControllerException;
import sma.AcheteurContainer;


public class AcheteurAgent extends GuiAgent{
	private AID meilleureOffre;
	private double meilleurPrix;
	private int index; 
	
	private AcheteurContainer gui;
	private AID[] listevendeur;
	
	
	
	@Override
	protected void setup() {
		gui=(AcheteurContainer) getArguments()[0];
		gui.setAcheteurAgent(this);
		System.out.println("Initialisation de l'agent :"+this.getAID().getName());
		
		//demmare plusieurs comportement 
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,6000) {
			
			@Override
			protected void onTick() {
				
				 try {
					//System.out.println("hmdhmdhmd");
					DFAgentDescription description = new DFAgentDescription();
					ServiceDescription serviceDescription= new ServiceDescription();
					serviceDescription.setType("Vente");
					description.addServices(serviceDescription);
					DFAgentDescription[] agentDescriptions= DFService.search(myAgent, description);
					listevendeur = new AID[agentDescriptions.length];
					for(int i=0;i<agentDescriptions.length;i++) {
					listevendeur[i]=agentDescriptions[i].getName();
					}	
					
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}
		
		});
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				
				MessageTemplate template=
						MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
				ACLMessage aclMessage=receive(template);	
 
				if(aclMessage!=null){
					switch(aclMessage.getPerformative()){
					case ACLMessage.REQUEST :
						
						System.out.println("Reception d'un message"+ aclMessage.getContent());
						GuiEvent guiEvent= new GuiEvent(this,1);
						String nomproduit= aclMessage.getContent();
						guiEvent.addParameter(nomproduit);				
						gui.viewMessage(guiEvent);
						
						ACLMessage aclMessage1 = new ACLMessage(ACLMessage.CFP);
						aclMessage1.setContent(nomproduit);
						
						aclMessage1.addReceiver(new AID("vendeur1", AID.ISLOCALNAME));
						aclMessage1.addReceiver(new AID("vendeur2", AID.ISLOCALNAME));
						aclMessage1.addReceiver(new AID("vendeur3", AID.ISLOCALNAME));
						send(aclMessage1);
						System.out.println("en cours");
						send(aclMessage1);	
						
						break;
						
					case ACLMessage.PROPOSE :
						GuiEvent guiEvent1= new GuiEvent(this,1);
						String prix=aclMessage.getContent();
						int prixfin = Integer.parseInt(prix);
			
						guiEvent1.addParameter(prixfin);				
						gui.viewMessage(guiEvent1);
						if(index==0){
							meilleurPrix=prixfin;
							meilleureOffre=aclMessage.getSender();
							}
							else{
							if(prixfin<meilleurPrix){
							meilleurPrix=prixfin;
							meilleureOffre=aclMessage.getSender();
							 
							
							} }
							++index;
							if(index==3){
							ACLMessage aclMessage2=new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
							
							aclMessage2.addReceiver(meilleureOffre);
							aclMessage2.setContent("votre prix est accepter");
							send(aclMessage2);
							
							ACLMessage msg3=new ACLMessage(ACLMessage.INFORM);
							msg3.addReceiver(new AID("consommateur", AID.ISLOCALNAME));
							msg3.setContent(" Voici l'adresse de l'agent qui offre un meilleur prix : "
									 + meilleureOffre.getName());
							send(msg3);
							
							doDelete();
							
							}
							break;
					}
				}
				
					else{
						
						block();
								}	
					
			}
		
		  
		
		});
	}
	
	

		/*
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
		
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				MessageTemplate messageTemplate1 = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
				ACLMessage msg=  receive(messageTemplate1);
				if(msg!=null) {
					
					GuiEvent guiEvent= new GuiEvent(this,1);
					String prix=msg.getContent();
					int prixfin = Integer.parseInt(prix);
		
					guiEvent.addParameter(prixfin);				
					gui.viewMessage(guiEvent);
					
					
					if(index==0){
						meilleurPrix=prixfin;
						meilleureOffre=msg.getSender();
						}
						else{
						if(prixfin<meilleurPrix){
						meilleurPrix=prixfin;
						meilleureOffre=msg.getSender();
						 String pr =msg.getContent();
						
						} }
						++index;
						if(index==3){
						ACLMessage aclMessage2=new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
						
						aclMessage2.addReceiver(meilleureOffre);
						aclMessage2.setContent("votre prix est accepter");
						send(aclMessage2);
						
						ACLMessage msg3=new ACLMessage(ACLMessage.INFORM);
						msg3.addReceiver(new AID("consommateur", AID.ISLOCALNAME));
						msg3.setContent(" Voici l'adresse de l'agent qui offre un meilleur prix : "
								 + meilleureOffre.getName());
						send(msg3);
						
						
						}
				}
				
				}
		});
			
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				
			
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message=  receive(messageTemplate);
			if(message!=null) {
				System.out.println("Reception d'un message"+ message.getContent());
				GuiEvent guiEvent= new GuiEvent(this,1);
				String nomproduit= message.getContent();
				guiEvent.addParameter(nomproduit);				
				gui.viewMessage(guiEvent);
				
				ACLMessage aclMessage = new ACLMessage(ACLMessage.CFP);
				aclMessage.setContent(nomproduit);
				aclMessage.addReceiver(new AID("vendeur1", AID.ISLOCALNAME));
				aclMessage.addReceiver(new AID("vendeur2", AID.ISLOCALNAME));
				aclMessage.addReceiver(new AID("vendeur3", AID.ISLOCALNAME));
				send(aclMessage);
				System.out.println("en cours");
				send(aclMessage);
			}
	
			
			}
			
		});
	}
		
 */
@Override
public void onGuiEvent(GuiEvent guiEvent) {
	
	
}
}
