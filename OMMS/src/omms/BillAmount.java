/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.util.ArrayList;

/**
 *
 * @author Ajmir
 */
public class BillAmount {
    Double bf;
    Double lunch;
    Double dinner;
    ArrayList<DailyItem> item;
    
    BillAmount(Double bf, Double lunch, Double dinner){
        this.bf = bf;
        this.lunch = lunch;
        this.dinner = dinner;
        item = new ArrayList<>();
    }
    
    public void setbf(Double bf){
        this.bf = bf;
    }
    
}
