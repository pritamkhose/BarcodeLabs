package com.pritam.barcodelabs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
// https://stackoverflow.com/questions/10353392/generate-barcode-image-in-android-application

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Spinner barcode_type;
    private EditText barcode_text;
    private ImageView barcode_img;
    private Button barcode_submit;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Spinner element
        barcode_type = (Spinner) findViewById(R.id.barcode_type);
        barcode_text = (EditText) findViewById(R.id.barcode_text);
        barcode_img = (ImageView) findViewById(R.id.barcode_img);
        barcode_submit = (Button) findViewById(R.id.submit);
        barcode_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
               String s = barcode_text.getText().toString();
               if(s != null && s.length() > 0){
                  createBarcode(s);
               } else {
                   Toast.makeText(getApplicationContext(), "Enter barcode data", Toast.LENGTH_SHORT).show();
               }

            }
        });

        // Spinner Drop down elements
        List<String> categories = getBarcodeList();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        barcode_type.setAdapter(dataAdapter);



    }

    private void createBarcode(String barcode_data) {
        try {
            Bitmap bitmap = encodeAsBitmap(barcode_data, getBarcodeType() , 600, 300);
            if(bitmap != null) {
                barcode_img.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "Unable to generate barcode", Toast.LENGTH_SHORT).show();
            }
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error in generate barcode\n"+e.toString(), Toast.LENGTH_LONG).show();
        }
    }


        /**************************************************************
         * getting from com.google.zxing.client.android.encode.QRCodeEncoder
         *
         * See the sites below
         * http://code.google.com/p/zxing/
         * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
         * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
         */

        private static final int WHITE = 0xFFFFFFFF;
        private static final int BLACK = 0xFF000000;

        Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
            String contentsToEncode = contents;
            if (contentsToEncode == null) {
                return null;
            }
            Map<EncodeHintType, Object> hints = null;
            String encoding = guessAppropriateEncoding(contentsToEncode);
            if (encoding != null) {
                hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                hints.put(EncodeHintType.CHARACTER_SET, encoding);
            }
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix result;
            try {
                result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }

        private static String guessAppropriateEncoding(CharSequence contents) {
            // Very crude at the moment
            for (int i = 0; i < contents.length(); i++) {
                if (contents.charAt(i) > 0xFF) {
                    return "UTF-8";
                }
            }
            return null;
        }

    public List<String> getBarcodeList() {
        List<String> categories =  new ArrayList<String>();
        /** Aztec 2D barcode format. */
        categories.add("AZTEC");

        /** CODABAR 1D format. */
        categories.add("CODABAR");

        /** Code 39 1D format. */
        categories.add("CODE_39");

        /** Code 93 1D format. */
        categories.add("CODE_93");

        /** Code 128 1D format. */
        categories.add("CODE_128");

        /** Data Matrix 2D barcode format. */
        categories.add("DATA_MATRIX");

        /** EAN-8 1D format. */
        categories.add("EAN_8");

        /** EAN-13 1D format. */
        categories.add("EAN_13");

        /** ITF (Interleaved Two of Five) 1D format. */
        categories.add("ITF");

        /** MaxiCode 2D barcode format. */
        categories.add("MAXICODE");

        /** PDF417 format. */
        categories.add("PDF_417");

        /** QR Code 2D barcode format. */
        categories.add("QR_CODE");

        /** RSS 14 */
        categories.add("RSS_14");

        /** RSS EXPANDED */
        categories.add("RSS_EXPANDED");

        /** UPC-A 1D format. */
        categories.add("UPC_A");

        /** UPC-E 1D format. */
        categories.add("UPC_E");

        /** UPC/EAN extension format. Not a stand-alone format. */
        categories.add("UPC_EAN_EXTENSION");

        return categories;
    }

    public BarcodeFormat getBarcodeType() {
        BarcodeFormat barcodeType = null;
        switch(barcode_type.getSelectedItem().toString()){
            case "AZTEC" : barcodeType = BarcodeFormat.AZTEC; break;
            case "CODABAR" : barcodeType = BarcodeFormat.CODABAR; break;
            case "CODE_39" : barcodeType = BarcodeFormat.CODE_39; break;
            case "CODE_93" : barcodeType = BarcodeFormat.CODE_93; break;
            case "CODE_128" : barcodeType = BarcodeFormat.CODE_128; break;
            case "DATA_MATRIX" : barcodeType = BarcodeFormat.DATA_MATRIX; break;
            case "EAN_8" : barcodeType = BarcodeFormat.EAN_8; break;
            case "EAN_13" : barcodeType = BarcodeFormat.EAN_13; break;
            case "ITF" : barcodeType = BarcodeFormat.ITF; break;
            case "MAXICODE" : barcodeType = BarcodeFormat.MAXICODE; break;
            case "PDF_417" : barcodeType = BarcodeFormat.PDF_417; break;
            case "QR_CODE" : barcodeType = BarcodeFormat.QR_CODE; break;
            case "RSS_14" : barcodeType = BarcodeFormat.RSS_14; break;
            case "RSS_EXPANDED" : barcodeType = BarcodeFormat.RSS_EXPANDED; break;
            case "UPC_A" : barcodeType = BarcodeFormat.UPC_A; break;
            case "UPC_E" : barcodeType = BarcodeFormat.UPC_E; break;
            case "UPC_EAN_EXTENSION" : barcodeType = BarcodeFormat.UPC_EAN_EXTENSION; break;
        }
        return barcodeType;
    }
}
