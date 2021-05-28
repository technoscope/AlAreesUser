package com.alarees.tailoruserapp.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;

import java.util.Calendar;


public class ContactFragment extends Fragment implements TitledFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new AboutPage(getContext())
                .isRTL(false)
                .setDescription(getString(R.string.app_description))
                .setImage(R.drawable.logonew)
                .addGroup(getString(R.string.contact_group))
                .addEmail("BotSolutions.tech.com", "Email")
                .addFacebook("https://www.facebook.com/www.botsolutions.tech", "Facebook")
                .addInstagram("https://www.instagram.com/botsolutions.tech/","Instagram")
                .addWebsite("http://botsolutions.tech/","Visit our site")
                .addGroup("Rate our app")
                .addPlayStore("https://play.google.com/store/apps","Tailor App")
                .addGroup(getString(R.string.developed_by))
                .addWebsite("http://botsolutions.tech/", "Bot Solutions")
                .addItem(getCopyRightsElement())
                .create();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format("Copyrights © 2021", Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    @Override
    public String getTitle() {
        //String s = getActivity().getResources().getString(R.string.aboutus);
        return "About Us";
    }
}