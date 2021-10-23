package com.gzeinnumer.oneiday7.session;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gzeinnumer.oneiday7.R;
import com.gzeinnumer.oneiday7.databinding.FragmentSessionBinding;

import java.util.Base64;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SessionFragment extends Fragment {

    public SessionFragment() {
        // Required empty public constructor
    }

    private FragmentSessionBinding binding;

    private String username;
    private String password;

    private Executor backgroundThread = Executors.newSingleThreadExecutor();
    private Executor mainThread = new Executor() {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initOnClick();
    }

    private void initOnClick() {

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagerUtil.getInstance().endUserSession(requireActivity());
                startMainActivity();
            }
        });
    }

    private void validate(){

        username = binding.userName.getEditText().getText().toString();
        password = binding.userPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Username dan password tidak boleh kosong!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.equalsIgnoreCase("user") && password.equalsIgnoreCase("pass")) {
            login();
        } else {
            Toast.makeText(getActivity(), "Username dan password tidak cocok!!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void login(){
        binding.progressBar.setVisibility(View.VISIBLE);
        backgroundThread.execute(new Runnable() {
            @Override
            public void run() {
                // connect server
                SystemClock.sleep(3000);
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        startAndStoreSession();
                        startMainActivity();
                    }
                });
            }
        });
    }

    private String generateToken(String username, String password){
        String feeds = username+":"+password;
        String token = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            token = Base64.getEncoder().encodeToString(feeds.getBytes());
        } else {
            token = feeds;
        }
        return token;
    }

    private void startAndStoreSession(){
        SessionManagerUtil.getInstance()
                .storeUserToken(requireActivity(), generateToken(username, password));
        SessionManagerUtil.getInstance()
                .startUserSession(requireActivity(), 30);
    }

    private void startMainActivity(){
        Intent intent = new Intent(requireActivity(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        requireActivity().startActivity(intent);
    }
}