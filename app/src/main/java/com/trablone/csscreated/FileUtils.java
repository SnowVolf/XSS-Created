package com.trablone.csscreated;
import java.io.*;
import android.os.*;
import android.content.*;

public class FileUtils
{
	public static String patch = Environment.getExternalStorageDirectory() + "/data/XssCreated/css/";
	public static String name = "custom.css";
	
	public static void openNamedFile(String filename)
	{
		try
		{
			File f = new File(filename);
			//	Log.e("kuinfa", "filename= " + filename);
			FileInputStream fis = new FileInputStream(f);

			long size = f.length();
			name = f.getName();
			patch = f.getParentFile().toString();
			DataInputStream dis = new DataInputStream(fis);
			byte[] b = new byte[(int) size];
			int length = dis.read(b, 0, (int) size);

			dis.close();
			fis.close();

			String ttt = new String(b, 0, length, "UTF-8");

			try
			{
				ttt = new String(ttt.getBytes(), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
			}
			

		}
		catch (FileNotFoundException e)
		{
				}
		catch (IOException e)
		{
				}
	}
	/*private void filePatch(){
	 if(mFile == null){
	 if(default_patch){
	 //	patch = FILEPATCH + "/" + name;
	 nameFile = editTitle.getText().toString();
	 }
	 }


	 }*/


	public static void saveNamedFile(String patch, String data, String name)
	 {

	 try
	 {
	 
	 File direct = new File(patch);
	 if(!direct.exists())
	 direct.mkdirs();

	 File f = new File(patch + "/" + name);
	 if (!f.exists())
	 	f.createNewFile();
	 

	 FileOutputStream fos = new FileOutputStream(f);
	 String s = data;
	 fos.write(s.getBytes("UTF-8"));
	 fos.close();
	 
	 

	 }
	 catch (FileNotFoundException e)
	 {
	 	 }
	 catch (IOException e)
	 {
	 	 }

	 }
	/*' protected void renameNamedFile(){
	 File from = new File(patch, nameFile);
	 File to = new File(patch, editTitle.getText().toString());
	 from.renameTo(to);
	 String s = editTitle.getText().toString();
	 if(s.length() > 0){
	 nameFile = s;
	 default_patch = false;
	 }
	 }*/
	 
	public static String LoadData(Context context,String inFile) {
		String tContents = "";
		try {
			InputStream stream = context.getAssets().open(inFile);
			int size =	stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			tContents = new String(buffer);
		} catch (IOException e) {
			// Handle exceptions

		}


		return tContents;
	}
}
