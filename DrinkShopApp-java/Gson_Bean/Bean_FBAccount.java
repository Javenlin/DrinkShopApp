package com.osiog.myoldmancare.Gson_Bean;

import java.util.ArrayList;

/**
 * Created by OSIOG on 2018/6/20.
 */

public class Bean_FBAccount {

    public ArrayList<FBAccount> fbaccount;

    public ArrayList<FBAccount> getFbaccount() {
        return fbaccount;
    }

    public void setFbaccount(ArrayList<FBAccount> fbaccount) {
        this.fbaccount = fbaccount;
    }

    public class FBAccount {

        public String id;
        public String link;
        public String time;
        public String name;
        public String email;
        public String gender;
        public String birthday;
        public String picture;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }


    }


}
