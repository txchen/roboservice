package com.tc.roboservice;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyApplication extends Application {

    private final String TAG = "RSAPP";

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "setting up exception trap");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // print the exception to System.err

        // read log from logcat and write to file
        extractLogToFile();

        System.exit(1); // kill off the crashed app
    }

    private String extractLogToFile() {
        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        String fullName = Environment.getExternalStorageDirectory() + "/roboservicecrash.log";

        // Extract to file.
        File file = new File(fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
//            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
//                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
//                    "logcat -d -v time";
            String cmd = "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader(process.getInputStream());

            // write output stream
            writer = new FileWriter(file, true);

            char[] buffer = new char[10000];
            do {
                int n = reader.read(buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write(buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
            Log.i(TAG, "crash exception wrote to " + fullName);
        } catch (IOException e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }

            // You might want to write a failure message to the log here.
            Log.e(TAG, "failed to write log to file" + e.getMessage());
            return null;
        }

        return fullName;
    }
}
