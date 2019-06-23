
package types;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetEventListResponse
 * <p>
 * get eventlist response
 * 
 */
public class GetEventListResponse {

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

    public GetEventListResponse withEventList(List<Event> eventList) {
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
        if ((other instanceof GetEventListResponse) == false) {
            return false;
        }
        GetEventListResponse rhs = ((GetEventListResponse) other);
        return new EqualsBuilder().append(eventList, rhs.eventList).isEquals();
    }

}
