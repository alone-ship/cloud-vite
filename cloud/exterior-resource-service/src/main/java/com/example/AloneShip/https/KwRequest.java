package com.example.AloneShip.https;

import entitys.exterior.KwData.KwBase;
import entitys.utils.Result;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@Component
public class KwRequest implements KwBase {
    @Override
    public Result<String> requestOrderList(String order, String pn, String rn) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, OrderList.getPath());
        String url = "%s?order=%s&pn=%s&rn=%s".formatted(baseUrl, order, pn, rn);
        return request(url,OrderList.getReferer());
    }

    @Override
    public Result<String> requestListTags() throws Exception {
        String url = "%s://%s%s".formatted(RequestType,Host, ListTags.getPath());
        return request(url,ListTags.getReferer());
    }

    @Override
    public Result<String> requestTagList(String id, String pn, String rn) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, TagList.getPath());
        String url = "%s?id=%s&pn=%s&rn=%s".formatted(baseUrl, id, pn, rn);
        return request(url,OrderList.getReferer());
    }

    @Override
    public Result<String> requestListInfo(String pid, String pn, String rn) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, ListInfo.getPath());
        String url = "%s?pid=%s&pn=%s&rn=%s".formatted(baseUrl, pid, pn, rn);
        return request(url,OrderList.getReferer());
    }

    @Override
    public Result<String> requestBangMenu() throws Exception {
        String url = "%s://%s%s".formatted(RequestType,Host, BangMenu.getPath());
        return request(url,BangMenu.getReferer());
    }

    @Override
    public Result<String> requestBangInfo(String bangId, String pn, String rn) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, BangInfo.getPath());
        String url = "%s?bangId=%s&pn=%s&rn=%s".formatted(baseUrl, bangId, pn, rn);
        return request(url,BangInfo.getReferer());
    }

    @Override
    public Result<String> requestArtist(String category, String prefix) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, Artist.getPath());
        String url = "%s?category=%s&prefix=%s".formatted(baseUrl, category, prefix);
        return request(url,Artist.getReferer());
    }

    @Override
    public Result<String> requestArtistInfo(String artistId) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, ArtistInfo.getPath());
        String url = "%s?artistId=%s".formatted(baseUrl, artistId);
        return request(url,ArtistInfo.getReferer());
    }

    @Override
    public Result<String> requestArtistMusic(String artistId) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, ArtistMusic.getPath());
        String url = "%s?artistId=%s".formatted(baseUrl, artistId);
        return request(url,ArtistMusic.getReferer());
    }

    @Override
    public Result<String> requestMusicBykeyWord(String key, String pn, String rn) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, MusicBykeyWord.getPath());
        String url = "%s?key=%s&pn=%s&rn=%s".formatted(baseUrl, key, pn, rn);
        return request(url,MusicBykeyWord.getReferer());
    }

    @Override
    public Result<String> requestSongInfo(String musicId) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,"m.kuwo.cn", SongInfo.getPath());
        String url = "%s?musicId=%s".formatted(baseUrl, musicId);
        return request(url,SongInfo.getReferer());
    }

    @Override
    public Result<String> requestMusicUrl(String mid, String type) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, MusicUrl.getPath());
        String url = "%s?mid=%s&type=%s".formatted(baseUrl, mid, type);
        return request(url,MusicUrl.getReferer()+mid);
    }

    @Override
    public Result<String> requestArtistBykeyWord(String key, String pn, String rn) throws Exception {
        String baseUrl = "%s://%s%s".formatted(RequestType,Host, ArtistBykeyWord.getPath());
        String url = "%s?key=%s&pn=%s&rn=%s".formatted(baseUrl, key, pn, rn);
        return request(url,ArtistBykeyWord.getReferer());
    }

    private Result<String> request(String url,String referer) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // 设置请求方法为GET
        con.setRequestMethod("GET");
        // 设置请求头
        con.setRequestProperty("Content-Type", ContentType);
        con.setRequestProperty("Cookie", Cookie);
        con.setRequestProperty("csrf", csrf);
        if(!Objects.equals(referer, "")&&referer!=null) con.setRequestProperty("Referer", referer);
        // 获取响应码
        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            // 读取响应体
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new Result<>(200,"SUCCESS",response.toString());
        }else{
            return new Result<>(400,"FAILURE",con.getResponseMessage());
        }
    }
}
