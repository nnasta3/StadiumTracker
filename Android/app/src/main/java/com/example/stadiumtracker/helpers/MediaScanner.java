package com.example.stadiumtracker.helpers;

import java.io.File;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScanner implements MediaScannerConnectionClient {

    private MediaScannerConnection mediaScannerConnection;
    private File file;
    /* John Strauser
        Constructor for the MediaScanner class
        This class is used to cause the photo gallery in android to update and display the recently saved ticket stub
     */
    public MediaScanner(Context context, File file) {
        this.file = file;
        mediaScannerConnection = new MediaScannerConnection(context, this);
        mediaScannerConnection.connect();
    }
    /* John Strauser
        Calls the mediaScannerConnection to perform the update
     */
    @Override
    public void onMediaScannerConnected() {
        mediaScannerConnection.scanFile(file.getAbsolutePath(), null);
    }
    /* John Strauser
        Closes the connection when the scanner is done
     */
    @Override
    public void onScanCompleted(String path, Uri uri) {
        mediaScannerConnection.disconnect();
    }

}
