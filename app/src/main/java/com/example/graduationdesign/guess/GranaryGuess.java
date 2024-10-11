package com.example.graduationdesign.guess;

import android.content.Context;

public class GranaryGuess {
    private GranaryGuessStrategy strategy;

    public void setGranaryGuessStrategy(GranaryGuessStrategy strategy){
        this.strategy = strategy;
    }

    public boolean ifIsEmpty(){
        if(strategy == null){
            throw new IllegalStateException("Guess strategy not set.");
        }
        return strategy.ifIsEmpty();
    }

    public void getDouble(String weight,String water,String impurity){
        if(strategy == null){
            throw new IllegalStateException("Guess strategy not set.");
        }
        strategy.getDouble(weight,water,impurity);
    }

    public void judgeLevel(){
        if (strategy == null) {
            throw new IllegalStateException("Guess strategy not set.");
        }
        strategy.judgeLevel();
    }

    public void getTotal() {
        if (strategy == null) {
            throw new IllegalStateException("Guess strategy not set.");
        }
        strategy.getTotal();
    }

    public void setDialog(Context mContext, boolean Switch){
        if (strategy == null) {
            throw new IllegalStateException("Guess strategy not set.");
        }
        strategy.setDialog(mContext,Switch);
    }

}
