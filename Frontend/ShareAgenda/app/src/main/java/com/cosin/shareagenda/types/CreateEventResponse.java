
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * CreateEventRequest
 * <p>
 * create event response
 * 
 */
public class CreateEventResponse {

    /**
     * event id
     * (Required)
     * 
     */
    private String eventId;

    /**
     * event id
     * (Required)
     * 
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * event id
     * (Required)
     * 
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public CreateEventResponse withEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventId", eventId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateEventResponse) == false) {
            return false;
        }
        CreateEventResponse rhs = ((CreateEventResponse) other);
        return new EqualsBuilder().append(eventId, rhs.eventId).isEquals();
    }

}
