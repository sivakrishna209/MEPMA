package com.mepma.smid.Util;

//public class Const {
//
//    static String baseURL = "http://fab-tech.co.in/befitnepalapi/api/";
//
//
//    static class Keys {
//        static String exerciseId = "exercise_id";
//    }
//}


import android.graphics.Color;
import android.support.annotation.IntDef;

import com.mepma.smid.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public class Const {

    static public class Url {
        static public String base = "https://smid.tmepma.in/";

        static public String api = "api/";

        static public String login = "login";
        static public String logout = "logout";
        static public String register = "register";

        static public String events = "events";
        static public String eventInfo = "event";
        static public String update = "update";
        static public String tlfDetails = "tlfdetails";
        static public String tlfMembers = "tlfmembers";
        static public String corpusUpdate = "addcorpus";


        static public String fullPath(String path) {
            return Url.base + Url.api + path;
        }
        static public String fullPath(String path, String idComponent) {
            return Const.Url.fullPath(path) + "/" + idComponent;
        }

    }


    static public class Params {

        static public String token = "token";
        static public String status = "status";
        static public String data = "data";
        static public String message = "message";

        static public String mobileNumber = "mobile_no";
        static public String password = "password";
        static public String confirmPassword = "c_password";

        static public String age = "age";

        static public  String error = "error";
        static public  String success = "success";

        // Profile
        static public String address = "address";
        static public String bodyType = "body_type";
        static public String city = "city";
        static public String createdDate = "created_at";
        static public String email = "email";
        static public String emergencyContactName = "emergency_contact_name";
        static public String emergencyNumber = "emergency_number";
        static public String flage = "flag";
        static public String foodPreference = "food_prefernce";
        static public String goal = "goal";

        static public String uid = "id";
        static public String name = "name";
        static public String phoneNumber = "number";
        static public String numberRelation = "number_relation";
        static public String medicalCondition = "medical_condition";

        static public String createdAt = "created_at";

        // Events or Meeting
        static public String meetingId ="meetingId" ;
        static public String meetingStatus = "meetingStatus";
        static public String meetingImage = "meetingImage";
        static public String lastMeetingDetails = "last_meeting_details";
        static public String meetingDate = "meetingDate";
        static public String meetingDate2 = "meetingDate2";
        static public String activeDate = "activeDate";
        static public String meetingDescription = "meetingDescription";
        static public String cdsCode = "cdscode";
        static public String cdsName = "cdsname";
        static public String ulbId = "ulb_id";
        static public String districtId = "dist_id";
        static public String attendanceCount = "attendance_count";
        static public String startTime = "start_time";
        static public String endTime = "end_time";

        static public String latitude = "lat";
        static public String longitude = "longt";

        static public String memberName = "member_name";

        static public String savings = "savings";
        static public String lending = "lending";
        static public String lendingReturn= "return";
        static public String fundAmount= "fund_amount";
        static public String fundReceiverName = "fundname";
        static public String expenditure = "expenditure";
        static public String corpusBalance = "balance";



    }


    static public class Value {

        static public String programId = "2";
        static public String menuListSeparator = "#1#";


        static public class Username {
            static public Integer min = 4;
            static public Integer max = 20;

            static public boolean isInRange(Integer value) {
                return value >= min && value <= max;
            }
        }


        static public class Password {
            static public Integer min = 3;
            static public Integer max = 12;

            static public boolean isInRange(Integer value) {
                return value >= min && value <= max;
            }
        }

        static public class Mobile {
            static public Integer length = 10;
            static public String pattern = "### ### ####";
        }


        static public class HttpHeader {
            static public String accept = "Accept";
            static public String appJson = "application/json";
            static public String authorization = "Authorization";
            static public String Bearer = "Bearer ";
            static public String contentType = "Content-type";
            static public String multipartForm = "multipart/form-data";


        }

    }

    static public class TabOrder {

        static public class Home {
            public static final int Events = 0;
            public static final int Meetings = 1;
        }
    }

    static public enum MeetingStatus {
        proposed(0),
        completed(1),
        cancelled(2);

        private int value;
        private static Map map = new HashMap<>();

        private MeetingStatus(int value) {
            this.value = value;
        }

        static {
            for (MeetingStatus statusType : MeetingStatus.values()) {
                map.put(statusType.value, statusType);
            }
        }

        public static MeetingStatus valueOf(int statusType) {
            return (MeetingStatus) map.get(statusType);
        }

        public int getValue() {
            return value;
        }


        static public String stringValueFromInt(Integer index) {
            String str = proposed.toString();
            switch (index) {
                case 0:
                    str = proposed.toString();
                    break;
                case 1:
                    str = completed.toString();
                    break;
                case 2:
                    str = cancelled.toString();
                    break;
            }
            return str;
        }

        static public int color(Integer index) {
            int color = Color.GREEN;
            switch (index) {
                case 0:
                    color = Color.GREEN;
                    break;
                case 1:
                    color = Color.GRAY;
                    break;
                case 2:
                    color =  Color.RED;
                    break;
            }
            return color;
        }
    }

    static public enum ChangeMeetingStatus {
        yes(0),
        cancelled(1),
        notyet(2);

        private int value;
        private static Map map = new HashMap<>();

        private ChangeMeetingStatus(int value) {
            this.value = value;
        }

        static {
            for (ChangeMeetingStatus statusType : ChangeMeetingStatus.values()) {
                map.put(statusType.value, statusType);
            }
        }

        public static ChangeMeetingStatus valueOf(int statusType) {
            return (ChangeMeetingStatus) map.get(statusType);
        }

        public int getValue() {
            return value;
        }

    }

        public enum PageType {
        ABOUT(1),
        CODING(2),
        DATABASES(3);

        private int value;
        private static Map map = new HashMap<>();

        private PageType(int value) {
            this.value = value;
        }

        static {
            for (PageType pageType : PageType.values()) {
                map.put(pageType.value, pageType);
            }
        }

        public static PageType valueOf(int pageType) {
            return (PageType) map.get(pageType);
        }

        public int getValue() {
            return value;
        }
    }


}
