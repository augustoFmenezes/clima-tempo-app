package api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClimaTempoAPI {

    public static JSONObject getCondicaoClima(String localizacao) {

        JSONArray coordenadas = getDadosLocalizacao(localizacao);

        if (coordenadas == null || coordenadas.isEmpty()) {
            return null;
        }

        JSONObject coordenadasLocalizacao = (JSONObject) coordenadas.get(0);

        double latitude = (double) coordenadasLocalizacao.get("latitude");
        double longitude = (double) coordenadasLocalizacao.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?"
                + "latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m"
                + "&timezone=America%2FSao_Paulo";

        try {

            HttpURLConnection conexao = buscaRespostaApi(urlString);

            if (conexao.getResponseCode() != 200) {
                System.out.println("Erro. Não foi possível conectar-se à API");
                return null;
            }

            StringBuilder resultadosJson = new StringBuilder();
            Scanner sc = new Scanner(conexao.getInputStream());

            while (sc.hasNext()) {
                resultadosJson.append(sc.nextLine());
            }

            sc.close();
            conexao.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultadosJsonOBJ = (JSONObject) parser.parse(resultadosJson.toString());

            JSONObject current = (JSONObject) resultadosJsonOBJ.get("current");

            double temperatura = (double) current.get("temperature_2m");

            long codigoClima = (long) current.get("weather_code");
            String condicaoClima = converterCodigoClima(codigoClima);

            double velocidadeDoVento = (double) current.get("wind_speed_10m");

            long humidade = (long) current.get("relative_humidity_2m");

            JSONObject dadosDoClima = new JSONObject();

            dadosDoClima.put("temperatura", temperatura);
            dadosDoClima.put("condicaoClima", condicaoClima);
            dadosDoClima.put("humidade", humidade);
            dadosDoClima.put("velocidadeDoVento", velocidadeDoVento);

            return dadosDoClima;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getDadosLocalizacao(String localizacao) {

        localizacao = localizacao.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
                + localizacao
                + "&count=10&language=en&format=json";

        try {

            HttpURLConnection conexao = buscaRespostaApi(urlString);

            if (conexao.getResponseCode() != 200) {
                System.out.println("Erro. Não foi possível conectar-se à API");
                return null;
            }

            StringBuilder resultadosJson = new StringBuilder();
            Scanner sc = new Scanner(conexao.getInputStream());

            while (sc.hasNext()) {
                resultadosJson.append(sc.nextLine());
            }

            sc.close();
            conexao.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultadosJsonOBJ = (JSONObject) parser.parse(resultadosJson.toString());

            JSONArray coordenadas = (JSONArray) resultadosJsonOBJ.get("results");

            return coordenadas;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection buscaRespostaApi(String urlString) {

        try {

            URL url = new URL(urlString);

            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");

            conexao.connect();

            return conexao;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String converterCodigoClima(long codigoClima) {

        if (codigoClima == 0) {
            return "Limpo";
        }

        if (codigoClima <= 3) {
            return "Nublado";
        }

        if ((codigoClima >= 51 && codigoClima <= 67) || (codigoClima >= 80 && codigoClima <= 99)) {
            return "Chuva";
        }

        if (codigoClima >= 71 && codigoClima <= 77) {
            return "Neve";
        }

        return "Desconhecido";
    }
}