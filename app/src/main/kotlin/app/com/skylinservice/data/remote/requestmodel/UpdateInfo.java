package app.com.skylinservice.data.remote.requestmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuxuan on 2017/6/6.
 */

public class UpdateInfo implements Parcelable {
    public String info;

    public String version;

    public String path;

    public String size;

    public String packageName;

    public String createTime;

    public long id;

    public int versionCode;


    public UpdateInfo() {

    }

//    protected UpdateInfo(Parcel in) {
//        info = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(info);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
//        @Override
//        public UpdateInfo createFromParcel(Parcel in) {
//            return new UpdateInfo(in);
//        }
//
//        @Override
//        public UpdateInfo[] newArray(int size) {
//            return new UpdateInfo[size];
//        }
//    };

    protected UpdateInfo(Parcel in) {
        info = in.readString();
        version = in.readString();
        path = in.readString();
        size = in.readString();
        packageName = in.readString();
        createTime = in.readString();
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(version);
        dest.writeString(path);
        dest.writeString(size);
        dest.writeString(packageName);
        dest.writeString(createTime);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
        @Override
        public UpdateInfo createFromParcel(Parcel in) {
            return new UpdateInfo(in);
        }

        @Override
        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };
}
