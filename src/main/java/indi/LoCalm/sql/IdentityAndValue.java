package indi.LoCalm.sql;

import lombok.Data;

/**
 * @author LoCalm
 */
@Data
public class IdentityAndValue {

	private Identity identity;
	private Object value;

	public IdentityAndValue() {
	}

	public IdentityAndValue(Identity identity, Object value) {
		this.identity = identity;
		this.value = value;
	}
}
