package com.bitri.service.schooladministration;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ofentse
 */
public class Batch {
    
    SimpleStringProperty id, description, start, end, streamID;

    public Batch() {
        id = new SimpleStringProperty("");
        description = new SimpleStringProperty("");
        end = new SimpleStringProperty("");
        start = new SimpleStringProperty("");
        streamID = new SimpleStringProperty("");
    }
    
    public Batch(String id, String description,String start,String end, String streamID){
        
        this.id = new SimpleStringProperty(id);
        this.description = new SimpleStringProperty(description);
        this.end = new SimpleStringProperty(end);
        this.start = new SimpleStringProperty(start);
        this.streamID = new SimpleStringProperty(streamID);
    }

    public String getId(){return id.get();}
    public void setId(String id){this.id.set(id);}
    
    public String getDescription(){return description.get();}
    public void setDescription(String id){description.set(id);}
        
    public String getEnd(){return end.get();}
    public void setEnd(String id){end.set(id);}
    
    public String getStart(){return start.get();}
    public void setStart(String fnam){start.set(fnam);}
    
    public String getStreamID(){return streamID.get();}
    public void setStreamID(String fnam){streamID.set(fnam);}
    
}
