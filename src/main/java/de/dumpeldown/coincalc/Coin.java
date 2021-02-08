package de.dumpeldown.coincalc;

import org.json.JSONObject;

public class Coin {
    public static double EXCHANGE_RATE;
    String sym;
    double amountBoughtEuro;
    double boughtAt;
    double amountBoughtCoin;
    double fee;

    public Coin(String sym, double amountBought, double boughtAt) {
        if (EXCHANGE_RATE == 0.0) EXCHANGE_RATE = getExchangeRate();
        this.sym = sym;
        this.amountBoughtEuro = amountBought;
        this.boughtAt = boughtAt;
        if (CoinCalc.SUB_COINBASE_FEES) {
            this.fee = amountBought - subFee(amountBought);
            this.amountBoughtCoin = subFee(amountBought) / boughtAt;
        } else {
            this.fee = 0;
            this.amountBoughtCoin = amountBought/boughtAt;
        }
    }

    private double subFee(double amountBought) {
        double amountUSD;
        amountUSD = EXCHANGE_RATE * amountBought;
        if (amountUSD <= 10) return (amountBought - 0.99);
        if (amountUSD <= 25) return (amountBought - 1.49);
        if (amountUSD <= 50) return (amountBought - 1.99);
        if (amountUSD <= 200) return (amountBought - 2.99);
        return -1;
    }

    public String stringify() {
        return "Name: " + sym + ", amountBought(€): " + amountBoughtEuro + ", boughtAt(€ per " +
                "Coin):" + boughtAt;
    }

    private double getExchangeRate() {
        ResponseHelper responseHelper = new ResponseHelper();
        String URL = "https://api.exchangeratesapi.io/latest";
        JSONObject response = responseHelper.getResponse(URL);
        double rate = response.getJSONObject("rates").getDouble("USD");
        System.out.println("--------------EXCHANGE RATE-------------------");
        System.out.println("-------|1Euro is worth " + rate + "USD|------------");
        return rate;
    }
}
