package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.detailInfo.entity.DetailInfo;
import com.example.calenduck.domain.detailInfo.service.DetailInfoService;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.enumstring.AlarmString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserMyPage implements MyPageEmployee {

    private final BookmarkService bookmarkService;
    private final DetailInfoService detailInfoService;

    // 알람 전체 조회
    @Transactional
    @Override
    public List<String> getAlarms(User user) {
        String formattedCurrentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<String> alarmMessages = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkService.findBookmarks(user);

        for (Bookmark bookmark : bookmarks) {
            DetailInfo detailInfo = detailInfoService.findDetailInfo(bookmark.getMt20id());
            String prfnm = detailInfo.getPrfnm();
            String[] alarms = bookmark.getAlarm().split(",");

            for (String alarm : alarms) {
                String trimmedAlarm = alarm.trim();
                alarmMessages = checkAlarmMessage(formattedCurrentDate, trimmedAlarm, bookmark, prfnm);
            }
        }

        Collections.sort(alarmMessages, Comparator.comparing(this::getDaysDifferenceFromResult));
        return alarmMessages;
    }

    private List<String> checkAlarmMessage(String formattedCurrentDate, String trimmedAlarm, Bookmark bookmark, String prfnm) {
        List<String> alarmMessages = new ArrayList<>();

        if (trimmedAlarm.startsWith(formattedCurrentDate)) {
            int daysDifference = Integer.parseInt(bookmark.getReservationDate()) - Integer.parseInt(trimmedAlarm);
            switch (daysDifference) {
                case 1 -> alarmMessages.add(prfnm + AlarmString.A_DAY_BEFORE.getAlarm());
                case 3 -> alarmMessages.add(prfnm + AlarmString.THREE_DAY_BEFORE.getAlarm());
                case 7 -> alarmMessages.add(prfnm + AlarmString.A_WEEK_BEFORE.getAlarm());
            }
        }
        return alarmMessages;
    }

    private int getDaysDifferenceFromResult(String alarmMessage) {
        if (AlarmString.A_DAY_BEFORE.isContainedMessage(alarmMessage)) {
            return 1;
        } else if (AlarmString.THREE_DAY_BEFORE.isContainedMessage(alarmMessage)) {
            return 3;
        } else if (AlarmString.A_WEEK_BEFORE.isContainedMessage(alarmMessage)) {
            return 7;
        }
        return 0;
    }

}