package com.example.khaledelsayed.bluetalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.widget.Button;
import android.widget.EditText;


import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.khaledelsayed.bluetalk.HomeFragment.CONNECTION_TIMEOUT;
import static com.example.khaledelsayed.bluetalk.HomeFragment.READ_TIMEOUT;


public class MainActivity extends AppCompatActivity
implements HomeFragment.OnFragmentcInteractionListener,ConStatusFragment.OnFragmentInteractionListener,ChatFragment.OnFragmentInteractionListener,ChatsFragment.OnListFragmentInteractionListener,SignUp.SignupInterface {

    public String MyName = "khaled";
    public int PhoneNumber;

    public String userName;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public int mobile_num;


    public boolean logged_in=false;
    public  void sign_the_user_up(String user,int mobile){
        userName=user;
        mobile_num= mobile;
       HomeFragment home = HomeFragment.newInstance();
        new signupasync(userName,mobile).execute();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, home);
        transaction.commit();
    }

    public MyMessageAdapter myMessageAdapter;

    public void onFragmentInteraction(Uri uri) {

    }

    public void onJoinPiconet(int PiconetId) {

        new JoinAsyncFetch(PiconetId).execute();


    }

    /************************************************************/
    private class JoinAsyncFetch extends AsyncTask<String, String, String> {
        JoinAsyncFetch(int PiconetId) {
            PiconetID = PiconetId;


        }

        int PiconetID;
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("https://bluetalk.herokuapp.com/piconets#join?piconet_id="+PiconetID + "&user_id=" + mobile_num);//?username1="+MyName+"+username2="+mUser


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.setRequestMethod("GET");
                conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                conn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");


                //  conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();

            } finally {
                conn.disconnect();
            }
            return ("success");
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();


        }
    }

        public void onCreatePiconet(int ChannelId) {
            new CreateAsyncSend(ChannelId).execute();

        }


          /************************************************************/
          private class CreateAsyncSend extends AsyncTask<String, String, String> {

              CreateAsyncSend(int ChannelId) {
                  ChannelID = ChannelId;

              }

              int ChannelID;
              ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
              HttpURLConnection conn;
              URL url = null;

              @Override
              protected void onPreExecute() {
                  super.onPreExecute();

                  //this method will be running on UI thread
                  pdLoading.setMessage("\tLoading...");
                  pdLoading.setCancelable(false);
                  pdLoading.show();

              }

              @Override
              protected String doInBackground(String... params) {
                  try {

                      url = new URL("https://bluetalk.herokuapp.com/piconets/new");//?username1="+MyName+"+username2="+mUser


                  } catch (MalformedURLException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      return e.toString();
                  }
                  try {

                      // Setup HttpURLConnection class to send and receive data from php and mysql
                      conn = (HttpURLConnection) url.openConnection();

                      conn.setReadTimeout(READ_TIMEOUT);
                      conn.setConnectTimeout(CONNECTION_TIMEOUT);

                      conn.setRequestMethod("POST");
                      conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                      conn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");


                      //  conn.setDoOutput(true);

                  } catch (IOException e1) {
                      // TODO Auto-generated catch block
                      e1.printStackTrace();
                      return e1.toString();
                  }
                  try {
                      String urlParameters = "Channel=" + ChannelID + "&Master=71226125&Timer=null&Name=Stars";

                      DataOutputStream outputPost = new DataOutputStream(conn.getOutputStream());
                      outputPost.writeBytes(urlParameters);
                      outputPost.flush();
                      outputPost.close();
//                conn.setFixedLengthStreamingMode(urlParameters.getBytes().length);
                      //               conn.setChunkedStreamingMode(0);

                  } catch (IOException e) {
                      e.printStackTrace();
                      return e.toString();
                  } finally {
                      conn.disconnect();
                  }
                  return ("success");
              }

              @Override
              protected void onPostExecute(String result) {

                  //this method will be running on UI thread

                  pdLoading.dismiss();

              }
          }





  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = logged_in?HomeFragment.newInstance():SignUp.newInstance();
                    break;
                case R.id.navigation_status:
                    selectedFragment = ConStatusFragment.newInstance();
                    break;
                case R.id.navigation_chats:
                    selectedFragment = ChatsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            logged_in=true;
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,  logged_in?HomeFragment.newInstance():SignUp.newInstance());
       transaction.addToBackStack(null);
    transaction.commit();
      BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
      navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataMessage data){
        if(data!=null&&!data.message.isEmpty()) {
            myMessageAdapter.data.add(data);
            myMessageAdapter.notifyItemInserted(myMessageAdapter.getItemCount() - 1);
            //  findViewById(R.id.messages_list).computeScroll();
            //  findViewById(R.id.messages_list).setScrollY(findViewById(R.id.messages_list).getBottom());
            findViewById(R.id.messages_list).setMinimumHeight(200);
        }
    }

    @Override
    public void onListFragmentInteraction(DataUser item) {
    Fragment selectedFragment = ChatFragment.newInstance(item.name);
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.content, selectedFragment);
    transaction.commit();
}

    @Override
    public void onFragmentcInteraction(DataChannel dataChannel) {

    }

    ProgressDialog progress;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(MainActivity.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .build();

                    Request request = new Request.Builder()
                            .url("https://khaled-sayed.000webhostapp.com/messages/create.php")
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        myMessageAdapter.data.add(new DataMessage("PIC:"+file_path,MyName));

                        myMessageAdapter.notifyItemInserted(myMessageAdapter.getItemCount() - 1);
                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();




        }
    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    private class signupasync extends AsyncTask<String, String,String> {

        signupasync(String name,int id){
            mName=name;
            mId = id;
        }
        String mName;
        int mId;
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("https://bluetalk.herokuapp.com/users");//?username1="+MyName+"+username2="+mUser


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                conn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");


                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                String urlParameters = "name="+mName+"&phone_number="+mId;
                DataOutputStream outputPost = new DataOutputStream(conn.getOutputStream());
                outputPost.writeBytes(urlParameters);
                outputPost.flush();
                outputPost.close();
//                conn.setFixedLengthStreamingMode(urlParameters.getBytes().length);
                //               conn.setChunkedStreamingMode(0);

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
            return ("success");
        }


    }
}
