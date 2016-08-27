package todo.iyzico.com.todoapp.webservice;

/**
 * Created by emrealkan on 27/08/16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.Callback;
import todo.iyzico.com.todoapp.models.BaseResponse;
import todo.iyzico.com.todoapp.models.User;

public class WebServiceBuilder
{
    private final static String URL_ADDRESS = "http://192.168.0.17:8080/api/public";// "http://garantitest.eu-gb.mybluemix.net/api";

    public interface WebService
    {
        @FormUrlEncoded
        @POST("/login")
        public void login(@Field("username") String username,
                          @Field("password") String password,
                          Callback<BaseResponse<User>> callback);

        @FormUrlEncoded
        @POST("/signup")
        public void signup(@Field("username") String username,
                           @Field("password") String password,
                           @Field("email") String email,
                           Callback<BaseResponse<User>> callback);

    }

    public static WebService webService;

    public static WebService getInstance()
    {
        if(webService == null)
        {
            Gson gson = new GsonBuilder()
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(URL_ADDRESS)
                    .setConverter(new GsonConverter(gson))
                    .build();

            webService = restAdapter.create(WebService.class);
        }
        return webService;
    }
}

