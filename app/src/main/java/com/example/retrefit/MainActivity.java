package com.example.retrefit;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    String API_URL = "https://pixabay.com/";
    String q = "bad dog";
    String key = "16115131-f2ac4e59ef4204b7d06f11215";
    String image_type;
    Picasso picasso;
    ListView iv;
    ImageList adapter;
    ArrayList<Hit> huits = new ArrayList<>();
    Spinner spinner;


    interface PixabayAPI {
        @GET("/api") // метод запроса (POST/GET) и путь к API
            // пример содержимого веб-формы q=dogs+and+people&key=MYKEY&image_type=photo
        Call<Response> search(@Query("q") String q, @Query("key") String key, @Query("image_type") String image_type);
        // Тип ответа, действие, содержание запроса
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spin);
        picasso = new Picasso.Builder(this).build();
        iv = findViewById(R.id.image);
        adapter = new ImageList(this, huits, picasso);
        iv.setAdapter(adapter);

    }

    public void startSearch(String text) {
        // вызывается, когда пользователь вводит текст и нажимает кнопку поиска

        // создаём экземпляр службы для обращения к API
        // можно использовать экземпляр для нескольких API сразу
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL) // адрес API сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // создаём обработчик, определённый интерфейсом PixabayAPI выше
        PixabayAPI api = retrofit.create(PixabayAPI.class);

        // указываем, какую функцию API будем использовать
        Call<Response> call = api.search(text, key, image_type);

        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                // класс Response содердит поля, в которые будут записаны
                // результаты поиска по картинкам
                Response r = response.body(); // получили ответ в виде объекта
                displayResults(r.hits);
                Log.d("mytag", "hits:" + r.hits.length); // сколько картинок нашлось
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                // обрабатываем ошибку, если она возникла
                Log.d("mytag", "Error: " + t.getLocalizedMessage());
            }
        };
        call.enqueue(callback); // ставим запрос в очередь

    }

    public void displayResults(Hit[] hits) {
        // вызывается, когда появятся результаты поиска
//        picasso.load(hits[0].previewURL).into(iv);
        huits.clear();
        Collections.addAll(huits, hits);
        Log.d("mytag", String.valueOf(huits.size()));
        adapter.notifyDataSetChanged();
    }

    public void onSearchClick(View v) {
        EditText etSearch = findViewById(R.id.text);
        String text = etSearch.getText().toString();
        image_type = spinner.getSelectedItem().toString();
        startSearch(text);

    }
}