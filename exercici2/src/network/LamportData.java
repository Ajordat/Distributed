package network;

import models.Role;

import java.io.Serializable;

/**
 * @author Ajordat
 * @version 1.0
 **/
public class LamportData implements Serializable {

	private Role src;
	private int data;

	public LamportData(Role src, int data) {
		this.src = src;
		this.data = data;
	}

	public Role getSrc() { return src; }

	public int getData() { return data; }

}
