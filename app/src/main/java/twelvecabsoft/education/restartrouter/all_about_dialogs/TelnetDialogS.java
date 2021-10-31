package twelvecabsoft.education.restartrouter.all_about_dialogs;





import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TelnetDialogS {
    private ArrayList<TelnetDialog> all_dialogs;

    public void saveDialogs(String appPath) {

        boolean fileOK = false;
        File F = new File(appPath + "/DialogS.txt");
        fileOK = F.exists();
        if (!fileOK) {
            try {
                fileOK = F.createNewFile();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        if (fileOK) {

            try {
                OutputStream outputStream = new FileOutputStream(F);
                Gson StoF = new GsonBuilder().setPrettyPrinting().create();
                String res = StoF.toJson(all_dialogs);
                try {
                    outputStream.write(res.getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //String[] fl = fileList();
                //fl=null;
                //int f;
                //f=3;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //   InputStreamReader tmp = new InputStreamReader(InS, StandardCharsets.UTF_8);
        }
    }
    public void LoadDialogs (String appPath){
        String res=null;
        try
        {
            File F=new File(appPath+"/DialogS.txt");
            InputStream InS =new FileInputStream( F );
            InputStreamReader tmp = new InputStreamReader(InS, StandardCharsets.UTF_8);
            //FileInputStream fin =  openFileInput("test.txt");
            //fin.
            //String Enc=fin.
            int a = 0;
            StringBuilder temp = new StringBuilder();
            while ((a = tmp.read()) != -1)
            {
                temp.append((char)a);
            }

            // setting text from the file.
            //fileContent.setText(temp.toString());
            res=temp.toString();
            tmp.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (res!=null)
        {

            Gson FtoJ =new GsonBuilder().setPrettyPrinting().create();
            all_dialogs=FtoJ.fromJson(res,new TypeToken<ArrayList<TelnetDialog>>(){}.getType());
        }
    }
}
