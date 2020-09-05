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
    ArrayList<Double> bfbill;
    ArrayList<Double> lunchbill;
    ArrayList<Double> dinnerbill;
    ArrayList<DailyItem> item;
    
    BillAmount(){
        bfbill = new ArrayList<>();
        lunchbill = new ArrayList<>();
        dinnerbill = new ArrayList<>();
        item = new ArrayList<>();
    }
    
    
    
}
