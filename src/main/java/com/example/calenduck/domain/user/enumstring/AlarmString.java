package com.example.calenduck.domain.user.enumstring;

public enum AlarmString {
    A_DAY_BEFORE("1일전"),
    THREE_DAY_BEFORE("3일전"),
    A_WEEK_BEFORE("7일전");

    private final String alarm;

    AlarmString(String alarm) {
        this.alarm = alarm;
    }

    public String getAlarm() {
        return " 공연이 " + alarm + "입니다.";
    }

    public boolean isContainedMessage(String alarmMessage) {
        return alarmMessage.contains(this.alarm);
    }

}
