package com.game.legend.singletouch.edit_game;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.game.legend.singletouch.R;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPreferenceFragment extends PreferenceFragmentCompat {


    private EditTextPreference step;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference);
        step= (EditTextPreference) findPreference("step");

        initStep();

        int e=((EditActivity) Objects.requireNonNull(getActivity())).getStep();

        step.setText(String.valueOf(e));
        step.setSummary(step.getText());

    }

    private void initStep(){

        step.setOnPreferenceChangeListener((preference, o) -> {



            if (isNumeric((String) o)) {

                ((EditActivity) (Objects.requireNonNull(getActivity()))).setStep(Integer.parseInt(String.valueOf(o)));
                step.setSummary((String) o);

            }else {
                Toast.makeText(getActivity(), "你很有想法啊~", Toast.LENGTH_SHORT).show();

                return false;
            }
            return true;

        });



    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }



}
