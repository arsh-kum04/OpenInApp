```java
    // Folder Name: com.example.openinapp.data
    // File Name: DataResponse.java
    // Line by line documented Code:
    package com.example.openinapp.data;

    import com.google.gson.annotations.SerializedName;

    // Data class to hold the API response
    public class DataResponse {

        @SerializedName("applied_campaign")
        private int appliedCampaign; // Number of campaigns applied to the user

        @SerializedName("data")
        private Data data; // Object containing user-specific data

        @SerializedName("extra_income")
        private double extraIncome; // Extra income earned by the user

        @SerializedName("links_created_today")
        private int linksCreatedToday; // Number of links created by the user today

        @SerializedName("message")
        private String message; // API response message

        @SerializedName("startTime")
        private String startTime; // Start time of the data collection period

        @SerializedName("status")
        private boolean status; // Status of the API response (true/false)

        @SerializedName("statusCode")
        private int statusCode; // HTTP status code of the API response

        @SerializedName("support_whatsapp_number")
        private String supportWhatsappNumber; // WhatsApp number for support

        @SerializedName("today_clicks")
        private int todayClicks; // Number of clicks on user's links today

        @SerializedName("top_location")
        private String topLocation; // Top location of user's clicks

        @SerializedName("top_source")
        private String topSource; // Top source of user's clicks

        @SerializedName("total_clicks")
        private int totalClicks; // Total number of clicks on user's links

        @SerializedName("total_links")
        private int totalLinks; // Total number of links created by the user

        // Getter methods for each field
        public int getAppliedCampaign() {
            return appliedCampaign;
        }

        public Data getData() {
            return data;
        }

        public double getExtraIncome() {
            return extraIncome;
        }

        public int getLinksCreatedToday() {
            return linksCreatedToday;
        }

        public String getMessage() {
            return message;
        }

        public String getStartTime() {
            return startTime;
        }

        public boolean isStatus() {
            return status;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getSupportWhatsappNumber() {
            return supportWhatsappNumber;
        }

        public int getTodayClicks() {
            return todayClicks;
        }

        public String getTopLocation() {
            return topLocation;
        }

        public String getTopSource() {
            return topSource;
        }

        public int getTotalClicks() {
            return totalClicks;
        }

        public int getTotalLinks() {
            return totalLinks;
        }
    }
```