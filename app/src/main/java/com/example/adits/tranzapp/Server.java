package com.example.adits.tranzapp;

/**
 * Created by adits on 09-04-2016.
 */
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import java.net.*;
import java.io.*;
public class Server
{
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static final byte[] iv = {11, 22, 33, 44, 99, 88, 77, 66};

    public static void main(String[] args) throws IOException
    {
        int filesize = 1022386;
        int currentTot = 0;
        int bytesRead;
        String path = args[0];
        String clearTextFile = args[1];
        String cipherTextFile = path+"/cipher.txt";

        ServerSocket serverSocket = new ServerSocket(15123);
        Socket socket = serverSocket.accept();
        System.out.println("Accepted connection : " + socket);
        /************************* Receive key file ****************************/
        byte[] bytearray1 = new byte[filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(path+"/rcvdKeyFile.txt");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray1, 0, bytearray1.length);
        currentTot = bytesRead;
        do {
            bytesRead = is.read(bytearray1, currentTot, (bytearray1.length - currentTot));
            if (bytesRead >= 0) currentTot += bytesRead;
        } while (bytesRead > -1);
        bos.write(bytearray1, 0, currentTot);
        bos.flush();
        bos.close();
        socket.close();
        /********************* Read received key ***********************/
        try
        {
            InputStream isk = new FileInputStream(path+"/rcvdKeyFile.txt");
            byte[] bkey = new byte[8];
            int numRead = 0;
            while ((numRead = isk.read(bkey)) >= 0)
            {
            }
            /*********************** Encrypt msg using received key from client *************************/
            SecretKey key = new SecretKeySpec(bkey, 0, bkey.length, "DES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            //get Cipher instance and initiate in encrypt mode
            encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            //get Cipher instance and initiate in decrypt mode
            decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            //method to encrypt clear text file to encrypted file
            encrypt(new FileInputStream(clearTextFile), new FileOutputStream(cipherTextFile));

            //method to decrypt encrypted file to clear text file

            System.out.println("DONE");
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                InvalidAlgorithmParameterException |
                IOException e) {
            e.printStackTrace();
        }

        /*************************** Send encrypted file to client ***************************/
        socket = serverSocket.accept();
        try
        {
            File transferFile = new File(path+"/cipher.txt");	/*Change this to cipher.txt*/
            byte[] bytearray = new byte[(int) transferFile.length()];
            FileInputStream fin = new FileInputStream(transferFile);
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray, 0, bytearray.length);
            OutputStream os = socket.getOutputStream();
            System.out.println("Sending Files...");
            os.write(bytearray, 0, bytearray.length);
            os.flush();
            socket.close();
            System.out.println("File transfer complete");
        }catch (Exception ex) { System.out.println(ex);}

    } //end of main

    private static void encrypt(InputStream is, OutputStream os) throws IOException {

        //create CipherOutputStream to encrypt the data using encryptCipher
        os = new CipherOutputStream(os, encryptCipher);
        writeData(is, os);
    }

    //utility method to read data from input stream and write to output stream
    private static void writeData(InputStream is, OutputStream os) throws IOException{
        byte[] buf = new byte[1024];
        int numRead = 0;
        //read and write operation
        while ((numRead = is.read(buf)) >= 0) {
            os.write(buf, 0, numRead);
        }
        os.close();
        is.close();
    }
}
