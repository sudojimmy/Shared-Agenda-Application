package com.cosin.shareagenda.api.plugin.uwapi;

public class UWCourseQueryInfo {
    private String term;
    private String subject;
    private String catalog_number;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCatalog_number() {
        return catalog_number;
    }

    public void setCatalog_number(String catalog_number) {
        this.catalog_number = catalog_number;
    }
}
