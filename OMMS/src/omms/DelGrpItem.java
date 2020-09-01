/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

/**
 *
 * @author Asus
 */
public class DelGrpItem {

    String grpName;
    String state;
    int date;
    
    public DelGrpItem() {
        grpName = null;
        state = null;
        date = 0;
    }
    
    public DelGrpItem(String grpName, String state, int date) {
        this.grpName = grpName;
        this.state = state;
        this.date = date;
        System.out.println("Del Class " + grpName + " " + state + " " + date);
    }
    
    
}
