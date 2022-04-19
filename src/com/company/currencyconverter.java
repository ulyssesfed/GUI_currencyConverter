package com.company;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Currency;
import javax.swing.*;
import java.awt.event.*;

public class currencyconverter {
    private JPanel CurrencyConverter;
    private JTextField textField1;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JButton button1;

    public currencyconverter() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected1 = comboBox1.getSelectedItem().toString();
                String selected2 = comboBox2.getSelectedItem().toString();
                double amount = Double.parseDouble(textField1.getText());
                double result = 0;
                String GET_URL = "https://v6.exchangerate-api.com/v6/f723ef02c3296f4fb698bbc7/pair/" + selected1 + "/" + selected2;
                URL url = null;
                try {
                    url = new URL(GET_URL);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    conn.setRequestMethod("GET");
                } catch (ProtocolException ex) {
                    ex.printStackTrace();
                }
                int responseCode = 0;
                try {
                    responseCode = conn.getResponseCode();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                StringBuffer response = null;
                if (responseCode == 200) {
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    String inputLine = null;
                    response = new StringBuffer();

                    while (true) {
                        try {
                            if (!((inputLine = in.readLine()) != null)) break;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        response.append(inputLine);
                    }
                    try {
                        in.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                JSONObject jsonObject = new JSONObject(response.toString());
                double rate = jsonObject.getDouble("conversion_rate");
                System.out.println("The conversion rate is: " + rate);
                result = amount * rate;
                DecimalFormat df = new DecimalFormat("#.##");
                JOptionPane.showMessageDialog(null, "The result is: " + df.format(result));

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("currencyconverter");
        frame.setContentPane(new currencyconverter().CurrencyConverter);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
