package com.example.admin.animewatchmaster.Jsoup;

import android.content.Context;

import com.example.admin.animewatchmaster.databaseUtils.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 6/10/2016.
 */
public class JsoupDataImport {

    public static ArrayList<Object> getWatchlistInfo(Context context){

        DBHelper dbinstance = DBHelper.getInstance(context);

        ArrayList<Object> returndata = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Integer> episodes = new ArrayList<>();
        ArrayList<String> lastupdated = new ArrayList<>();
        ArrayList<String> titlesnew = new ArrayList<>();
        ArrayList<Integer> episodesnew = new ArrayList<>();
        ArrayList<String> lastupdatednew = new ArrayList<>();

        Document doc = null;

        try {
            doc = Jsoup.connect("http://www.animefreak.tv/tracker").timeout(120*1000).get();
            Elements tbody = doc.getElementsByTag("tbody");
            Element ttbody = tbody.first();
            Elements links = ttbody.getElementsByTag("a");
            Elements ems = ttbody.getElementsByTag("em");
            for(int i=0; i<links.size();i++){
                data.add(links.get(i).text());
                data2.add(ems.get(i).text());
            }
            doc = Jsoup.connect("http://www.animefreak.tv/tracker?page=1").timeout(120*1000).get();
            tbody = doc.getElementsByTag("tbody");
            ttbody = tbody.first();
            links = ttbody.getElementsByTag("a");
            ems = ttbody.getElementsByTag("em");
            for(int i=0; i<links.size();i++){
                data.add(links.get(i).text());
                data2.add(ems.get(i).text());
            }
            doc = Jsoup.connect("http://www.animefreak.tv/tracker?page=2").timeout(120*1000).get();
            tbody = doc.getElementsByTag("tbody");
            ttbody = tbody.first();
            links = ttbody.getElementsByTag("a");
            ems = ttbody.getElementsByTag("em");
            for(int i=0; i<links.size();i++){
                data.add(links.get(i).text());
                data2.add(ems.get(i).text());
            }

            //divide titles from episodes
            int g=0;
            for(String line : data){
                int spaceamount = 0;
                for(int i=0; i<line.length()-1;i++){
                    if(line.charAt(i)==' ')
                        spaceamount++;
                }
                int spacecount = 0;
                int discardnum = spaceamount;
                String tit = "";
                String ep = "";
                int j=0;
                boolean foundep = false;

                for (int i = 0; i < spaceamount + 1; i++) {
                    if (!foundep) {
                        if (i != discardnum - 1) {
                            while (j<line.length()&&line.charAt(j) != ' ') {
                                tit += line.charAt(j);
                                j++;
                            }
                            if (i != discardnum - 2)
                                tit += ' ';
                            j++;
                        } else {
                            while (line.charAt(j) != ' ')
                                j++;
                            j++;
                            foundep = true;
                        }
                    } else {
                        for (int k = j; k < line.length(); k++) {
                            ep += line.charAt(k);
                        }
                    }
                }


                try{
                    episodes.add(Integer.parseInt(ep));
                    titles.add(tit);
                    lastupdated.add(data2.get(g));
                    g++;
                }catch (NumberFormatException e) {
                    g++;
                }
            }
            for(int i=0;i<titles.size();i++){
                String t = titles.get(i);
                if(t.equals("Gangsta"))
                    t += ".";
                if(dbinstance.checkIfExistsInWatchlist(t)){
                    titlesnew.add(t);
                    episodesnew.add(episodes.get(i));
                    lastupdatednew.add(lastupdated.get(i));
                }
            }
            titles = new ArrayList<>();
            episodes = new ArrayList<>();
            lastupdated = new ArrayList<>();
            ArrayList<String> testti = new ArrayList<>();
            testti.add("yolo");
            for(int i=0; i<titlesnew.size();i++){
                int max=episodesnew.get(i);
                String ti = titlesnew.get(i);
                String la = lastupdatednew.get(i);
                boolean check = false;
                for(String stt : testti){
                    if(ti.equals(stt))
                        check=true;
                }
                if(!check) {
                    for (int j = i; j < titlesnew.size(); j++) {
                        if (ti.equals(titlesnew.get(j))) {
                            if (episodesnew.get(j) > max) {
                                max = episodesnew.get(j);
                            }
                        }
                    }
                    titles.add(ti);
                    testti.add(ti);
                    lastupdated.add(la);
                    episodes.add(max);
                }
            }
            returndata.add(titles);
            returndata.add(episodes);
            returndata.add(lastupdated);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return returndata;
    }
}
