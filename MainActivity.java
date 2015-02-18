package com.cs246.eachee.multithreadedprogramming;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Create a new file in the internal storage area, called numbers.txt .
     * You can get a File object in the internal storage area like so:
     *      "File file = new File(context.getFilesDir(), filename);"
     *      (see this page for more information: http://developer.android.com/training/basics/data-storage/files.html )
     * In the file, print the numbers 1-10, one per line.
     * After writing each line, add a "Thread.sleep(250);" to pause for a quarter of a second
     *      to simulate a more difficult task.
     *
     * @param view
     */
    public void create(View view) {

        class AsyncCreate extends AsyncTask<String, Integer, String> {

            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

            @Override
            protected String doInBackground(String... params) {
                //Create a new file in the internal storage area, called numbers.txt.
                String fileName = "numbers.txt";

                try {
                    File file = new File(getFilesDir(), fileName);

                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
                    for (Integer i = 1; i <= 10; i++) {
                        bWriter.write(Integer.toString(i));
                        bWriter.newLine();
                        publishProgress((int) (i * 10));
                        Thread.sleep(100);
                    }
                    bWriter.close();
                } catch (IOException e) {
                    System.out.println("ERROR: IOException");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.out.println("ERROR: InterruptedException");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setProgress(values[0]);
            }

        }
        new AsyncCreate().execute();
    }

    /**
     * Load the file numbers.txt and read it line by line.
     * As you read each line, store the number in a list.
     * After reading each line, add a "Thread.sleep(250);" to simulate a more difficult task.
     * Then add this list your ListView (Hint: Use an ArrayAdapter)
     *
     * @param view
     */
    public void load(View view) {

        class AsyncLoad extends AsyncTask<String, Integer, String> {

            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

            @Override
            protected String doInBackground(String... params) {
                String fileName = "numbers.txt";
                File file = new File(getFilesDir(), fileName);

                String[] readNumbers = new String[10];
                final ArrayList<String> arrayList = new ArrayList();

                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
                    String inputString;
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((inputString = inputReader.readLine()) != null) {

                        arrayList.add(inputString);
                        Thread.sleep(250);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final ListView list = (ListView) findViewById(R.id.listView);

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.setAdapter(adapter);
                    }
                });
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setProgress(values[0]);
            }
        }
        new AsyncLoad().execute();
    }

        /**
         * Add an event handler for the Clear button that clears the list. (Hint: Clear the ArrayAdapter)
         */
    public void clear(View view) {

        final ListView list = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
