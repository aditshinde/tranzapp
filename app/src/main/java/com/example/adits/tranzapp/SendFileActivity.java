package com.example.adits.tranzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class SendFileActivity extends AppCompatActivity {

    private int PICKFILE_REQUEST_CODE = 101;
    private ProgressDialog simpleWaitDialog;
    private TextView serverIp;
    private TextView file;
    String[] strings = new String[2];
    Button chooseFile;
    Button sendFile;
    private String fPath;
    private String dirPath;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        serverIp = (TextView) findViewById(R.id.ServerIPTextView);
        serverIp.setText(getIpAddress());


        chooseFile = (Button) findViewById(R.id.chooseFile);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });


        sendFile = (Button) findViewById(R.id.sendFile);
        sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                file = (TextView) findViewById(R.id.file);
                String fPath = (String) file.getText();

                strings[0] = dirPath;
                strings[1] = fPath;

                if (!(strings[0].equals("") && strings[1].equals(""))) {
                    Log.i("File", "Sending...");
                    Snackbar.make(v, "Sending file...", Snackbar.LENGTH_SHORT).show();
                    new SendAsyncTask().execute(strings);
                    Log.i("File", "Sent.");
                    Snackbar.make(v, "File Sent.", Snackbar.LENGTH_SHORT).show();
                } else if (strings[1].equals("")) {
                    Snackbar.make(v, "File Not Selected", Snackbar.LENGTH_SHORT).show();
                }

                /*
                String cipherFile = Environment.getExternalStorageDirectory()+"/temp";

                //method to encrypt clear text file to encrypted file
                try {
                    DESEncryption.init();
                    DESEncryption.encrypt(new FileInputStream(new File(fPath)), new FileOutputStream(new File(cipherFile)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                strings[0]=ipStr;
                strings[1]=cipherFile;
                if(!(strings[0].equals("")&&strings[1].equals("")))
                {
                    Log.i("File","Sending...");
                    Snackbar.make(v, "Sending file...", Snackbar.LENGTH_SHORT).show();
                    new SendAsyncTask().execute(strings);
                    Log.i("File", "Sent.");
                    Snackbar.make(v, "File Sent.", Snackbar.LENGTH_SHORT).show();
                }
                else if(strings[0].equals(""))
                {
                    Snackbar.make(v, "IP Address Missing", Snackbar.LENGTH_SHORT).show();
                }
                else if(strings[1].equals(""))
                {
                    Snackbar.make(v, "File Not Selected", Snackbar.LENGTH_SHORT).show();
                }
                */

            }
        });

    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("Result", "OK");
                fPath = data.getData().getPath();
                Log.i("File Path",fPath);
                dirPath = fPath.substring(0, fPath.lastIndexOf("/"));
                Log.i("Result", "File Found");
                file = (TextView) findViewById(R.id.file);
                file.setText(fPath);

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Result", "Cancelled");
                Toast.makeText(SendFileActivity.this, "File Not Selected", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(SendFileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }

    }


    class SendAsyncTask extends AsyncTask<String, Object, Object> {

        int filesize = 6022386;
        int bytesRead;
        int current = 0;
        File file;


        @Override
        protected Object doInBackground(String... strings) {
            try {
            /*
            Socket sock = new Socket(strings[0], 9999);

            Log.i("Connection","Established.");

            file = new File(strings[1]);
            byte[] mybytearray = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = sock.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            sock.close();
            */

                Server.main(strings);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SendFileActivity.this);
            progressDialog.setMessage("Sending File");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();

        }
    }
}
