package sma.agents;

import jade.core.AID;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.ControllerException;
import sma.CnosommateurContainer;
import sma.vendeurcont1;
import sma.vendeurcont2;

public class VendeurAgent2 extends GuiAgent{
	private vendeurcont2 gui;
	
	@Override
	protected void setup() {
		gui=(vendeurcont2) getArguments()[0];
		gui.setVendeur2(this);
		System.out.println("Initialisation de l'agent :"+this.getAID().getName());
		
		//pour demmare plusieurs comportement 
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		
parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
			
			// publication de services
			
			@Override
			public void action() {
				System.out.println("salut");
				DFAgentDescription dfa = new DFAgentDescription();
				dfa.setName(getAID());
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Vente ");
				sd.setName("Vente livre");
				dfa.addServices(sd);
				try {
					// myAgent fait reference a l'agent en cours
					DFService.register(myAgent, dfa);
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			// publication de services
			
			@Override
			public void action() {
			
				
				MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
				ACLMessage aclMessage= receive(messageTemplate);
				if(aclMessage!=null) {
					
					GuiEvent guiEvent = new GuiEvent(this, 1);
					guiEvent.addParameter(aclMessage.getContent());
					gui.viewMessage(guiEvent);
					

				
				}		
			}
		});
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
				ACLMessage aclMessage= receive(messageTemplate);
				if(aclMessage!=null) {
					
					GuiEvent guiEvent = new GuiEvent(this, 1);
					guiEvent.addParameter(aclMessage.getContent());
					gui.viewMessage(guiEvent);
					

				
				}	
			}
		});
	}
 
@Override
public void onGuiEvent(GuiEvent guiEvent) {
	
	if(guiEvent.getType()==1) {
	ACLMessage  aclMessage= new ACLMessage(ACLMessage.PROPOSE);
	String livre = guiEvent.getParameter(0).toString();
	aclMessage.setContent(livre);
	aclMessage.addReceiver(new AID("acheteur",AID.ISLOCALNAME));
	send(aclMessage);
	}
}
}
