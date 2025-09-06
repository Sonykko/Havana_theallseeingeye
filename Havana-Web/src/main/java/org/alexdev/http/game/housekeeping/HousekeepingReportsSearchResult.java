package org.alexdev.http.game.housekeeping;

public class HousekeepingReportsSearchResult {
    private boolean showResults = false;
    private int totalReportsSearch = 0;
    private String searchCriteria = "";

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    public int getTotalReportsSearch() {
        return totalReportsSearch;
    }

    public void setTotalReportsSearch(int totalReportsSearch) {
        this.totalReportsSearch = totalReportsSearch;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}