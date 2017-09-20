package bean.message;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import bean.Essai;
import util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.component.api.UIColumn;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;


/**
 *
 * @author SI-MJLDH
 */
@ManagedBean(name = "messageBean")
@ViewScoped
public class MessageBean implements Serializable {

    private String input1;
    private String input2, selection, selection2, selectedConsole;
    private List<String> listChaine;
    private Date date1;
    private List<String> selectedConsoles;
    private List<Essai> listEssais;
    private Essai selectedEssai;
    private List<Essai> filteredList, selectedEssais;
    private List<SortMeta> preSortOrder;

    private ScheduleModel eventModel;

    private ScheduleModel lazyEventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();
    private String[] selectedCities;
    private List<String> cities;

    /**
     * Creates a new instance of MessageBean
     */
    public MessageBean() {
    }

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
        eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
        eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
        eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));

        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                Date random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));

                random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
            }
        };

        listChaine = new ArrayList<>();
        listChaine.add("Sélection 1");
        listChaine.add("Sélection 2");
        listChaine.add("Sélection 3");
        listChaine.add("Sélection 4");
        listChaine.add("Sélection 5");
        listEssais = new ArrayList<>();
        listEssais.add(new Essai("ID1", "Input 1", "Sélection 1.1", "Sélection 2.1", new Date()));
        listEssais.add(new Essai("ID2", "Input 2", "Sélection 1.2", "Sélection 2.2", new Date()));
        listEssais.add(new Essai("ID3", "Input 3", "Sélection 1.3", "Sélection 2.3", new Date()));
        listEssais.add(new Essai("ID4", "Input 4", "Sélection 1.4", "Sélection 2.4", new Date()));
        listEssais.add(new Essai("ID5", "Input 5", "Sélection 1.5", "Sélection 2.5", new Date()));
        listEssais.add(new Essai("ID6", "Input 6", "Sélection 1.6", "Sélection 2.6", new Date()));
        listEssais.add(new Essai("ID7", "Input 7", "Sélection 1.7", "Sélection 2.7", new Date()));
        listEssais.add(new Essai("ID8", "Input 8", "Sélection 1.8", "Sélection 2.8", new Date()));
        listEssais.add(new Essai("ID9", "Input 9", "Sélection 1.9", "Sélection 2.9", new Date()));
        listEssais.add(new Essai("ID10", "Input 10", "Sélection 1.10", "Sélection 2.10", new Date()));
        listEssais.add(new Essai("ID11", "Input 11", "Sélection 1.11", "Sélection 2.11", new Date()));
        listEssais.add(new Essai("ID12", "Input 12", "Sélection 1.12", "Sélection 2.12", new Date()));
        listEssais.add(new Essai("ID13", "Input 13", "Sélection 1.13", "Sélection 2.13", new Date()));
        listEssais.add(new Essai("ID14", "Input 14", "Sélection 1.14", "Sélection 2.14", new Date()));
        listEssais.add(new Essai("ID15", "Input 15", "Sélection 1.15", "Sélection 2.15", new Date()));

        cities = new ArrayList<String>();
        cities.add("Miami");
        cities.add("London");
        cities.add("Paris");
        cities.add("Istanbul");
        cities.add("Berlin");
        cities.add("Barcelona");
        cities.add("Rome");
        cities.add("Brasilia");
        cities.add("Amsterdam");
        cities.add("Cotonou");
        cities.add("Abidjan");
        cities.add("Lagos");
        cities.add("Douala");
        cities.add("Lomé");
        cities.add("Ouagadougou");


        /*    selectedAdresse = new Adresse();
         selectedAdresse.setPays(authenticateBean.getCurrentPays());
         selectedAdresse.setDomicile("Maison akouègnon");
         selectedAdresse.setRue("846, Rue Gondwana");
        
         newAdresse = new Adresse();
         newAdresse.setPays(authenticateBean.getCurrentPays());
         newAdresse.setDomicile("Maison akouègnon");
         newAdresse.setRue("846, Rue Gondwana");
        
         selectedContact = new Contact();
         selectedContact.setTelephones(new ArrayList<String>());
         selectedContact.getTelephones().add("+22996843944");
         selectedContact.getTelephones().add("+22995711122");
        
         selectedContact.setMails(new ArrayList<String>());
         selectedContact.getMails().add("s.agonvinon@justice.bj");
         selectedContact.getMails().add("christian.agonvinon@gmail.com");
         selectedContact.getMails().add("scongo2002@yahoo.fr");
        
         selectedContact.setBPs(new ArrayList<String>());
         selectedContact.getBPs().add("02 BP 2626 Cotonou");
        
         selectedContact2 = new Contact();
         selectedContact2.setTelephones(new ArrayList<String>());
         selectedContact2.getTelephones().add("+22996448232");
         selectedContact2.getTelephones().add("+22995568432");
        
         selectedContact2.setMails(new ArrayList<String>());
         selectedContact2.getMails().add("o.honvoh@justice.bj");
         selectedContact2.getMails().add("omega.honvoh@gmail.com");
         selectedContact2.getMails().add("junomegy@yahoo.fr");
        
         selectedContact2.setBPs(new ArrayList<String>());
        
         newContact = new Contact();
         newContact.setTelephones(new ArrayList<String>());
         newContact.setMails(new ArrayList<String>());
         newContact.setBPs(new ArrayList<String>());
         newContact.setIndicatif(authenticateBean.getCurrentPays());
        
         newContact2 = new Contact();
         newContact2.setTelephones(new ArrayList<String>());
         newContact2.setMails(new ArrayList<String>());
         newContact2.getMails().add("scongo2002@outlook.fr");
         newContact2.setBPs(new ArrayList<String>());
         newContact2.setIndicatif(authenticateBean.getCurrentPays());*/
        buildSortOrder();
    }

    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1);    //set random day of month

        return date.getTime();
    }

    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar.getTime();
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar;
    }

    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);

        return t.getTime();
    }

    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);

        return t.getTime();
    }

    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);

        return t.getTime();
    }

    private Date theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);

        return t.getTime();
    }

    private Date today6Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);

        return t.getTime();
    }

    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);

        return t.getTime();
    }

    private Date nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);

        return t.getTime();
    }

    private Date fourDaysLater3pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
        t.set(Calendar.HOUR, 3);

        return t.getTime();
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void addEvent(ActionEvent actionEvent) {
        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }

        event = new DefaultScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /*
     * method to build initial sort order for multisort
     */
    private void buildSortOrder() {
        preSortOrder = new ArrayList<>();
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        UIComponent column = viewRoot.findComponent("listForm:elementTable:preOrderColumn");

        SortMeta sm1 = new SortMeta();
        sm1.setSortBy((UIColumn) column);
        //sm1.setSortField("input1");
        sm1.setSortOrder(SortOrder.ASCENDING);
        preSortOrder.add(sm1);
    }

    public void reset(ActionEvent e) {
        this.input1 = null;
        this.input2 = null;
        this.selection = null;
        this.selection2 = null;
        this.date1 = null;
        this.selectedConsoles = null;
        this.selectedConsole = null;
    }

    public String getInput1() {
        return input1;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public List<String> getSelectedConsoles() {
        return selectedConsoles;
    }

    public void setSelectedConsoles(List<String> selectedConsoles) {
        this.selectedConsoles = selectedConsoles;
    }

    public Essai getSelectedEssai() {
        return selectedEssai;
    }

    public void setSelectedEssai(Essai selectedEssai) {
        this.selectedEssai = selectedEssai;
    }

    public List<Essai> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Essai> filteredList) {
        this.filteredList = filteredList;
    }

    public void actionListener(ActionEvent e) {

        listEssais.add(new Essai(input1, input2, selection, selection2, date1));
        reset(e);
        String msg = "Essai créé avec succès";
        String aExecuter = "swal({title:\"" + JsfUtil.getBundleMsg("EndCreateTitle")
                    + "\", text:\"" + "" + msg + ". " + JsfUtil.getBundleMsg("DemandeCreateSuccesEndMsg")
                    + "\", type:\"success\", "
                    + "showCancelButton: true, "
                    + "confirmButtonClass: \"btn-primary btn-sm\", "
                    + "confirmButtonText: \"" + JsfUtil.getBundleMsg("Traiter") + "\", "
                    + "cancelButtonClass: \"btn-default btn-sm\", "
                    + "cancelButtonText: \"" + JsfUtil.getBundleMsg("Continuer") + "\", "
                    + "closeOnConfirm: false, "
                    + "closeOnCancel: false}, "
                    + "function(isConfirm){ "
                    + "if(isConfirm){window.location='/laboutique" + "';}else{window.location='/laboutique'}}); ";
            RequestContext.getCurrentInstance().execute(aExecuter);
        JsfUtil.addSuccessMessage("Essai créé avec succès");
        JsfUtil.addSuccessMessage("ça a réussi");
        JsfUtil.addErrorMessage("ça n'a pas réussi");

        JsfUtil.addWarningMessage("ça a réussi");
        JsfUtil.addFatalMessage("ça n'a pas réussi");
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getSelection2() {
        return selection2;
    }

    public void setSelection2(String selection2) {
        this.selection2 = selection2;
    }

    public List<String> getListChaine() {
        return listChaine;
    }

    public void setListChaine(List<String> listChaine) {
        this.listChaine = listChaine;
    }

    public List<Essai> getListEssais() {
        return listEssais;
    }

    public void setListEssais(List<Essai> listEssais) {
        this.listEssais = listEssais;
    }

    public List<Essai> getSelectedEssais() {
        return selectedEssais;
    }

    public String getSelectedConsole() {
        return selectedConsole;
    }

    public void setSelectedConsole(String selectedConsole) {
        this.selectedConsole = selectedConsole;
    }

    public void setSelectedEssais(List<Essai> selectedEssais) {
        this.selectedEssais = selectedEssais;
    }

//    public String getJsonMessageList(List<FacesMessage> listMessage){
//        ObjectMapper mapper = new ObjectMapper();
//        
//        List<Message> messages = new ArrayList<>();
//        //System.out.println(listMessage);
//        for (Iterator<FacesMessage> it = listMessage.iterator(); it.hasNext();) {
//            FacesMessage message = it.next();
//            messages.add(new Message(message.getSummary(), message.getDetail(), severityToString(message.getSeverity())));
//        }
//        String ret = "";
//        try {
//            ret = mapper.writeValueAsString(messages);
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return ret;
//        
//    }
    public String severityToString(FacesMessage.Severity s) {
        if (s.equals(FacesMessage.SEVERITY_INFO)) {
            return "info";
        }
        if (s.equals(FacesMessage.SEVERITY_WARN)) {
            return "warning";
        }
        if (s.equals(FacesMessage.SEVERITY_ERROR)) {
            return "error";
        }
        if (s.equals(FacesMessage.SEVERITY_FATAL)) {
            return "fatal";
        }
        return "info";
    }

    public void preRenderView() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
      //tune session params, eg. session.setMaxInactiveInterval(..);  

        //perform other pre-render stuff, like setting user context...  
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }

        return results;
    }

    public String[] getSelectedCities() {
        return selectedCities;
    }

    public void setSelectedCities(String[] selectedCities) {
        this.selectedCities = selectedCities;
    }

    public List<String> getCities() {
        return cities;
    }
    
    public List<String> completeCitie(String query)
    {
        List<String> results = new ArrayList<>();
        for (String citie : cities) {
            if(citie.toLowerCase().contains(query.toLowerCase()))
            {
                results.add(citie);
            }
        }

        return results;
    }

}
