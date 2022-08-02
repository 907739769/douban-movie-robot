package cn.jackding.doubanmovierobot.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Jack
 * @Date 2022/8/2 23:26
 * @Version 1.0.0
 */
@NoArgsConstructor
@Data
public class Movieinfo {


    @JSONField(name = "title")
    private String title;
    @JSONField(name = "originalTitle")
    private String originalTitle;
    @JSONField(name = "originalLanguage")
    private OriginalLanguageDTO originalLanguage;
    @JSONField(name = "alternateTitles")
    private List<?> alternateTitles;
    @JSONField(name = "secondaryYearSourceId")
    private Integer secondaryYearSourceId;
    @JSONField(name = "sortTitle")
    private String sortTitle;
    @JSONField(name = "sizeOnDisk")
    private Integer sizeOnDisk;
    @JSONField(name = "status")
    private String status;
    @JSONField(name = "overview")
    private String overview;
    @JSONField(name = "images")
    private List<ImagesDTO> images;
    @JSONField(name = "website")
    private String website;
    @JSONField(name = "remotePoster")
    private String remotePoster;
    @JSONField(name = "year")
    private Integer year;
    @JSONField(name = "hasFile")
    private Boolean hasFile;
    @JSONField(name = "youTubeTrailerId")
    private String youTubeTrailerId;
    @JSONField(name = "studio")
    private String studio;
    @JSONField(name = "qualityProfileId")
    private Integer qualityProfileId;
    @JSONField(name = "monitored")
    private Boolean monitored;
    @JSONField(name = "minimumAvailability")
    private String minimumAvailability;
    @JSONField(name = "isAvailable")
    private Boolean isAvailable;
    @JSONField(name = "folderName")
    private String folderName;
    @JSONField(name = "runtime")
    private Integer runtime;
    @JSONField(name = "cleanTitle")
    private String cleanTitle;
    @JSONField(name = "imdbId")
    private String imdbId;
    @JSONField(name = "tmdbId")
    private Integer tmdbId;
    @JSONField(name = "titleSlug")
    private String titleSlug;
    @JSONField(name = "folder")
    private String folder;
    @JSONField(name = "genres")
    private List<String> genres;
    @JSONField(name = "tags")
    private List<?> tags;
    @JSONField(name = "added")
    private String added;
    @JSONField(name = "ratings")
    private RatingsDTO ratings;

    @NoArgsConstructor
    @Data
    public static class OriginalLanguageDTO {
        @JSONField(name = "id")
        private Integer id;
        @JSONField(name = "name")
        private String name;
    }

    @NoArgsConstructor
    @Data
    public static class RatingsDTO {
        @JSONField(name = "imdb")
        private ImdbDTO imdb;
        @JSONField(name = "tmdb")
        private TmdbDTO tmdb;
        @JSONField(name = "rottenTomatoes")
        private RottenTomatoesDTO rottenTomatoes;

        @NoArgsConstructor
        @Data
        public static class ImdbDTO {
            @JSONField(name = "votes")
            private Integer votes;
            @JSONField(name = "value")
            private Double value;
            @JSONField(name = "type")
            private String type;
        }

        @NoArgsConstructor
        @Data
        public static class TmdbDTO {
            @JSONField(name = "votes")
            private Integer votes;
            @JSONField(name = "value")
            private Integer value;
            @JSONField(name = "type")
            private String type;
        }

        @NoArgsConstructor
        @Data
        public static class RottenTomatoesDTO {
            @JSONField(name = "votes")
            private Integer votes;
            @JSONField(name = "value")
            private Integer value;
            @JSONField(name = "type")
            private String type;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ImagesDTO {
        @JSONField(name = "coverType")
        private String coverType;
        @JSONField(name = "url")
        private String url;
        @JSONField(name = "remoteUrl")
        private String remoteUrl;
    }
}
