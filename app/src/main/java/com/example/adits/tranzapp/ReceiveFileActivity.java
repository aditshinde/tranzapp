package com.example.adits.tranzapp;

import android.app.ProgressDialog;
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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ReceiveFileActivity extends AppCompatActivity {
    EditText ipText;
    TextView clientIp;
    Button receiveFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_file);

        ipText = (EditText) findViewById(R.id.ip);

        clientIp = (TextView) findViewById(R.id.clientIPTextView);
        clientIp.setText(getIpAddress());

        receiveFile = (Button) findViewById(R.id.recieveFile);
        receiveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download";
                String ip = (String) ipText.getText().toString();
                String[] args = new String[10];

                args[0] = downloadDir;
                args[1] = ip;

                if (!(args[0].equals("") && args[1].equals(""))) {
                    Log.i("File", "Receiving...");
                    Snackbar.make(v, "Receiving file...", Snackbar.LENGTH_SHORT).show();
                    new ReceiveAsyncTask().execute(args);
                    Log.i("File", "Received.");
                    Snackbar.make(v, "File Received", Snackbar.LENGTH_SHORT).show();
                } else if (args[1].equals("")) {
                    Snackbar.make(v, "IP Address Missing", Snackbar.LENGTH_SHORT).show();
                }


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


    class ReceiveAsyncTask extends AsyncTask<String, Object, Object> {

        int filesize = 6022386;
        int bytesRead;
        int current = 0;
        File file;
        ProgressDialog progressDialog;


        @Override
        protected Object doInBackground(String... args) {
            try {
            /*
                ServerSocket serverSocket = new ServerSocket(9999);
                Socket sock = serverSocket.accept();
                Log.i("Listening","on port 9999");
                byte [] mybytearray  = new byte [filesize];
                InputStream is = sock.getInputStream();
                file = new File(Environment.getExternalStorageDirectory(),"/Download/.cipher");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int bytesRead;

                while((bytesRead=is.read(mybytearray))>0)
                {
                    bos.write(mybytearray, 0, bytesRead);
                    Log.i("File","Writing...");
                }
                Log.i("File","Written");

                bos.close();
                fos.close();
                Log.i("File", "Saved");
                sock.close();
            serverSocket.close();
            Log.i("Server","Stopped");
            */
                Client.main(args);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReceiveFileActivity.this);
            progressDialog.setMessage("Receiving File");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();

        }
    }

}