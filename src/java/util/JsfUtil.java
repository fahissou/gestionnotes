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
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
//import util.barcode.BarCode;
//import util.barcode.SimpleBarCodeGenerator;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javax.servlet.http.HttpServletResponse;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.module.Semestre;
import jpa.module.Ue;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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

    public static String convertObjectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String s = "";
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
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * (chars.length() - 1));
            pass.append(chars.charAt(i));
        }
        return pass.toString() + code;
    }

    public static List<String> listeNiveauCycle1() {
        List<String> liste = new ArrayList<>();
        liste.add("LICENCE 1");
        liste.add("LICENCE 2");
        liste.add("LICENCE 3");
        return liste;
    }

    public static List<String> listeNiveauCycle2() {
        List<String> liste = new ArrayList<>();
        liste.add("MASTER 1");
        liste.add("MASTER 2");
        return liste;
    }

    public static List<String> listeNiveauCycle3() {
        List<String> liste = new ArrayList<>();
        liste.add("THÈSE 1");
        liste.add("THÈSE 2");
        liste.add("THÈSE 3");
        return liste;
    }

    public static List<String> listeCycle() {
        List<String> liste = new ArrayList<>();
        liste.add("Cycle 1");
        liste.add("Cycle 2");
        liste.add("Cycle 3");
        return liste;
    }

    public static boolean validAcademicYear(String arg) {
        boolean resultat = false;
        String[] t = arg.split("-");
        Date date = new Date();
        int anneeInscrip = Integer.parseInt(t[1].trim());
        int mois = date.getMonth();
        int annee = date.getYear() + 1900;
        if (mois >= 9 && mois <= 12) {
            if (anneeInscrip == (annee + 1)) {
                resultat = true;
            }
        } else {
            if (anneeInscrip == annee) {
                resultat = true;
            }
        }
        return resultat;
    }

    public static String encryptPasswordReal(String data, String format) {
        String mpCrypter = null;
        try {
            MessageDigest md = MessageDigest.getInstance(format);
            md.update(data.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            mpCrypter = bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            JsfUtil.addErrorMessage(JsfUtil.getBundleMsg("ErrorCryptage"));
        }
        return mpCrypter;
    }

    public static int valideAcademicYear(String arg1, String arg2) {
        int resultat;
        String[] tab1 = arg1.split("-");
        String[] tab2 = arg2.split("-");
        int a11 = Integer.parseInt(tab1[0].trim()) + 1;
        int a12 = Integer.parseInt(tab1[1].trim()) + 1;
        int a21 = Integer.parseInt(tab2[0].trim());
        int a22 = Integer.parseInt(tab2[1].trim());
        if (arg1.equals(arg2)) {
            resultat = 0;
        } else {
            if (a11 == a21 && a12 == a22) {
                resultat = 1;
            } else {
                resultat = 0;
            }
        }
        return resultat;
    }

    public static Date stringToDate(String arg) {
        String[] arg1 = arg.split(",");
        Date date = new Date(Integer.parseInt(arg1[2].trim()) - 1900, Integer.parseInt(arg1[1].trim()), Integer.parseInt(arg1[0].trim()));
        return date;
    }

    public static String previousAcademicYear(String arg) {
        String[] tab1 = arg.split("-");
        int a1 = Integer.parseInt(tab1[0].trim()) - 1;
        int a2 = Integer.parseInt(tab1[1].trim()) - 1;
        return String.valueOf(a1) + " - " + String.valueOf(a2);
    }

    public static String previousGP(String arg) {
        String res;
        String[] tab1 = arg.split("-");
        String a1 = tab1[0].trim();
        int a2 = Integer.parseInt(tab1[1].trim());
        if (a2 > 1 && a2 <= 3) {
            a2 = a2 - 1;
            res = String.valueOf(a1) + "-" + String.valueOf(a2);
        } else {
            res = arg;
        }
        return res;
    }

    public static String nextGP(String arg) {
        String res;
        String[] tab1 = arg.split("-");
        String a1 = tab1[0].trim();
        int a2 = Integer.parseInt(tab1[1].trim());
        if (a2 >= 1 && a2 < 3) {
            a2 = a2 + 1;
            res = String.valueOf(a1) + "-" + String.valueOf(a2);
        } else {
            res = arg;
        }
        return res;
    }

    public static String[] getEnteteProcesVerbal(int taille) {
        String[] arg = new String[taille];
        for (int i = 0; i < taille; i++) {
            arg[i] = "UE" + (i + 1);
        }
        return arg;
    }

    public static int[] choixParametre(int a) {
        int[] choix = new int[2];
        switch (a) {
            case 12:
                choix[0] = 1;
                choix[1] = 0;
                break;
            case 11:
                choix[0] = 1;
                choix[1] = 1;
                break;
            case 10:
                choix[0] = 1;
                choix[1] = 2;
                break;
            case 9:
                choix[0] = 1;
                choix[1] = 3;
                break;
            case 8:
                choix[0] = 1;
                choix[1] = 4;
                break;
            case 7:
                choix[0] = 1;
                choix[1] = 5;
                break;
            case 6:
                choix[0] = 1;
                choix[1] = 6;
                break;
            case 5:
                choix[0] = 0;
                choix[1] = 1;
                break;
        }
        return choix;
    }

    public static String getAbrevUE(String arg) {
        String[] arg1 = arg.split(" ");
        String result = "";
        for (int i = 0; i < arg1.length; i++) {
            if ((arg1[i].length() > 3) || i == 0 || i == arg1.length - 1) {
                String val = arg1[i];
                for (int j = 0; j < val.length(); j++) {
                    if (j < 5) {
                        result += val.charAt(j);
                    }
                }
                if (i < arg1.length - 1) {
                    result += "-";
                }

            }
        }
        return result;
    }

    public static int[] choiseParameter(int a) {
        int[] choix = new int[3];
        int constante = 18;
        if (a <= 18 && a >= 12) {
            choix[0] = 0;
            choix[1] = 0;
            choix[2] = constante - a;
        } else if (a < 12 && a >= 6) {
            constante = 12;
            choix[0] = 0;
            choix[1] = constante - a;
            choix[2] = 6;
        } else if (a < 6 && a >= 4) {
            constante = 6;
            choix[0] = constante - a;
            choix[1] = 6;
            choix[2] = 6;
        } else {
            choix[0] = 6;
            choix[1] = 6;
            choix[2] = 6;
        }
        return choix;
    }

    public static String[] getFileNameRapport(int a) {
        String[] choix = new String[3];
        switch (a) {
            case 18:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "proces18.docx";
                break;
            case 17:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "proces17.docx";
                break;
            case 16:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "proces16.docx";
                break;
            case 15:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "proces15.docx";
                break;
            case 14:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "proces14.docx";
                break;
            case 13:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "proces13.docx";
                break;
            case 12:
                choix[0] = "proces0.docx";
                choix[1] = "proces12.docx";
                choix[2] = "";
                break;
            case 11:
                choix[0] = "proces0.docx";
                choix[1] = "proces11.docx";
                choix[2] = "";
                break;
            case 10:
                choix[0] = "proces0.docx";
                choix[1] = "proces10.docx";
                choix[2] = "";
                break;
            case 9:
                choix[0] = "proces0.docx";
                choix[1] = "proces9.docx";
                choix[2] = "";
                break;
            case 8:
                choix[0] = "proces0.docx";
                choix[1] = "proces8.docx";
                choix[2] = "";
                break;
            case 7:
                choix[0] = "proces0.docx";
                choix[1] = "proces7.docx";
                choix[2] = "";
                break;
            case 6:
                choix[0] = "proces0.docx";
                choix[1] = "";
                choix[2] = "";
                break;
            case 5:
                choix[0] = "proces5.docx";
                choix[1] = "";
                choix[2] = "";
                break;
            case 4:
                choix[0] = "proces4.docx";
                choix[1] = "";
                choix[2] = "";
                break;
            default:
                choix[0] = "";
                choix[1] = "";
                choix[2] = "";
        }
        return choix;
    }

    public static String getDateEdition() {
        Date date1 = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy  HH:mm"); 
        String date2 = formatDate.format(date1);
        return date2;
    }

    public static String getRealPath(String lien) {
        String[] liens = lien.split("/");
        String var = "";
        for (int i = 0; i < liens.length; i++) {
            if (i > 1 && !liens[i].equals("web")) {
                var += "/" + liens[i];
            }
        }
        return var;
    }

    public static String getFileName2(String arg) {
        String[] liens = arg.split("/");
        return liens[liens.length - 1];
    }

    public static String nextAcademicYear(String arg) {
        String[] tab1 = arg.split("-");
        int a1 = Integer.parseInt(tab1[0].trim()) + 1;
        int a2 = Integer.parseInt(tab1[1].trim()) + 1;
        return String.valueOf(a1) + " - " + String.valueOf(a2);
    }

    public static String getPathIntModelReleve() {
        return System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/resources/releve/";
    }

    public static String getPathIntModelProces() {
        return System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/resources/releve/releveNouveau/";
    }

    public static String getPathOutTmp() {
        return System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/fichiergenerer/rapportgestionnotes/";
    }

    public static String getPathOutPDF() {
        return System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/fichiergenerer/rapportgestionnotes/touslesrapports/";
    }

    public static void deleteFile(String folderName) {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        try {
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    boolean bool = files[i].delete();
                }
            }
        } catch (Exception e) {
        }
    }

    public static void docxToPDF(String folderName, String destination) {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        try {
            int i;
            for (i = 0; i < files.length; i++) {
                String fileNam = files[i].getName();
                createPDF(folderName + fileNam, destination + "file" + i + ".pdf");
            }
        } catch (Exception e) {
            System.out.println(" pdf error " + e.getMessage());
        }
    }

    public static void createPDF(String pathIn, String pathOut) {
        try {
            long start = System.currentTimeMillis();
            // 1) Load DOCX into XWPFDocument
            InputStream is = new FileInputStream(new File(pathIn));
            XWPFDocument document = new XWPFDocument(is);
            // 2) Prepare Pdf options
            PdfOptions options = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(pathOut));
            PdfConverter.getInstance().convert(document, out, options);
        } catch (Throwable e) {
            System.out.println(" pdf error " + e.getMessage());
        }
    }

    public static void mergePDF(String folderName, String pathOut, String fileName) throws IOException {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        try {
            for (int i = 0; i < files.length; i++) {
                PDFmerger.addSource(files[i]);
            }
            PDFmerger.setDestinationFileName(pathOut + fileName + ".pdf");
            PDFmerger.mergeDocuments();
        } catch (Exception e) {
        }
    }

    public static boolean generateurXDOCReport(String fichier, List<String> champs, List< Map<String, Object>> conteneur, String tableName, String chemin, String fileName, Map<String, Object> parametreEntetes) {
        boolean resultat = false;
        try {
            System.out.println("Proces debut");
            InputStream in = new FileInputStream(new File(fichier));
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            //Put the table
            FieldsMetadata metadata = new FieldsMetadata();
            for (String ch : champs) {
                metadata.addFieldAsList(tableName + "." + ch);
            }
            report.setFieldsMetadata(metadata);
            // 3) Create context Java model
            IContext context = report.createContext();
            context.put(tableName, conteneur);
            context.putMap(parametreEntetes);
            // context.putMap(mapsP);
            // fichier de sortie
            String outputFile = fileName + ".docx";

            // 4) Generate report by merging Java model with the Docx
            OutputStream out = new FileOutputStream(new File(chemin + outputFile));
            report.process(context, out);
            resultat = true;
            System.out.println("Proces fin");
        } catch (IOException | XDocReportException ex) {
        }

        return resultat;
    }

    public static String formatNote(double note) {
        String resultat ="***";
        if(note != 0.0){
        note = (double) Math.round((note) * 100) / 100;
        String noteString = String.valueOf(note);
        String[] args = noteString.split("\\.");
        resultat = args[0] + "," + args[1];
        }
        
        return resultat;
    }

    public static void generateurXDOCReportStatic(String fichier, Map<String, Object> maps, String chemin, String nomfichier) throws Exception {
        String outputFile = "";
        OutputStream out = null;
        InputStream in = null;
        try {

            in = new FileInputStream(new File(fichier));
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            // 3) Create context Java model
            IContext context = report.createContext();
            context.putMap(maps);

            FieldsMetadata metadata = new FieldsMetadata();
            report.setFieldsMetadata(metadata);
            outputFile = nomfichier + ".docx";
            // 4) Generate report by merging Java model with the Docx
            out = new FileOutputStream(new File(chemin + outputFile));
            report.process(context, out);

            out.close();
        } catch (IOException | XDocReportException ex) {
            System.out.println(" Exce " + ex.getMessage());
        }
    }

    public static String[] semestreGP(List<Semestre> semest) {
        String[] s = new String[2];
        s[0] = "";
        s[1] = "";
        if (!semest.isEmpty()) {
            s[0] = String.valueOf(semest.get(0).getValeur());
            s[1] = String.valueOf(semest.get(1).getValeur());
        }
        return s;
    }

    public static List<String> getNiveauEtude(int a) {
        Map<Integer, List<String>> data = new HashMap<>();
        // Cycle 1
        List<String> liste1 = new ArrayList<>();
        liste1.add("LICENCE 1");
        liste1.add("LICENCE 2");
        liste1.add("LICENCE 3");
        data.put(1, liste1);
        // Cycle 2
        List<String> liste2 = new ArrayList<>();
        liste2.add("MASTER 1");
        liste2.add("MASTER 2");
        data.put(2, liste2);
        // Cycle 3
        List<String> liste3 = new ArrayList<>();
        liste3.add("THÈSE 1");
        liste3.add("THÈSE 2");
        liste3.add("THÈSE 3");
        data.put(3, liste3);
        return data.get(a);
    }

    public static int getNiveauLabel(String niveau) {
        Map<String, Integer> data = new HashMap<>();
        data.put("LICENCE 1", 1);
        data.put("LICENCE 2", 2);
        data.put("LICENCE 3", 3);
        data.put("MASTER 1", 1);
        data.put("MASTER 2", 2);
        data.put("THÈSE 1", 1);
        data.put("THÈSE 2", 2);
        data.put("THÈSE 3", 3);
        return data.get(niveau);

    }

    public static List<Integer> listeCycles() {
        List<Integer> listeCycles = new ArrayList<>();
        listeCycles.add(1);
        listeCycles.add(2);
        listeCycles.add(3);
        return listeCycles;
    }
    
    public static void flushToBrowser(File file, String contentType) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext
                    .getExternalContext().getResponse();
            response.reset();
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.setContentLength((int) file.length());
            FileInputStream input = new FileInputStream(file);
            try (BufferedInputStream buf = new BufferedInputStream(input)) {
                int readBytes;
                while ((readBytes = buf.read()) != -1) {
                    response.getOutputStream().write(readBytes);
                }
                response.getOutputStream().flush();
                response.getOutputStream().close();
                facesContext.responseComplete();
                facesContext.renderResponse();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
        }
    }
    
    public static List<Inscription> getListConcat(List<Inscription> l1, List<Inscription> l2){
        int index1 = l1.size();
        for (int i = 0; i < l2.size(); i++) {
            int index = index1 + i;
            l1.add(index , l2.get(i));
        }
        return l1;
    }
    
    public static int getOrder(int a, int b){
        int ordre;
        if(a == 1 && b == 1){
            ordre = 0;
        }else if(a == 1 && b == 2){
            ordre = 1;
        }else if(a == 1 && b == 3){
            ordre = 2;
        }else if(a == 2 && b == 1){
            ordre = 3;
        }else if(a == 2 && b == 2) {
            ordre = 4;
        }else{
            ordre = -1;
        }
        return ordre;
    }
    
    public static List<Integer> getListSemestre(int a){
        List<Integer> listesemestres = new ArrayList<>();
        for (int i = 1; i < a; i++) {
            listesemestres.add(i);
        } 
        return listesemestres;
    }
    
    public static String[] getParametres(int a) {
        String[] tab = new String[2];
        tab[0] = "-1"; tab[1] = ""; 
        if( a == 1 || a == 2){
            tab[0] = "0"; tab[1] = "LICENCE 1"; 
        }
        if( a == 3 || a == 4){
            tab[0] = "1"; tab[1] = "LICENCE 2"; 
        }
        if( a == 5 || a == 6){
            tab[0] = "2"; tab[1] = "LICENCE 3"; 
        }
        if( a == 7 || a == 8){
            tab[0] = "3"; tab[1] = "MASTER 1"; 
        }
        if( a == 9 || a == 10){
            tab[0] = "4"; tab[1] = "MASTER 2"; 
        }
        return tab; 
    }
    
    public static boolean compareDate(Date date1, Date date2) {
        int j1 = date1.getDate();
        int m1 = date1.getMonth()+1;
        int a1 = date1.getYear()+1900;
        int j2 = date2.getDate();
        int m2 = date2.getMonth()+1;
        int a2 = date2.getYear()+1900;
        boolean bool;
        if(a2 > a1) {
            bool = true;
        }else if(a1 == a2) {
            if(m2 > m1) {
                bool = true;
            }else if(m2 == m1) {
                if(j2 > j1) {
                    bool = true;
                }else{
                    bool = false;
                }
            }else{
                bool = false;
            }
        }else{
            bool = false;
        }
        
        return bool;
    }
    
    public static String getLabelGradeEnseignant(String grade) {
        Map<String,String> map = new HashMap<>();
        map.put("DOCTEUR", "Dr. ");
        map.put("PROFESSEUR", "Prof. ");
        map.put("INGENIEUR", "Ing. ");
        return map.get(grade);
    }
    
    public static List<String> getGradeEnseignant() {
        List<String> listeGrade = new ArrayList<>();
        listeGrade.add("PROFESSEUR");
        listeGrade.add("DOCTEUR");
        listeGrade.add("INGENIEUR");
        return listeGrade;
    }
    
    public static List<String> getResponsabilite() {
        List<String> listeResponsabilite = new ArrayList<>();
        listeResponsabilite.add("DIRECTEUR");
        listeResponsabilite.add("DIRECTEUR AJOINT");
        listeResponsabilite.add("RESPONSABLE DE FORMATION");
        listeResponsabilite.add("AUCUNE");
        return listeResponsabilite;
    }
    
    public static int getCreditTotal(List<Ue> ue) {
        int som = 0;
        for (int i = 0; i < ue.size(); i++) {
            som += ue.get(i).getCredit();
        }
        return som;
    }
    
     public static String decisionFinal(int creditValide, int creditTotal,GroupePedagogique groupeP ) {
        String resultat = "Réfusé";
        double creditAdmis = groupeP.getParametres().getProportionAdmission()*creditTotal;
        if(creditValide >= creditAdmis) {
            resultat = "Admis";
        }
        return resultat;
    }
    
    public static String getSessionValidation(String s1, String s2) {
        String s11 = s1.split("-")[1].trim();
        String s12 = s2.split("-")[1].trim();
        int a1 = Integer.parseInt(s11);
        int a2 = Integer.parseInt(s12);
        String resultat = s2;
        if(a1 > a2) {
            resultat = s1;
        }
        return resultat;
    }
}
