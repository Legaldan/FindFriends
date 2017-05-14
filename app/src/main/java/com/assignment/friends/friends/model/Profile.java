package com.assignment.friends.friends.model;

import java.util.Date;

/**
 * Created by Li Yan on 2017-05-06.
 */

public class Profile {
    private int id;
    private String firstName;
    private String surname;
    private String birth;
    private int gender;
    private String course;
    private int studyMode;
    private String address;
    private String suburb;
    private String nationality;
    private String nativeLanguage;
    private String favMovie;
    private String favUnit;
    private String favSport;
    private String currentJob;
    private String email;
    private String password;

    public Profile(){}

    public Profile(String firstName, String surname, String email, String password) {
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Profile(int id, String firstName, String surname, String birth, int gender, String course, int studyMode, String address, String suburb, String nationality, String nativeLanguage, String favMovie, String favUnit, String currentJob,String favSport, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.birth = birth;
        this.gender = gender;
        this.course = course;
        this.studyMode = studyMode;
        this.address = address;
        this.suburb = suburb;
        this.nationality = nationality;
        this.nativeLanguage = nativeLanguage;
        this.favMovie = favMovie;
        this.favUnit = favUnit;
        this.currentJob = currentJob;
        this.email = email;
        this.password = password;
        this.favSport = favSport;
    }

    public String getFavSport() {
        return favSport;
    }

    public void setFavSport(String favSport) {
        this.favSport = favSport;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setStudyMode(int studyMode) {
        this.studyMode = studyMode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public void setFavMovie(String favMovie) {
        this.favMovie = favMovie;
    }

    public void setFavUnit(String favUnit) {
        this.favUnit = favUnit;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirth() {
        return birth;
    }

    public int getGender() {
        return gender;
    }

    public String getCourse() {
        return course;
    }

    public int getStudyMode() {
        return studyMode;
    }

    public String getAddress() {
        return address;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getFavMovie() {
        return favMovie;
    }

    public String getFavUnit() {
        return favUnit;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
