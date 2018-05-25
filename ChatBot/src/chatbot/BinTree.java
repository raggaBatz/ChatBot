/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

/**
 *
 * @author RAGGA-PC
 */
public class BinTree {
    
    public int     id;
    public String  text = null;
    public BinTree yes  = null;
    public BinTree no   = null;

    public BinTree(int newID, String newText) {
        id   = newID;
        text = newText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BinTree getYes() {
        return yes;
    }

    public void setYes(BinTree yes) {
        this.yes = yes;
    }

    public BinTree getNo() {
        return no;
    }

    public void setNo(BinTree no) {
        this.no = no;
    }
    
    
    
}
