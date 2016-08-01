package com.nnit.guiacopaco.data;

import java.util.List;

public interface IPBDataSet {
	
	public IPBDataSet filter(PhoneBookField field, Object value);
	public List<PhoneBookItem> getPBItems();
	
}
