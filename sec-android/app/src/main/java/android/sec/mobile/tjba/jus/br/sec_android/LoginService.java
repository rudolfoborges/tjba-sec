package android.sec.mobile.tjba.jus.br.sec_android;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by rudolfoborges on 19/04/17.
 */

public interface LoginService {

    @POST("/v1/login")
    Call<ResponseBody> login();


}
