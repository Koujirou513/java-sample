// WeatherCondition.java (新規作成)
package com.example.myapp.enums;

public enum Weather{
    SUNNY(1, "晴れ", "☀️"),
    CLOUDY(2, "曇り", "☁️"), 
    RAINY(3, "雨", "☔️"),
    HEAVY_RAIN(4, "大雨", "⚡️"),
    SNOWY(5, "雪", "❄️");

    private final int code;
    private final String text;
    private final String icon;

    Weather(int code, String text, String icon) {
        this.code = code;
        this.text = text;
        this.icon = icon;
    }

    public int getCode() { return code; }
    public String getText() { return text; }
    public String getIcon() { return icon; }
    public String getDisplayText() { return icon + " " + text; }

    public static Weather fromCode(int code) {
        for (Weather condition : values()) {
            if (condition.code == code) {
                return condition;
            }
        }
        return null;
    }

    public static String getTextByCode(int code) {
        Weather condition = fromCode(code);
        return condition != null ? condition.text : "不明";
    }

    public static String getDisplayTextByCode(int code) {
        Weather condition = fromCode(code);
        return condition != null ? condition.getDisplayText() : "❓ 不明";
    }
}
