package de.dumpeldown.coincalc;

import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.stream.Collectors;

public class CoinCalc {
    private final String URL = "https://min-api.cryptocompare.com/data/price?fsym=";
    private final String dataFile = "data.csv";
    private final String outputFile = "output.csv";
    private static int DEFAULT_FREQ = 3 * 60 * 1000;

    public ArrayList<Coin> coins;
    private static Timer timer;

    public static void main(String[] args) {
        if (args.length != 0) {
            if (args[0].equals("-freq") && Integer.parseInt(args[1]) > 1) {
                DEFAULT_FREQ = Integer.parseInt(args[0]) * 1000;
            }
        }

        timer = new Timer();
        timer.schedule(new CoinTask(), 10, DEFAULT_FREQ);
    }

    public JSONObject price(String coinString, String currencyString) {
        ResponseHelper responseHelper = new ResponseHelper();
        String sb = this.URL +
                coinString +
                "&tsyms=" +
                currencyString;
        return responseHelper.getResponse(sb);
    }

    public void parseData() {
        FileReader fr;
        try {
            fr = new FileReader(this.dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> lines = null;
        try (br) {
            lines = br.lines().collect(Collectors
                    .toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : lines) {
            String[] values = s.split(";");
            coins.add(new Coin(
                    values[0],
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[2])
            ));
        }
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void writeToFile(double ret) {
        BufferedWriter bw;
        FileWriter fw;
        String value;
        try {
            int infront = (int) ret;
            int after = (int) (round(ret, 2) * 100) % 100;
            if (after < 10) {
                value = infront + ",0" + after;
            } else {
                value = infront + "," + after;
            }
            fw = new FileWriter(this.outputFile, true);
            bw = new BufferedWriter(fw);
            try (bw) {
                bw.write(LocalDateTime.now() + ";" + value);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
