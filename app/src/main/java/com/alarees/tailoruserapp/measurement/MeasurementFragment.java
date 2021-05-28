package com.alarees.tailoruserapp.measurement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ms_android_sdk.CallBack;
import com.example.ms_android_sdk.Mirrorsize_Function;
import com.alarees.tailoruserapp.account.AccountModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStorageDirectory;

public class MeasurementFragment extends Fragment {
    private Mirrorsize_Function mirrorsize_function;
    static String userID;
    private int angle;
    //ImageView IDProf;
    public static String productname = "GET_MEASURED";
    public static String gender = "Male";
    CountDownTimer countDownTimer;
    private String Document_img1 = "";
    private String Document_img2 = "";
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView tv;
    Button measurementbtn, frontbtn, sidebtn, getm, inituser;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int PICK_IMAGE_REQUEST = 9;
    int PICK_IMAGE_REQUEST2 = 99;
    String useriid, url, genderr, height, weight;
    SharedPreferences sharedpreferences;
    DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences("UserMeasurement", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inituser = view.findViewById(R.id.inituser);
        inituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AccountModel model = snapshot.getValue(AccountModel.class);
                        Init_User(getContext(), "Male", Integer.parseInt(model.getHeight()), Float.parseFloat(model.getWeight()), new CallBack() {
                            @Override
                            public void onSuccess(String s) {
                                try {
                                    tv.append(s);

//                                    JSONObject response = new JSONObject(s);
//                                    url=response.getString("url");
//                                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                                    editor.putString("userid", response.getString("userId"));
//                                    editor.putString("url",response.getString("url"));
//                                    editor.putString("weight",response.getString("weight"));
//                                    editor.putString("height",response.getString("height"));
//                                    editor.putString("gender",response.getString("gender"));
//                                    editor.apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError volleyError) {
                                Toast.makeText(getContext(), "Error\n \n" + volleyError, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        tv = view.findViewById(R.id.textview);
        frontbtn = view.findViewById(R.id.selectfrontimg);
        frontbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mirrorsize_function.startAngleSensor(getContext());
//                //start before capturing
//                countDownTimer=new CountDownTimer(60000,500) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        angle = mirrorsize_function.angle;
//                    }
//
//                    @Override
//                    public void onFinish() {
//                    }
//                }.start();
                //selectImage();
                SelectImage();
                //stop when captured successfully
                //    mirrorsize_function.stopSensor(getContext());
            }
        });
        getm = view.findViewById(R.id.getmymeasrement);
        getm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_measurement(new CallBack() {
                    @Override
                    public void onSuccess(String s) {
                        tv.setText(s);
                    }

                    @Override
                    public void onError(VolleyError volleyError) {
                        tv.setText(volleyError.toString());
                    }
                });
            }
        });
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        sidebtn = view.findViewById(R.id.selectsideimg);
        sidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mirrorsize_function.startAngleSensor(getContext());
//                //start before capturing
//                countDownTimer=new CountDownTimer(60000,500) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        angle = mirrorsize_function.angle;
//                    }
//
//                    @Override
//                    public void onFinish() {
//                    }
//                }.start();
                SelectImage2();
                //stop when captured successfully
                //   mirrorsize_function.stopSensor(getContext());
            }
        });
        measurementbtn = view.findViewById(R.id.mymeasurement);
        measurementbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MS_measurement_Process(getContext(), sharedpreferences.getString("gender", "Male"), Integer.parseInt(sharedpreferences.getString("height", "150")), Float.parseFloat(sharedpreferences.getString("weight", "60")), Document_img1, Document_img2
                        , new CallBack() {
                            @Override
                            public void onSuccess(String s) {
                                tv.append(s);
                            }

                            @Override
                            public void onError(VolleyError volleyError) {
                                Toast.makeText(getContext(), "" + volleyError, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    //    public void myUserMeasurement(){
//        mirrorsize_function.MS_GetMeasurement(getContext(), API_KEY, "Thob", "thob", "Male", MERCHANT_ID, userID, new CallBack() {
//            @Override
//            public void onSuccess(String s) {
//                tv.append(s);
//            }
//
//            @Override
//            public void onError(VolleyError volleyError) {
//                tv.setText(volleyError.toString());
//            }
//        });
//    }
//    public void getmymeaurement(){
//        //to upload to Images
//       // Document_img1="https://firebasestorage.googleapis.com/v0/b/tailor-admin-side.appspot.com/o/images%2FUserMeasurement%2Ffront.png?alt=media&token=93879528-6506-4d4a-9d9f-f4a2f2af8a01";
//       // Document_img2="https://firebasestorage.googleapis.com/v0/b/tailor-admin-side.appspot.com/o/images%2FUserMeasurement%2Fside.png?alt=media&token=1ae8a54b-02d9-479d-bd94-54fb4dd0fa4f";
//        Toast.makeText(getContext(), userID, Toast.LENGTH_SHORT).show();
//        mirrorsize_function.MS_measurement_Process_loosefit(getContext(), userID, 90, 1484, 56,24, gender, productname, "Ahmad Rehman",
//                "userphone", API_KEY, Document_img1, Document_img2, MERCHANT_ID, getDeviceName(), new CallBack() {
//                    @Override
//                    public void onSuccess(String s) {
//                        tv.append(s);
//                    }
//
//                    @Override
//                    public void onError(VolleyError volleyError) {
//                        tv.append(volleyError.toString());
//                    }
//                });
//    }
//    /** Returns the consumer friendly device name */
//    public static String getDeviceName() {
//        String manufacturer = Build.MANUFACTURER;
//        String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        }
//        return capitalize(manufacturer) + " " + model;
//    }
    //    private static String capitalize(String str) {
//        if (TextUtils.isEmpty(str)) {
//            return str;
//        }
//        char[] arr = str.toCharArray();
//        boolean capitalizeNext = true;
//
//        StringBuilder phrase = new StringBuilder();
//        for (char c : arr) {
//            if (capitalizeNext && Character.isLetter(c)) {
//                phrase.append(Character.toUpperCase(c));
//                capitalizeNext = false;
//                continue;
//            } else if (Character.isWhitespace(c)) {
//                capitalizeNext = true;
//            }
//            phrase.append(c);
//        }
//
//        return phrase.toString();
//    }
    /*
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 22);
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 434);
                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory().toString(), "/temp.jpg");
                         intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                         intent.setDataAndType(Uri.parse(f.getPath()),"Uri");
                        startActivityForResult(intent, 1);
                    }

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage2() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        //checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 22);
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 434);
                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 2);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    */
    // Select Image method
    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Select Image method
    private void SelectImage2() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = getResizedBitmap(bitmap, 400);
                    // IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                        Document_img1 = uploadImage();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == 2) {
                File f = new File(getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = getResizedBitmap(bitmap, 400);
                    //IDProf.setImageBitmap(bitmap);
                    BitMapToString2(bitmap);
                    String path = getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                        Document_img2 = uploadImage();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //temporary
            if (requestCode == PICK_IMAGE_REQUEST
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null) {

                // Get the Uri of data
                filePath = data.getData();
                //Document_img1=StringToBase64(data.getData().toString());//uploadImage();

                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getActivity().getContentResolver(),
                                    filePath);
                    Document_img1 = encodeTobase64(bitmap);
                    // imagePocket.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            }
            if (requestCode == PICK_IMAGE_REQUEST2
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null) {

                // Get the Uri of data
                filePath = data.getData();

                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getActivity().getContentResolver(),
                                    filePath);
                    Document_img2 = encodeTobase64(bitmap);//=uploadImage();
                    // imagePocket.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            }
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public String StringToBase64(String p) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(p);//decodeResource(getResources(),filePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public String BitMapToString2(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img2 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img2;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private Uri filePath;

    // UploadImage method
    private String uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String id = "images/UserMeasurement/" + userID + ""
                    + UUID.randomUUID().toString();
            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(id);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    StorageReference storageReferencee = storage.getReference();
                                    storageReferencee.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            filePath = uri;

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                    // filePath = taskSnapshot.getUploadSessionUri();
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT)
                                            .show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
        return filePath.toString();
    }

    public void Init_User(Context context, String gender, int height, float weight, final CallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("gender", gender);
//            jsonObject.put("height", height);
//            jsonObject.put("weight", weight);
        } catch (Exception var20) {
            var20.printStackTrace();
        }
        // if (jsonObject.length() > 0) {
        String api = "https://saia.3dlook.me/api/v2/persons/";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, api, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //  headers.put("Content-Type", "application/json");
                headers.put("Authorization", "APIKey a81c478a60cff15d5b60b32d03158965f4dd9b0b");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, 0, 1.0F));
        Volley.newRequestQueue(context).add(jsonObjReq);
        //   }
    }

     public void MS_measurement_Process(Context context, String gender, int height, float weight, String frontimage, String sideimage, final CallBack callBack) {
         JSONObject jsonObject = new JSONObject();
         try {
             jsonObject.put("gender", "male");
             jsonObject.put("height", 170);
             jsonObject.put("weight", 60.6);
             jsonObject.put("front_image", frontimage);
             jsonObject.put("side_image", sideimage);

         } catch (Exception var20) {
             var20.printStackTrace();
         }
         if (jsonObject.length() > 0) {
             String api = "https://saia.3dlook.me/api/v2/persons/?measurements_type=all";// sharedpreferences.getString("url","https://saia.3dlook.me/api/v2/persons/?measurements_type=all");
             JsonObjectRequest jsonObjReq = new JsonObjectRequest(1, api, jsonObject, new Response.Listener<JSONObject>() {
                 public void onResponse(JSONObject response) {
                     Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                     callBack.onSuccess(response.toString());
                 }
             }, new Response.ErrorListener() {
                 public void onErrorResponse(VolleyError error) {
                     Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                    // callBack.onError(error);
                 }
             }) {
                 @Override
                 public Map<String, String> getHeaders() throws AuthFailureError {
                     super.getHeaders();
                     Map<String, String> headers = new HashMap<>();
                     headers.put("Authorization", "APIKey a81c478a60cff15d5b60b32d03158965f4dd9b0b");
                     headers.put("Content-Type", "application/json");
                     return headers;
                 }
             };
             jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(300000, 0, 1.0F));
             Volley.newRequestQueue(context).add(jsonObjReq);
         }
     }

    public void get_measurement(final CallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        String api = "https://saia.3dlook.me/api/v2/persons/259269/";//+sharedpreferences.getString("userid","0");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, api, jsonObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "APIKey a81c478a60cff15d5b60b32d03158965f4dd9b0b");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(300000, 0, 1.0F));
        Volley.newRequestQueue(getContext()).add(jsonObjReq);

    }
}