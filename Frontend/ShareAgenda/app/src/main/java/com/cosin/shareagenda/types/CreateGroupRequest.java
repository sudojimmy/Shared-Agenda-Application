
package types;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * CreateGroupRequest
 * <p>
 * create group request
 * 
 */
public class CreateGroupRequest {

    /**
     * group name
     * (Required)
     * 
     */
    private String name;
    /**
     * group owner
     * (Required)
     * 
     */
    private String ownerid;
    /**
     * description of group
     * 
     */
    private String description;
    /**
     * group members
     * (Required)
     * 
     */
    private List<String> members = new ArrayList<String>();

    /**
     * group name
     * (Required)
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * group name
     * (Required)
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    public CreateGroupRequest withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * group owner
     * (Required)
     * 
     */
    public String getOwnerid() {
        return ownerid;
    }

    /**
     * group owner
     * (Required)
     * 
     */
    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public CreateGroupRequest withOwnerid(String ownerid) {
        this.ownerid = ownerid;
        return this;
    }

    /**
     * description of group
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * description of group
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public CreateGroupRequest withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * group members
     * (Required)
     * 
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * group members
     * (Required)
     * 
     */
    public void setMembers(List<String> members) {
        this.members = members;
    }

    public CreateGroupRequest withMembers(List<String> members) {
        this.members = members;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("ownerid", ownerid).append("description", description).append("members", members).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(description).append(ownerid).append(members).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateGroupRequest) == false) {
            return false;
        }
        CreateGroupRequest rhs = ((CreateGroupRequest) other);
        return new EqualsBuilder().append(name, rhs.name).append(description, rhs.description).append(ownerid, rhs.ownerid).append(members, rhs.members).isEquals();
    }

}
