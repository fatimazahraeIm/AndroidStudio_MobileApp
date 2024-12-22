package com.android.travaildevoir3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText editTextNom, editTextPrenom, editTextClasse, editTextRemarques;
    private ListView listViewNotes;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNom = findViewById(R.id.editTextText);
        editTextPrenom = findViewById(R.id.editTextText2);
        editTextClasse = findViewById(R.id.editTextText3);
        editTextRemarques = findViewById(R.id.editTextText4);
        listViewNotes = findViewById(R.id.listView2); // Assurez-vous que cet ID existe dans votre layout

        Button downloadButton = findViewById(R.id.button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()) {
                    new DownloadTask().execute("https://belatar.name/rest/profile.php?nom=amari&prenom=youssef&classe=DSE&remarques=Developpement%20Mobile&notes[0]=14&notes[1]=16");
                } else {
                    Toast.makeText(MainActivity.this, "Aucune connexion Internet disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize the adapter with an empty list
        noteAdapter = new NoteAdapter(this, new ArrayList<Note>());
        listViewNotes.setAdapter(noteAdapter);
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(this, "Pas de connexion Internet", Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = "";

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                if (builder.length() == 0) {
                    return null;
                }
                jsonResponse = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(MainActivity.this, "Échec de la récupération des données", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(result);

                if (!jsonResponse.has("debug")) {
                    Toast.makeText(MainActivity.this, "Le JSON reçu ne contient pas de champ 'debug'", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject debugInfo = jsonResponse.getJSONObject("debug");

                String nom = debugInfo.getString("nom");
                String prenom = debugInfo.getString("prenom");
                String classe = debugInfo.getString("classe");
                String remarques = debugInfo.getString("remarques");

                EditText editTextNom = findViewById(R.id.editTextText);
                EditText editTextPrenom = findViewById(R.id.editTextText3);
                EditText editTextClasse = findViewById(R.id.editTextText2);
                EditText editTextRemarques = findViewById(R.id.editTextText4);

                editTextNom.setText(nom);
                editTextPrenom.setText(prenom);
                editTextClasse.setText(classe);
                editTextRemarques.setText(remarques);

                // Afficher ou utiliser ces informations comme requis

                // Extraire les notes du champ "debug"
                JSONArray notesArray = debugInfo.getJSONArray("notes");
                ArrayList<Note> noteList = new ArrayList<>();
                for (int i = 0; i < notesArray.length(); i++) {
                    int score = Integer.parseInt(notesArray.getString(i));
                    String imageResourceName = score > 12 ? "ic_like" : "ic_dislike";
                    int imageResource = getResources().getIdentifier(imageResourceName, "drawable", getPackageName());
                    noteList.add(new Note("Matière " + (i + 1), score, score >= 12));
                }

                // Afficher ou utiliser ces notes comme requis

                noteAdapter.clear();
                noteAdapter.addAll(noteList);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Erreur de parsing JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
