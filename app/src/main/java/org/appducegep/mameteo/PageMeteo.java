package org.appducegep.mameteo;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PageMeteo extends AppCompatActivity {

    private TextView libelleTitre;
/*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_meteo:
                    libelleTitre.setText(R.string.titre_accueil);
                    return true;
                case R.id.navigation_meteo_detail:
                    libelleTitre.setText(R.string.titre_meteo_detail);
                    return true;
                case R.id.navigation_notifications:
                    libelleTitre.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_meteo);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        libelleTitre = (TextView) findViewById(R.id.message);
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String xml = "";

        try {
            URL url = new URL("https://www.lemonde.fr/rss/une.xml");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                xml = stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        System.out.println(xml);

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = builderFactory.newDocumentBuilder();
            Document doc = null;
            doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            Element elementArticle = (Element)doc.getElementsByTagName("item").item(0);
            Element elementTitre = (Element)elementArticle.getElementsByTagName("title").item(0);
            String titre = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                titre = elementTitre.getTextContent();
            }
            Element elementContenu= (Element)elementArticle.getElementsByTagName("description").item(0);
            String contenu = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                contenu = elementContenu.getTextContent();
            }


            TextView afficherTitre = this.findViewById(R.id.message);
            afficherTitre.setText(titre);


            TextView affichageMeteo = (TextView)this.findViewById(R.id.meteo);
            affichageMeteo.setText(contenu);



            //MeteoDAO meteoDAO = new MeteoDAO(getApplicationContext());
            //meteoDAO.ajouterMeteo(soleilOuNuage, Integer.parseInt(humidite), vent);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
         catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

}
