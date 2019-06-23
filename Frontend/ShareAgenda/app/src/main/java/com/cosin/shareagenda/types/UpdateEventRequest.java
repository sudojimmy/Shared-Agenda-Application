
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * UpdateEventRequest
 * <p>
 * update event request
 * 
 */
public class UpdateEventRequest {

    /**
     * eventId is unique, provided by frontend
     * (Required)
     * 
     */
    private String eventId;
    /**
     * event name entered by user (repeatable)
     * (Required)
     * 
     */
    private String eventname;
    /**
     * the starter user id
     * (Required)
     * 
     */
    private String starterId;
    /**
     * Event Type
     * <p>
     * valid Type enum
     * (Required)
     * 
     */
    private types.Event.Type type;
    /**
     * DD/MM/YYYY
     * 
     */
    private String date;
    /**
     * the event start time(multiple of quarter)
     * 
     */
    private int start;
    /**
     * the total time(multiple of quarter)
     * 
     */
    private int count;
    /**
     * Event Repeat
     * <p>
     * valid Repeat enum
     * 
     */
    private types.Event.Repeat repeat;
    /**
     * event occurs location
     * 
     */
    private String location;
    /**
     * Event State
     * <p>
     * valid State enum
     * 
     */
    private types.Event.State state;
    /**
     * event description
     * 
     */
    private String description;

    /**
     * eventId is unique, provided by frontend
     * (Required)
     * 
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * eventId is unique, provided by frontend
     * (Required)
     * 
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public UpdateEventRequest withEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    /**
     * event name entered by user (repeatable)
     * (Required)
     * 
     */
    public String getEventname() {
        return eventname;
    }

    /**
     * event name entered by user (repeatable)
     * (Required)
     * 
     */
    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public UpdateEventRequest withEventname(String eventname) {
        this.eventname = eventname;
        return this;
    }

    /**
     * the starter user id
     * (Required)
     * 
     */
    public String getStarterId() {
        return starterId;
    }

    /**
     * the starter user id
     * (Required)
     * 
     */
    public void setStarterId(String starterId) {
        this.starterId = starterId;
    }

    public UpdateEventRequest withStarterId(String starterId) {
        this.starterId = starterId;
        return this;
    }

    /**
     * Event Type
     * <p>
     * valid Type enum
     * (Required)
     * 
     */
    public types.Event.Type getType() {
        return type;
    }

    /**
     * Event Type
     * <p>
     * valid Type enum
     * (Required)
     * 
     */
    public void setType(types.Event.Type type) {
        this.type = type;
    }

    public UpdateEventRequest withType(types.Event.Type type) {
        this.type = type;
        return this;
    }

    /**
     * DD/MM/YYYY
     * 
     */
    public String getDate() {
        return date;
    }

    /**
     * DD/MM/YYYY
     * 
     */
    public void setDate(String date) {
        this.date = date;
    }

    public UpdateEventRequest withDate(String date) {
        this.date = date;
        return this;
    }

    /**
     * the event start time(multiple of quarter)
     * 
     */
    public int getStart() {
        return start;
    }

    /**
     * the event start time(multiple of quarter)
     * 
     */
    public void setStart(int start) {
        this.start = start;
    }

    public UpdateEventRequest withStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * the total time(multiple of quarter)
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * the total time(multiple of quarter)
     * 
     */
    public void setCount(int count) {
        this.count = count;
    }

    public UpdateEventRequest withCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * Event Repeat
     * <p>
     * valid Repeat enum
     * 
     */
    public types.Event.Repeat getRepeat() {
        return repeat;
    }

    /**
     * Event Repeat
     * <p>
     * valid Repeat enum
     * 
     */
    public void setRepeat(types.Event.Repeat repeat) {
        this.repeat = repeat;
    }

    public UpdateEventRequest withRepeat(types.Event.Repeat repeat) {
        this.repeat = repeat;
        return this;
    }

    /**
     * event occurs location
     * 
     */
    public String getLocation() {
        return location;
    }

    /**
     * event occurs location
     * 
     */
    public void setLocation(String location) {
        this.location = location;
    }

    public UpdateEventRequest withLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * Event State
     * <p>
     * valid State enum
     * 
     */
    public types.Event.State getState() {
        return state;
    }

    /**
     * Event State
     * <p>
     * valid State enum
     * 
     */
    public void setState(types.Event.State state) {
        this.state = state;
    }

    public UpdateEventRequest withState(types.Event.State state) {
        this.state = state;
        return this;
    }

    /**
     * event description
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * event description
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public UpdateEventRequest withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventId", eventId).append("eventname", eventname).append("starterId", starterId).append("type", type).append("date", date).append("start", start).append("count", count).append("repeat", repeat).append("location", location).append("state", state).append("description", description).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(date).append(eventId).append(starterId).append(repeat).append(start).append(count).append(description).append(location).append(state).append(type).append(eventname).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UpdateEventRequest) == false) {
            return false;
        }
        UpdateEventRequest rhs = ((UpdateEventRequest) other);
        return new EqualsBuilder().append(date, rhs.date).append(eventId, rhs.eventId).append(starterId, rhs.starterId).append(repeat, rhs.repeat).append(start, rhs.start).append(count, rhs.count).append(description, rhs.description).append(location, rhs.location).append(state, rhs.state).append(type, rhs.type).append(eventname, rhs.eventname).isEquals();
    }

}
