package mad.com.stayintouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailedAlbum extends Activity implements gvPhotoAdapter.ReLoad,UserSelectionAdapter.ReLoad {

    List<ParseObject> photos;
    List<ParseUser> users;
    List<String> selectedUsers;
    String AlbumID;
    int pos=0,GET_FROM_GALLERY=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_album);
        AlbumID =getIntent().getStringExtra("AlbumID");

        getPhotosObjects(AlbumID);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            final Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmapLogo= BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapLogo.compress(Bitmap.CompressFormat.PNG,0, stream);
            byte[] image = stream.toByteArray();
            String filename="profile.png";
            final ParseFile file = new ParseFile(filename, image);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    ParseObject obj=new ParseObject("Photos");
                    obj.put("Image",file);
                    obj.put("CreatedBy", ParseUser.getCurrentUser());
                    obj.put("AlbumID", ParseObject.createWithoutData("Albums", AlbumID));
                    try {
                        obj.save();
                        getPhotosObjects(AlbumID);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }
    private void getPhotosObjects(String albumID) {
        photos=new ArrayList<ParseObject>();
//        final GridView gd=(GridView) findViewById(R.id.gridView);
//        ParseQuery query=new ParseQuery("Photos");
//        ParseObject obj=new ParseObject("Albums");
//        query.whereEqualTo("AlbumID", ParseObject.createWithoutData("Albums", albumID));
//        gvPhotoAdapter adapter = null;
//        try {
//            photos=query.find();
//            adapter = new gvPhotoAdapter(DetailedAlbum.this, getBaseContext(), R.layout.activity_photo_adapter,photos);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        gd.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showAlertDialog(final int position) {
        pos=position;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailedAlbum.this);
        alertDialogBuilder.setTitle("SlideShow..");
        LayoutInflater li = LayoutInflater.from(DetailedAlbum.this);
        final View dialogView = li.inflate(R.layout.activity_slide_show, null);
        ImageButton btnPrev=(ImageButton) dialogView.findViewById(R.id.imgPrev);
        ImageButton btnNext=(ImageButton) dialogView.findViewById(R.id.imgNext);
        final ImageView imgView=(ImageView) dialogView.findViewById(R.id.imgPhotoSlideShow);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(pos>0)
                    Picasso.with(getBaseContext()).load(photos.get(pos--).getParseFile("Image").getFile()).error(R.drawable.avatar).into(imgView);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(pos<photos.size()-1)
                        Picasso.with(getBaseContext()).load(photos.get(pos++).getParseFile("Image").getFile()).error(R.drawable.avatar).into(imgView);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void AssignData(String ObjectID,Boolean isChecked) {
        if(isChecked)
            selectedUsers.add(ObjectID);
        else
            selectedUsers.remove(ObjectID);
    }
}
