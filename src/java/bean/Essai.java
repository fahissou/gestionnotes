package bean;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author SI-MJLDH
 */
public class Essai implements Serializable{
    private String input1;
    private String input2, selection, selection2;
    private Date date1;

    public Essai(String input1, String input2, String selection, String selection2, Date date1) {
        this.input1 = input1;
        this.input2 = input2;
        this.selection = selection;
        this.selection2 = selection2;
        this.date1 = date1;
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

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.input1);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Essai other = (Essai) obj;
        if (!Objects.equals(this.input1, other.input1)) {
            return false;
        }
        return true;
    }
    
}
