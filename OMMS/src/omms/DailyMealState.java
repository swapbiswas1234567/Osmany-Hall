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
public class DailyMealState {
    int bf;
    int lunch;
    int dinner;
    int bfgrp;
    int lunchgrp;
    int dinnergrp;
    
    DailyMealState(int bf, int lunch, int dinner, int bfgrp, int lunchgrp, int dinnergrp){
        this.bf = bf;
        this.lunch = lunch;
        this.dinner = dinner;
        this.bfgrp = bfgrp;
        this.lunchgrp = lunchgrp;
        this.dinnergrp = dinnergrp;
    }    
}
