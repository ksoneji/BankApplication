
package com.bank.model.json;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fromAccountId",
    "fromAccountBalance",
    "toAccountId",
    "toAccountBalance",
    "transferAmount"
})
public class TransferBalance {

    @JsonProperty("fromAccountId")
    private Integer fromAccountId;
    @JsonProperty("fromAccountBalance")
    private Integer fromAccountBalance;
    @JsonProperty("toAccountId")
    private Integer toAccountId;
    @JsonProperty("toAccountBalance")
    private Integer toAccountBalance;
    @JsonProperty("transferAmount")
    private Integer transferAmount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("fromAccountId")
    public Integer getFromAccountId() {
        return fromAccountId;
    }

    @JsonProperty("fromAccountId")
    public void setFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public TransferBalance withFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
        return this;
    }

    @JsonProperty("fromAccountBalance")
    public Integer getFromAccountBalance() {
        return fromAccountBalance;
    }

    @JsonProperty("fromAccountBalance")
    public void setFromAccountBalance(Integer fromAccountBalance) {
        this.fromAccountBalance = fromAccountBalance;
    }

    public TransferBalance withFromAccountBalance(Integer fromAccountBalance) {
        this.fromAccountBalance = fromAccountBalance;
        return this;
    }

    @JsonProperty("toAccountId")
    public Integer getToAccountId() {
        return toAccountId;
    }

    @JsonProperty("toAccountId")
    public void setToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
    }

    public TransferBalance withToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
        return this;
    }

    @JsonProperty("toAccountBalance")
    public Integer getToAccountBalance() {
        return toAccountBalance;
    }

    @JsonProperty("toAccountBalance")
    public void setToAccountBalance(Integer toAccountBalance) {
        this.toAccountBalance = toAccountBalance;
    }

    public TransferBalance withToAccountBalance(Integer toAccountBalance) {
        this.toAccountBalance = toAccountBalance;
        return this;
    }

    @JsonProperty("transferAmount")
    public Integer getTransferAmount() {
        return transferAmount;
    }

    @JsonProperty("transferAmount")
    public void setTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
    }

    public TransferBalance withTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public TransferBalance withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TransferBalance.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("fromAccountId");
        sb.append('=');
        sb.append(((this.fromAccountId == null)?"<null>":this.fromAccountId));
        sb.append(',');
        sb.append("fromAccountBalance");
        sb.append('=');
        sb.append(((this.fromAccountBalance == null)?"<null>":this.fromAccountBalance));
        sb.append(',');
        sb.append("toAccountId");
        sb.append('=');
        sb.append(((this.toAccountId == null)?"<null>":this.toAccountId));
        sb.append(',');
        sb.append("toAccountBalance");
        sb.append('=');
        sb.append(((this.toAccountBalance == null)?"<null>":this.toAccountBalance));
        sb.append(',');
        sb.append("transferAmount");
        sb.append('=');
        sb.append(((this.transferAmount == null)?"<null>":this.transferAmount));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.fromAccountBalance == null)? 0 :this.fromAccountBalance.hashCode()));
        result = ((result* 31)+((this.fromAccountId == null)? 0 :this.fromAccountId.hashCode()));
        result = ((result* 31)+((this.transferAmount == null)? 0 :this.transferAmount.hashCode()));
        result = ((result* 31)+((this.toAccountId == null)? 0 :this.toAccountId.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.toAccountBalance == null)? 0 :this.toAccountBalance.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TransferBalance) == false) {
            return false;
        }
        TransferBalance rhs = ((TransferBalance) other);
        return (((((((this.fromAccountBalance == rhs.fromAccountBalance)||((this.fromAccountBalance!= null)&&this.fromAccountBalance.equals(rhs.fromAccountBalance)))&&((this.fromAccountId == rhs.fromAccountId)||((this.fromAccountId!= null)&&this.fromAccountId.equals(rhs.fromAccountId))))&&((this.transferAmount == rhs.transferAmount)||((this.transferAmount!= null)&&this.transferAmount.equals(rhs.transferAmount))))&&((this.toAccountId == rhs.toAccountId)||((this.toAccountId!= null)&&this.toAccountId.equals(rhs.toAccountId))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.toAccountBalance == rhs.toAccountBalance)||((this.toAccountBalance!= null)&&this.toAccountBalance.equals(rhs.toAccountBalance))));
    }

}
