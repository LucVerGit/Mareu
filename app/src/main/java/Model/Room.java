package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {

    /** Id */
    private long id;

    /** Email */
    private String room;

    /**
     * Constructor
     */
    public Room(long id, String room) {
        this.id = id;
        this.room = room;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(room);
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    private Room(Parcel in) {
        this.id = in.readLong();
        this.room = in.readString();
    }
}
