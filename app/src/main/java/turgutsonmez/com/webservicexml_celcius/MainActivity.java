package turgutsonmez.com.webservicexml_celcius;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  //NAMESPACE VE METHODNAME birleşiyor, SOAPACTİON'ı oluşturuyor

  final String NAMESPACE = "https://www.w3schools.com/xml/";
  final String URL = "https://www.w3schools.com/xml/tempconvert.asmx";
  final String SOAPACTİON = "https://www.w3schools.com/xml/CelsiusToFahrenheit";
  final String METHOD_NAME = "CelsiusToFahrenheit";

  Button btnConvert;
  TextView txtSonuc;
  EditText etGiris;
  String celsius;
  String fahrenheit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btnConvert = (Button) findViewById(R.id.btnConvert);
    txtSonuc = (TextView) findViewById(R.id.txtSonuc);
    etGiris = (EditText) findViewById(R.id.etGiris);

    btnConvert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (etGiris.getText().length() != 0 && etGiris.getText().toString() != null) {
          celsius = etGiris.getText().toString();
          AsyncCallWebService task = new AsyncCallWebService();
          task.execute();
        } else {
          txtSonuc.setText("Lütfen Celsius değerini giriniz");
        }
      }
    });
  }

  private class AsyncCallWebService extends AsyncTask<String, Void, Void> {


    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      txtSonuc.setText("Hesaplanıyor");
    }

    @Override
    protected Void doInBackground(String... params) {
      getFahrenheit(celsius);
      return null;
    }

    @Override
    protected void onPostExecute(Void o) {
      super.onPostExecute(o);
      txtSonuc.setText(fahrenheit + " F");
    }


    @Override
    protected void onProgressUpdate(Void... values) {
      super.onProgressUpdate(values);
    }
  }

  public void getFahrenheit(String celsius) {
    //inputları oluşturmak için bir talep oluşturma
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

    //request'e bu özellikleri ekledim
    PropertyInfo celsiusPi = new PropertyInfo();
    celsiusPi.setName("Celsius");
    celsiusPi.setValue(celsius);
    celsiusPi.setType(double.class);
    request.addProperty(celsiusPi);

    //request'i yukarda hazırlayıp. Alttada mektubun içine koyduk
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);

    //Web servise gönderme
    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    try {
      //isteği yolladık
      androidHttpTransport.call(SOAPACTİON, envelope);
      //cevabı al
      SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
      fahrenheit = response.toString();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
  }
}
