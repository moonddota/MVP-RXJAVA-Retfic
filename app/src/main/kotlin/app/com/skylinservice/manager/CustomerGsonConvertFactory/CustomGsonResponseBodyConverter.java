package app.com.skylinservice.manager.CustomerGsonConvertFactory;

import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by liuxuan on 2017/5/17.
 */

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        HttpStatus httpStatus = gson.fromJson(response, HttpStatus.class);

        if (httpStatus.getStatus() == 200 || httpStatus.getmRet() == 200)

        {
            value.close();

        } else if (httpStatus.isRetInvalid()) {
            value.close();
            throw new ApiException(httpStatus.getmRet(), httpStatus.getmMessage());
        } else {
            try {
                HttpStatus<HttpStatus.DataBean> httpDataBean = gson.fromJson(response, new TypeToken<HttpStatus<HttpStatus.DataBean>>() {
                }.getType());
                if (httpDataBean.isDataInvalid()) {
                    value.close();
                    throw new ApiException(httpStatus.getmRet(), httpStatus.getmMessage());
                }
            } catch (JsonSyntaxException je) {

                try {
                    HttpStatus<Integer> httpDataBean = gson.fromJson(response, new TypeToken<HttpStatus<Integer>>() {
                    }.getType());
                    if (httpDataBean.isDataInvalid()) {
                        value.close();
                        throw new ApiException(httpStatus.getmRet(), httpStatus.getmMessage());
                    }
                } catch (JsonSyntaxException jee) {
                    HttpStatus<List> httpDataBean = gson.fromJson(response, new TypeToken<HttpStatus<List>>() {
                    }.getType());
                    if (httpDataBean.isDataInvalid()) {
                        value.close();
                        throw new ApiException(httpStatus.getmRet(), httpStatus.getmMessage());
                    }
                }
            } catch (Exception ee) {
                value.close();
                throw new ApiException(httpStatus.getmRet(), ee.getMessage());
            }

        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);

        }
//        catch (Exception ee)
//        {
//            throw new ApiException(httpStatus.getmRet(), ee.getMessage());
//        }
        finally {
            value.close();
        }
    }
}