package soloproject.seomoim.security.FormLogin;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import soloproject.seomoim.member.entity.Member;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends Member implements UserDetails {

    /*todo! customAuthorityUtils 여기서 주입받는게 최선일까*/
    private CustomAuthorityUtils customAuthorityUtils;

    public CustomUserDetails(Member member, CustomAuthorityUtils customAuthorityUtils) {
        this.customAuthorityUtils = customAuthorityUtils;
        setId(member.getId());
        setName(member.getName());
        setAge(member.getAge());
        setAddress(member.getAddress());
        setGender(member.getGender());
        setEmail(member.getEmail());
        setPassword(member.getPassword());
        setRoles(member.getRoles());
        setCreatedAt(member.getCreatedAt());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customAuthorityUtils.createAuthorities(this.getRoles());
    }

    @Override
    public String getUsername() {
        return getEmail();
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
}
