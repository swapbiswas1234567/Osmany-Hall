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
public class DailyItem {
    String name;
    Double bfamount;
    int bfgrp;
    Double lunchamount;
    int lunchgrp;
    Double dinneramount;
    int dinnergrp;
    Double avgprice;
    
    DailyItem(String name, Double bfamount, Double lunchamount, Double dinneramount, Double avgprice, int bfgrp, int lunchgrp, int dinnergrp){
        this.name = name;
        this.bfamount = bfamount;
        this.lunchamount = lunchamount;
        this.dinneramount = dinneramount;
        this.avgprice = avgprice;
        this.bfgrp = bfgrp;
        this.lunchgrp = lunchgrp;
        this.dinnergrp = dinnergrp;
    }
    
}
