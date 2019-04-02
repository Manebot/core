package io.manebot.database.search;

import java.util.List;

public class DefaultSearchResult<T> implements SearchResult<T> {
    private final Search search;
    private final SearchHandler<T> handler;
    private final int pageSize;
    private final long totalResults, page;
    private final List<T> results;

    public DefaultSearchResult(Search search, SearchHandler<T> handler,
                               long totalResults, int pageSize, long page, List<T> results) {
        this.search = search;
        this.handler = handler;
        this.totalResults = totalResults;
        this.pageSize = pageSize;
        this.page = page;
        this.results = results;
    }

    @Override
    public Search getQuery() {
        return getQuery();
    }

    @Override
    public SearchHandler<T> getHandler() {
        return handler;
    }

    @Override
    public long getTotalResults() {
        return totalResults;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getPage() {
        return page;
    }

    @Override
    public List<T> getResults() {
        return results;
    }
}
