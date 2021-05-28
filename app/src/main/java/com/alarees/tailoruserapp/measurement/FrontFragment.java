package com.alarees.tailoruserapp.measurement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alarees.tailoruserapp.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

public class FrontFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_front, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    String mCurrentPhotoPath;
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//
//        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        if (Build.VERSION.SDK_INT >= 24) {
//            mCurrentPhotoPath = String.valueOf(FileProvider.getUriForFile(getContext(),
//                    BuildConfig.APPLICATION_ID + ".provider", image));
//        } else {
//            mCurrentPhotoPath = String.valueOf(Uri.fromFile(image));
//        }
//
//        return image;
//    }

    private void selectImage(){
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //BitMapToString(r.getBitmap());
                    }
                })
                .setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show(getActivity().getSupportFragmentManager());
    }

//    private void selectImage1() {
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, 1);
////
////                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
////                        File photoFile = null;
////                        try {
////                            photoFile = createImageFile();
////                        } catch (IOException ex) {
////                            Log.d("mylog", "onClick: "+ex.getMessage());
////                        }
////                        if (photoFile != null) {
////
////                            Uri photoURI;
////                            if (Build.VERSION.SDK_INT >= 24) {
////                                photoURI = FileProvider.getUriForFile(getContext(),
////                                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
////                            } else {
////                                photoURI = Uri.fromFile(photoFile);
////
////                            }
////                            //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
////                            startActivityForResult(intent, 1);
////                        }}
//
//                } else if (options[item].equals("Choose from Gallery")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    @SuppressLint("LongLogTag")
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                Uri selectedImage = data.getData();
//                Toast.makeText(getContext(), ""+selectedImage.toString(), Toast.LENGTH_SHORT).show();
////                Uri imageUri = Uri.parse(mCurrentPhotoPath);
////                File f = new File(imageUri.getPath());
////                try {
////                    Bitmap bitmap;
////                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
////                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
////                            bitmapOptions);
////                    // viewImage.setImageBitmap(bitmap);
////                    String path = android.os.Environment
////                            .getExternalStorageDirectory()
////                            + File.separator
////                            + "Phoenix" + File.separator + "default";
////                    f.delete();
////                   // OutputStream outFile = null;
////                   // File file = new File(mCurrentPhotoPath, String.valueOf(System.currentTimeMillis()) + ".jpg");
////                    try {
////                     //   outFile = new FileOutputStream(file);
//                       // bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
////                        BitMapToString(bitmap);
//                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.nav_host_fragment, new SideFragment());
//                        ft.addToBackStack("SideFragment");
//                        ft.commit();
//                        //outFile.flush();
//                        //outFile.close();
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
//              //      } catch (Exception e) {
//                //        e.printStackTrace();
//                  //  }
//           //     } catch (Exception e) {
//             //       e.printStackTrace();
//                }
//            } else if (requestCode == 2) {
//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                BitMapToString(thumbnail);
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.nav_host_fragment, new SideFragment());
//                ft.addToBackStack("FronFragment");
//                ft.commit();
//                Log.w("path of image from gallery......******************.........", picturePath + "");
//                // viewImage.setImageBitmap(thumbnail);
//            }
//        }





}