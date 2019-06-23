
package types;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Calendar
 * <p>
 * calendar object describes user information
 * 
 */
public class Calendar {

    /**
     * calendar id
     * (Required)
     * 
     */
    private String calendarId;
    /**
     * a list of event id
     * (Required)
     * 
     */
    private List<String> eventList = new ArrayList<String>();

    /**
     * calendar id
     * (Required)
     * 
     */
    public String getCalendarId() {
        return calendarId;
    }

    /**
     * calendar id
     * (Required)
     * 
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Calendar withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    /**
     * a list of event id
     * (Required)
     * 
     */
    public List<String> getEventList() {
        return eventList;
    }

    /**
     * a list of event id
     * (Required)
     * 
     */
    public void setEventList(List<String> eventList) {
        this.eventList = eventList;
    }

    public Calendar withEventList(List<String> eventList) {
        this.eventList = eventList;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("calendarId", calendarId).append("eventList", eventList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendarId).append(eventList).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Calendar) == false) {
            return false;
        }
        Calendar rhs = ((Calendar) other);
        return new EqualsBuilder().append(calendarId, rhs.calendarId).append(eventList, rhs.eventList).isEquals();
    }

}
