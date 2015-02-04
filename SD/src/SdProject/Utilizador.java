package SdProject;

import java.io.Serializable;


public class Utilizador implements Serializable{
    
	private String userName;	
	private String password;
	

	public Utilizador() {
		this.userName = "";
		this.password = "";

	}

	public Utilizador(String nick, String pw) {
		this.userName = nick;
		this.password = pw;
	
	}

	public Utilizador(Utilizador c) {
		this.userName = c.getUserName();
		this.password = c.getPassword();
		
	}
	
	
	
	
	public String getUserName() {
		return this.userName;
	}

	public String getPassword() {
		return this.password;
	}

	public Utilizador clone() {
		return new Utilizador(this);
	}

	public String toString() {
		StringBuilder s = new StringBuilder("### Utilizador ###\n");
		s.append("Utilizador: ").append(this.getUserName());
		s.append("Password ").append(this.getPassword());
		return s.toString();
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (o.getClass() != this.getClass()))
			return false;
		else {
			Utilizador c = (Utilizador) o;
			return this.getUserName().equals(c.getUserName());
		}
	}

}
