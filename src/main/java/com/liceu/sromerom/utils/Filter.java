package com.liceu.sromerom.utils;

public class Filter {

    //Metode que ens permet saber quin tipus de filtre de cerca esta fent l'usuari
    public static String checkTypeFilter(String title, String initDate, String endDate) {
        if (title != null && initDate != null && endDate != null) {

            if (!title.equals("") && !initDate.equals("") && !endDate.equals("")) {
                return "filterAll";
            }

            if (!title.equals("") && initDate.equals("") && endDate.equals("")) {
                return "filterByTitle";
            }

            if (title.equals("") && !initDate.equals("") && !endDate.equals("")) {
                return "filterByDate";
            }

        }
        return null;
    }

    //Metode que ens permetra saber quin son els parametres de cerca escollits una vegada pasem de pagina a la paginacio
    public static String getURLFilter(String typeNote, String title, String initDate, String endDate) {
        if (title != null && initDate != null && endDate != null) {

            if (!title.equals("") && !initDate.equals("") && !endDate.equals("")) {
                return String.format("&typeNote=%s&titleFilter=%s&noteStart=&s&noteEnd=&s", typeNote, title, initDate, endDate);
            }

            if (!title.equals("") && initDate.equals("") && endDate.equals("")) {
                return String.format("&typeNote=%s&titleFilter=%s&noteStart=&noteEnd=", typeNote, title);
            }

            if (title.equals("") && !initDate.equals("") && !endDate.equals("")) {
                return String.format("&typeNote=%s&titleFilter=&noteStart=%s&noteEnd=&s", typeNote, initDate, endDate);
            }

        }
        if (typeNote != null) {
            return String.format("&typeNote=%s&titleFilter=&noteStart=&noteEnd=", typeNote);
        }
        return null;
    }

    public static boolean checkFilter(String search, String initDate, String endDate) {
        if (search != null && initDate != null && endDate != null) {
            if (!search.equals("") && !initDate.equals("") && !endDate.equals("")) return true;
            if (!search.equals("") && initDate.equals("") && endDate.equals("")) return true;
            return search.equals("") && !initDate.equals("") && !endDate.equals("");

        }
        return false;
    }
}
