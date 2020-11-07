package com.example.ui.controler;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.example.ui.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogNumberPicker extends AppCompatDialogFragment {

    private int minute;
    private int hour;
    private DialogNumberPickerListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_number_picker, (ViewGroup) getView());

        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.durationListener(hour, minute);
                    }
                });

        NumberPicker numberPickerHour = view.findViewById(R.id.picker_hour);
        NumberPicker numberPickerMinute = view.findViewById(R.id.picker_minutes);

        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(12);

        //Minutes by steps of x
        final int interval = 10;
        String[] minutesValues = new String[60/interval];
        for(int i = 0; i < minutesValues.length; i++)
        {
            String number = Integer.toString(i*interval);
            minutesValues[i] = number;
        }
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(minutesValues.length-1);
        numberPickerMinute.setDisplayedValues(minutesValues);

        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
            }
        });
        numberPickerMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minute = newVal*interval;
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogNumberPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement DialogNumberPickerListener");
        }
    }

    public interface DialogNumberPickerListener{
        void durationListener(int hour, int minute);
    }

}
