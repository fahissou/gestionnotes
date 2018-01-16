/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.event.ComponentSystemEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "documentViewBean")
@ViewScoped
public class DocumentViewBean implements Serializable {

    /**
     * Creates a new instance of DocumentViewBean
     */
    private StreamedContent content;  
    public DocumentViewBean() {
    }
    
    public void onPrerender(ComponentSystemEvent event) {  
        
          try {  
      
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            Document document = new Document();  
            PdfWriter.getInstance(document, out);
            document.open();  
            for (int i = 0; i < 50; i++) {  
                document.add(new Paragraph("All work and no play makes Jack a dull boy"));  
            }  
              
            document.close();  
//            content = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), "C:\\Users\\AHISSOU Florent\\Desktop\\IGISA2.pdf"); 
            content = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), "application/pdf");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

    public StreamedContent getContent() {
        return content;
    }
    
    public void setContent(StreamedContent content) {
        this.content = content;
    }
    
}
