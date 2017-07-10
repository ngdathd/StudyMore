package com.ngdat.studymore.models.user;

public class UserInfor {

    private String phone;
    private String email;
    private String local;
    private String birthday;
    private String gender;
    private String edu;
    private String favor;
    private String key;

    public UserInfor() {
    }

    public UserInfor(String phone,
                     String email,
                     String local,
                     String birthday,
                     String gender,
                     String edu,
                     String favor,
                     String key) {
        this.phone = phone;
        this.email = email;
        this.local = local;
        this.birthday = birthday;
        this.gender = gender;
        this.edu = edu;
        this.favor = favor;
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getFavor() {
        return favor;
    }

    public void setFavor(String favor) {
        this.favor = favor;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
