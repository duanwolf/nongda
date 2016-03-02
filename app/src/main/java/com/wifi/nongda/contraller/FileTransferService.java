package com.wifi.nongda.contraller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 段碧伟 on 2016/2/21.
 */
public class FileTransferService extends IntentService {

    private String name;
    public static final String ACTION_RECEIVE_FILE = "com.wifidirect.receive";

    public FileTransferService() {
        super(null);
    }
    public FileTransferService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent.getAction().equals(ACTION_RECEIVE_FILE)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        int port = intent.getExtras().getInt("port");
                        ServerSocket serverSocket = new ServerSocket(port);
                        Socket client = serverSocket.accept();
                        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        while (!Thread.currentThread().isInterrupted()) {
                            String message = null;
                            message = input.readLine();
                            if (message != null) {
                                Log.d("Input", "getMessage == " + message);

                            } else {
                                Log.d("Input", "Null!!!!!!!!");
                            }
                        }
                        input.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}
