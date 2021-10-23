package com.gzeinnumer.oneiday7.background;

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
import com.gzeinnumer.oneiday7.databinding.FragmentMultiThreadBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiThreadFragment extends Fragment {

    public MultiThreadFragment() {
        // Required empty public constructor
    }

    private FragmentMultiThreadBinding binding;

    private Executor poolWorker = Executors.newFixedThreadPool(2);
    private Executor mainThread = new Executor() {
        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    float multiplication = 2;
    float division = 2;
    float summation = 2;
    float reduction = 2;

    final int base = 2;
    final int step = 20;
    final long clockSleep = 500;

    private Thread workMul = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < step; i++) {
                multiplication = multiplication * base;
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        binding.threadOne.setText(String.valueOf(multiplication));
                    }
                });
                SystemClock.sleep(clockSleep);
            }
        }
    };

    private Thread workDiv = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < step; i++) {
                division = division / base;
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        binding.threadTwo.setText(String.valueOf(division));
                    }
                });
                SystemClock.sleep(clockSleep);
            }
        }
    };

    private Thread workSum = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < step; i++) {
                summation = summation + base;
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        binding.threadThree.setText(String.valueOf(summation));
                    }
                });
                SystemClock.sleep(clockSleep);
            }
        }
    };

    private Thread workSub = new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < step; i++) {
                reduction = reduction - base;
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        binding.threadFour.setText(String.valueOf(reduction));
                    }
                });
                SystemClock.sleep(clockSleep);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMultiThreadBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.startWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    public void start(){
        poolWorker.execute(workMul);
        poolWorker.execute(workDiv);
        poolWorker.execute(workSum);
        poolWorker.execute(workSub);
    }
}