package com.example.packetsniffer.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class FileLoader extends AsyncTask<Uri, Void, String>
{
    private WeakReference<Context> contextRef;
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void fileLoadFinish(String result);
    }
    public FileLoader(Context context, AsyncResponse delegate) {
        contextRef = new WeakReference<>(context);
        this.delegate =  delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(Uri... uris) {
        Context context = contextRef.get();
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = uris[0];
        try {
            Cursor returnCursor = contentResolver.query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            InputStream inputStream =  contentResolver.openInputStream(uri);

            File downloadDir =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File f = new File(downloadDir +  "/" + fileName);

            FileOutputStream out = new FileOutputStream(f);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            inputStream.close();
            returnCursor.close();
            return  f.getPath();
        }
        catch (Exception e){
            Log.e(FileLoader.class.getName(), e.getMessage(), e);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        delegate.fileLoadFinish(result);
        super.onPostExecute(result);
    }
}
