package android.sec.mobile.tjba.jus.br.sec_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private static final String ALGORITHM = "RSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsyncTask<String, Void, String> execute = new LoginTask().execute();
        try {
            final String token = execute.get();
            System.out.println(token);

            if(token != null){
                //Decode do token RSA enviado pelo servidor como login
                final InputStream in = getBaseContext().getResources().openRawResource(R.raw.public_key_dev);

                final byte[] keyBytes = IOUtils.toByteArray(in);

                final KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
                final X509EncodedKeySpec ks = new X509EncodedKeySpec(keyBytes);
                final PublicKey publicKey = kf.generatePublic(ks);

                final Cipher cipherRsa = Cipher.getInstance(ALGORITHM);

                cipherRsa.init(Cipher.DECRYPT_MODE, publicKey);

                final byte[] data = cipherRsa.doFinal(Base64.decode(token));

                System.out.println(getJson(new String(data)));

                final Session session = new Gson().fromJson(getJson(new String(data)), Session.class);

                System.out.println(session);


                //Encode do token 3Des que servirá para autenticacao
                final SecretKey secretKey = new SecretKeySpec(Base64.decode(session.getTripleDesKey()), "DESede");
                final Cipher cipher3Des = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
                final IvParameterSpec iv = new IvParameterSpec(new byte[8]);

                cipher3Des.init(Cipher.ENCRYPT_MODE, secretKey, iv);

                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final TriploDesToken triploDesToken = new TriploDesToken(simpleDateFormat.format(new Date()), "0.0.0.0", "Android"); //Obter o ip do Android

                final String jsonToken = new Gson().toJson(triploDesToken);

                final byte [] authToken = cipher3Des.doFinal(jsonToken.getBytes("utf-8"));

                //Esse token será enviado para o serviço (TJBAMobile) junto com o sessionId para ser validado
                System.out.println("Token de autenticação 3Des: " + new String(Base64.encode(authToken)));

                //Token no Authorization do cabeçalho do request
                //sessionId no SessionID do cabeçalho do request

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getJson(final String data){
        if(!data.startsWith("{")) {
            final int index = data.indexOf("{");
            final int size = data.length();
            return data.substring(index, size);
        }

        return data;
    }

    private String bytesToString(byte[] data) {
        String dataOut = "";
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0x00 && data[i] != -1)
                dataOut += (char)data[i];
        }
        return dataOut;
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {

                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                final URL url = new URL("https://acrab.tjba.jus.br/tjbamobile2/service/processo/token");

                final HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                con.setRequestProperty("Content-Type", "text/plain");


                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String arg) {
        }


    }
}
