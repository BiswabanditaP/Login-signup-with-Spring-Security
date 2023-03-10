package com.biswa.loginrestapi.Entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name="users",uniqueConstraints= {
		@UniqueConstraint(columnNames= { "username"}
		),
		@UniqueConstraint(columnNames= {"email"})
})
public class User {
	 @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
		private long id;
		private String email;
		private String username;
		private String name;
		private String password;
		private String token;
		@Column(columnDefinition = "TIMESTAMP")
		private LocalDateTime tokenCreationDate;
		
		
		public LocalDateTime getTokenCreationDate() {
			return tokenCreationDate;
		}

		public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
			this.tokenCreationDate = tokenCreationDate;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
		@JoinTable(name="user_roles",
		joinColumns=@JoinColumn(name="user_id",referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="role_id",referencedColumnName="id"))
		 private Set<Role> roles;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Set<Role> getRoles() {
			return roles;
		}

		public void setRoles(Set<Role> roles) {
			this.roles = roles;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", email=" + email + ", username=" + username + ", name=" + name + ", password="
					+ password + ", roles=" + roles + ",token="+token+",tokenCreationDate="+tokenCreationDate+"]";
		}

}
