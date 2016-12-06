package com.objects.beans;

public interface DatabaseCommunication {

	public boolean Fetch();
	public boolean beginEdit();
	public boolean delete(int ID);
	public boolean applyEdit();
	public boolean Load(int ID);
	
}
