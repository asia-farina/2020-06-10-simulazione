/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	txtResult.clear();
    	List<Actor> attori;
    	try {
    		   attori=model.raggiungibili(boxAttore.getValue());
    	} catch (NullPointerException e) {
    		txtResult.appendText("Seleziona un attore");
    		return ;
    	}
        String s="ATTORI RAGGIUNGIBILI:\n";
        if (attori.size()==0) {
        	s+="Non ci sono attori raggiungibili";
        } else {
        	for (Actor a:attori) {
        		s+=a.toString()+"\n";
        	}
        }
        txtResult.appendText(s);     
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	try {
    		model.creaGrafo(boxGenere.getValue());
    	} catch (NullPointerException e) {
    		txtResult.appendText("Seleziona un genere");
    		return ;
    	}
    	boxAttore.getItems().addAll(model.getAttori());
        txtResult.appendText("GRAFO CREATO"+"\n"+"#VERTICI: "+model.getNumVertici()+"\n"+"#ARCHI: "+model.getNumArchi());
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	txtResult.clear();
    	int giorni;
    	try {
    		String s=txtGiorni.getText();
    		giorni=Integer.parseInt(s);
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Inserire un numero valido");
    		return ;
    	}
    	
    	model.initialize(giorni);
    	model.run();
    	String ss="INTERVISTATI: \n";
    	Actor intervistati []=model.getIntervistati();
    	for (int i=0; i<intervistati.length; i++) {
    		ss+="Giorno "+(i+1)+": "+intervistati[i].toString()+"\n";
    	}
    	ss+="NUMERO PAUSE: "+model.getPause();
    	txtResult.appendText(ss);

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxGenere.getItems().addAll(model.getGeneri());
    }
}
