package com.alarees.tailoruserapp.measurement;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;
import com.alarees.tailoruserapp.measurement.measurementlist.MeasurementListFragment;
import com.alarees.tailoruserapp.more.MoreItemFragment;

public class HowToFragment extends Fragment implements TitledFragment {
    VideoView videoView;
    MediaController mediaControls;
    TextView chngetext;
    ImageView replayvideo;
    Button nextbtn;
    public static int childflag = 0;
    LinearLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_how_to, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container=view.findViewById(R.id.container_howto);
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                container.setBackgroundResource(R.drawable.background_white);
                break;
        }
        videoView = view.findViewById(R.id.videoview);
        chngetext = view.findViewById(R.id.chngetimetext);
        replayvideo = view.findViewById(R.id.replay_video);
        if (getArguments() != null) {
            childflag = getArguments().getInt("child");

        } else {
            childflag = 0;
        }
        nextbtn = view.findViewById(R.id.next_how_too);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FrontActivity.class));
            }
        });
        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = new MediaController(getContext());
            mediaControls.setAnchorView(videoView);
        }
        // set the media controller for video view
        videoView.setMediaController(mediaControls);
        // set the uri for the video view
        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video));
        // start a video
        videoView.start();
        replayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chngetext.setText("Ask someone to help take 2 photo of you.\\nkeep the device at 90 angle at the waistline.");
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chngetext.setText("For the side photo turn to your left.");
            }
        }, 7000);

        // implement on completion listener on video view
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getContext(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    backtoparent();
                    return true;
                }
                return false;
            }
        });
    }

    private void backtoparent() {
        if (getArguments() != null) {
            if (getArguments().getInt("child") != 1) {
                loadFragment(new MoreItemFragment());
            } else {
                loadFragment(new MeasurementListFragment());
            }
        } else {
            loadFragment(new MoreItemFragment());
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("MoreItemFragment");
        transaction.commit();
    }

    @Override
    public String getTitle() {
        return "Howto";
    }
}