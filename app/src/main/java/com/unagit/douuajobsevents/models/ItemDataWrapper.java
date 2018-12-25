package com.unagit.douuajobsevents.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Data wrapper for xml, which is returned by Dou API.
 * '@Root' annotation specifies that xml element named 'rss'
 * is a root element, so we don't have to create separate wrapper for it.
 * 'strict = false' means that number of xml elements don't have to match exactly
 * number of fields in this class - we need only 'item' element.
 */
@Root(name="rss", strict = false)
public class ItemDataWrapper {

    // This code kept purely for possible code reuse in this or other projects.
    /*
    @Element(name="title")
    @Path("channel")
    String title;
    */

    /**
     * '@ElementList' annotation specifies that we have a list of elements rather than a single element.
     * '@Path' annotation provides a path to the element 'item', i.e.
     * <channel>
     *     <item></item>
     *     ...
     * </channel>
     */
    @ElementList(name="item", inline = true)
    @Path("channel")
    List<XmlItem> xmlItems;
}
