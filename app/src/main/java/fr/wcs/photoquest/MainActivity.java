package fr.wcs.photoquest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1234;
    public ImageView mImageView;
    private Uri mFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        mImageView = findViewById(R.id.iv_photo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        // ouvrir l'application de prise de photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // lors de la validation de la photo
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // créer le fichier contenant la photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // TODO : gérer l'erreur
            }

            if (photoFile != null) {
                // récupèrer le chemin de la photo
                mFileUri = FileProvider.getUriForFile(this,
                        "fr.wcs.photoquest.fileprovider",
                        photoFile);
                // déclenche l'appel de onActivityResult
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mImageView.setImageURI(mFileUri);
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        return image;
    }
}

