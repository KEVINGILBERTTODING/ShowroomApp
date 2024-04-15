package com.example.rizkimotor.data.remote;

import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.BankAccountModel;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.CreditModel;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.features.auth.model.user.UserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    public static final String IP_ADDRESS = "192.168.43.215";
    public static final String BASE_URL = "http://" + IP_ADDRESS + ":8000/api/";
    public static final String END_POINT =  "http://" + IP_ADDRESS + ":8000/";

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseModel<UserModel>> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/register")
    Call<ResponseModel> registerUser(
            @FieldMap HashMap<String, String> data
            );

    @GET("client/main")
    Call<ResponseModel<AppInfoModel>> getAppInfo();

    @GET("client/car")
    Call<ResponseModel<List<CarModel>>> getAllCar();

    @GET("client/car/{id}")
    Call<ResponseModel<CarModel>> getCarDetail(
            @Path("id") int id
    );

    @GET("client/finance/{id}")
    Call<ResponseModel<FinanceModel>> getFinanceById(
            @Path("id") int id
    );

    @GET("client/finance")
    Call<ResponseModel<List<FinanceModel>>> getAllFinance();

    @GET("client/credit/{mobilId}/{financeId}")
    Call<ResponseModel<CreditModel>> getDP(
            @Path("mobilId") int mobilId,
            @Path("financeId") int financeId
    );

    @FormUrlEncoded
    @POST("client/countcredit")
    Call<ResponseModel<CreditModel>>countCredit(
            @FieldMap HashMap<String, String> map
    );

    @Multipart
    @POST("client/profile/updatephoto")
    Call<ResponseModel> updatePhotoProfile(
            @Part("user_id") RequestBody userId,
            @Part MultipartBody.Part imageFile
            );

    @GET("client/profile/{id}")
    Call<ResponseModel<UserModel>> getUserById(
            @Path("id") int userId
    );

    @Multipart
    @POST("client/credit/insertPengajuanKredit")
    Call<ResponseModel> sendCredit(
            @PartMap Map<String, RequestBody> map,
            @Part List<MultipartBody.Part> partList


            );

    @FormUrlEncoded
    @POST("client/profile/update")
    Call<ResponseModel> updateUserProfile(
            @FieldMap HashMap<String, String> map
    );

    @FormUrlEncoded
    @POST("client/profile/update/password")
    Call<ResponseModel> userUpdatePassword(
            @FieldMap HashMap<String, String> map
    );

    @Multipart
    @POST("client/transaction/store")
    Call<ResponseModel> storeTransaction(
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part part


    );

    @GET("client/bankaccount")
    Call<ResponseModel<List<BankAccountModel>>> getBankAcc();

    @GET("client/transaction/{userId}/{status}")
    Call<ResponseModel<List<TransactionModel>>> getHistoryTransaction(
            @Path("userId") int userId,
            @Path("status") int status
    );

    @Multipart
    @POST("client/review/store")
    Call<ResponseModel> storeReview(
            @PartMap Map<String, RequestBody> requestBodyMap,
            @Part List<MultipartBody.Part> part
    );

    @GET("client/transaction/{id}")
    Call<ResponseModel<TransactionModel>> getTransactionById(
            @Path("id") String id
    );

    @GET("client/invoice/download/{id}")
    Call<ResponseBody> downloadInvoice(
            @Path("id") String id
    );

    @GET("client/search")
    Call<ResponseModel<List<CarModel>>> carSearch(
            @Query("keyword") String query
    );


}
