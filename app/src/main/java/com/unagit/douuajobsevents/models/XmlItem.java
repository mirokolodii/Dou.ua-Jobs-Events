package com.unagit.douuajobsevents.models;


import android.util.Xml;

import com.unagit.douuajobsevents.helpers.ItemType;
import com.unagit.douuajobsevents.helpers.Language;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Calendar;

/**
 * This class is used to map XML 'item' element to Item object.
 * XmlItem, which is received from web, consists only from 3 elements -
 * title, guid and description,
 * while Item class includes more fields.
 * As example, 'description' includes also image URL,
 * which should be extracted to separate
 * field in Item.
 *
 * @author Myroslav Kolodii
 * @see Item
 */
@Root(name = "item", strict = false)
class XmlItem {
    @Element(name = "title")
    private String title;

    @Element(name = "guid")
    String guid;

    @Element(name = "description")
    private String description;

    /**
     * Converts XmlItem into job as Item object.
     * Parses html code in body, extracts img url and description.
     *
     * @return Item, converted from xml.
     * @see XmlItem
     * @see Item
     */
    Item transformJobToItem() {
        return new Item(
                this.guid,
                this.title,
                ItemType.JOB.getValue(),
                getImgUrlForJob(),
                this.description,
                Calendar.getInstance().getTimeInMillis(),
                false
        );
    }

    /**
     * Converts XmlItem into event as Item object.
     * Parses html code in body, extracts img url and description.
     *
     * @return Item, converted from xml.
     * @see XmlItem
     * @see Item
     */
    Item transformEventToItem() {
        String title = prepareHtmlTitle();

        Document doc = Jsoup.parseBodyFragment(this.description);
        // Get image url from first paragraph
        String imgUrl = doc.body().selectFirst("p").selectFirst("img").attr("src");

        // Get HTML paragraphs omitting first two
        String description = doc.select("body > :gt(1)").html();

        return new Item(
                this.guid,
                title,
                ItemType.EVENT.getValue(),
                imgUrl,
                description,
                Calendar.getInstance().getTimeInMillis(),
                false
        );
    }

    private String getImgUrlForJob() {
        for (Language l : Language.values()) {
            if (title.toLowerCase()
                    .contains(l.name().toLowerCase())) {
                return l.getUrl();
            }
        }
        return Language.DEFAULT.getUrl();
    }

    /**
     * Adds HTML bold tabs (<b></b>) to a part of title before first occurance of ',' symbol,
     * followed by a 'new line' (<br>) tag.
     * Example:
     *
     * @return title with added HTML tags.
     * @code {val input = "A Java conference, 17th of December, Lviv"}
     * @code {prepareHtmlTitle(input)} // returns "<b>A Java conference</b><br>, 17th of December, Lviv"
     */
    private String prepareHtmlTitle() {
        return
                // Bold text before first comma followed by new line
                "<b>" + title.substring(0, title.indexOf(",")) + "</b>" + ",<br>" +
                        // Normal text after first comma
                        title.substring(title.indexOf(",") + 1).trim();
    }
}

