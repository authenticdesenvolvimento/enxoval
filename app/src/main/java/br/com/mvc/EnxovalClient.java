package br.com.mvc;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by robinson on 23/08/2016.
 */
public interface EnxovalClient {
    @GET("marcadores/listar")
    Call<List<Marcadores>> getMarcadores();
}
