package com.example.datereminder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;

public class Event implements Parcelable {
    private int id;
    private String name;
    private Long eventDate;
    private Boolean willRemind;

    public Event(String name, Long eventDate, Boolean willRemind) {
        this.name = name;
        this.eventDate = eventDate;
        this.willRemind = willRemind;
    }

    public Event(int id, String name, Long eventDate, Boolean willRemind) {
        this. id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.willRemind = willRemind;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            eventDate = null;
        } else {
            eventDate = in.readLong();
        }
        byte tmpWillRemind = in.readByte();
        willRemind = tmpWillRemind == 0 ? null : tmpWillRemind == 1;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEventDate() {
        return eventDate;
    }

    public void setEventDate(Long eventDate) {
        this.eventDate = eventDate;
    }

    public Boolean getWillRemind() {
        return willRemind;
    }

    public void setWillRemind(Boolean willRemind) {
        this.willRemind = willRemind;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        if (eventDate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(eventDate);
        }
        parcel.writeByte((byte) (willRemind == null ? 0 : willRemind ? 1 : 2));
    }
}
