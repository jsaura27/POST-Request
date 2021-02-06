package com.company;




import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;

public class Main {
    public static void exec() throws IOException, InterruptedException, ScriptException {

        //Http get request to get the values key and question
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://f47iqbodgf.execute-api.eu-west-2.amazonaws.com/default/OAJobApplicationForm"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //From the answer i get the JSONObject which has the values we need
        JSONObject jsonResponse = new JSONObject(response.body());

        //I get the individual values
        String value = jsonResponse.getString("question");
        String key = jsonResponse.getString("key");

        //Here i calculate the value of the question thanks to Javascript
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        int value2 = (int) engine.eval(value);

        //I add the value of the operation in a JSONObject
        JSONObject answer = new JSONObject();
        answer.put("ans", value2);
        answer.put("key", key);

        //I create the POST request to send the values
        HttpClient clientFinal = HttpClient.newHttpClient();
        HttpRequest requestFinal = HttpRequest.newBuilder()
                .uri(URI.create("https://f47iqbodgf.execute-api.eu-west-2.amazonaws.com/default/OAJobApplicationForm"))
                .POST(HttpRequest.BodyPublishers.ofString(answer.toString()))
                .headers("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> responseFinal = client.send(requestFinal,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(responseFinal.body());
    }


    public static void main(String[] args) throws IOException, InterruptedException, ScriptException {
        exec();
    }
}
