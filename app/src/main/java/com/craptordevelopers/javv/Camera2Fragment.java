package com.craptordevelopers.javv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Eric on 3/15/2017.
 */

public class Camera2Fragment extends Fragment {
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview, iv_photocam_icon , iv_videocam_icon;
    private VideoView videoPreview;
    private Button btn_camera;
    private ImageButton ib_reset;
    private TextView tv_take_picture , tv_take_video;
    private View mView;


    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        return inflater.inflate(R.layout.fragment_camera2, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        init(view);
    }

    private void init(final View view){
        imgPreview = (ImageView) view.findViewById(R.id.iv_picture_preview);
        iv_photocam_icon = (ImageView) view.findViewById(R.id.iv_photocam_icon);
        iv_videocam_icon = (ImageView) view.findViewById(R.id.iv_videocam_icon);
        videoPreview = (VideoView) view.findViewById(R.id.video_preview);
        btn_camera = (Button) view.findViewById(R.id.btn_camera);
        tv_take_picture = (TextView) view.findViewById(R.id.tv_take_picture) ;
        tv_take_video = (TextView) view.findViewById(R.id.tv_take_video) ;
        ib_reset = (ImageButton) view.findViewById(R.id.ib_reset);

        /**
         * Capture image button click event
         */

        ib_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_take_reset(view);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                media_take(view);
                captureImage();
            }
        });

        /**
         * Record video button click event
         */

        btn_camera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                media_take(view);
                recordVideo();
                return false;
            }
        });


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(context,
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
//            this.getActivity().finish();
        }
    }

    private void media_take(View view){
        btn_camera.setVisibility(view.GONE);
        tv_take_picture.setVisibility(view.GONE);
        tv_take_video.setVisibility(view.GONE);
        ib_reset.setVisibility(view.VISIBLE);
    }

    private void  media_take_reset(View view){
        imgPreview.setImageResource(android.R.color.transparent);
        videoPreview.setVideoURI(null);
        btn_camera.setVisibility(view.VISIBLE);
        tv_take_picture.setVisibility(view.VISIBLE);
        tv_take_video.setVisibility(view.VISIBLE);
        ib_reset.setVisibility(view.GONE);
    }

    private boolean isDeviceSupportCamera() {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        Fragment fragment = this;
        // start the image capture Intent
        fragment.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            savedInstanceState.putParcelable("file_uri", fileUri);
        }
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        savedInstanceState.putParcelable("file_uri", fileUri);
//
//    }

    /**
     * Recording video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        Fragment fragment = this;
        fragment.startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }




    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image capture
                media_take_reset(mView);
//                Toast.makeText(context,
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
            } else {
                // failed to capture image
                media_take_reset(mView);
//                Toast.makeText(context,
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                previewVideo();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled recording
                media_take_reset(mView);
//                Toast.makeText(context,
//                        "User cancelled video recording", Toast.LENGTH_SHORT)
//                        .show();
            } else {
                // failed to record video
                media_take_reset(mView);
//                Toast.makeText(context,
//                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
//                        .show();
            }
        }
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Previewing recorded video
     */
    private void previewVideo() {
        try {
            // hide image preview
            imgPreview.setVisibility(View.GONE);

            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp =  new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}

