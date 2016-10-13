package com.kikrlib.bean;

import java.util.List;

import android.util.Log;


public class Categories {

    String cat1;
    String cat2;
    String cat3;
    String cat4;
    String cat5;
    String cat6;
    String cat7;
    List<Categories> list;
    String searchResult;
    boolean isSearch;
    boolean isHasSubcategory;
    String displayName;
    int maxLimit = 0;
    String title = "";

    /**
     * @return the list
     */
    public List<Categories> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<Categories> list) {
        this.list = list;
    }

    /**
     * @return the searchResult
     */
    public String getSearchResult() {
        return searchResult;
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    /**
     * @return the isSearch
     */
    public boolean isSearch() {
        return isSearch;
    }

    /**
     * @param isSearch the isSearch to set
     */
    public void setSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    /**
     * @return the isHasSubcategory
     */
    public boolean isHasSubcategory() {
        return isHasSubcategory;
    }

    /**
     * @param isHasSubcategory the isHasSubcategory to set
     */
    public void setHasSubcategory(boolean isHasSubcategory) {
        this.isHasSubcategory = isHasSubcategory;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the cat1
     */
    public String getCat1() {
        return cat1;
    }

    /**
     * @param cat1 the cat1 to set
     */
    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    /**
     * @return the cat2
     */
    public String getCat2() {
        return cat2;
    }

    /**
     * @param cat2 the cat2 to set
     */
    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public String getCat4() {
        return cat4;
    }

    public void setCat4(String cat4) {
        this.cat4 = cat4;
    }

    public String getCat5() {
        return cat5;
    }

    public void setCat5(String cat5) {
        this.cat5 = cat5;
    }

    public String getCat6() {
        return cat6;
    }

    public void setCat6(String cat6) {
        this.cat6 = cat6;
    }

    public String getCat7() {
        return cat7;
    }

    public void setCat7(String cat7) {
        this.cat7 = cat7;
    }

    public void validate() {
        if (cat2 == null) {
            maxLimit = 1;
            isSearch = false;
            isHasSubcategory = true;
            displayName = cat1;
        } else if (cat2.length() == 0) {
            maxLimit = 1;
            isSearch = true;
            isHasSubcategory = false;
            displayName = cat2;
        } else if (cat3 == null) {
            maxLimit = 2;
            isSearch = false;
            isHasSubcategory = true;
            displayName = cat2;
        } else if (cat3.length() == 0) {
            maxLimit = 2;
            isSearch = true;
            isHasSubcategory = false;
            displayName = cat3;
        } else if (cat4 == null) {
            maxLimit = 3;
            isSearch = false;
            isHasSubcategory = true;
            displayName = cat3;
        } else if (cat4.length() == 0) {
            maxLimit = 3;
            isSearch = true;
            isHasSubcategory = false;
            displayName = cat4;
        } else if (cat5 == null) {
            maxLimit = 4;
            isSearch = false;
            isHasSubcategory = true;
            displayName = cat4;
        } else if (cat5.length() == 0) {
            maxLimit = 4;
            isSearch = true;
            isHasSubcategory = false;
            displayName = cat5;
        } else if (cat6 == null) {
            maxLimit = 5;
            isSearch = false;
            isHasSubcategory = true;
            displayName = cat5;
        } else if (cat6.length() == 0) {
            maxLimit = 5;
            isSearch = true;
            isHasSubcategory = false;
            displayName = cat6;
        } else if (cat7 == null) {
            maxLimit = 6;
            isSearch = false;
            isHasSubcategory = true;
            displayName = cat6;
        } else if (cat7.length() == 0) {
            maxLimit = 6;
            isSearch = true;
            isHasSubcategory = false;
            displayName = cat7;
        }

    }

    public String getSubcatRequest() {
        String value = "";
        for (int i = 0; i < maxLimit; i++) {
            if (i == 0) {
                value += ",\"cat1\":\"" + cat1 + "\"";
            } else if (i == 1) {
                value += ",\"cat2\":\"" + cat2 + "\"";
            } else if (i == 2) {
                value += ",\"cat3\":\"" + cat3 + "\"";
            } else if (i == 3) {
                value += ",\"cat4\":\"" + cat4 + "\"";
            } else if (i == 4) {
                value += ",\"cat5\":\"" + cat5 + "\"";
            } else if (i == 5) {
                value += ",\"cat6\":\"" + cat6 + "\"";
            } else if (i == 6) {
                value += ",\"cat7\":\"" + cat7 + "\"";
            }
        }
        return value;
    }

    public String getTitle() {
        title = "";
        if (cat1 != null)
            title = title + cat1;
        if (cat2 != null)
            title = title + ">" + cat2;
        if (cat3 != null)
            title = title + ">" + cat3;
        if (cat4 != null)
            title = title + ">" + cat4;
        if (cat5 != null)
            title = title + ">" + cat5;
        if (cat6 != null)
            title = title + ">" + cat6;
        if (cat7 != null)
            title = title + ">" + cat7;
        return title;
    }

    public String getSearchRequest() {
        String value = "";
        for (int i = 0; i < maxLimit; i++) {
            if (i == 0) {
                value += cat1 + " ";
            } else if (i == 1) {
                value += cat2 + " ";
            } else if (i == 2) {
                value += cat3 + " ";
            } else if (i == 3) {
                value += cat4 + " ";
            } else if (i == 4) {
                value += cat5 + " ";
            } else if (i == 5) {
                value += cat6 + " ";
            } else if (i == 6) {
                value += cat7 + " ";
            }
        }
        return value;
    }

    public String getSearchRequestForTextView() {
        String value = "";
        for (int i = 0; i < maxLimit; i++) {
            if (i == 0) {
                value += capitalizeEachWord(cat1.trim());
            } else if (i == 1) {
                value += " > " + capitalizeEachWord(cat2.trim());
            } else if (i == 2) {
                value += " > " + capitalizeEachWord(cat3.trim());
            } else if (i == 3) {
                value += " > " + capitalizeEachWord(cat4.trim());
            } else if (i == 4) {
                value += " > " + capitalizeEachWord(cat5.trim());
            } else if (i == 5) {
                value += " > " + capitalizeEachWord(cat6.trim());
            } else if (i == 6) {
                value += " > " + capitalizeEachWord(cat7.trim());
            }
        }
        return value;
    }

    private StringBuilder capitalizeEachWord(String s) {
        StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for (int i = 0, l = words.length; i < l; ++i) {
            if (i > 0) result.append(" ");
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));

        }
        return result;
    }


    public int getLavel() {
        return maxLimit;
    }

}
