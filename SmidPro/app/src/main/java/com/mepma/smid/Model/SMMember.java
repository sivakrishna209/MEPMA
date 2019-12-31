package com.mepma.smid.Model;

import com.mepma.smid.Util.Const;
import com.mepma.smid.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SMMember {

    String id = "";
    String name = "";
    String designation = "";


    static public List<SMMember> parseList(JSONObject object) {

        try {
            JSONArray arr = object.getJSONArray(Const.Params.data);
            List<SMMember> list = new ArrayList<SMMember>();
            for ( int i=0; i< arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                SMMember memberInfo = SMMember.frameObject(obj);
                if (memberInfo != null) {
                    list.add(memberInfo);
                }
            }

            return list;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    static SMMember frameObject(JSONObject object) {

        SMMember memberInfo = new SMMember();
        memberInfo.id = Util.optString(object, Const.Params.uid);
        memberInfo.name = Util.optString(object,Const.Params.memberName);
        return memberInfo;

    }

    static public String[] extractNames(List<SMMember> members) {
//        ArrayList<String> namesList = new ArrayList<String>();
        String[] namesList = new String[members.size()];
        Iterator<SMMember> itr = members.iterator();
        int i= 0;
        while (itr.hasNext()) {
            namesList[i] = itr.next().name;
            i++;
        }
        return namesList;
    }

    static public String[] extractIds(List<SMMember> members) {
        String[] idsList = new String[members.size()];
        Iterator<SMMember> itr = members.iterator();
        int i= 0;
        while (itr.hasNext()) {
            idsList[i] = itr.next().id;
            i++;
        }
        return idsList;
    }



    }
