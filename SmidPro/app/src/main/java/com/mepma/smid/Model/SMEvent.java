package com.mepma.smid.Model;

import com.mepma.smid.Util.Const;
import com.mepma.smid.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SMEvent {

    public String mettingId = "";
    public String meetingDescription = "";
    public String cdsCode = "";
    public String attendedCount = "";
    public String meetingStatus = "";
    public String lastMeetDetails = "";
    public String meetingDate = "";
    public String cdsName = "";
    public String ulbId = "";
    public String districtId = "";


    static public List<SMEvent> parseList(JSONObject object) {

        try {
            JSONArray arr = object.getJSONArray(Const.Params.data);
            List<SMEvent> list = new ArrayList<SMEvent>();
            for ( int i=0; i< arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                SMEvent eventInfo = SMEvent.frameObject(obj);
                if (eventInfo != null) {
                    list.add(eventInfo);
                }
            }

            return list;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    static SMEvent frameObject(JSONObject object) {

        SMEvent memberInfo = new SMEvent();
        memberInfo.mettingId = Util.optString(object, Const.Params.meetingId);
        memberInfo.meetingDescription = Util.optString(object,Const.Params.meetingDescription);
        memberInfo.meetingStatus = Util.optString(object,Const.Params.meetingStatus);
        memberInfo.meetingDate = Util.optString(object,Const.Params.meetingDate);
        memberInfo.lastMeetDetails = Util.optString(object,Const.Params.lastMeetingDetails);
        memberInfo.attendedCount = Util.optString(object,Const.Params.attendanceCount);

        memberInfo.cdsCode = Util.optString(object,Const.Params.cdsCode);
        memberInfo.cdsName = Util.optString(object,Const.Params.cdsName);
        memberInfo.ulbId = Util.optString(object,Const.Params.ulbId);
        memberInfo.districtId = Util.optString(object,Const.Params.districtId);


        return memberInfo;

    }


}
