package com.unagit.douuajobsevents.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
    String title;

    @Element(name = "guid")
    String guid;

    @Element(name = "description")
    String description;

}

