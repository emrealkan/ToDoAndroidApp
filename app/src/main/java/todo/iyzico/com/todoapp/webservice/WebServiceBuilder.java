package todo.iyzico.com.todoapp.webservice;

/**
 * Created by emrealkan on 27/08/16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import todo.iyzico.com.todoapp.models.BaseResponse;
import todo.iyzico.com.todoapp.models.ToDo;
import todo.iyzico.com.todoapp.models.User;

public class WebServiceBuilder {
    private final static String URL_ADDRESS = "https://iyzicotodo.herokuapp.com/api/public";

    public interface WebService {
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

        @FormUrlEncoded
        @POST("/createTodo")
        public void createTodo(@Field("userId") Long userId,
                               @Field("title") String title,
                               @Field("subTitle") String subTitle,
                               @Field("content") String content,
                               @Field("endDate") String endDate,
                               @Field("startDate") String startDate,
                               Callback<BaseResponse> callback);

        @FormUrlEncoded
        @POST("/getTodos")
        public void getTodos(@Field("userId") Long userId,
                             Callback<BaseResponse<List<ToDo>>> callback);

        @FormUrlEncoded
        @POST("/deleteTodo")
        public void deleteTodo(@Field("todoId") Long todoId,
                             Callback<BaseResponse> callback);
    }

    public static WebService webService;

    public static WebService getInstance() {
        if (webService == null) {
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

