/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

/**
 *
 * @author Ajmir
 */
public class StoredItem {
    int dateserial;
    String itemname;
    Double inamount;
    Double price;
    String memono;
    Double bf;
    Double lunch;
    Double dinner;
    int flag;
    
    
    StoredItem(int dateserial , String itemname, Double inamount, Double price){
        this.dateserial = dateserial;
        this.itemname= itemname;
        this.inamount = inamount;
        this.price = price;
        memono="###";
        bf= -1.0;
        lunch =-1.0;
        dinner =-1.0;
    }
    
    StoredItem(int dateserial,Double bf, Double lunch, Double dinner){
        this.dateserial = dateserial;
        itemname= null;
        inamount = -1.0;
        price= -1.0;
        memono= null;
        this.bf =bf;
        this.lunch = lunch;
        this.dinner = dinner;
    }
    
    StoredItem(int dateserial , String itemname, Double inamount, Double price,Double bf, Double lunch, Double dinner,
            int flag){
        this.dateserial = dateserial;
        this.itemname= itemname;
        this.inamount = inamount;
        this.price= price;
        memono= "###";
        this.bf =bf;
        this.lunch = lunch;
        this.dinner = dinner;
        this.flag = flag;
    }
    
    
    
}
