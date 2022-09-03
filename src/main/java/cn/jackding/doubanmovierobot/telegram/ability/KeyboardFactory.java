package cn.jackding.doubanmovierobot.telegram.ability;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboard movieButtons(JSONArray movieJsonArray) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Object json : movieJsonArray) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String title = json1.getString("title");
            String year = json1.getString("year");
            String imdbId = json1.getString("imdbId");
            if (StringUtils.isBlank(imdbId) || StringUtils.isBlank(title)) {
                continue;
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(InlineKeyboardButton.builder().text(title + "(" + year + ")").callbackData(imdbId).build());
            rowsInline.add(rowInline);
        }
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public static ReplyKeyboard seriesButtons(JSONArray seriesJsonArray) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (Object json : seriesJsonArray) {
            JSONObject json1 = JSON.parseObject(json.toString());
            String title = json1.getString("title");
            String year = json1.getString("year");
            String tvdbId = json1.getString("tvdbId");
            if (StringUtils.isBlank(tvdbId) || StringUtils.isBlank(title)) {
                continue;
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(InlineKeyboardButton.builder().text(title + "(" + year + ")").callbackData(tvdbId).build());
            rowsInline.add(rowInline);
        }
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

}
