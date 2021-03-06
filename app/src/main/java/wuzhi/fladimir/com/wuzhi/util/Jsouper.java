package wuzhi.fladimir.com.wuzhi.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import wuzhi.fladimir.com.wuzhi.model.entity.Now;

/**
 * Created by Sc_Ji on 2018-01-03.
 * Jsoup Util
 */

public class Jsouper {
    private static String Url_LastDiary = "https://wuzhi.me/last";
    private static String Url_UserDiary = "https://wuzhi.me/u/";

    public static ArrayList<Now> getLastDiary() {
        ArrayList<Now> mNows = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Url_LastDiary).get();
            Elements table = doc.getElementsByTag("table");
            Element tbody = table.get(0);
            Elements trs = tbody.getElementsByTag("tr");
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                for (Element td : tds) {
                    Now now = new Now();
                    String id = td.getElementsByTag("a").get(0).attr("href");
                    now.setUserId(id.substring(3, id.length()));
                    Element img = td.getElementsByTag("span").get(0).getElementsByTag("img").get(0);
                    now.setUserImg(img.attr("src"));
                    now.setUserName(img.attr("alt"));
                    mNows.add(now);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mNows;
    }

    /**
     * 获取内容(电脑端)
     *
     * @return 255148
     */
    public static Now getCompleteNovel_Pc(String html) throws Exception {
        Now now = new Now();
        ArrayList<Now.diary> novelList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        String sign;
        try {
            sign = doc.getElementsByClass("quote").get(0)
                    .getElementsByTag("span").get(0).text();
        } catch (Exception e) {
            sign = "";
        }
        String img = doc.getElementsByClass("img_shadow").get(0)
                .getElementsByTag("img").get(0).attr("src");
        Element content = doc.getElementsByClass("main_right").get(0);
        Elements dateInfo = content.getElementsByTag("span");
        String date = dateInfo.get(0).text();
        String flower = dateInfo.get(1).text();
        for (Element noteEach : content.getElementsByClass("note_each")) {
            Now.diary novel = new Now.diary();
            novel.setDiaryContent(noteEach.getElementsByTag("div").get(0).text());
            novel.setDiaryTime(noteEach.getElementsByTag("div").get(1).text());
            novelList.add(novel);
        }

        String[] s = html.split("/");
        Logger.e("url-->" + html + "\n" + "id-->" + s[s.length - 1]);
        now.setUserId(s[s.length - 1]);
        now.setUserName(doc.title());
        now.setUserImg(img);
        now.setDate(date);
        now.setFlowers(flower);
        now.setDiary(novelList);
        now.setUserSign(sign);
        return now;
    }

    /**
     * 手机端
     *
     * @param userId
     * @param html
     * @return
     * @throws Exception
     */
    public static Now getCompleteNovel_Android(String userId, String html) throws Exception {
        Now now = new Now();
        ArrayList<Now.diary> novelList = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        String userName = doc.getElementsByClass("img_shadow").get(0)
                .getElementsByTag("img").get(0).attr("alt");
        String userImg = doc.getElementsByClass("img_shadow").get(0)
                .getElementsByTag("img").attr("src");
        String userSign = doc.getElementsByClass("quote_text").text();
        String date = doc.getElementsByClass("date_line").get(0)
                .getElementsByTag("span").get(0).text();
        String flower = doc.getElementsByClass("date_line").get(0)
                .getElementsByTag("span").get(1).text();
        Elements novels = doc.getElementsByClass("note_each");
        for (Element each : novels) {
            Now.diary novel = new Now.diary();
            novel.setDiaryTime(each.getElementsByClass("note_time").get(0).text());
            novel.setDiaryContent(each.getElementsByClass("note_content").get(0).text());
            novelList.add(novel);
        }
        now.setUserName(userName);
        now.setDate(date);
        now.setFlowers(flower);
        now.setDiary(novelList);
        now.setUserId(userId);
        now.setUserImg(userImg);
        now.setUserSign(userSign);
        return now;
    }
}
