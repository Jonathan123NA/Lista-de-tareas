package com.example.listatareas;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.listatareas.Model.Modelo;
import com.example.listatareas.Utils.BaseDatos;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AgregarNuevaNota extends BottomSheetDialogFragment {

    public static final String TAG = "AgregarNuevaNota";

    //widgets
    private EditText mEditText;
    private Button mSaveButton;

    private BaseDatos myDb;

    public static AgregarNuevaNota newInstance(){
        return new AgregarNuevaNota();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.agregar_nuevanota, container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edt_ingresarNota);
        mSaveButton = view.findViewById(R.id.btn_guardar);

        myDb = new BaseDatos(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if (task.length() > 0 ){
                mSaveButton.setEnabled(false);
            }

        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().equals("")){
                   mSaveButton.setEnabled(false);
                   mSaveButton.setBackgroundColor(Color.GRAY);
               }else{
                   mSaveButton.setEnabled(true);
                   mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String text = mEditText.getText().toString();

               if (finalIsUpdate){
                   myDb.updateTask(bundle.getInt("id") , text);
               }else{
                   Modelo item = new Modelo();
                   item.setTask(text);
                   item.setStatus(0);
                   myDb.insertTask(item);
               }
               dismiss();

            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
