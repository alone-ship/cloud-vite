package entitys.exterior.KwData;

import entitys.exterior.requestBlock;
import entitys.utils.Result;

public interface KwBase {
    // SETTING
    String RequestType = "http";
    String Host = "www.kuwo.cn";
    String ContentType = "application/json";
    String Cookie = "kw_token=MJ4SSCLG84J";
    String csrf = "MJ4SSCLG84J";
    requestBlock OrderList = new requestBlock(
            "http://www.kuwo.cn/playlists",
            "/api/www/classify/playlist/getRcmPlayList"
    );
    Result<String> requestOrderList(String order, String pn, String rn) throws Exception;
    requestBlock ListTags = new requestBlock(
            "http://www.kuwo.cn/playlists",
            "/api/www/playlist/getTagList"
    );
    Result<String> requestListTags() throws Exception;
    requestBlock TagList = new requestBlock(
            "http://www.kuwo.cn/singers",
            "/api/www/classify/playlist/getTagPlayList"
    );
    Result<String> requestTagList(String id, String pn, String rn) throws Exception;
    requestBlock ListInfo = new requestBlock(
            "http://www.kuwo.cn/playlists",
            "/api/www/playlist/playListInfo"
    );
    Result<String> requestListInfo(String pid, String pn, String rn) throws Exception;
    requestBlock BangMenu = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/www/bang/bang/bangMenu"
    );
    Result<String> requestBangMenu() throws Exception;
    requestBlock BangInfo = new requestBlock(
            "http://www.kuwo.cn/rankList",
            "/api/www/bang/bang/musicList"
    );
    Result<String> requestBangInfo(String bangId, String pn, String rn) throws Exception;
    requestBlock Artist = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/www/artist/artistInfo"
    );
    Result<String> requestArtist(String category, String prefix) throws Exception;
    requestBlock ArtistInfo = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/www/artist/artist"
    );
    Result<String> requestArtistInfo(String artistId) throws Exception;
    requestBlock ArtistMusic = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/www/artist/artistMusic"
    );
    Result<String> requestArtistMusic(String artistId) throws Exception;
    requestBlock MusicBykeyWord = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/www/search/searchMusicBykeyWord"
    );
    Result<String> requestMusicBykeyWord(String key, String pn, String rn) throws Exception;
    requestBlock SongInfo = new requestBlock(
            "http://www.kuwo.cn/",
            "/newh5/singles/songinfoandlrc"
    );
    Result<String> requestSongInfo(String musicId) throws Exception;
    requestBlock MusicUrl = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/v1/www/music/playUrl"
    );
    Result<String> requestMusicUrl(String mid, String type) throws Exception;
    requestBlock ArtistBykeyWord = new requestBlock(
            "http://www.kuwo.cn/",
            "/api/www/search/searchArtistBykeyWord"
    );
    Result<String> requestArtistBykeyWord(String key, String pn, String rn) throws Exception;
}
