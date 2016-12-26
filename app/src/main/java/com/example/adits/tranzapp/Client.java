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
    public class Client {
        private static Cipher encryptCipher;
        private static Cipher decryptCipher;
        private static final byte[] iv = {11, 22, 33, 44, 99, 88, 77, 66};

        public static void main(String[] args) throws IOException {
            int filesize = 1022386;
            int bytesRead;
            int currentTot = 0;
            String downloadDir=args[0];
            String ip=args[1];
            String cipherTextFile = downloadDir+"/rcvdEncFile.txt";
            String clearTextNewFile = downloadDir+"/decFile.txt";

            Socket socket = new Socket(ip, 15123);
            /******************** Generate Key ***********************/
            try {
                SecretKey key = KeyGenerator.getInstance("DES").generateKey();
                OutputStream os = new FileOutputStream(downloadDir+"/key.txt");
                os.write(key.getEncoded()); // writes the bytes
                os.close();
            }catch (Exception ex) { System.out.println(ex);}
            /********************** Key file send ***********************/
            try
            {
                File transferFile = new File(downloadDir+"/key.txt");
                byte[] bytearray1 = new byte[(int) transferFile.length()];
                FileInputStream fin = new FileInputStream(transferFile);
                BufferedInputStream bin = new BufferedInputStream(fin);
                bin.read(bytearray1, 0, bytearray1.length);
                OutputStream os = socket.getOutputStream();
                System.out.println("Sending Key file...");
                os.write(bytearray1, 0, bytearray1.length);
                os.flush();
                os.close();
                System.out.println("Key file send");
                socket.close();
            }catch (Exception ex) { System.out.println(ex);}
            /********************** receive encrypted file *************************/
            socket = new Socket(ip, 15123);
            try
            {
                byte[] bytearray = new byte[filesize];
                InputStream is = socket.getInputStream();
                //System.out.println("after socket.getInputStream ");
                FileOutputStream fos = new FileOutputStream(downloadDir+"/rcvdEncFile.txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(bytearray, 0, bytearray.length);
                currentTot = bytesRead;
                do {
                    bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
                    if (bytesRead >= 0) currentTot += bytesRead;
                } while (bytesRead > -1);
                bos.write(bytearray, 0, currentTot);
                bos.flush();
                bos.close();
                is.close();
                socket.close();
            }catch (Exception ex) { System.out.println(ex);}
            /*************************** Decrypt file **********************************/
            try {
                //create SecretKey using KeyGenerator
                //SecretKey key = KeyGenerator.getInstance("DES").generateKey();
                InputStream isk = new FileInputStream(downloadDir+"/key.txt");
                byte[] bKey = new byte[8];
                int numRead = 0;
                //read and write operation
                while ((numRead = isk.read(bKey)) >= 0)
                {
                }
                /******************************
                 byte[] encodedKey = Base64.decode(keyStr, Base64.DEFAULT);
                 // rebuild key using SecretKeySpec*/
                SecretKey key = new SecretKeySpec(bKey, 0, bKey.length, "DES");
                /****************************/
                AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

                //get Cipher instance and initiate in encrypt mode
                encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

                //get Cipher instance and initiate in decrypt mode
                decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

                //method to encrypt clear text file to encrypted file
                //encrypt(new FileInputStream(clearTextFile), new FileOutputStream(cipherTextFile));

                //method to decrypt encrypted file to clear text file
                decrypt(new FileInputStream(cipherTextFile), new FileOutputStream(clearTextNewFile));

                System.out.println("DONE");
            } catch (NoSuchAlgorithmException |
                    NoSuchPaddingException |
                    InvalidKeyException |
                    InvalidAlgorithmParameterException |
                    IOException e) {
                e.printStackTrace();
            }
            /*********************************************************************/
        } //end of main

        private static void decrypt(InputStream is, OutputStream os) throws IOException {

            //create CipherOutputStream to decrypt the data using decryptCipher
            is = new CipherInputStream(is, decryptCipher);
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

