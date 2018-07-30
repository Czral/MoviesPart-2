package com.example.android.movies;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by XXX on 01-Jul-18.
 */

public class CheckConnection extends AsyncTask<Void, Void, Boolean> {

    public CheckConnection() {

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            int timeoutMs = 200000;
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
            socket.connect(socketAddress, timeoutMs);
            socket.close();
            return true;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);

    }
}
