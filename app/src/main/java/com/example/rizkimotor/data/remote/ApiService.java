package com.example.rizkimotor.data.remote;

import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.BankAccountModel;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.CreditModel;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseFilterModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.features.car.admin.model.CarComponentModel;
import com.example.rizkimotor.features.home.admin.model.ChartModel;
import com.example.rizkimotor.features.transactions.admin.model.ResponseAdminTransactionModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
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
import retrofit2.http.QueryMap;

public interface ApiService {
    public static final String IP_ADDRESS = "192.168.18.113";
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
            @PartMap HashMap<String, RequestBody> map,
            @Part MultipartBody.Part imageFile
            );

    @GET("client/profile/{id}/{role}")
    Call<ResponseModel<UserModel>> getUserById(
            @Path("id") String userId,
            @Path("role") int role
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

    @GET("client/datafilter")
    Call<ResponseFilterModel> getDataFilter();



    @GET("client/filter")
    Call<ResponseModel<List<CarModel>>> carFilter(
            @QueryMap HashMap<String, String> map
    );

    @GET("admin/main")
    Call<ResponseModel<ChartModel>> getChart();


    @GET("admin/transaction/profit/download")
    Call<ResponseBody> downloadProfitPdf(
            @Query("admin_id") int adminId
    );

    @GET("admin/car/getDataCarComponents")
    Call<ResponseModel<CarComponentModel>> getCarComponents();

    @Multipart
    @POST("admin/car/store")
    Call<ResponseModel> storeCar(
            @PartMap Map<String, RequestBody> map,
            @Part List<MultipartBody.Part> partList
    );

    @DELETE("admin/car/destroy/{id}")
    Call<ResponseModel> destroyCar(
            @Path("id") int carId
    );

    @GET("admin/car/{id}")
    Call<ResponseModel<CarModel>> adminGetCarById(
            @Path("id") int carId
    );


    @Multipart
    @POST("admin/car/update/{id}")
    Call<ResponseModel> updateCar(
            @Path("id") int carId,
            @PartMap Map<String, RequestBody> map,
            @Part List<MultipartBody.Part> partList
    );

    @GET("admin/car/report/{id}")
    Call<ResponseBody> downloadCarReport(
            @Path("id") String id,
            @QueryMap HashMap<String, String> map
    );

    @GET("admin/transaction/all/{status}")
    Call<ResponseModel<ResponseAdminTransactionModel>> adminGetTransaction(
            @Path("status") int status
    );

    @GET("downloadfile/{type}/{filename}")
    Call<ResponseBody> downloadFileServer(
            @Path("type") String type,
            @Path("filename") String fileName
    );

    @DELETE("admin/transaction/destroy/{id}/{payment}")
    Call<ResponseModel> deleteTransaction(
            @Path("id") String transactionId,
            @Path("payment") int payment
    );

    @FormUrlEncoded
    @POST("admin/transaction/update/{transId}")
    Call<ResponseModel> updateStatusTransaction(
            @Path("transId") String transId,
            @FieldMap HashMap<String, String> map
    );


    @GET("admin/transaction/filter")
    Call<ResponseModel<List<TransactionModel>>> filterTransaction(
            @QueryMap HashMap<String, String> hashMap
    );


    @GET("admin/transaction/filter/download")
    Call<ResponseBody> downloadReportTransaction(
            @QueryMap HashMap<String, String> map
    );





}
