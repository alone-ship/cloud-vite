package com.example.AloneShip.controller;


import com.example.AloneShip.https.KwRequest;
import entitys.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KwController {
    @Resource
    private KwRequest kwRequest;

    @PostMapping("/ArtistBykeyWord")
    public Result<String> kw_ArtistBykeyWord(String key, String pn, String rn) throws Exception {
        return kwRequest.requestArtistBykeyWord(key, pn, rn);
    }

    @PostMapping("/Artist")
    public Result<String> kw_Artist(String category, String prefix) throws Exception {
        return kwRequest.requestArtist(category, prefix);
    }

    @PostMapping("/ArtistInfo")
    public Result<String> kw_ArtistInfo(String artistid) throws Exception {
        return kwRequest.requestArtistInfo(artistid);
    }

    @PostMapping("/ArtistMusic")
    public Result<String> kw_ArtistMusic(String artistid) throws Exception {
        return kwRequest.requestArtistMusic(artistid);
    }

    @PostMapping("/BangInfo")
    public Result<String> kw_BangInfo(String bangId, String pn, String rn) throws Exception {
        return kwRequest.requestBangInfo(bangId, pn, rn);
    }

    @GetMapping("/BangMenu")
    public Result<String> kw_BangMenu() throws Exception {
        return kwRequest.requestBangMenu();
    }

    @GetMapping("/ListTag")
    public Result<String> kw_ListTag() throws Exception {
        return kwRequest.requestListTags();
    }

    @PostMapping("/MusicBykeyWord")
    public Result<String> kw_MusicBykeyWord(String key, String pn, String rn) throws Exception {
        return kwRequest.requestMusicBykeyWord(key, pn, rn);
    }

    @PostMapping("/MusicUrl")
    public Result<String> kw_MusicUrl(String mid, String type) throws Exception {
        return kwRequest.requestMusicUrl(mid, type);
    }

    @PostMapping("/ListInfo")
    public Result<String> kw_ListInfo(String pid, String pn, String rn) throws Exception {
        return kwRequest.requestListInfo(pid, pn, rn);
    }

    @PostMapping("/SongInfo")
    public Result<String> kw_SongInfo(String musicId) throws Exception {
        return kwRequest.requestSongInfo(musicId);
    }

    @PostMapping("/TagList")
    public Result<String> kw_TagList(String id, String pn, String rn) throws Exception {
        return kwRequest.requestTagList(id, pn, rn);
    }

    @PostMapping("/OrderList")
    public Result<String> kw_OrderList(String order, String pn, String rn) throws Exception {
        return kwRequest.requestOrderList(order, pn, rn);
    }
}
