
package types;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetCalendarEventListByDateResponse
 * <p>
 * get calendar eventlist by date response
 * 
 */
public class GetCalendarEventListByDateResponse {

    /**
     * 
     * (Required)
     * 
     */
    private List<Event> eventList = new ArrayList<Event>();

    /**
     * 
     * (Required)
     * 
     */
    public List<Event> getEventList() {
        return eventList;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public GetCalendarEventListByDateResponse withEventList(List<Event> eventList) {
        this.eventList = eventList;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventList", eventList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventList).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GetCalendarEventListByDateResponse) == false) {
            return false;
        }
        GetCalendarEventListByDateResponse rhs = ((GetCalendarEventListByDateResponse) other);
        return new EqualsBuilder().append(eventList, rhs.eventList).isEquals();
    }

}
