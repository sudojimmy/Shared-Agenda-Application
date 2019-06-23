
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * CreateAccountRequest
 * <p>
 * create account request
 * 
 */
public class CreateAccountRequest {

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    private String accountId;
    /**
     * account nickname entered by user (repeatable)
     * (Required)
     * 
     */
    private String nickname;
    /**
     * user description
     * 
     */
    private String description;

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public CreateAccountRequest withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * account nickname entered by user (repeatable)
     * (Required)
     * 
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * account nickname entered by user (repeatable)
     * (Required)
     * 
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public CreateAccountRequest withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * user description
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * user description
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public CreateAccountRequest withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accountId", accountId).append("nickname", nickname).append("description", description).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nickname).append(accountId).append(description).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateAccountRequest) == false) {
            return false;
        }
        CreateAccountRequest rhs = ((CreateAccountRequest) other);
        return new EqualsBuilder().append(nickname, rhs.nickname).append(accountId, rhs.accountId).append(description, rhs.description).isEquals();
    }

}
