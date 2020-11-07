package Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Meeting implements Parcelable{

    private long id;
    private String name;
    private int avatarColor;
    private Room location;
    private Date beginTime;
    private Date endTime;
    private List<Delegate> delegates;
    private String info;

    /**
     * constructor
     */
    public Meeting(long id, String name, int avatarColor, Room location, Date beginTime, Date endTime, List<Delegate> delegates, String info) {
        this.id = id;
        this.name = name;
        this.avatarColor = avatarColor;
        this.location = location;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.delegates = delegates;
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatarColor() {
        return avatarColor;
    }

    public void setAvatarColor(int avatarColor) {
        this.avatarColor = avatarColor;
    }

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Delegate> getDelegates() {
       return delegates;
   }

    public void setDelegates(List<Delegate> delegates) {
        this.delegates = delegates;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeLong(id);
       dest.writeString(name);
       dest.writeInt(avatarColor);
       dest.writeParcelable(location, flags);
       dest.writeLong(beginTime.getTime());
       dest.writeLong(endTime.getTime());
       dest.writeParcelableList (delegates, flags);
       dest.writeString(info);
    }

    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    private Meeting(Parcel in) {
     this. id = in.readLong();
     this. name = in.readString();
     this. avatarColor = in.readInt();
     this. location = in.readParcelable(Room.class.getClassLoader());
     this. beginTime = new Date (in.readLong());
     this. endTime = new Date (in.readLong());
     this. delegates = in.readParcelableList(new ArrayList<Delegate>(), Delegate.class.getClassLoader());
     this. info = in.readString();
    }

}
