package com.assignment.friends.friends.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by Li Yan on 2017-05-06.
 */

public class Friendship {
    private int id;
    @JSONField (format = "yyyy-MM-dd")
    private Date startTime;
    @JSONField (format = "yyyy-MM-dd")
    private Date endTime;
    private int stu1Id;
    private int stu2Id;

    public Friendship(Date startTime, Date endTime, int stu1Id, int stu2Id) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.stu1Id = stu1Id;
        this.stu2Id = stu2Id;
    }

    public int getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getStu1Id() {
        return stu1Id;
    }

    public int getStu2Id() {
        return stu2Id;
    }
}
