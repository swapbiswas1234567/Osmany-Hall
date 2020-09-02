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
public class PreviousValue {
    Double avgprice;
    Double prevavailable;
    
    PreviousValue(Double price, Double available){
        this.avgprice = price;
        this.prevavailable = available;
    }
    
}
