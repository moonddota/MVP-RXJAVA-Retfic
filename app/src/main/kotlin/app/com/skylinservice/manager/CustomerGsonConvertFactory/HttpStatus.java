package app.com.skylinservice.manager.CustomerGsonConvertFactory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuxuan on 2017/5/17.
 */

public class HttpStatus<T> {
    @SerializedName("data")
    private T mData;
    @SerializedName("msg")
    private String mMessage;

    @SerializedName("ret")
    private int mRet;

    @SerializedName("status")
    private int status;

    public T getmData() {
        return mData;
    }

    public String getmMessage() {
        return mMessage;
    }

    public int getmRet() {
        return mRet;
    }


    public int getStatus() {
        return status;
    }

    public class DataBean<T> {
        @SerializedName("code")
        private int mCode;
        @SerializedName("msg")
        private String mMsg;

        @SerializedName("info")
        private T mInfo;


        public int getmCode() {
            return mCode;
        }

        public String getmMsg() {
            return mMsg;
        }

        public T getmInfo() {
            return mInfo;
        }
    }


    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isRetInvalid() {
        return mRet != 200;
    }

    public boolean isDataInvalid() {
        if (mData instanceof DataBean) {
            if (mRet == 200 && ((DataBean) mData).mCode == 0)
                return false;
            else
                return true;
        } else if (mData instanceof Integer) {
            if (((Integer) mData) == 0)
                return false;
            else
                return true;

        } else if (mData instanceof List) {
            try {
                double ff = (double) ((List) mData).get(0);
                if (ff > 0)
                    return false;
                Double obj1 = new Double(ff);

                Double obj2 = new Double(1.0);

                int retval = obj1.compareTo(obj2);

                if (retval > 0) {

                    return true;

                } else if (retval == 0) {
                    return false;
                } else {
                    return true;
                }


            } catch (Exception eeee) {
                return false;
            }

        }

        return false;
    }
}
