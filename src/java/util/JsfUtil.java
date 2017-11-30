/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import util.barcode.BarCode;
//import util.barcode.SimpleBarCodeGenerator;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import org.jasypt.util.text.BasicTextEncryptor;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.Years;

/**
 *
 * @author mbohm
 */
public class JsfUtil {

    public static String LDAPURL, GROUPBASEDN, PEOPLEBASEDN, CURRENTPRISONCODE, IDWEZONCODE, IDCOMPTE,
            RECIPIENTSMAIL, SMTPSERVER, SMTPUSER, SMTPPASSWORD, VERSION, CONTACT;

    
    public static String getCONTACT() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        CONTACT = ctx.getExternalContext().getInitParameter("CONTACT");
        return CONTACT;
    }

    public static String getVERSION() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        VERSION = ctx.getExternalContext().getInitParameter("VERSION");
        return VERSION;
    }

    public static String getRECIPIENTSMAIL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        RECIPIENTSMAIL = ctx.getExternalContext().getInitParameter("RECIPIENTSMAIL");
        return RECIPIENTSMAIL;
    }

    public static String getSMTPSERVER() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        SMTPSERVER = ctx.getExternalContext().getInitParameter("SMTPSERVER");
        return SMTPSERVER;
    }

    public static String getSMTPUSER() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        SMTPUSER = ctx.getExternalContext().getInitParameter("SMTPUSER");
        return SMTPUSER;
    }

    public static String getSMTPPASSWORD() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        SMTPPASSWORD = ctx.getExternalContext().getInitParameter("SMTPPASSWORD");
        return SMTPPASSWORD;
    }

    public static String getLDAPURL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        LDAPURL = ctx.getExternalContext().getInitParameter("LDAPURL");
        return LDAPURL;
    }

    public static String getGROUPBASEDN() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        GROUPBASEDN = ctx.getExternalContext().getInitParameter("GROUPBASEDN");
        return GROUPBASEDN;
    }

    public static String getPEOPLEBASEDN() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        PEOPLEBASEDN = ctx.getExternalContext().getInitParameter("PEOPLEBASEDN");
        return PEOPLEBASEDN;
    }

    public static String getCURRENTPRISONCODE() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        CURRENTPRISONCODE = ctx.getExternalContext().getInitParameter("CURRENTPRISONCODE");
        return CURRENTPRISONCODE;
    }

    public static String getIDWEZONCODE() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        IDWEZONCODE = ctx.getExternalContext().getInitParameter("IDWEZONCODE");
        return IDWEZONCODE;
    }

    public static String getIDCOMPTE() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        IDCOMPTE = ctx.getExternalContext().getInitParameter("IDCOMPTE");
        return IDCOMPTE;
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void ensureAddErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getBundleMsg("MsgTitleError"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMsg("MsgTitleInfo"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, getBundleMsg("MsgTitleWarn"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addFatalMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, getBundleMsg("MsgTitleFatal"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addFlashErrorMessage(String msg) {

        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        Flash flash;
        flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getBundleMsg("MsgTitleError"), msg);
        facesContext.addMessage(null, facesMsg);
    }

    public static void addFlashSuccessMessage(String msg) {
        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        Flash flash;
        flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMsg("MsgTitleInfo"), msg);
        facesContext.addMessage(null, facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static <T> Collection<T> arrayToCollection(T[] arr) {
        if (arr == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(arr);
    }

    public static Object[] collectionToArray(Collection<?> c) {
        if (c == null) {
            return new Object[0];
        }
        return c.toArray();
    }

    public static String getAsConvertedString(Object object, Converter converter) {
        return converter.getAsString(FacesContext.getCurrentInstance(), null, object);
    }

    public static String getAsString(Object object) {
        if (object instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) object;
            if (collection.isEmpty()) {
                return "(No Items)";
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Object item : collection) {
                if (i > 0) {
                    sb.append("<br />");
                }
                sb.append(item);
                i++;
            }
            return sb.toString();
        }
        return String.valueOf(object);
    }

    public static String convertDate(Date d, String format) {
        try {
            SimpleDateFormat affiche = new SimpleDateFormat(format, Locale.FRENCH);
            return affiche.format(d);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static int convertDateToYear(Date d, String format) {
        try {
            SimpleDateFormat affiche = new SimpleDateFormat(format, Locale.FRENCH);
            return Integer.valueOf(affiche.format(d));
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static int convertToInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static String currencyFormat(double value) {
        try {
            NumberFormat f = new DecimalFormat("##,###.##");
            return f.format(value);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String formatCompte(String value) {
        //DecimalFormat f = new DecimalFormat("##,####.## ");
        NumberFormat f = new DecimalFormat("##,####.##");
        f.setMinimumIntegerDigits(12);
        return f.format(Long.valueOf(value));
    }

    public static String getBundleMsg(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("util.Bundle", ctx.getViewRoot().getLocale());
        return bundle.getString(key);
    }

    public static String encryptPassword(String password, String algo)
            throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algo);
        byte[] bs;
        messageDigest.reset();
        bs = messageDigest.digest(password.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        //hex encode the digest
        for (int i = 0; i < bs.length; i++) {
            String hexVal = Integer.toHexString(0xFF & bs[i]);
            if (hexVal.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hexVal);
        }
        return stringBuilder.toString();
    }

    public static String computeDuree(Date d) {
        String result = "";
        DateMidnight today = new DateMidnight();
        DateMidnight birthday = new DateMidnight(d);
        Period p = new Period(birthday, today);
//        int year = Years.yearsBetween(birthday, today).getYears();
//        int month = Months.monthsBetween(birthday, today).dividedBy(12).getMonths();
//        int day = Days.daysBetween(birthday, today).getDays();

        int year = p.getYears();
        int month = p.getMonths();
        int week = p.getWeeks();
        int day = p.getDays();

        if (year > 0) {
            result += year + "a";
        }

        if (month > 0) {
            result += month + "m";
        }
        if (week > 0) {
            result += week + "s";
        }
        if (day > 0) {
            result += day + "j";
        }

        return result;
    }
    
    public static String computeDureeJours(Date debut) {
        String result = "";
        DateTime today = new DateTime();
        DateTime birthday = new DateTime(debut);
        Period p = new Period(birthday, today);
//        int year = Years.yearsBetween(birthday, today).getYears();
//        int month = Months.monthsBetween(birthday, today).dividedBy(12).getMonths();
//        int day = Days.daysBetween(birthday, today).getDays();

        
        int day = Days.daysBetween(birthday, today).getDays();
        int hour = p.getHours();
        int min = p.getMinutes();
        int sec = p.getSeconds();
        int mill = p.getMillis();

        if (day > 0) {
            result += day + " jr ";
        }

        if (hour > 0) {
            result += hour + " h ";
        }
        if (min > 0) {
            result += min + " mn ";
        }
        if (sec > 0) {
            result += sec + " s ";
        }
        if (mill > 0) {
            result += mill + " ms";
        }

        return result;
    }
    
    public static String computeDureeJours(Date debut, Date fin) {
        String result = "";
        DateTime today = new DateTime(fin);
        DateTime birthday = new DateTime(debut);
        Period p = new Period(birthday, today);
//        int year = Years.yearsBetween(birthday, today).getYears();
//        int month = Months.monthsBetween(birthday, today).dividedBy(12).getMonths();
//        int day = Days.daysBetween(birthday, today).getDays();

        
        int day = Days.daysBetween(birthday, today).getDays();
        int hour = p.getHours();
        int min = p.getMinutes();
        int sec = p.getSeconds();
        int mill = p.getMillis();

        if (day > 0) {
            result += day + " jr ";
        }

        if (hour > 0) {
            result += hour + " h ";
        }
        if (min > 0) {
            result += min + " mn ";
        }
        if (sec > 0) {
            result += sec + " s ";
        }
        if (mill > 0) {
            result += mill + " ms";
        }

        return result;
    }

    public static Date computeDuree(Date debut, int y, int m, int w, int d) {
        DateTime deb = new DateTime(debut).withTimeAtStartOfDay();
        DateTime fin = deb.plusYears(y).plusMonths(m).plusWeeks(w).plusDays(d);
        return fin.toDate();
    }

    //determine l'age du detenu
    public static int determineAge(Date d) {
        Instant birthday = new DateMidnight(d).toInstant();
        Instant now = Instant.now();
        int age = Years.yearsBetween(birthday, now).getYears();
        Days.daysBetween(birthday, now).getDays();

//        Duration age = new Duration(birthday, now);
//        System.out.println("jai " + age + " ans");
//        //int result = age.toStandardDays().getDays();
        return age;
    }

    public static String getLabel(Integer frais, Integer carburant) {
        String str = "";
        if (frais != 0) {
            str = getBundleMsg("FraisMission") + ": " + formatMillier(frais.toString());
        }
        if (carburant != 0) {
            if (!str.equals("")) {
                str = str + " ";
            }
            str = str + getBundleMsg("FraisCraburant") + ": " + formatMillier(carburant.toString());
        }
        return str;
    }

    public static String formatMillier(String value) {
        DecimalFormat f = new DecimalFormat("##,###.##");
        if (value.equals("")) {
            return "0";
        }
        return f.format(Integer.valueOf(value));
    }

    public static int formatLong(Long l) {
        return l.intValue();
    }

    public static boolean existFlag(String code) {

        String flagFile = FacesContext.getCurrentInstance().
                getExternalContext().getRealPath("/resources/images/flags") + File.separator + code.toLowerCase() + ".png";
        File file = new File(flagFile);
        return file.exists();
    }

    public static byte[] Base64StringToByteArray(String data) {
        byte[] res = Base64.getDecoder().decode(data);
        return res;
    }

    public static String getExtension(String filename) {
        String extension;
        String[] ext = filename.split("\\.");
        if (ext.length > 0) {
            extension = "." + ext[ext.length - 1];
        } else {
            extension = "";
        }
        return extension;
    }

    public static String byteArrayToBase64String(String mimeType, byte[] data) {
        String res = Base64.getEncoder().encodeToString(data);
        if (mimeType.equals("")) {
            return res;
        } else {
            return "data:" + mimeType + ";base64," + res;
        }
    }

    public static String encrypt(String plainText, String key) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(key);
        return textEncryptor.encrypt(plainText);

    }

    public static String decrypt(String cryptText, String key) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(key);
        return textEncryptor.decrypt(cryptText);
    }

    public static String getFileName(String extension) {
        String retour = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date());
        if (!((extension == null) || extension.equals(""))) {
            retour += "." + extension.toUpperCase();
        }
        return retour;
    }

    public static String getFileName(String extension, String prefixe) {
        String retour = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date());
        String pre = prefixe;
        pre = pre.replaceAll(" ", "_");
        pre = sansAccent(pre);
        pre = pre.toUpperCase();
        if (!((extension == null) || extension.equals(""))) {
            retour += "." + extension.toUpperCase();
        }
        retour = pre + "_" + retour;
        return retour;
    }

//    public static Image readBarecode(String leCode) {
//        SimpleBarCodeGenerator gen = new SimpleBarCodeGenerator("code128", "image/x-png", 60);
//        try {
//            BarCode b = gen.generateBarCode(leCode);
//            return ImageIO.read(new ByteArrayInputStream(b.getData()));
//        } catch (IOException ex) {
//            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }

    public static String sansAccent(String chaine) {
        return Normalizer.normalize(chaine, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
    }

    /**
     * Renvoie la liste des années avec annee au milieu puis c années
     * inférieures et c années supérieures
     *
     * @param annee
     * @param moinsC
     * @param plusC
     * @return
     */
    public static List<String> getAnnees(String annee, int moinsC, int plusC) {
        List<String> annees = new ArrayList<>();
        if (annee == null) {
            annee = new SimpleDateFormat("YYYY", Locale.FRENCH).format(new Date());
        }
        int year = Integer.parseInt(annee);
        for (int i = moinsC; i <= plusC; i++) {
            annees.add(String.valueOf(year + i));
        }
        return annees;
    }
    
    public static List<String> getAnneesToCurrentYear(String annee, int moinsC) {
        List<String> annees = new ArrayList<>();
        if (annee == null) {
            annee = new SimpleDateFormat("YYYY", Locale.FRENCH).format(new Date());
        }
        int year = Integer.parseInt(annee);
        int yearStart = year + moinsC;
        int yearEnd = Integer.parseInt(new SimpleDateFormat("YYYY", Locale.FRENCH).format(new Date()));
        for (int i = yearStart; i <= yearEnd; i++) {
            annees.add(String.valueOf(i));
        }
        return annees;
    }
    public static List<String> getAnneesFromThisYearToCurrentYear(String annee) {
        List<String> annees = new ArrayList<>();
        if (annee == null) {
            annee = new SimpleDateFormat("YYYY", Locale.FRENCH).format(new Date());
        }
        int yearStart = Integer.parseInt(annee);
        int yearEnd = Integer.parseInt(new SimpleDateFormat("YYYY", Locale.FRENCH).format(new Date()));
        for (int i = yearStart; i <= yearEnd; i++) {
            annees.add(String.valueOf(i));
        }
        return annees;
    }

    /**
     * Retourne la date de début ou de fin du mois ou de l'année de la date en
     * paramètres
     *
     * @param date
     * @param position
     * @return
     */
    public static Date getPoleDate(Date date, int position) {
        DateTime now = new DateTime(date);
        Date dt = null;
        switch (position) {
            case Constants.MONTHSTART:
                dt = now.dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
                break;
            case Constants.MONTHEND:
                dt = now.dayOfMonth().withMaximumValue().withTime(23, 59, 59, 999).toDate();
                break;
            case Constants.YEARSTART:
                dt = now.dayOfYear().withMinimumValue().withTimeAtStartOfDay().toDate();
                break;
            case Constants.YEAREND:
                dt = now.dayOfYear().withMaximumValue().withTime(23, 59, 59, 999).toDate();
                break;
        }
        return dt;
    }

    public static boolean inSidePeriode(Date startPeriod, Date endPeriod, Date date) {
        DateTime d = new DateTime(date);
        return d.isAfter(new DateTime(startPeriod)) && d.isBefore(new DateTime(endPeriod));

    }

    public static boolean leftSidePeriode(Date startPeriod, Date endPeriod, Date date) {
        return new DateTime(date).isBefore(new DateTime(startPeriod));
    }

    public static boolean rightSidePeriode(Date startPeriod, Date endPeriod, Date date) {
        return new DateTime(date).isAfter(new DateTime(endPeriod));
    }

    public static Double roundDouble(Double value) {
        DecimalFormat df = new DecimalFormat(".##");
        String s = df.format(value);
        try {
            Number n = NumberFormat.getInstance().parse(s);
            return n.doubleValue();
        } catch (ParseException ex) {
            return value;
        }
    }

    /**
     * Millisecondes en jours, heures, minutes et secondes
     *
     * @param millis
     * @return
     */
    public static String millisecondsTo(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
        long min = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long sec = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        String format = "";
        if (days > 0) {
            format += String.format("%02d j ", days);
        }
        if (hours > 0) {
            format += String.format("%02d h ", hours);
        }
        if (min > 0) {
            format += String.format("%02d mn ", min);
        }
        if (sec > 0) {
            format += String.format("%02d sec ", sec);
        }
        if (format.equals("")) {
            format = "--";
        }
        return format;
    }
    
    public static String convertObjectToJson(Object object){
        ObjectMapper mapper = new ObjectMapper();
        String s="";
        try {
            s = mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            //Logger.getLogger(DashBoardBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    public static String formatMillierDouble(Double value) {
        if (value == null) {
            return "--";
        }
        DecimalFormat f = new DecimalFormat("##,###.## ");
        return f.format(value);
        //return s;
    }
    
    public static String generateId() {
        int length = 6;
        SimpleDateFormat formatCode = new SimpleDateFormat("ddMMyyyyHHmmss");
        String code = formatCode.format(new Date());
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; 
        StringBuffer pass = new StringBuffer();
        for(int x=0;x<length;x++)   {
           int i = (int)Math.floor(Math.random() * (chars.length() -1));
           pass.append(chars.charAt(i));
        }
        return pass.toString() + code;
    }
    
    public static List<String> listeNiveauCycle1(){
        List<String> liste = new ArrayList<>();
        liste.add("LICENCE 1");
        liste.add("LICENCE 2");
        liste.add("LICENCE 3");
        return liste;
    }
    
    public static List<String> listeNiveauCycle2(){
        List<String> liste = new ArrayList<>();
        liste.add("MASTER 1");
        liste.add("MASTER 2");
        return liste;
    }
    
    public static List<String> listeNiveauCycle3(){
        List<String> liste = new ArrayList<>();
        liste.add("THÈSE 1");
        liste.add("THÈSE 2");
        liste.add("THÈSE 3");
        return liste;
    }
    
    public static List<String> listeCycle(){
        List<String> liste = new ArrayList<>();
        liste.add("Cycle 1");
        liste.add("Cycle 2");
        liste.add("Cycle 3");
        return liste;
    }
    
    public static boolean validAcademicYear(String arg) {
        boolean resultat = false;
        String [] t = arg.split("-");
        Date date = new Date();
        int anneeInscrip = Integer.parseInt(t[1].trim());
        int mois = date.getMonth();
        int annee = date.getYear() + 1900;
        if( mois >= 9 && mois <=12 ){
            if(anneeInscrip == (annee+1)){
                resultat = true;
            }
        }else{
            if(anneeInscrip == annee) {
                resultat = true;
            }
        }
        return resultat; 
    }
    
}
