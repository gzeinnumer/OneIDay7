package com.gzeinnumer.oneiday7.background;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzeinnumer.oneiday7.R;
import com.gzeinnumer.oneiday7.databinding.FragmentAsyncTaskBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTaskFragment extends Fragment {

    public AsyncTaskFragment() {
        // Required empty public constructor
    }

    private FragmentAsyncTaskBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAsyncTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask task = new DownloadTask();
                task.execute("http://file");
            }
        });
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {

        private int count = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.tvDownload.setText("Proses download akan segera dimulai");
        }

        @Override
        protected String doInBackground(String... voids) {
            String s = voids[0];
            SystemClock.sleep(2000);
            for (int i = 0; i < 10; i++) {
                count = count + 10;
                publishProgress(count);
                SystemClock.sleep(500);
            }
            return "Proses download sudah selesai sebesar : "+count+"%";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            binding.hzProgresBar.setProgress(values[0]);
            binding.tvDownload.setText("Proses "+values[0]+"% download dari server.");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            binding.tvDownload.setText(s);
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(){
            @Override
            public void run() {
                super.run();
                // eksekusi code disini
            }
        }.start();

        new Executor() {
            @Override
            public void execute(Runnable command) {
                // eksekusi code disini
            }
        };

        Executors.newFixedThreadPool(10);

        Handler handler = new Handler(Looper.getMainLooper());
    }
}