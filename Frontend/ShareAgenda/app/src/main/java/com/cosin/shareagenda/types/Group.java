
package types;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Group
 * <p>
 * A group of user accounts
 * 
 */
public class Group {

    /**
     * group id
     * 
     */
    private String _id;
    /**
     * group name
     * (Required)
     * 
     */
    private String name;
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
     * group owner
     * 
     */
    private String ownerid;

    /**
     * group id
     * 
     */
    public String get_id() {
        return _id;
    }

    /**
     * group id
     * 
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    public Group with_id(String _id) {
        this._id = _id;
        return this;
    }

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

    public Group withName(String name) {
        this.name = name;
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

    public Group withDescription(String description) {
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

    public Group withMembers(List<String> members) {
        this.members = members;
        return this;
    }

    /**
     * group owner
     * 
     */
    public String getOwnerid() {
        return ownerid;
    }

    /**
     * group owner
     * 
     */
    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public Group withOwnerid(String ownerid) {
        this.ownerid = ownerid;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("_id", _id).append("name", name).append("description", description).append("members", members).append("ownerid", ownerid).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(description).append(_id).append(ownerid).append(members).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Group) == false) {
            return false;
        }
        Group rhs = ((Group) other);
        return new EqualsBuilder().append(name, rhs.name).append(description, rhs.description).append(_id, rhs._id).append(ownerid, rhs.ownerid).append(members, rhs.members).isEquals();
    }

}
