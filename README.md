# Tranzapp
An Android text file sharing application using Java Sockets and Shared Key Cryptography

Tranzapp is an application developed on the android platform for transferring files from one
device to another. One of the key features of this application is that the file can be 
transferred in such a way that no harm is caused to the file while being transferred. This is
achieved by using cryptography. The file that is being transmitted is first encrypted by the 
sender device and then it is transferred to the receiver. When the receiver receives the file, it
decrypts the file so as to obtain the original contents of the file. DES encryption is used to 
encrypt the file .To transmit a file, the sender has to first create a hotspot network to which 
any other device could connect.The receiver has to connect to this network. Both the 
devices are now connected in a network and are for file transmission. The sender would
now open Tranzapp and click on “Send” Button, after this, he has to select the file he wishes
to send. At the same time receiver has to click on “Receive” Button, after that he has to 
enter the IP address of the Sender device and click on “Submit” Button. As soon as the 
receiver presses the “Submit” Button, the receiver device generates a random key and 
transfers it to the sender device. The sender device uses this key to encrypt the file and 
sends the encrypted file to the receiver. The receiver device now decrypts the file using the
earlier generated key, and thus transmission of file has been successfully achieved.

Connect the phones:
1. Switch on hotspot in one phone and connect the second phone to the first
2. Open TRANZAPP and check if your IP address is being displayed, if not try again.

How to send file:
1. Click SEND button
2. Click CHOOSE FILE button
3. Select file using file manager app
4. Click SEND after the selected file is shown

How to receive file:
1. Click RECEIVE button
2. Enter the IP address of the sender(displayed above the chosen file)
3. Hit RECEIVE

If the app unfortunately stops..then please report me with the error.

Note: Transfered file is in decFile.txt change the extension to your desired one and you are good to go.
