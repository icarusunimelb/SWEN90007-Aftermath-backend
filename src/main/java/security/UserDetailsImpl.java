package security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import domain.User;
import exceptions.RecordNotExistException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String authority;

	private String id;

	private String email;

	@JsonIgnore
	private String password;

	public UserDetailsImpl(String id, String email, String password,
			String authority) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authority = authority;
	}

	public static UserDetailsImpl build(User user) {
		try {
			return new UserDetailsImpl(
					user.getId(),
					user.getEmail(),
					user.getPassword(),
					user.getRole());
		} catch (RecordNotExistException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getAuthorities() {
		return authority;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
