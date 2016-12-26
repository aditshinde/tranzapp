package com.example.adits.tranzapp;

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
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class DESEncryption {
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static final byte[] iv = {11, 22, 33, 44, 99, 88, 77, 66, 12, 14, 67, 18, 10, 45, 56, 99};


    public static void init() {

        try {
            //create SecretKey using KeyGenerator
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            //get Cipher instance and initiate in encrypt mode
            encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            //get Cipher instance and initiate in decrypt mode
            decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            System.out.println("DONE");
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }


    public static void encrypt(InputStream is, OutputStream os) throws IOException {

        //create CipherOutputStream to encrypt the data using encryptCipher
        os = new CipherOutputStream(os, encryptCipher);
        writeData(is, os);

    }


    public static void decrypt(InputStream is, OutputStream os) throws IOException {

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