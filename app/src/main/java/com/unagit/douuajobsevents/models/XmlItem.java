package com.unagit.douuajobsevents.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This class is used to map XML 'item' element to object.
 * @author Myroslav Kolodii
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

