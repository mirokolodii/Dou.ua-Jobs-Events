package com.unagit.douuajobsevents.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name="rss", strict = false)
public class ItemDataWrapper {
/*
    @Element(name="title")
    @Path("channel")
    String title;
*/

    @ElementList(name="item", inline = true)
    @Path("channel")
    List<XmlItem> items;
}
