
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetEventListRequest
 * <p>
 * get eventlist request
 * 
 */
public class GetEventListRequest {

    /**
     * eventname is not unique, return list of events
     * (Required)
     * 
     */
    private String eventname;

    /**
     * eventname is not unique, return list of events
     * (Required)
     * 
     */
    public String getEventname() {
        return eventname;
    }

    /**
     * eventname is not unique, return list of events
     * (Required)
     * 
     */
    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public GetEventListRequest withEventname(String eventname) {
        this.eventname = eventname;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventname", eventname).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventname).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GetEventListRequest) == false) {
            return false;
        }
        GetEventListRequest rhs = ((GetEventListRequest) other);
        return new EqualsBuilder().append(eventname, rhs.eventname).isEquals();
    }

}
