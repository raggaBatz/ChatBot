package chatbot;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
 
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
 
 
public class Main extends JFrame {
 
    private static JTextField txtEnter = new JTextField();
    private static JTextArea txtChat = new JTextArea();
    static BinTree root = null;
    static BinTree current = null;
    static boolean bandera = false;
    
    static class Matrix{  
        int parent;  
        int id;
        String type;
        String text;
        Matrix(){}
        public Matrix(int parent, int id, String type, String text) {
            this.parent = parent;
            this.id = id;
            this.type = type;
            this.text = text;
        }
    } 
   
    public Main() throws IOException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("Chat aseguradora");
       
        txtEnter.setLocation(5, 550);
        txtEnter.setSize(985, 20);
        Border border = BorderFactory.createLineBorder(Color.lightGray);
       
        txtChat.setLocation(5, 5);
        txtChat.setSize(984, 540);
        txtChat.setEditable(false);
        txtChat.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
       
        this.add(txtEnter);
        this.add(txtChat);
          
        System.out.println("");
        System.out.println("-------------------------------------------------");
        System.out.println("---------------ARBOL-DE-DECISIONES---------------");
        System.out.println("-------------------------------------------------");
               
        txtEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                String answer = txtEnter.getText();
                answer = answer.toLowerCase();
                if(bandera){
                    txtChat.append(answer + "\n");
                    txtEnter.setText("");
                    bandera = false;
                    try {
                        log();
                        txtChat.setText("");
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (answer.equals("si")){
                        System.exit(0);
                    } else {
                        if(answer.equals("no")){ 
                            try {
                                queryTree();
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else {
                            txtChat.append("ERROR: Debe responder \"Si\" o \"No\"");
                        }
                    }
                }else{
                    try {
                        txtChat.append("\n" + answer + "\n");
                        txtEnter.setText("");
                        if (answer.equals("si")){
                            queryBinTree(current.yes);
                        } else {
                            if (answer.equals("no")){ 
                                queryBinTree(current.no);
                            } else {
                                txtChat.append("ERROR: Debe responder \"Si\" o \"No\"");
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
       loadTree();
       outputBinTree();
       queryTree();
    }
    
    public void log() throws IOException{
        File f = new File("src/chatbot/log.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f, true)); // true for append
        txtChat.write(writer);
        writer.close();
    }
      
    public static void main(String[] args) throws IOException{
        new Main();
    }
    
    static void queryTree() throws IOException {
        txtChat.append("**************************************************************************************************\n");
        txtChat.append("                            BIENVENIDO AL SISTEMA DE LA ASEGURADORA GENERAL\n");
        txtChat.append("**************************************************************************************************\n");
        queryBinTree();
    }  
    
    public static void queryBinTree() throws IOException{
        queryBinTree(root);
    }
    
    private static void queryBinTree(BinTree currentNode) throws IOException{
        if (currentNode.yes==null) {
            if (currentNode.no==null){
                txtChat.append(currentNode.text);
                bandera = true;
                txtChat.append("\nSalir? (responda \"Si\" or \"No\")\n");               
            }else{
                txtChat.append("Error: Falta \"Yes\" en \"" + currentNode.text + "\"");
            }
            return;
        }
        if (currentNode.no==null) {
            txtChat.append("Error: Falta \"No\" en \"" + currentNode.text + "\"");
            return;
        }
        current = currentNode;
        txtChat.append("Agente: "+current.text + " (responda \"Si\" o \"No\")");
    }
    
    private static void loadTree(){
        System.out.println("-------------------------------------------------");
        System.out.println("------------------CARGA-ARCHIVO------------------");
        System.out.println("-------------------------------------------------");
        try {
            File f = new File("src/chatbot/Load.txt");
            BufferedReader b = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = b.readLine()) != null) {
                String[] s = line.split("-");
                Matrix node = new Matrix();
                node.parent = Integer.valueOf(s[0]);
                node.id     = Integer.valueOf(s[1]);
                node.type   = s[2];
                node.text   = s[3];
                if(node.parent == 0){
                    createRoot(node.id,node.text);
                }if(node.type.equals("Y")){
                    addYesNode(node.parent, node.id, node.text);
                }else{
                    addNoNode(node.parent, node.id, node.text);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void outputBinTree() {
        System.out.println("-------------------------------------------------");
        System.out.println("------------------ARBOL-GENERADO-----------------");
        System.out.println("-------------------------------------------------");
        outputBinTree("1",root);
    }
    private void outputBinTree(String tag, BinTree currentNode) {
        if (currentNode == null){ 
            return;
        }
        System.out.println("[" + tag + "] nodo = " + currentNode.id + ", texto = " + currentNode.text);
        outputBinTree(tag + ".1",currentNode.yes);
        outputBinTree(tag + ".2",currentNode.no);
    }
    private static void createRoot(int newID, String text) {
	root = new BinTree(newID,text);	
	System.out.println("Crea nodo raiz " + newID);	
    }
    public static void addYesNode(int parentID, int newID, String text) {
	if (root == null) {
	    System.out.println("ERROR: No encuentra raiz");
	    return;
        }
	if (searchTreeAndAddYesNode(root,parentID,newID,text)) {
	    System.out.println("Añade nodo " + newID + " en \"yes\" de nodo padre " + parentID);
        }
	else System.out.println("Nodo " + parentID + " no encontrado");
    }
    private static boolean searchTreeAndAddYesNode(BinTree currentNode, int parentID, int newID, String newText) {
    	if (currentNode.id == parentID) {
	    if (currentNode.yes == null){ 
                currentNode.yes = new BinTree(newID, newText);
            } else {
	        System.out.println("PRECAUCION: Sobreescribe nodo previo " + "(id = " + currentNode.yes.id + ") yes de nodo " + parentID);
		currentNode.yes = new BinTree(newID, newText);
            }		
    	    return(true);
        }else {
	    if (currentNode.yes != null) { 	
	        if (searchTreeAndAddYesNode(currentNode.yes, parentID, newID, newText)) {    	
	            return(true);
                } else {
    	            if (currentNode.no != null) {
    	    		return(searchTreeAndAddYesNode(currentNode.no, parentID, newID, newText));
                    }else{ 
                        return(false);
                    }
                }
            }
	    return(false);
        }
    } 	
    public static void addNoNode(int parentID, int newID, String newText) {
	if (root == null) {
	    System.out.println("ERROR: No encuentra raiz");
	    return;
        }
	if (searchTreeAndAddNoNode(root, parentID, newID, newText)) {
	    System.out.println("Añade nodo " + newID + " en \"no\" de nodo padre " + parentID);
        }else{ 
            System.out.println("Nodo " + parentID + " no encontrado");
        }
    }
    private static boolean searchTreeAndAddNoNode(BinTree currentNode, int parentID, int newID, String newText) {
    	if (currentNode.id == parentID) {
	    if (currentNode.no == null){ 
                currentNode.no = new BinTree(newID, newText);
            } else {
	        System.out.println("PRECAUCION: Sobreescribe nodo previo " +"(id = " + currentNode.no.id +") no de nodo " + parentID);
		currentNode.no = new BinTree(newID, newText);
            }		
    	    return(true);
        } else {
	    if (currentNode.yes != null) { 	
	        if (searchTreeAndAddNoNode(currentNode.yes, parentID, newID, newText)) {    	
	            return(true);
                } else {
    	            if (currentNode.no != null) {
    	    		return(searchTreeAndAddNoNode(currentNode.no, parentID, newID, newText));
                    }else{
                        return(false);
                    }	
                }
            }else{ 
                return(false);
            }
        }
    }
}