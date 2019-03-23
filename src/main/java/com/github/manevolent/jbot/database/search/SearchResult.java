package com.github.manevolent.jbot.database.search;


import java.util.List;

public interface SearchResult<T> {

    /**
     * Gets the query associated with this search result.
     * @return Search query instance.
     */
    Search getQuery();

    /**
     * Gets the search handler that build this result.
     * @return SearchHandler instance.
     */
    SearchHandler<T> getHandler();

    /**
     * Gets the count of total results found in this result.
     * @return total result count.
     */
    int getTotalResults();

    /**
     * Gets the maximum size of the page offered by this search result.
     *
     * This is usually the value supplied to <b>maxResults</b> in the SearchHandler.
     *
     * @return maximum page size.
     */
    int getPageSize();

    /**
     * Gets the page number that the associated result set expresses.
     * @return page number.
     */
    int getPage();

    /**
     * Gets the total number of pages expressed by this search result.
     * @return page count.
     */
    default int getTotalPages() {
        return (int) Math.floor((double)getTotalResults() / (double)getPageSize());
    }

    /**
     * Gets the objects found on the page this search result expresses.
     * @return objects found in search.
     */
    List<T> getResults();

}
