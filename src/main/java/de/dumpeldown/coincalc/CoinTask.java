package de.dumpeldown.coincalc;

import org.json.JSONObject;

import java.util.TimerTask;

public class CoinTask extends TimerTask {
    @Override
    public void run() {
        CoinCalc api = new CoinCalc();
        api.parseData();
        double totalInvested = 0;
        double totalReturnEuro = 0;
        double totalFees = 0;
        for (Coin c : api.coins) {
            System.out.println("---------------------------");
            System.out.println(c.stringify());
            JSONObject priceJSON = api.price(c.sym, "EUR");
            double priceDouble = priceJSON.getDouble("EUR");
            System.out.println("One Coin of " + c.sym + " ist now worth: " + priceDouble);
            System.out.println("Your " + c.amountBoughtEuro + "€ are now worth " + api.round(c.amountBoughtCoin * priceDouble, 2) + "€");
            double returnEuro = (c.amountBoughtCoin * priceDouble) - c.amountBoughtEuro;
            totalInvested += c.amountBoughtEuro;
            totalReturnEuro += returnEuro;
            totalFees += c.fee;
            double percent = api.round((returnEuro) / c.amountBoughtEuro * 100, 2);
            System.out.println("For this Coin, this equals to an return of " + (api.round(returnEuro, 2)) + "€ (" + percent + "%)");
        }
        System.out.println("----------------");
        System.out.println("Total € invested: " + totalInvested + "€");
        if(CoinCalc.SUB_COINBASE_FEES) {
            System.out.println("----------------");
            System.out.println("Total Fees payed: " + api.round(totalFees, 2) + "€");
        }
        System.out.println("----------------");
        System.out.println("Total Return for all Coins " + api.round(totalReturnEuro, 2) + "€");

        api.writeToFile(totalReturnEuro);
    }
}
