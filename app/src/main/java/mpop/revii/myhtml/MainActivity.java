package mpop.revii.myhtml;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.SharedPreferences;
import android.text.TextWatcher;
import android.text.Editable;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class MainActivity extends Activity { 
    EditText code;
	SharedPreferences pref;
	public static String PREF_NAME = "mpop.revii.pref.NAME";
	public static String PREF_CODE = "mpop.revii.pref.CODE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
		code = findViewById(R.id.code);
		code.setText(pref.getString(PREF_CODE,""));
		code.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					pref.edit().putString(PREF_CODE,code.getText().toString()).commit();
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
    }
	/* android:onClick="html" */
	public void html(View v){
		startActivity(new Intent(this,MyHTML.class));
	}
	/* android:onClick="save" */
	public void save(View v){
		if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
			save();
		}else{
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
		}
	}
	/* Prompting and asking to save the code as a file */
	public void save(){
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		final EditText et = new EditText(this);
		et.setSingleLine();
		build.setTitle("Save as html file:");
		build.setView(et);
		build.setPositiveButton("Save", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					if(et.getText().toString().replace(" ","").isEmpty()){
						/* Empty value */
						Toast.makeText(MainActivity.this, "The filename must not be empty", Toast.LENGTH_LONG).show();
					}else{
				    	save(et.getText().toString(),!(new File("/storage/emulated/0/MPOP HTML FILES/" + et.getText().toString()).exists()));
					}
				}
			});
		build.setNegativeButton("Cancel", null);
		build.setCancelable(false);
		build.show();
	}
	/* save file */
	public void save(String str,boolean rep){
		File folder = new File("/storage/emulated/0/MPOP HTML FILES");
		if(!folder.exists()){
			folder.mkdirs();
			folder.mkdir();
		}
		File file = new File("/storage/emulated/0/MPOP HTML FILES",str);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(file, rep);
			byte[] cont = code.getText().toString().getBytes();
			try {
				out.write(cont);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/* asking for permission */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch(requestCode){
			case 0:
				if(grantResults.length > 0 & grantResults[0] == PackageManager.PERMISSION_GRANTED){
					/* Granted permission */
					save();
					Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
				}else{
					/* Denied permission */
					Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
				}
			break;
		}
	}
	/* if user want to exit the app */
	@Override
	public void onBackPressed() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle("Confirmation:");
		build.setMessage("Are you sure you want to exit the app?");
		build.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					finishAndRemoveTask();
				}
			});
		build.setNegativeButton("No",null);
		build.show();
	}
} 
