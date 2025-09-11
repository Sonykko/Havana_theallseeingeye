package org.alexdev.http.game.housekeeping;

public class HousekeepingHotCampaign {
        private int id;
        private String title;
        private String description;
        private String image;
        private String url;
        private String urlText;
        private int status;
        private int orderId;

        public HousekeepingHotCampaign() {
        }

        public HousekeepingHotCampaign(int id, String title, String description, String image, String url, String urlText, int status, int orderId) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.image = image;
            this.url = url;
            this.urlText = urlText;
            this.status = status;
            this.orderId = orderId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlText() {
            return urlText;
        }

        public void setUrlText(String urlText) {
            this.urlText = urlText;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
}
