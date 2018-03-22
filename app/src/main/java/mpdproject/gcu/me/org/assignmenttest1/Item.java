package mpdproject.gcu.me.org.assignmenttest1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ZICONG LI
 * S1635332
 *
 * This class holds details of an item parsed from the rss feed
 * It contains getter and setter methods for the item properties
 */

public class Item {
    private String title;
    private String description;
    private String link;
    private String georssPoint;
    private String author;
    private String comments;
    private String pubDate;
    private String more_info = "";

    private Date startDate;
    private Date endDate;
    private String delayInformation = "";
    private int daysToComplete = 0;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description)  {

        String[] info = description.split("<br />");

        String endDateStr = "";
        String startDateStr = "";

        startDateStr = info[0];

        if(info.length > 1)
            endDateStr = info[1];

        if(info.length>2){
            delayInformation = info[2];
        }

        startDateStr = startDateStr.substring(12);
        startDateStr.trim();
        try {
            endDateStr = endDateStr.substring(10);
        }catch (IndexOutOfBoundsException i){}
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(startDateStr);
            endDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getGeorssPoint() {
        return georssPoint;
    }
    public void setGeorssPoint(String georssPoint) {
        this.georssPoint = georssPoint;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    public Date getStartDate(){
        return startDate;
    }
    public Date getEndDate(){
        return endDate;
    }
    public String getDelayInfo(){
        return delayInformation;
    }
    public String getDateString(){
        return startDate.toString() + " " + endDate.toString();
    }
    public void setDaysToComplete(int daysIn){

        this.daysToComplete = daysIn;
    }
    public int getDaysToComplete(){
        return this.daysToComplete;
    }
    public void setMoreInfo(String link){
        more_info = link;
    }
    public String GetMoreInfo(){
        return more_info;
    }
}
