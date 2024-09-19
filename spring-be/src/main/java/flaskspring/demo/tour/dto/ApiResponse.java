package flaskspring.demo.tour.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;

        @Data
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        @Data
        public static class Body {
            private ItemWrapper items;
            private int numOfRows;
            private int pageNo;
            private int totalCount;

            @Data
            public static class ItemWrapper {
                private List<Item> item;

                @Data
                public static class Item {
                    private String addr1;
                    private String addr2;
                    private String areacode;
                    private String booktour;
                    private String cat1;
                    private String cat2;
                    private String cat3;
                    private String contentid;
                    private String contenttypeid;
                    private String createdtime;
                    private String dist;
                    private String firstimage;
                    private String firstimage2;
                    private String mapx;
                    private String mapy;
                    private String mlevel;
                    private String modifiedtime;
                    private int readcount;
                    private String sigungucode;
                    private String tel;
                    private String title;
                }
            }
        }
    }
}
