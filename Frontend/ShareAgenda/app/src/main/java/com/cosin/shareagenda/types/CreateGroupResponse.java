
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * CreateGroupRequest
 * <p>
 * create group response
 * 
 */
public class CreateGroupResponse {

    /**
     * group id
     * (Required)
     * 
     */
    private String groupId;

    /**
     * group id
     * (Required)
     * 
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * group id
     * (Required)
     * 
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public CreateGroupResponse withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("groupId", groupId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(groupId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateGroupResponse) == false) {
            return false;
        }
        CreateGroupResponse rhs = ((CreateGroupResponse) other);
        return new EqualsBuilder().append(groupId, rhs.groupId).isEquals();
    }

}
